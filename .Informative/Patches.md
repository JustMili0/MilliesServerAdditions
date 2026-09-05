# Millie's Server Additions, Patch #6 - 1.3.0 Full Release

### ***GENERIC***
- New *Update Patches* format. Changelog format will also be changing with update 1.3.1 for this and all other projects by or involving JustMili
- Updated to Minecraft 26.2 from 1.21.11
- Changed project's buildscript from Groovy to Kotlin
- Dropped dependency from SuperMartijn642's Config Lib and replaced it with a stripped down version of my Core Libs mod

### ***ADDED***
**Configuration**
- Added a config key for Villagers and tamable mobs to lose their AI when named "NoAI"
- Added a config key for min-max values usable by regular players for `/scale`
- Added a config key for `/fillextras` command

**Gameplay**
- Armor Stands named "display" in an anvil will be given arms when placed
- Shift-right-clicking an anvil with an Iron Ingot or Iron Block will repair it
  - Chipped Anvil - 33% chance of success with an Iron Ingot, or 100% with an Iron Block
  - Damaged Anvil - 25% chance of success with an Iron Ingot, or 80% with an Iron Block
  - Repairing an anvil can't fail more than 2 times, on 3rd try repair is always guaranteed
- Enchantment Book duplication is now possible by shift-right-clicking an Enchantment Table with an enchanted book in your offhand and a normal book in your main hand
  - The Enchanted Book can not have more than one enchantment on it
  - Duplication costs experience levels. Cost is calculated based on enchantment rarity, max enchantment level and current enchantment level
- All incompatible enchantments can now be combined with each other
  - Note that some enchantment effects may conflict or take priority over one another
- Some enchantments are now available at a higher max level than vanilla
  - What is made higher level can be found in [BoostedEnchantmentLevels.md](https://github.com/JustMili0/MilliesServerAdditions/blob/master/.Informative/Features/BoostedEnchantmentLevels.md)
- Names of invisible players become obfuscated in death/kill messages
- Tridents with Riptide now charge over 3 times faster (10t -> 3t charge time)

**Commands**
- Added `/smpperms <player> <permission_level>`, permission levels:
  - `default` - Regular player permissions
  - `moderator` - Can use all commands that a command block can
  - `administrator` - Can use all commands (except `/smpperms`), can not stop the server
  - `limited_operator` (owner but fair) - Operator permission level but limited to only a few commands essential (or so) for server moderation
  - `operator` (owner) - Regular operator permissions
  
***Millie's server permissions on Millie's Cove community Minecraft server are now set to `limited_operator`**

**Player Abilities**
- Added `canine` ability preset
- Added `scares_skeletons` - Skeletons within 16 blocks flee from the player
- Added `weaver` ability - Doesn't get slowed by Cobwebs (Requires client installation)
- Added `bovid` ability - Can be milked with a bucket by other players
- Added `squishy` ability - Decreases `fall` and `fly_into_wall` damage by 75% each
- Added `insectivore` debuff - Can feed on bug-like entities (Silverfish, Endermites, and size-1 Slimes and Magma Cubes) as well as items like Slimeballs, Magma Cream and Spider Eyes
  - Consuming a Cave Spider grants bonus nutrition but poisons player
  - Consuming a Sulfur Cube poisons player
  - Consuming a Magma Cube deals half a heart of fire damage

**Fixes & Workarounds**
- Worked around MC-271325 bug - prevents console sometimes being spammed with invalid disconnection packet errors

### ***BUG FIXES/TECHNICAL CHANGES***
**Configuration**
- Changed config file from `config\servertweaks\common.toml` into `config\servertweaks\common.properties`
- Renamed config key `removeAnvilLimit` to `disableAnvilLimit`
- Renamed config key `despawnMonsters` to `despawnMonstersPostAfk`
- All `limit.....Speed` as well as `enableScaleCommand`, `enableBanishCommand` and `enableDamageToggleCommand` config keys are now false by default

**Gameplay**
- You can now die in `servertweaks:banishment` dimension by `/kill` (or generally dealt more than 2^18 points of damage)

**Commands**
- Cleaned up and possibly optimized the code of all the command classes
- Renamed `/fillExtras` to `/fillextras`
- Updated texts and options in `/abilities`
  - `pickPreset <preset>` now utilizes `getDisplayName` instead of `getId` for informing that a preset had been applied
  - `pickPreset <preset>` now informs the player if any  or preset in given preset requires a client-side installation to function properly
  - `grant` and `revoke` now utilizes `getDisplayName` instead of `getId` for showing ability, debuff and modifier names
  - `grant <player> <ability|debuff|modifier> <id>` - Reworked from `grant <player> <abilityOrDebuff|modifier>`, now properly distinguishes between abilities, debuffs and modifiers
  - `revoke <player> <ability|debuff|modifier|everything> <id>` - Reworked from `revoke <player> <ability|modifier|everything> <abilityOrDebuff|modifier>`, now properly distinguishes between abilities, debuffs and modifiers
- Fixed one-hit mace exploit in `/afk` not resetting fall distance
- `/afk` now uses `Vec3` `player.position()` to store player position instead of individual x, y and z `double` values
- `/afk` can now show the exact remaining time of its cooldown in days, hours, minutes and seconds format instead of just seconds

**Player Abilities**
- Mod can be now installed client-side for some abilities to work properly
- Added `Debuff`, `TickingDebuff` and `TickingModifier` class
- Reworked registries and other core elements of player abilities
  - Class names and packages were altered a lot
  - Split Abilities registry class into Abilities and Debuffs
  - Large optimizations to AbilityProfiles, cutting down on repetitive code and making it more efficient
  - All abilities, debuffs and modifiers (including ticking) now define an Identifier, Display Name and whether it requires a client-side installation or not (for most; not)
  - All presets now define an Identifier and Display Name
- Fixed `"name"` entries in `player_abilities.json` getting removed on file update if the referenced player was offline
- All abilities, debuffs, modifiers and presets are no longer plain full-capitalized strings (`EXAMPLE_THING`) now being Identifiers (`mod_id:example_thing`)
- Some abilities and debuffs were renamed for consistency and better language
  - `FRIENDS_WITH_NATURE` -> `child_of_nature`
  - `GRASS_EATER` -> `herbivore`
  - `ONLY_EATS_SWEETS` -> `saccharivore`
  - `AQUA_GRACE` -> `aquatic_grace`
- All presets with diet restriction debuffs now come with `can_eat_golden_foods` modifier
- Added Big Dripleaf, Small Dripleaf, Vines, Cave Vines, Glow Lichen, Ferns, Large Ferns, Bushes, Firefly Bushes, Seagrass, Tall Seagrass, Sea Pickles and Kelp to `herbivore`'s diet tag
- Removed Jungle and its variants from biome tag `hot_biomes`
- Some abilities and debuffs now have special interactions with potion effects (mainly with Weakness and Poison)
- Decreased max health of `strong` ability from 100 to 80, minimum still being 40
- `scares_creepers` now has a 16 block range instead of 12
- Dolphin's Grace now applies to `aquatic_grace` with Depth Strider 2, below or none; if poisoned, only Depth Strider 1 or none, instead of just Depth Strider 1
- `fire_immune` and `lava_immune` now automatically get extinguished unless have Weakness effect
- `fall_immune` is no longer affected by Slow Falling unless has Weakness effect
- `freeze_immune` no longer gets the freezing overlay and hearts unless has Weakness effect
- `aquarian` preset no longer grants `breathes_underwater`, as water breathing is already provided by Conduit Power effect from `aquatic_grace` when in water
- Fixed and optimized damage immunity handling
- Fixed and readjusted damage multipliers for `weak_to_damage` debuff to match its descriptions in [PlayerAbilities.md](https://github.com/JustMili0/MilliesServerAdditions/blob/master/.Informative/Features/PlayerAbilities.md)
- Fixed being able to eat anything by just having it in your offhand
- Fixed `herbivore` debuff being able to eat foliage even when full
- Fixed food values not updating on client when eating foliage as `herbivore`
- Fixed block placement, block interactions and fish bucket place/pickup with any diet restriction debuffs applied
- Fixed `child_of_nature` ability - Taming chance with any animal is now 100%
- Fixed `hunted_by_fox` debuff - Foxes now attack even if you're not crouching
- Fixed `cant_swim` debuff - Player no longer can swim up in water (Requires client installation)
- Fixed `climbs_walls` ability - Now can actually climb walls (Requires client installation)
- Fixed `light` ability applying Slow Falling at any fall distance, even when going downstairs
  - Now requires 3 blocks or more of fall distance to apply Slow Falling
- Fixed `aquatic_grace` checking HEAD equipment slot instead of FEET for Depth Strider enchantment
- Fixed multiple issues with `is_monster` debuff, and made it more efficient

Feedback is appreciated ^^
