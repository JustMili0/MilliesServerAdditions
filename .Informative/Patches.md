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

Feedback is appriciated ^^

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

Feedback is appriciated ^^

------------------------------------------------------------------------------------------------------------------------

(AWAITING COMPLETION, WORK IN PROGRESS)
# Millie's Server Additions, Patch #4 - 1.3.0-beta.14
**Generic**
- Mod name changed

**Commands**
- `/abilities <option>`, Options:
    - New presets for `pickPremadeSet <set>`
    - (Admin) `wipeAbilitiesProfile` - Erases a player from `player_abilities.json` file and resets `picked_ability_preset` player variable back to false
    - (Admin) `grant <player> <abilityOrDebuff | modifier>` - Allows permission level 2 and above staff to grant players abilities, ability debuffs or ability modifiers
    - (Admin) `revoke <player> <abilityOrDebuff | modifier>` - Allows permission level 2 and above staff to revoke players' abilities, ability debuffs or ability modifiers

**Player Abilities**
- Dropped hardcoded ability sets for JustMili (SillyMili), Flufaye and Zarsai
  - These players will receive custom ability sets
- Some abilities now instantly `return;` and do not execute if player isn't in survival
- IS_MONSTER - Fixed Zombie and Skeleton ignore behaviors and made Husks, Parched, Drowned, Vindicators, Evokers, Witches and Slimes also ignore the player
- IS_MONSTER - Fixed Villager fear behavior, now running away at their usual speed
- COLD_SENSITIVE - Removed `cold_ocean` and `deep_cold_ocean` from list tag of cold biomes due to how frequent they are in the world
- GRASS_EATER - Added underwater foliage and sugar cane to diet list tag
- GRASS_EATER - Client now gets updated about updated food values
- GRASS_EATER - Now properly restricts food consumption when no other diets are applied
- SLOW - Decreased slowness modifier
- FIRE_IMMUNE - Split into `FIRE_IMMUNE`, `LAVA_IMMUNE` and `HEAT_IMMUNE` for very specific use cases
- FIRE_IMMUNE - Now only protects against fire and on-fire damage
- (NEW) LAVA_IMMUNE - Immune lava damage
- (NEW) HEAT_IMMUNE - Immune to standing-on-magma damage
- (NEW) PREDATORY - Smaller animals like for example chickens, fish and frogs will run away from the player.
- (NEW) PEARLING - ??? (Check TODO)    ***NOT DONE YET***

Feedback is appriciated ^^

------------------------------------------------------------------------------------------------------------------------

(AWAITING COMPLETION, NOT STARTED IN THE SLIGHTEST)
# Millie's Server Additions, Patch #5 - 1.3.0-beta.15

**Player Abilities**
- (NEW) SQUISHY - Decreases fall and fly_into_wall damage by 75% each.
- (NEW) BUG EATER - Can only feed on bug-like entities, slimes, magma cubes, sulfur cubes, and items like slime balls and magma creams.
- (NEW) MAGNETIC - ??? (Check TODO)

Feedback is appriciated ^^