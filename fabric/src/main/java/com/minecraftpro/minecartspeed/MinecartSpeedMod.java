package com.minecraftpro.minecartspeed;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecartSpeedMod implements ModInitializer {
	public static final String MOD_ID = "minecart_speed";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			MinecartSpeedCommand.register(dispatcher);
		});
		LOGGER.info("Minecart Speed mod loaded — use /minecartspeed to control per-cart speed");
	}
}
