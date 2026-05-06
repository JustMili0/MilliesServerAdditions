This file was made specifically for Millie's discord announcements about small patches and additions being added to the main server mod.

------------------------------------------------------------------------------------------------------------------------

# UnF ServerTweaks Patch #1 - 1.3.0-beta.11

**Player Abilities**
- AQUA_GRACE - Dolphin's Grace will not be applied if player already has Depth Strider above level 1.
- CANT_BREATHE_AIR - Air will not deplete if player has a active Water Breathing effect.
- IS_MONSTER - Villagers no longer flee at insane speeds
- IS_MONSTER - Adjusted Zombie and Skeleton ignore ranges to be accurate to their detection ranges

------------------------------------------------------------------------------------------------------------------------

# UnF ServerTweaks Patch #2 - 1.3.0-beta.12
**Player Abilities**
- (NEW) HEAT_SENSITIVE - Takes 1hp of fire damage every second is hot biomes, with exceptions (Damage can be neutralized with Fire Resistance)
- (NEW) COLD_SENSITIVE - Starts freezing in cold/snowy biomes, taking 1hp every 2s or so (Can be countered with leather armor)
- HYDROPHOBIC - Added more wet biomes to the list of biomes where a helmet won't save you from rain damage (Added all ocean biomes)

Feedback is appreciated ^^

------------------------------------------------------------------------------------------------------------------------

# UnF ServerTweaks Patch #3 - 1.3.0-beta.13
**Commands**
- (NEW) `/abilities <option>`, Options:
    - `pickPremadeSet <set>` - Allows a player to choose a premade set of abilities, debuffs and modifiers mainly based on DnD and Minecraft mobs
        - A brief description will be sent in chat for the player before the player can either confirm or reject getting the set they chose
            - Currently only "feline" is available.
        - Has option `Custom` - "Contact any online staff that you'd like a custom set. Your chosen abilities, debuffs and ability modifiers will be reviewed by staff and implemented if it's compliant with server's ability creation guidelines if there are any."
    - (Admin) `reload` - Reloads abilities from file

**Player Abilities**
- COLD_SENSITIVE - Freezing overlay disappears when wearing full leather armor. Freezing damage stops with just partial leather armor but freezing overlay stays.

Feedback is appreciated ^^

------------------------------------------------------------------------------------------------------------------------

# Millie's Server Additions, Patch #4 - 1.3.0-beta.14
**Generic**
- Mod name changed

**Commands**
- `/abilities <options>`, new options:
    - New presets for `pickPremadeSet <set>`
    - (Admin) `grant <player> <abilityOrDebuff|modifier>` - Allows permission level 2 and above staff to grant players abilities, ability debuffs or ability modifiers
    - (Admin) `revoke <player> <ability|modifier|everything> <abilityOrDebuff|modifier>` - Allows permission level 2 and above staff to revoke players' abilities, ability debuffs, ability modifiers or the entire abilities profile
- `/damagetoggle <damage_type> <true|false|status>`, was reworked into `/damagetoggle <options>`:
    - Removed `<damage_type> true` - Replaced with `enable <damage_type>`
    - Removed `<damage_type> false` - Replaced with `disable <damage_type>`
    - Removed `<damage_type> status` - Replaced with `get <damage_type>`
    - Added `listDisabled` - Lists all currently disabled damage types
    - Added `enableAll` - Enables all disabled damage types
    - Added `disableAll` - Disables all known damage types

**Player Abilities**
- Dropped hardcoded ability sets for JustMili (SillyMili), Flufaye and Zarsai
  - These players will receive custom ability sets
- Some abilities now instantly `return;` and do not execute if player isn't in survival
- Fixed some diets preventing the player from placing blocks like Glow Berries, Sweet Berries etc.
- STRONG - Fixed player health resetting back to 20hp after relogging
- IS_MONSTER - Fixed Zombie and Skeleton ignore behaviors and made Husks, Parched, Drowned, Vindicators, Evokers, Witches and Slimes also ignore the player
- IS_MONSTER - Fixed Villager fear behavior, now running away at their usual speed
- COLD_SENSITIVE - Removed `cold_ocean` and `deep_cold_ocean` from list tag of cold biomes due to how frequent they are in the world
- HEAT_SENSITIVE - Damage is no longer dealt if player is in a roofed, shaded area, if standing in water, if it's raining at player position or if it's dark outside
- GRASS_EATER - Added underwater foliage and sugar cane to diet list tag
- GRASS_EATER - Client now gets updated about updated food values
- GRASS_EATER - Now properly restricts food consumption when no other diets are applied
- SLOW - Decreased slowness modifier from `-0.47` multiplier to `-0.32` multiplier
- FIRE_IMMUNE - Split into `FIRE_IMMUNE`, `LAVA_IMMUNE` and `HEAT_IMMUNE` for very specific use cases
  - (NEW) LAVA_IMMUNE - Protects against lava damage
  - (NEW) HEAT_IMMUNE - Protects against hot floor (magma) damage
- FIRE_IMMUNE - Now only protects against fire and on-fire damage
- (NEW) LAVA_IMMUNE - Immune lava damage
- (NEW) HEAT_IMMUNE - Immune to standing-on-magma damage
- (NEW) PREDATORY - Smaller animals like for example chickens, fish and frogs will run away from the player
- (NEW) PEARLING - Ender Pearls never get used up when thrown

Feedback is appreciated ^^

------------------------------------------------------------------------------------------------------------------------

# Millie's Server Additions, Patch #5 - 1.3.0-beta.15
**Gameplay**
- You can now stop the AI of tamable animals and villagers by naming them `NoAI`

**Commands**
- Fixed container discarding for any and all `AbstractChestedHorse` entities (Any entities that can have chests mounted on them for storage) with `/discard inventory <entity>`
- (Admin) Changed permission and execution requirements for most commands
  - `/scale <force|unlock|reset|reset-nounlock>` - Permission Level Required `GAMEMASTERS (2)` -> `MODERATORS (1)`
  - `/scale <force|unlock|reset|reset-nounlock>` - Execution Source Required `PLAYER` -> `PLAYER OR SERVER`
  - `/abilities <reload|grant|revoke>` - Permission Level Required `GAMEMASTERS (2)` -> `MODERATORS (1)`
  - `/fly` - Permission Level Required `ADMINS (3)` -> `MODERATORS (1)`
  - `/fillExtras` - Permission Level Required `GAMEMASTERS (2)` -> `MODERATORS (1)`
  - `/discard` - Permission Level Required `GAMEMASTERS (2)` -> `MODERATORS (1)`
  - `/banish` - Permission Level Required `ADMINS (3)` -> `MODERATORS (1)`

**Player Abilities**
- Fixed ability preset confirmation message being sent to the server instead of the client
- Fixed cancelling choosing an ability preset (the `[CANCEL]` button)
- (NEW) MAGNETIC - On-ground items within 6 blocks of the player get pulled towards the player to  be picked up

Feedback is appreciated ^^

------------------------------------------------------------------------------------------------------------------------

(AWAITING COMPLETION, WIP)
# Millie's Server Additions, Patch #6 - 1.3.0-beta.16
**Generic**
- Changed mod id from `servertweaks` to `serveradditions`
- Changed config file location from `config/servertweaks/common.toml` to `config/serveradditions/config.toml`
- Villagers and Tamables losing AI when named "NoAI" now has a config entry

**Player Abilities**
- Fixed ability debuff `WEAK_TO_DAMAGE` - Now actually deals more damage
- [TODO] Fixed ability debuff `HUNTED_BY_FOX` - Foxes now attack even if you're not crouching
- [TODO] Fixed ability debuff `CANT_SWIM` - Players no longer can swim up higher than one block up from the (sea) floor
- [TODO] Fixed ability `FRIENDS_WITH_NATURE` - Taming chance with any animal is now 100%
- [TODO] Fixed ability `CLIMBS_WALLS` - Now can climb walls
- (NEW) SQUISHY - Decreases `fall` and `fly_into_wall` damage by 75% each.
- [TODO] (NEW) BUG EATER - Can only feed on bug-like entities, slimes, magma cubes, and items like slime balls and magma creams.
  - With Minecraft 26.2, you'll also be able to consume sulfur cubes
    - If you eat a sulfur cube with a special block inside it, some effect will be given (e.g. TNT = You blow up, Magma Block = Slight Fire damage)

Feedback is appreciated ^^