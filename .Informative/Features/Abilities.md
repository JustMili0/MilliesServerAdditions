# What are Player Abilities?
Player abilities are a complex ability, debuff and ability modifier system basing itself mostly on behavior found in Minecraft mobs but also based on lore of characters made by this mod creator's friends.

Player abilities can be chosen by players via the `/abilities` command. They can either pick from hardcoded premade sets or send a request to permission level 2 and higher staff for a custom set.<br>
Those sets are mainly designed around Minecraft mobs and some DnD classes and races.
Moderators, administrators and operators can also grant or revoke abilities, debuffs and modifiers of players in-game or by editing `/config/servertweaks/player_abilities.json` file.

Some abilities and/or debuffs can be partially or completely incompatible with each other, or they will just negate each other's effects on the player.

# Why were Player Abilities added?
They supposed to be and are a server-side custom alternative to Origins/Origins Legacy for servers managed by JustMili.

### Abilities & Ability Debuffs
```
FIRE_IMMUNE - Nether Mob-based | Immune to fire damage. Incompatible with BURNS_IN_DAYLIGHT.
LAVA_IMMUNE - Nether Mob-based | Immune lava damage.
HEAT_IMMUNE - Nether Mob-based | Immune to standing-on-magma damage.
FREEZE_IMMUNE - Custom | Immune to freezing damage (e.g. powdered snow).
FALL_IMMUNE - Cat-based | Immune to fall damage.
HEAT_SENSITIVE - Snow Golem-based | Takes 1hp fire damage every 1s in warm and hot biomes. Having an active Fire Resistance effect negates fire damage.
COLD_SENSITIVE - Custom | Takes 1hp freezing damage every 2s in cold biomes.
                 Wearing partial leather armor negates freezing damage and wearing full leather armor gets rid of the freezing overlay.

LIGHT - Chicken-based | Gains Slow Falling when falling faster than 0.4 blocks/tick, preventing fatal fall damage.
SWIFT - Custom | Gains Speed 1 while sprinting.
SLOW - Zombie-based | Permanently slowed to roughly zombie walking speed via movement speed attribute reduction.
HOPPY - Rabbit-based | Permanent hidden Jump Boost 1.
DWARF - Custom | Height capped at 0.75 of normal scale. Players already at or below 0.75 via /scale are unaffected. Permanent hidden Haste 2.
SQUISHY - Custom | Decreases fall and fly_into_wall damage by 75% each.
MAGNETIC - Custom | On-ground items within 6 blocks of the player get pulled towards the player

TOUGH - Iron Golem-based | Immune to knockback.
STRONG - Custom | +4hp (2 hearts) attack damage. Max health scales inversely with armor: 100hp naked, 40hp at full Netherite.

AQUA_GRACE - Dolphin-based | Gains Conduit Power and Dolphin's Grace while in water.
             Dolphin's Grace will not be applied if player already has Depth Strider above level 1.
BREATHES_UNDERWATER - Fish-based | Breathes underwater indefinitely.
CANT_BREATHE_AIR - Fish-based | Drowns on land. Air supply drains while out of water and restores while submerged.
                   Air will not deplete if player has a active Water Breathing effect.
CANT_SWIM - Undead-based | Sinks in water.
HYDROPHOBIC - Enderman-based | Takes 1 damage per second from standing in water, water cauldrons, or rain.
              Rain damage does not apply if wearing a helmet. Damage still applies if player is in a wet biome, even if wearing a helmet.

HUNTED_BY_FOX - Custom | Non-trusting foxes within 12 blocks will target and attack the player unprovoked.
HUNTED_BY_WOLF - Custom | Untamed wolves within 16 blocks will target and attack the player unprovoked.

SCARES_CREEPERS - Cat-based | Creepers within 8 blocks flee from the player.
SCARES_PHANTOMS - Cat-based | Phantoms within 16 blocks flee from the player.

FRIENDS_WITH_NATURE - Custom | Overworld neutral mobs will not aggro the player and tamed pets won't defend against them.
                               Foxes within 24 blocks auto-trust the player. 100% tame chance when taming animals.
WEAK_TO_DAMAGE - Custom | Takes 1.25x damage from all sources except fall damage.
NIGHT_VISION - Custom | Gains hidden Night Vision only during nighttime (level.isDarkOutside()).

BURNS_IN_DAYLIGHT - Undead-based | Burns in direct sunlight (requires skylight level > 8 and clear sky view) dealing 1hp per 1s. Helmet negates all burning.
                    Incompatible with FIRE_IMMUNE.
IS_MONSTER - Monster-based | Iron golems and snow golems attack the player. Villagers flee. Pillagers, zombies and skeletons ignore the player.
CLIMBS_WALLS - Spider-based | Can climb any solid wall.
PEARLING - Enderman-based | Ender Pearls never get used up when thrown.

PREDATORY - Custom | Smaller animals like for example chickens, fish and frogs will run away from the player.
CARNIVORE - Wild Animal-based | Can only eat meat. Honey bottles, milk and other misc items are always allowed.
VEGETARIAN - Farm Animal-based | Can only eat plant-based foods. Honey bottles, milk and other misc items are always allowed.
ONLY_EATS_SWEETS - Custom | Can only eat sweet foods (berries, cookies, cake, pie, honey, apples, melon slices).
GRASS_EATER - Sheep-based | Can shift-right-click any grass (tall, short, dry) or bush to eat it, gaining 2 hunger and 0.2 saturation. Consumes the block.
BUG_EATER - Frog-based | Can only feed on bug-like entities, slimes, magma cubes, sulfur cubes, and items like slime balls and magma creams.
```

### Ability Modifiers
```
Ability modifiers:
ADD_GOLD_FOODS_TO_DIET - Adds golden apples, enchanted golden apples and golden carrots to any active diet restriction.
```