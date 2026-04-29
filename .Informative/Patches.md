This file was made specifically for Millie's discord announcements about small patches and additions being added to the main server mod.

------------------------------------------------------------------------------------------------------------------------

# UnF ServerTweaks Patch #1 - 1.3.0-beta.11
**Player Abilities**
- AQUA_GRACE - Dolphin's Grace will not be applied if player already has Depth Strider above level 1.
- CANT_BREATHE_AIR - Air will not deplete if player has a active Water Breathing effect.
- IS_MONSTER - Villagers no longer flee at insane speeds
- IS_MONSTER - Adjusted Zombie and Skeleton ignore ranges to be accurate to their detection ranges

Feedback is appriciated ^^

------------------------------------------------------------------------------------------------------------------------

# UnF ServerTweaks Patch #2 - 1.3.0-beta.12
**Player Abilities**
- (NEW) HEAT_SENSITIVE - Takes 1hp of fire damage every second is hot biomes, with exceptions (Damage can be neutralized with Fire Resistance)
- (NEW) COLD_SENSITIVE - Starts freezing in cold/snowy biomes, taking 1hp every 2s or so (Can be countered with leather armor)
- HYDROPHOBIC - Added more wet biomes to the list of biomes where a helmet won't save you from rain damage (Added all ocean biomes)

Feedback is appriciated ^^

------------------------------------------------------------------------------------------------------------------------

(AWAITING COMPLETION)
# UnF ServerTweaks Patch #3 - 1.3.0-beta.13
**Commands**
- (NEW) `/abilities <option>`, Options:
  - `pickPremadeSet <set>` - Allows a player to choose a premade set of abilities, debuffs and modifiers mainly based on DnD and Minecraft mobs
    - A brief description will be sent in chat for the player before the player can either confirm or reject getting the set they chose 
    - Has option `Custom` - "Contact any online staff that you'd like a custom set. Your chosen abilities, debuffs and ability modifiers will be reviewed by staff and implemented if it's compliant with server's ability creation guidelines if there are any."
  - (Admin) `reload` - Reloads abilities from file
  - (Admin) `wipeAbilitiesProfile` - Erases a player from `player_abilities.json` file and resets `picked_ability_preset` player variable back to false
  - (Admin) `grant <player> <abilityOrDebuff | modifier>` - Allows permission level 2 and above staff to grant players abilities, ability debuffs or ability modifiers
  - (Admin) `revoke <player> <abilityOrDebuff | modifier>` - Allows permission level 2 and above staff to revoke players' abilities, ability debuffs or ability modifiers

**Player Abilities**
- COLD_SENSITIVE - Freezing overlay disappears when wearing full leather armor. Freezing damage stops with just partial leather armor but freezing overlay stays.
- (NEW) Bug Eater - ??? (Check TODO)
- (NEW) Predatory - ??? (Check TODO)
- (NEW) Squishy - ??? (Check TODO)
- (NEW) Pearling - ??? (Check TODO)