# What are Player Abilities?


## Why were Player Abilities added?
They supposed to be and are a server-side custom alternative to Origins/Origins Legacy for servers managed by JustMili.

### Abilities & Ability Debuffs
```
Fire Immune - Nether Mob-based | Immune to fire damage. Incompatible with BURNS_IN_DAYLIGHT.
Lava Immune - Nether Mob-based | Immune lava damage.
Heat Immune - Nether Mob-based | Immune to standing-on-magma damage.
Freeze Immune - Custom | Immune to freezing damage (e.g. powdered snow).
Fall Immune - Cat-based | Immune to fall damage.
Heat Sensitive - Snow Golem-based | Takes 1hp fire damage every 1s in warm and hot biomes. Having an active Fire Resistance effect negates fire damage.
Cold Sensitive - Custom | Takes 1hp freezing damage every 2s in cold biomes.
                 Wearing partial leather armor negates freezing damage and wearing full leather armor gets rid of the freezing overlay.

Light - Chicken-based | Gains Slow Falling when falling faster than 0.4 blocks/tick, preventing fatal fall damage.
Swift - Custom | Gains Speed 1 while sprinting.
Slow - Zombie-based | Permanently slowed to roughly zombie walking speed via movement speed attribute reduction.
Hoppy - Rabbit-based | Permanent hidden Jump Boost 1.
Dwarf - Custom | Height capped at 75% of normal scale. Players already at or below 75% via /scale are unaffected. Permanent hidden Haste 2.
Squishy - Custom | Decreases fall and fly_into_wall damage by 75% each.
Magnetic - Custom | On-ground items within 6 blocks of the player get pulled towards the player

Tough - Iron Golem-based | Immune to knockback.
Strong - Custom | +4hp (2 hearts) attack damage. Max health scales inversely with armor: 100hp naked, 40hp at full Netherite.

Aqua Grace - Dolphin-based | Gains Conduit Power and Dolphin's Grace while in water.
             Dolphin's Grace will not be applied if player already has Depth Strider above level 1.
Breathes Underwater - Fish-based | Breathes underwater indefinitely.
Can't Breathe Air - Fish-based | Drowns on land. Air supply drains while out of water and restores while submerged.
                   Air will not deplete if player has a active Water Breathing effect.
CCan't Swim- Undead-based | Prevents the player from swimming up more than a block from the bottom of a body of water.
Hydrophobic - Enderman-based | Takes 1 damage per second from standing in water, water cauldrons, or rain.
              Rain damage does not apply if wearing a helmet. Damage still applies if player is in a wet biome, even if wearing a helmet.

Hunted By Fox - Custom | Non-trusting foxes within 12 blocks will target and attack the player unprovoked.
Hunted By Wolf - Custom | Untamed wolves within 16 blocks will target and attack the player unprovoked.

Scares Creepers - Cat-based | Creepers within 8 blocks flee from the player.
Scares Phantoms - Cat-based | Phantoms within 16 blocks flee from the player.

Child of Nature - Custom | Overworld neutral mobs will not aggro the player and tamed pets won't defend against them.
                               Foxes within 24 blocks auto-trust the player. 100% tame chance when taming animals.
Weak to Damage - Custom | Takes 25% more damage from all sources except fall damage.
Night Vision - Custom | Gains hidden Night Vision only during nighttime (level.isDarkOutside()).

Burns tn Daylight - Undead-based | Burns in direct sunlight (requires skylight level > 8 and clear sky view) dealing 1hp per 1s. Helmet negates all burning.
                    Incompatible with FIRE_IMMUNE.
Is Monster - Monster-based | Iron golems and snow golems attack the player. Villagers flee. Pillagers, zombies and skeletons ignore the player.
Climbs Walls - Spider-based | Can climb any solid wall.
Pearling - Enderman-based | Ender Pearls never get used up when thrown.

Predatory - Custom | Smaller animals like for example chickens, fish and frogs will run away from the player.
Bovid - Cow-based | Allows the player to be milked with a bucket.
Iron Belly - Custom | Prevents getting Poison and Hunger effect from eating rotten or raw (vanilla) foods. Can be negated with Weakness effect.

Carnivore - Wild Animal-based | Can only eat meat. Honey bottles, milk and other misc items are always allowed.
Vegetarian - Farm Animal-based | Can only eat plant-based foods. Honey bottles, milk and other misc items are always allowed.
Saccharivore - Custom | Can only eat sweet foods (berries, cookies, cake, pie, honey, apples, melon slices).
Herbivore - Sheep-based | Can shift-right-click any grass (tall, short, dry) or bush to eat it, gaining 2 hunger and 0.5 saturation points. Consumes the block.
Insectivore - Frog-based | Can only feed on bug-like entities, slimes, magma cubes, sulfur cubes, and items like slime balls and magma creams.
```

### Ability Modifiers
```
Ability modifiers:
Can Eat Golden Foods - Adds golden apples, enchanted golden apples and golden carrots to any active diet restriction.
```