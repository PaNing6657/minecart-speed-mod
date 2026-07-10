package com.minecraftpro.minecartspeed;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;

import java.util.Collection;

public class MinecartSpeedCommand {

	private static final String ARG_TARGETS = "targets";
	private static final String ARG_SPEED = "speed";

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("minecartspeed")
				.requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
				.then(Commands.literal("set")
					.then(Commands.argument(ARG_TARGETS, EntityArgument.entities())
						.then(Commands.argument(ARG_SPEED, FloatArgumentType.floatArg(0.0f))
							.executes(ctx -> executeSet(
								ctx.getSource(),
								EntityArgument.getEntities(ctx, ARG_TARGETS),
								FloatArgumentType.getFloat(ctx, ARG_SPEED)
							))
						)
					)
				)
				.then(Commands.literal("get")
					.then(Commands.argument(ARG_TARGETS, EntityArgument.entities())
						.executes(ctx -> executeGet(
							ctx.getSource(),
							EntityArgument.getEntities(ctx, ARG_TARGETS)
						))
					)
				)
				.then(Commands.literal("remove")
					.then(Commands.argument(ARG_TARGETS, EntityArgument.entities())
						.executes(ctx -> executeRemove(
							ctx.getSource(),
							EntityArgument.getEntities(ctx, ARG_TARGETS)
						))
					)
				)
		);
	}

	private static int executeSet(CommandSourceStack source, Collection<? extends Entity> targets, float speedBps) {
		int count = 0;
		for (Entity entity : targets) {
			if (entity instanceof AbstractMinecart minecart) {
				MinecartSpeedAccess access = (MinecartSpeedAccess) minecart;
				access.minecart_speed$setCustomSpeed((double) speedBps);
				count++;
			}
		}

		if (count == 0) {
			source.sendFailure(Component.translatable("minecart_speed.command.no_minecarts"));
			return 0;
		}

		Component msg = count == 1
			? Component.translatable("minecart_speed.command.set.single", String.format("%.2f", speedBps))
			: Component.translatable("minecart_speed.command.set.multiple", count, String.format("%.2f", speedBps));
		source.sendSuccess(() -> msg, true);
		return count;
	}

	private static int executeGet(CommandSourceStack source, Collection<? extends Entity> targets) {
		int count = 0;
		for (Entity entity : targets) {
			if (entity instanceof AbstractMinecart minecart) {
				MinecartSpeedAccess access = (MinecartSpeedAccess) minecart;
				Double speed = access.minecart_speed$getCustomSpeed();
				Component msg;
				if (speed != null) {
					msg = Component.translatable("minecart_speed.command.get.custom",
						minecart.getStringUUID().substring(0, 8),
						String.format("%.2f", speed),
						String.format("%.4f", speed / 20.0));
				} else {
					msg = Component.translatable("minecart_speed.command.get.vanilla",
						minecart.getStringUUID().substring(0, 8));
				}
				source.sendSuccess(() -> msg, false);
				count++;
			}
		}

		if (count == 0) {
			source.sendFailure(Component.translatable("minecart_speed.command.no_minecarts"));
			return 0;
		}
		return count;
	}

	private static int executeRemove(CommandSourceStack source, Collection<? extends Entity> targets) {
		int count = 0;
		for (Entity entity : targets) {
			if (entity instanceof AbstractMinecart minecart) {
				MinecartSpeedAccess access = (MinecartSpeedAccess) minecart;
				access.minecart_speed$setCustomSpeed(null);
				count++;
			}
		}

		if (count == 0) {
			source.sendFailure(Component.translatable("minecart_speed.command.no_minecarts"));
			return 0;
		}

		Component msg = count == 1
			? Component.translatable("minecart_speed.command.remove.single")
			: Component.translatable("minecart_speed.command.remove.multiple", count);
		source.sendSuccess(() -> msg, true);
		return count;
	}
}
