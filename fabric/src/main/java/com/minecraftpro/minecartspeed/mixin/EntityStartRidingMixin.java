package com.minecraftpro.minecartspeed.mixin;

import com.minecraftpro.minecartspeed.MinecartSpeedController;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityStartRidingMixin {

	@Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;ZZ)Z", at = @At("TAIL"))
	private void minecart_speed$onStartRiding(Entity vehicle, boolean force, boolean triggerAdvancement, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) return;
		if (!((Object) this instanceof ServerPlayer player)) return;
		if (!(vehicle instanceof AbstractMinecart)) return;

		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			if (MinecartSpeedController.isSpeedController(player.getInventory().getItem(i))) {
				return;
			}
		}

		MinecartSpeedController.giveSpeedController(player);
	}
}
