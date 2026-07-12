# Minecart Speed Plus

> [中文版](#矿车速度-plus) | Based on a fork of [MomoLawson/minecart-speed-mod](https://github.com/MomoLawson/minecart-speed-mod)

---

### Differences from the Original

| Feature | Original | Plus |
|---------|----------|------|
| Speed command | ✓ | ✓ |
| Speed controller item | ✗ | ✓ |
| Left-click speed up / Right-click slow down | ✗ | ✓ |
| Auto slow down on curves & restore | ✗ | ✓ (only when speed > 8) |
| Auto slow down on slopes & restore | ✗ | ✓ (only when speed > 8) |
| Detection toggle command | ✗ | ✓ |
| Non-OP access control | ✗ | ✓ |
| Speed limit (0 ~ 30 blocks/sec) | No limit | ✓ |
| Supported platforms | Fabric / Forge / NeoForge | **Fabric only** |

### Feature Details

#### 1. Speed Commands

```
/minecartspeed set <targets> <speed>    Set speed (blocks/sec, 0~30)
/minecartspeed get <targets>            Query speed
/minecartspeed remove <targets>         Restore default speed (8 blocks/sec)
/minecartspeed autoslowdown on|off    Toggle auto-slowdown on curves/slopes (default: on)
```

Requires OP level 2. Supports entity selectors like `@e[type=minecraft:minecart]`.

#### 2. Speed Controller (New)

- **Auto-grant on mount**: When a player sits in a minecart, a compass item named "Speed Controller" is automatically added to their inventory.
- **Left-click to speed up**: Left-click while holding the controller → minecart speed +2 blocks/sec.
- **Right-click to slow down**: Right-click while holding the controller → minecart speed -2 blocks/sec.
- **Auto-remove on dismount**: The controller is removed from inventory when leaving the minecart.
- Speed range: 0 ~ 30 blocks/sec, cooldown: 0.5 seconds.
- **Regular minecart only**: The controller only works on regular minecarts, not on chest minecarts, hopper minecarts, etc.

#### 3. Automatic Speed Adjustment (New)

When the custom speed **exceeds 8 blocks/sec**, and a minecart encounters a curve or slope (within 3 blocks ahead), it **automatically switches to default speed** (8 blocks/sec) and **restores** after passing:

| Detection | Description |
|-----------|-------------|
| Curve | Turning rail ahead |
| Slope | Ascending rail ahead |

> Only triggers when speed > 8 blocks/sec. Stationary minecarts do not trigger detection. Manual speed adjustment (command/controller) clears the auto state. Use `/minecartspeed autoslowdown off` to disable entirely.

#### 4. Non-OP Access Control (New)

Controls whether non-OP players can obtain and use the Speed Controller:

| Command | Effect |
|---------|--------|
| `/minecartspeed allowNonOp on` | Non-OP players can get and use controllers (default) |
| `/minecartspeed allowNonOp off` | Only admins can get and use controllers |

When **disabled**, non-OP players:
- No longer receive a controller when mounting a minecart
- Cannot use left-click/right-click to adjust speed (even if they already have a controller)

> Requires admin permission. OP players are always unaffected regardless of this setting.

### Installation

1. Download `minecart_speed-fabric-1.0.0.jar`
2. Place it in the server's `mods/` folder
3. **Server-side only**, no client installation required

**Requirements:** Minecraft 26.2, Fabric Loader >= 0.19.3, Fabric API

### Command Reference

```mcfunction
# Set the nearest minecart speed to 20 blocks/sec
/minecartspeed set @e[type=minecraft:minecart,limit=1,sort=nearest] 20

# Query all minecarts' speed
/minecartspeed get @e[type=minecraft:minecart]

# Restore default speed
/minecartspeed remove @e[type=minecraft:minecart,limit=1,sort=nearest]
```

### Speed Reference

| Blocks/sec | Description |
|-----------|-------------|
| 8.0 | Vanilla default |
| 16.0 | 2x speed |
| 24.0 | 3x speed |
| 30.0 | Maximum speed |

> Due to vanilla minecart physics, speeds above 30 blocks/sec have no practical effect.

### Building

```bash
cd fabric
./gradlew build
```

The compiled jar will be in `build/libs/`.

### Credits

- Original author: [MomoLawson](https://github.com/MomoLawson) — base framework and command system

### License

MIT

---

## 矿车速度 Plus

> [English Version](#minecart-speed-plus) | 基于 [MomoLawson/minecart-speed-mod](https://github.com/MomoLawson/minecart-speed-mod) 二次修改。

---

### 与原版的区别

| 功能 | 原版 | Plus |
|------|------|--------|
| 指令调速 | ✓ | ✓ |
| 速度控制器 | ✗ | ✓ |
| 左键加速 / 右键减速 | ✗ | ✓ |
| 弯道自动降速并恢复 | ✗ | ✓ |
| 上坡自动降速并恢复 | ✗ | ✓ |
| 检测开关指令 | ✗ | ✓ |
| 普通玩家访问控制 | ✗ | ✓ |
| 速度上限（0 ~ 30 方块/秒） | 无上限 | ✓ |
| 支持平台 | Fabric / Forge / NeoForge | **仅 Fabric** |

### 功能详解

#### 1. 指令调速

```
/minecartspeed set <targets> <speed>    设置速度（方块/秒，0~30）
/minecartspeed get <targets>            查询速度
/minecartspeed remove <targets>         恢复原版速度（8 方块/秒）
/minecartspeed autoslowdown on|off     开关自动降速（默认开启）
```

需要 OP 权限（level 2）。支持 `@e[type=minecraft:minecart]` 等实体选择器。

#### 2. 速度控制器（新增）

- **上车自动获得**：玩家乘坐矿车时，背包自动获得一个名为「速度控制器」的指南针物品
- **左键加速**：手持控制器左键挥手 → 矿车速度 +2 方块/秒
- **右键减速**：手持控制器右键挥手 → 矿车速度 -2 方块/秒
- **下车自动移除**：离开矿车时控制器自动从背包消失
- 速度范围：0 ~ 30 方块/秒，冷却 0.5 秒
- **仅普通矿车生效**：控制器仅在普通矿车上生效，运输矿车、漏斗矿车等不生效

#### 3. 自动变速（新增）

当自定义速度**超过 8 方块/秒**时，矿车行驶中遇到弯道或上坡（前方 3 格内），**自动切换为原版速度**（8 方块/秒），通过后**自动恢复**：

| 检测项目 | 说明 |
|----------|------|
| 弯道 | 前方出现转弯铁轨 |
| 上坡 | 前方出现上坡铁轨 |

> 仅速度 > 8 方块/秒时触发。静止矿车不触发检测。手动调速（指令/控制器）会清除自动状态。使用 `/minecartspeed autoslowdown off` 可完全关闭自动降速。

#### 4. 普通玩家访问控制（新增）

控制普通玩家是否能获得和使用速度控制器：

| 指令 | 效果 |
|------|------|
| `/minecartspeed allowNonOp on` | 普通玩家可获取并使用控制器（默认） |
| `/minecartspeed allowNonOp off` | 仅管理员可获取并使用控制器 |

关闭后，普通玩家：
- 乘坐矿车时不再自动获得控制器
- 左键/右键无法调整速度（即使背包中已有控制器）

> 需要管理员权限。OP玩家始终不受此设置影响。

### 安装

1. 下载 `minecart_speed-fabric-1.0.0.jar`
2. 放入服务器的 `mods/` 文件夹
3. **纯服务端 Mod**，客户端无需安装

**要求：** Minecraft 26.2，Fabric Loader >= 0.19.3，Fabric API

### 命令参考

```mcfunction
# 设置最近矿车速度为 20 方块/秒
/minecartspeed set @e[type=minecraft:minecart,limit=1,sort=nearest] 20

# 查询所有矿车速度
/minecartspeed get @e[type=minecraft:minecart]

# 恢复原版速度
/minecartspeed remove @e[type=minecraft:minecart,limit=1,sort=nearest]
```

### 速度参考

| 方块/秒 | 说明 |
|---------|------|
| 8.0 | 原版默认 |
| 16.0 | 2 倍速 |
| 24.0 | 3 倍速 |
| 30.0 | 最大速度 |

> 受原版矿车物理限制，超过 30 方块/秒的速度实际没有额外效果。

### 构建

```bash
cd fabric
./gradlew build
```

编译产物在 `build/libs/` 目录下。

### 致谢

- 原作者：[MomoLawson](https://github.com/MomoLawson) — 基础框架和指令系统

### License

MIT
