## 1.3.0+mc26.1.2 Changelog

### ***CONFIGURATION***
- Configuration file has been moved to `\config\serveradditions\` directory. Previous configuration files won't be used anymore

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
        - `fortuneDestroy` - Destroys all blocks with fortune effect, fortune level ahs to be specified
        - `fortuneDestroyOnly` - `destroyOnly` but with fortune effect, fortune level ahs to be specified
- `/fly <player>` command, allows for creative flight in survival
    - Can also be executed standalone as a toggle (`/fly` with no params)

### ***CHANGES***
- `/damagetoggle <damage_type> <true|false|status>`, was reworked into `/damagetoggle <options>`:
    - Removed `<damage_type> true` - Replaced with `enable <damage_type>`
    - Removed `<damage_type> false` - Replaced with `disable <damage_type>`
    - Removed `<damage_type> status` - Replaced with `get <damage_type>`
    - Added `listDisabled` - Lists all currently disabled damage types
    - Added `enableAll` - Enables all disabled damage types
    - Added `disableAll` - Disables all known damage types
- Changed permission and execution requirements for commands
    - `/scale <force|unlock|reset|reset-nounlock>` - Permission Level Required `GAMEMASTERS (2)` -> `MODERATORS (1)`
    - `/scale <force|unlock|reset|reset-nounlock>` - Execution Source Required `PLAYER` -> `PLAYER OR SERVER`
    - `/banish` - Permission Level Required `ADMINS (3)` -> `MODERATORS (1)`

### ***REMOVED***
- Removed `/daycount` command
- Removed `/duel` command

### ***BUG FIXES/TECHNICAL CHANGES***
- Removed "[ServerTweaks] " prefix from all messages sent by the mod

### ***DEV STUFF***
- A lot of package and file renames