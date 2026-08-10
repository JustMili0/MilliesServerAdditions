## 1.3.0+mc26.1.2 Changelog
***Most if not all features added in this update ARE CONFIGURABLE***

### ***GENERAL***
- Mod renamed from "Useful & Fun Server Tweaks" to "Millie's Server Additions"
- License changed from All-Rights-Reserved to MIT
- Updated to Minecraft 26.1.2

### ***CONFIGURATION***
- Configuration file has been moved to `\config\servertweaks\` directory. Previous configuration files won't be used anymore
- Config file format changed from `.toml` to `.properties`
- Renamed config key `removeAnvilLimit` to `disableAnvilLimit`
- All `limit*****Speed` config keys are now false by default
- `enableScaleCommand` and `enableBanishCommand` config keys are now false by default

### ***NEW CONTENT***
- Special Player Abilities
  - Feature disabled by default
  - Abilities are based on Minecraft mobs and other fictional entities similar to DnD but not exactly.
  - Player abilities and ability modifiers can be modified in-game with `/abilities` command or editing the `config/servertweaks/player_abilities.json` file.
- Right-Click-Harvest
  - Yes fortune applies
  - Larger affect area when using a hoe
- You can now stop the AI of tamable animals and villagers by naming them `NoAI`
- `/discard <target>` command, allows operators discard entities, blocks, and clear inventories (of any entity/block)
  - Entity discard - basically `/kill` without dropping loot or XP, just poof, and it's gone
  - Block discard - basically `/setblock x y z minecraft:air` without specifying air and no items or anything else (like XP) drops from the block
  - Inventory discard - basically `/clear` mixed with `/data` but much less complex
- `/fillExtras` command because vanilla didn't have what I needed
  - `/fill` but with more robust options:
    - `replaceOnly` - Basically vanilla's `replace` I guess
    - `destroyOnly` - Destroys only specified block
    - `silkDestroy` - Destroys all blocks with silk touch effect
    - `silkDestroyOnly` - `destroyOnly` but with silk touch effect
    - `fortuneDestroy` - Destroys all blocks with fortune effect, fortune level has to be specified
    - `fortuneDestroyOnly` - `destroyOnly` but with fortune effect, fortune level has to be specified
- `/fly <player>` command, allows for creative flight in survival
  - Can also be executed standalone as a toggle (`/fly` with no params)
- Shift-right-clicking anvils with an Iron Ingot or Iron Block will repair it
  - Chipped Anvil - 33% chance of success with an Iron Ingot, or 100% with an Iron Block
  - Damaged Anvil - 25% chance of success with an Iron Ingot, or 80% with an Iron Block
  - Repairing an anvil can't fail more than 2 times, on 3rd try repair is always guaranteed
- Previously incompatible enchantments such as different protection types, mending and infinity etc. are now compatible
- Some enchantments can now be at higher levels than vanilla
  - All protection types - up to level 5 (Vanilla: 4)
  - Sharpness - up to level 6 (Vanilla: 5)
  - Smite - up to level 6 (Vanilla: 5)
  - Bane of Arthropods - up to level 5 (Vanilla: 4)
  - Looting - up to level 4 (Vanilla: 3)
  - Lunge - up to level 5 (Vanilla: 3)
  - Efficiency - up to level 6 (Vanilla: 5)
  - Feather Falling - up to level 5 (Vanilla: 4)
  - Frost walker - up to level 5 (Vanilla: 2)
  - Unbreaking - up to level 5 (Vanilla: 3)
  - Multishot - up to level 3 (Vanilla: 1)
- Armor Stands named "display" will be given arms
- You can now duplicate enchantment books (at a level cost) by shift-right-clicking with an enchanted book in your offhand and a regular book in your main hand on an Enchantment Table
- You can now die in the Banishment dimension (if the damage you've been delt is more than 2^18)
  - Added that so `/kill` actually works in there
- Invisible players when killing others or dying will have their names obfuscated

### ***CHANGES***
- `/damagetoggle <damage_type> <true|false|status>`, reworked into `/damageToggle <options>`:
  - Removed `<damage_type> true` - Replaced with `enable <damage_type>`
  - Removed `<damage_type> false` - Replaced with `disable <damage_type>`
  - Removed `<damage_type> status` - Replaced with `get <damage_type>`
  - Added `listDisabled` - Lists all currently disabled damage types
  - Added `enableAll` - Enables all disabled damage types
  - Added `disableAll` - Disables all known damage types
- `/scale` now has configurable min-max values in the config
- `/afk` now uses `BlockPos` rather than individual x, y and z coordinates
- Patched one-hit mace exploit with `/afk`
- Changed permission requirements for commands
  - `/scale <force|unlock|reset|reset-nounlock>` - Permission Level Required `ADMINS (4)` -> `MODERATORS (1)`
  - `/banish` - Permission Level Required `ADMINS (4)` -> `MODERATORS (1)`
  - `/damageToggle` - Permission Level Required `ADMINS (4)` -> `GAMEMASTERS (2)`

### ***REMOVED***
- Removed `/daycount` command
- Removed `/duel` command

### ***BUG FIXES/TECHNICAL CHANGES***
- Removed "[ServerTweaks] " prefix from all messages sent by the mod

### ***DEV STUFF***
- A lot of package and class renames
- Changed project's buildscript from Groovy to Kotlin
- Dropped dependency on SuperMartijn642's Config Lib, replaced it with a stripped down version of my Core Libs mod
- Changed archives base name `UnF-ServerTweaks` to `MilliesServerAdditions`