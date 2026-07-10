package io.github.stcaomei.minecart_speed_plus.mixin;

import io.github.stcaomei.minecart_speed_plus.MinecartSpeedAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NewMinecartBehavior.class)
public abstract class NewMinecartBehaviorMixin {

	@Inject(method = "getMaxSpeed", at = @At("HEAD"), cancellable = true)
	private void minecart_speed$overrideMaxSpeed(ServerLevel level, CallbackInfoReturnable<Double> cir) {
		MinecartSpeedAccess access = (MinecartSpeedAccess) ((MinecartBehaviorAccessor) this).getMinecart();
		Double speed = access.minecart_speed$getCustomSpeed();
		if (speed != null) {
			cir.setReturnValue(speed / 20.0);
		}
	}
}
