package com.minecraftpro.minecartspeed.mixin;

import com.minecraftpro.minecartspeed.MinecartSpeedController;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Inject(method = "stopRiding", at = @At("HEAD"))
	private void minecart_speed$onStopRiding(CallbackInfo ci) {
		if (!((Object) this instanceof ServerPlayer player)) return;
		MinecartSpeedController.removeSpeedController(player);
	}

	@Inject(method = "swing(Lnet/minecraft/world/InteractionHand;)V", at = @At("HEAD"))
	private void minecart_speed$onSwing(InteractionHand hand, CallbackInfo ci) {
		if (!((Object) this instanceof ServerPlayer player)) return;
		ItemStack held = player.getItemInHand(hand);
		if (!MinecartSpeedController.isSpeedController(held)) return;
		if (!(player.getVehicle() instanceof AbstractMinecart minecart)) return;
		MinecartSpeedController.adjustSpeed(minecart, MinecartSpeedController.SPEED_STEP, player);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void minecart_speed$onTick(CallbackInfo ci) {
		if (!((Object) this instanceof ServerPlayer player)) return;
		if (player.tickCount % 20 != 0) return;
		if (player.getVehicle() instanceof AbstractMinecart) return;
		MinecartSpeedController.removeSpeedController(player);
	}
}
