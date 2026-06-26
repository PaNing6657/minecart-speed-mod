package com.minecraftpro.minecartspeed;

/**
 * Duck interface for accessing per-minecart custom speed.
 * Applied to AbstractMinecart via Mixin.
 */
public interface MinecartSpeedAccess {

    /**
     * Get the custom max speed in blocks/second, or null if using vanilla speed.
     */
    Double minecart_speed$getCustomSpeed();

    /**
     * Set the custom max speed in blocks/second, or null to revert to vanilla.
     */
    void minecart_speed$setCustomSpeed(Double speedBlocksPerSecond);
}
