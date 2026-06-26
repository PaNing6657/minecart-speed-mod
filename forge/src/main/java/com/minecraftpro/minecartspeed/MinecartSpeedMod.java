package com.minecraftpro.minecartspeed;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MinecartSpeedMod.MOD_ID)
public class MinecartSpeedMod {
    public static final String MOD_ID = "minecart_speed";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public MinecartSpeedMod() {
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("Minecart Speed mod loaded — use /minecartspeed to control per-cart speed");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        MinecartSpeedCommand.register(event.getDispatcher());
    }
}
