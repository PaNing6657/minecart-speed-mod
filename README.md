# Minecart Speed

A server-side Fabric/Forge/NeoForge mod for **Minecraft 26.2** that allows per-minecart speed control via commands. Set different speeds for individual minecarts instead of a global speed!

## Features

- **Per-minecart speed control** — each minecart can have its own speed
- **Persistent** — custom speeds survive server restarts and chunk unloads
- **Server-side only** — clients don't need to install this mod
- **Simple commands** — easy-to-use slash commands with entity selectors
- **Multi-platform** — supports Fabric, Forge, and NeoForge

## Installation

Download the JAR for your mod loader:

| Mod Loader | JAR File |
|------------|----------|
| Fabric | `minecart_speed-fabric-1.0.0.jar` |
| Forge | `minecart_speed-forge-1.0.0.jar` |
| NeoForge | `minecart_speed-neoforge-1.0.0.jar` |

Place the JAR file in your server's `mods/` folder and start the server.

**Note:** This mod only needs to be installed on the server. Clients connecting to the server do not need to install anything.

## Commands

All commands require **OP level 2** (admin) permission.

### Set Speed

```
/minecartspeed set <targets> <speed>
```

Set the maximum speed for one or more minecarts.

- `<targets>` — entity selector (e.g. `@e[type=minecraft:minecart,limit=1]`)
- `<speed>` — speed in blocks per second (vanilla default is 8.0)

**Examples:**

```mcfunction
# Set the nearest minecart to 20 blocks/s (2.5x vanilla speed)
/minecartspeed set @e[type=minecraft:minecart,limit=1,sort=nearest] 20

# Set all minecarts within 10 blocks to 50 blocks/s
/minecartspeed set @e[type=minecraft:minecart,distance=..10] 50

# Set a specific minecart by tag
/minecartspeed set @e[type=minecraft:minecart,tag=express] 100
```

### Get Speed

```
/minecartspeed get <targets>
```

Query the current custom speed of one or more minecarts.

**Example:**

```mcfunction
/minecartspeed get @e[type=minecraft:minecart,limit=1,sort=nearest]
```

Output: `Minecart [a1b2c3d4] custom speed: 20.00 blocks/s (1.0000 blocks/tick)`

### Remove Custom Speed

```
/minecartspeed remove <targets>
```

Remove custom speed and revert to vanilla speed (8 blocks/s).

**Example:**

```mcfunction
/minecartspeed remove @e[type=minecraft:minecart,limit=1,sort=nearest]
```

## Speed Reference

| Blocks/s | Description |
|:---------|:------------|
| 8.0      | Vanilla default |
| 16.0     | 2x speed |
| 32.0     | 4x speed |
| 64.0     | 8x speed |
| 100.0    | 12.5x speed (very fast!) |

## Supported Minecart Types

This mod works with all vanilla minecart types:

- `minecraft:minecart` — Regular Minecart
- `minecraft:chest_minecart` — Minecart with Chest
- `minecraft:furnace_minecart` — Minecart with Furnace
- `minecraft:tnt_minecart` — Minecart with TNT
- `minecraft:hopper_minecart` — Minecart with Hopper
- `minecraft:command_block_minecart` — Minecart with Command Block

## Building from Source

Each platform has its own directory with its own build system:

```bash
# Fabric
cd fabric && ./gradlew build

# Forge
cd forge && ./gradlew build

# NeoForge
cd neoforge && ./gradlew build
```

The compiled JAR will be in `build/libs/` under each platform directory.

## Technical Details

- Custom speed is stored in the minecart's NBT data under the key `MinecartSpeedModSpeed`
- Speed is stored in blocks per second and converted to blocks/tick internally (÷20)
- The mod uses a Mixin on `AbstractMinecart.getMaxSpeed()` to override vanilla speed limits
- Compatible with other mods that don't modify minecart speed behavior

## License

MIT
