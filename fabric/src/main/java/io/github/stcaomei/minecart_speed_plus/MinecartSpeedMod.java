package io.github.stcaomei.minecart_speed_plus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecartSpeedMod implements ModInitializer {
	public static final String MOD_ID = "minecart_speed_plus";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			MinecartSpeedCommand.register(dispatcher);
		});

		UseItemCallback.EVENT.register((player, level, hand) -> {
			if (level.isClientSide()) return InteractionResult.PASS;
			ItemStack stack = player.getItemInHand(hand);
			if (!MinecartSpeedController.isSpeedController(stack)) return InteractionResult.PASS;
			if (!(player.getVehicle() instanceof AbstractMinecart minecart)) return InteractionResult.PASS;
			if (player instanceof ServerPlayer serverPlayer) {
				MinecartSpeedController.adjustSpeed(minecart, -MinecartSpeedController.SPEED_STEP, serverPlayer);
			}
			return InteractionResult.SUCCESS;
		});

		LOGGER.info("Minecart Speed Plus mod loaded — use /minecartspeed to control per-cart speed");
	}
}
