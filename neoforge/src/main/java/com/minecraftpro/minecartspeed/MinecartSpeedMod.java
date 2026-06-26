package com.minecraftpro.minecartspeed;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MinecartSpeedMod.MOD_ID)
public class MinecartSpeedMod {
    public static final String MOD_ID = "minecart_speed";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public MinecartSpeedMod(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
        LOGGER.info("Minecart Speed mod loaded — use /minecartspeed to control per-cart speed");
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        MinecartSpeedCommand.register(event.getDispatcher());
    }
}
