package io.github.stcaomei.minecart_speed_plus;

// Duck interface for accessing per-minecart custom speed.
// Applied to AbstractMinecartEntity via Mixin.
public interface MinecartSpeedAccess {

	// 获取自定义最大速度（方块/秒），返回 null 表示使用原版速度
	Double minecart_speed$getCustomSpeed();

	// 设置自定义最大速度（方块/秒），设为 null 恢复原版
	void minecart_speed$setCustomSpeed(Double speedBlocksPerSecond);
}
