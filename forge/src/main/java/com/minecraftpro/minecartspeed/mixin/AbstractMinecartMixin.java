package com.minecraftpro.minecartspeed.mixin;

import com.minecraftpro.minecartspeed.MinecartSpeedAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartMixin implements MinecartSpeedAccess {

    @Unique
    private static final String SPEED_NBT_KEY = "MinecartSpeedModSpeed";

    /**
     * Custom max speed in blocks/second. Null = use vanilla speed.
     */
    @Unique
    private Double minecart_speed$customSpeedBps = null;

    @Override
    public Double minecart_speed$getCustomSpeed() {
        return minecart_speed$customSpeedBps;
    }

    @Override
    public void minecart_speed$setCustomSpeed(Double speedBlocksPerSecond) {
        this.minecart_speed$customSpeedBps = speedBlocksPerSecond;
    }

    /**
     * Override getMaxSpeed() to return custom speed when set.
     * Converts from blocks/second to blocks/tick (÷20).
     */
    @Inject(method = "getMaxSpeed", at = @At("HEAD"), cancellable = true)
    private void minecart_speed$overrideMaxSpeed(ServerLevel level, CallbackInfoReturnable<Double> cir) {
        if (minecart_speed$customSpeedBps != null) {
            cir.setReturnValue(minecart_speed$customSpeedBps / 20.0);
        }
    }

    /**
     * Persist custom speed to NBT when the entity is saved.
     */
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void minecart_speed$writeNbt(ValueOutput nbt, CallbackInfo ci) {
        if (minecart_speed$customSpeedBps != null) {
            nbt.putDouble(SPEED_NBT_KEY, minecart_speed$customSpeedBps);
        }
    }

    /**
     * Load custom speed from NBT when the entity is loaded.
     */
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void minecart_speed$readNbt(ValueInput nbt, CallbackInfo ci) {
        minecart_speed$customSpeedBps = nbt.getDoubleOr(SPEED_NBT_KEY, -1.0);
        if (minecart_speed$customSpeedBps < 0) {
            minecart_speed$customSpeedBps = null;
        }
    }
}
