# ⚡ PowerSMP

<p align="center">
  <img src="assets/banner.png" alt="PowerSMP Banner" width="100%">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.20.1-brightgreen" alt="Minecraft Version">
  <img src="https://img.shields.io/badge/Java-17%2B-orange" alt="Java Version">
  <img src="https://img.shields.io/badge/Platform-Paper%2FSpigot-blue" alt="Platform">
  <img src="https://img.shields.io/badge/License-MIT-yellow" alt="License">
</p>

A modular, high-performance, production-grade Minecraft SMP plugin built for **Paper / Spigot 1.20.1+**.  
PowerSMP introduces customizable elemental powers, a high-stakes life-stealing system, combat logging protection, integrated economy, and teleportation systems.

---

## 🌟 Key Features

* **⚡ Elemental Powers & Abilities:** Unique passive buffs and active abilities (Flame, Speed, Tank, Shadow, Lightning) with built-in cooldown management.
* **❤️ Life System:** Players gain or lose lives on PvP kills/deaths. Reaching 0 lives eliminates players into Spectator mode or bans them.
* **⚔️ Combat Tracker:** Disables unsafe commands during PvP, tags combatants, and punishes combat loggers with instant death.
* **💰 Economy Engine:** Integrated banking and direct player-to-player payments.
* **🏠 Teleport System:** Warmup-based teleportation for `/spawn`, `/home`, `/sethome`, `/tpa`, and `/tpaccept`.
* **💾 Multi-Storage Backend:** Seamless async data persistence supporting both **SQLite** and **YAML**.

---

## 🛠️ Installation

1. Save your banner image as `banner.png` inside an `assets/` folder in your project root.
2. Download or compile the latest `PowerSMP-1.0-SNAPSHOT.jar`.
3. Place the `.jar` file into your server's `plugins/` directory.
4. Restart or start your Paper/Spigot server (requires **Java 17+** and **Minecraft 1.20.1**).
5. Configure settings in `plugins/PowerSMP/config.yml` and `messages.yml`.

---

## 💻 Commands & Permissions

### Player Commands
| Command | Description | Permission |
| :--- | :--- | :--- |
| `/power info` | View your currently active power | `powersmp.use` |
| `/power list` | View all available server powers | `powersmp.use` |
| `/lives` | Check your current life count | `powersmp.use` |
| `/balance` (`/bal`) | Check your account balance | `powersmp.use` |
| `/pay <player> <amount>` | Send money to another player | `powersmp.use` |
| `/tpa <player>` | Request to teleport to a player | `powersmp.use` |
| `/tpaccept` | Accept a pending teleport request | `powersmp.use` |
| `/spawn` | Teleport to world spawn | `powersmp.use` |
| `/sethome` | Set your personal home location | `powersmp.use` |
| `/home` | Teleport to your home location | `powersmp.use` |

### Admin Commands
| Command | Description | Permission |
| :--- | :--- | :--- |
| `/power set <player> <power>` | Assign a power to a player | `powersmp.admin.power` |
| `/lives give <player> <amount>` | Grant extra lives to a player | `powersmp.admin.lives` |

---

## 🔨 Compiling from Source

To build the plugin yourself using Maven:

```bash
git clone [https://github.com/tussle1/PowerSMP.git](https://github.com/tussle1/PowerSMP.git)
cd PowerSMP
mvn clean package