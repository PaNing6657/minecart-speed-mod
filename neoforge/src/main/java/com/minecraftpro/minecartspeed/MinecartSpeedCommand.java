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
            source.sendFailure(Component.literal("No minecarts found in target selection"));
            return 0;
        }

        String msg = count == 1
            ? String.format("Set minecart speed to %.2f blocks/s", speedBps)
            : String.format("Set speed of %d minecarts to %.2f blocks/s", count, speedBps);
        source.sendSuccess(() -> Component.literal(msg), true);
        return count;
    }

    private static int executeGet(CommandSourceStack source, Collection<? extends Entity> targets) {
        int count = 0;
        for (Entity entity : targets) {
            if (entity instanceof AbstractMinecart minecart) {
                MinecartSpeedAccess access = (MinecartSpeedAccess) minecart;
                Double speed = access.minecart_speed$getCustomSpeed();
                String msg;
                if (speed != null) {
                    msg = String.format("Minecart [%s] custom speed: %.2f blocks/s (%.4f blocks/tick)",
                        minecart.getStringUUID().substring(0, 8), speed, speed / 20.0);
                } else {
                    msg = String.format("Minecart [%s] using vanilla speed (8.00 blocks/s)",
                        minecart.getStringUUID().substring(0, 8));
                }
                source.sendSuccess(() -> Component.literal(msg), false);
                count++;
            }
        }

        if (count == 0) {
            source.sendFailure(Component.literal("No minecarts found in target selection"));
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
            source.sendFailure(Component.literal("No minecarts found in target selection"));
            return 0;
        }

        String msg = count == 1
            ? "Removed custom speed — minecart reverted to vanilla"
            : String.format("Removed custom speed from %d minecarts — reverted to vanilla", count);
        source.sendSuccess(() -> Component.literal(msg), true);
        return count;
    }
}
