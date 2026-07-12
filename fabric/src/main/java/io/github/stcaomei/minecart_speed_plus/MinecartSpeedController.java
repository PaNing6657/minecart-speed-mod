package io.github.stcaomei.minecart_speed_plus;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MinecartSpeedController {

	public static final String NBT_KEY = "speed_controller";
	public static final double SPEED_STEP = 2.0;
	public static final double DEFAULT_SPEED = 8.0;
	public static final double MAX_SPEED = 30.0;
	public static final int COOLDOWN_TICKS = 10;

	public static boolean allowNonOp = true;

	public static boolean autoSlowdownEnabled = true;

	private static final Map<UUID, Integer> lastActionTick = new HashMap<>();

	public static boolean isSpeedController(ItemStack stack) {
		if (!stack.is(Items.COMPASS)) return false;
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		if (data == null || data.isEmpty()) return false;
		return data.copyTag().getBoolean(NBT_KEY).orElse(false);
	}

	private static boolean zh(ServerPlayer player) {
		return player.clientInformation().language().startsWith("zh");
	}

	public static boolean canUseController(ServerPlayer player) {
		return allowNonOp || player.permissions().hasPermission(Permissions.COMMANDS_ADMIN);
	}

	public static void giveSpeedController(ServerPlayer player) {
		if (!canUseController(player)) return;
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			if (isSpeedController(player.getInventory().getItem(i))) {
				return;
			}
		}

		boolean zh = zh(player);
		ItemStack compass = new ItemStack(Items.COMPASS);
		compass.set(DataComponents.CUSTOM_NAME, Component.literal(zh ? "速度控制器" : "Speed Controller").withStyle(style -> style.withItalic(false)));
		compass.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
		CompoundTag tag = new CompoundTag();
		tag.putBoolean(NBT_KEY, true);
		compass.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

		if (!player.getInventory().add(compass)) {
			player.sendSystemMessage(Component.literal(
				zh ? "§c背包已满，无法获得速度控制器"
				   : "§cInventory full, cannot receive Speed Controller"), false);
			return;
		}
		player.sendSystemMessage(Component.literal(
			zh ? "§a已获得速度控制器！左键加速，右键减速"
			   : "§aSpeed Controller received! Left-click = accelerate, Right-click = decelerate"), false);
	}

	public static void removeSpeedController(ServerPlayer player) {
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			if (isSpeedController(player.getInventory().getItem(i))) {
				player.getInventory().setItem(i, ItemStack.EMPTY);
			}
		}
	}

	public static void adjustSpeed(AbstractMinecart minecart, double delta, ServerPlayer player) {
		UUID uuid = player.getUUID();
		int currentTick = player.tickCount;
		Integer last = lastActionTick.get(uuid);
		if (last != null && currentTick - last < COOLDOWN_TICKS) {
			return;
		}
		lastActionTick.put(uuid, currentTick);

		MinecartSpeedAccess access = (MinecartSpeedAccess) minecart;
		Double currentSpeed = access.minecart_speed$getCustomSpeed();
		double current = (currentSpeed != null) ? currentSpeed : DEFAULT_SPEED;
		double newSpeed = Math.min(MAX_SPEED, Math.max(0.0, current + delta));
		access.minecart_speed$setCustomSpeed(newSpeed);
		MinecartSpeedMod.LOGGER.info("{} adjusted minecart#{} speed: {} -> {} bps", player.getScoreboardName(), minecart.getId(), current, newSpeed);

		boolean zh = zh(player);
		String dir = delta > 0 ? "§a" : "§c";
		String word = delta > 0 ? (zh ? "加速" : "Accelerate") : (zh ? "减速" : "Decelerate");
		String unit = zh ? "方块/秒" : "blocks/s";
		player.sendSystemMessage(Component.literal(
			dir + word + (zh ? "：当前速度 " : ": current speed ") + "§e" + String.format("%.1f", newSpeed) + " §f" + unit), false);
	}
}
