# Minecart Speed (Modified) / 矿车速度（修改版）


基于 [MomoLawson/minecart-speed-mod](https://github.com/MomoLawson/minecart-speed-mod) 二次修改。Based on a fork of the original mod.

---

### 与原版的区别

| 功能 | 原版 | 修改版 |
|------|------|--------|
| 指令调速 | ✓ | ✓ |
| 速度控制器 | ✗ | ✓ |
| 左键加速 / 右键减速 | ✗ | ✓ |
| 弯道自动降速并恢复 | ✗ | ✓ |
| 上坡自动降速并恢复 | ✗ | ✓ |
| 前方方块阻挡自动降速并恢复 | ✗ | ✓ |
| 速度上限（0 ~ 100 方块/秒） | 无上限 | ✓ |
| 支持平台 | Fabric / Forge / NeoForge | **仅 Fabric** |

### 功能详解

#### 1. 指令调速（原版功能）

```
/minecartspeed set <targets> <speed>    设置速度（方块/秒，0~100）
/minecartspeed get <targets>            查询速度
/minecartspeed remove <targets>         恢复原版速度（8 方块/秒）
```

需要 OP 权限（level 2）。支持 `@e[type=minecraft:minecart]` 等实体选择器。

#### 2. 速度控制器（新增）

- **上车自动获得**：玩家乘坐矿车时，背包自动获得一个名为「速度控制器」的指南针物品
- **左键加速**：手持控制器左键挥手 → 矿车速度 +2 方块/秒
- **右键减速**：手持控制器右键挥手 → 矿车速度 -2 方块/秒
- **下车自动移除**：离开矿车时控制器自动从背包消失
- 速度范围：0 ~ 100 方块/秒，冷却 0.5 秒

#### 3. 自动变速（新增）

矿车行驶中遇到以下情况（前方 5 格内），**自动切换为原版速度**（8 方块/秒），通过后**自动恢复**：

| 检测项目 | 说明 |
|----------|------|
| 弯道 | 前方出现转弯铁轨 |
| 上坡 | 前方出现上坡铁轨 |
| 方块阻挡 | 前方出现非铁轨的实体方块 |

> 静止矿车不触发检测。手动调速（指令/控制器）会清除自动状态。

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
| 32.0 | 4 倍速 |
| 64.0 | 8 倍速 |
| 100.0 | 最大速度 |

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
