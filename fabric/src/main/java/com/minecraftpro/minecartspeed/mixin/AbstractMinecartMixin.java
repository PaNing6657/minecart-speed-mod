package com.minecraftpro.minecartspeed.mixin;

import com.minecraftpro.minecartspeed.MinecartSpeedAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
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
	@Unique
	private static final int LOOK_AHEAD = 5;
	@Unique
	private static final double VANILLA_MAX_SPEED = 8.0;

	// 玩家设置的自定义速度 (blocks/s)，null = 使用原版速度
	@Unique
	private Double minecart_speed$customSpeedBps = null;
	// 自动减速时备份的原始速度
	@Unique
	private Double minecart_speed$savedSpeedBps = null;
	// 是否已因障碍物自动移除变速
	@Unique
	private boolean minecart_speed$autoRemoved = false;

	@Override
	public Double minecart_speed$getCustomSpeed() {
		// 自动减速时仍返回原始速度，让命令/get 查询显示真实设定值
		if (minecart_speed$autoRemoved && minecart_speed$savedSpeedBps != null) {
			return minecart_speed$savedSpeedBps;
		}
		return minecart_speed$customSpeedBps;
	}

	@Override
	public void minecart_speed$setCustomSpeed(Double speedBlocksPerSecond) {
		this.minecart_speed$customSpeedBps = speedBlocksPerSecond;
		// 手动调速时清除自动减速状态
		this.minecart_speed$autoRemoved = false;
		this.minecart_speed$savedSpeedBps = null;
	}

	// 每 tick 计算最大速度时注入：检测障碍物 → 自动降速 / 自动恢复
	@Inject(method = "getMaxSpeed", at = @At("HEAD"), cancellable = true)
	private void minecart_speed$overrideMaxSpeed(ServerLevel level, CallbackInfoReturnable<Double> cir) {
		// 有自定义速度且未自动移除 → 检测是否需要自动减速
		if (minecart_speed$customSpeedBps != null && !minecart_speed$autoRemoved) {
			if (minecart_speed$isObstacleAhead(level)) {
				// 前方有弯道/上坡/方块 → 保存速度，切换为原版速度
				minecart_speed$savedSpeedBps = minecart_speed$customSpeedBps;
				minecart_speed$autoRemoved = true;
				return;
			}
			cir.setReturnValue(minecart_speed$customSpeedBps / 20.0);
		} else if (minecart_speed$autoRemoved) {
			// 之前自动减速了，检查障碍物是否已通过
			if (!minecart_speed$isObstacleAhead(level)) {
				// 障碍物已通过 → 恢复自定义速度
				minecart_speed$customSpeedBps = minecart_speed$savedSpeedBps;
				minecart_speed$savedSpeedBps = null;
				minecart_speed$autoRemoved = false;
				cir.setReturnValue(minecart_speed$customSpeedBps / 20.0);
			}
		}
	}

	// 检测前方是否有需要减速的障碍物
	@Unique
	private boolean minecart_speed$isObstacleAhead(ServerLevel level) {
		AbstractMinecart self = (AbstractMinecart) (Object) this;
		BlockPos currentPos = self.blockPosition();
		BlockState currentState = level.getBlockState(currentPos);

		// 当前轨道是弯道/上坡 → 立即减速
		if (minecart_speed$isCurveOrSlope(currentState)) {
			return true;
		}

		// 根据运动方向判断前进方向
		Vec3 motion = self.getDeltaMovement();
		double absX = Math.abs(motion.x);
		double absZ = Math.abs(motion.z);
		if (absX < 0.001 && absZ < 0.001) {
			return false; // 静止不动时不检测
		}

		Direction dir;
		if (absX > absZ) {
			dir = motion.x > 0 ? Direction.EAST : Direction.WEST;
		} else {
			dir = motion.z > 0 ? Direction.SOUTH : Direction.NORTH;
		}

		// 向前扫描 5 格，检测弯道/上坡/实体方块
		BlockPos.MutableBlockPos checkPos = currentPos.mutable();
		for (int i = 0; i < LOOK_AHEAD; i++) {
			checkPos.move(dir);
			BlockState state = level.getBlockState(checkPos);
			if (minecart_speed$isCurveOrSlope(state)) {
				return true; // 前方有弯道或上坡
			}
			if (!state.isAir() && !(state.getBlock() instanceof BaseRailBlock)) {
				return true; // 前方有非铁轨的实体方块阻挡
			}
		}

		return false;
	}

	// 判断方块是否为弯道铁轨或上坡铁轨
	@Unique
	private static boolean minecart_speed$isCurveOrSlope(BlockState state) {
		if (!(state.getBlock() instanceof BaseRailBlock railBlock)) return false;
		RailShape shape = state.getValue(railBlock.getShapeProperty());
		return shape.isSlope()                       // 上坡
			|| shape == RailShape.SOUTH_EAST         // 弯道
			|| shape == RailShape.SOUTH_WEST
			|| shape == RailShape.NORTH_WEST
			|| shape == RailShape.NORTH_EAST;
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void minecart_speed$writeNbt(ValueOutput nbt, CallbackInfo ci) {
		if (minecart_speed$customSpeedBps != null) {
			nbt.putDouble(SPEED_NBT_KEY, minecart_speed$customSpeedBps);
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void minecart_speed$readNbt(ValueInput nbt, CallbackInfo ci) {
		minecart_speed$customSpeedBps = nbt.getDoubleOr(SPEED_NBT_KEY, -1.0);
		if (minecart_speed$customSpeedBps < 0) {
			minecart_speed$customSpeedBps = null;
		}
	}
}
