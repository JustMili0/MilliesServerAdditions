# TODO:
- Finish writing presets
- Figure out why suggestions for `/abilities grant|revoke` don't work
- Fix discarding containers on entities
- Fix abilities
  - Fix hp resetting to 10 hearts with 
  - Fix not being able to place any edible but placeable foods with diets other than vegetarian
  - Fix nausea appliance when eating non-diet foods
  - Fix kinda-working abilities
  - Fix non-working abilities
- Add new abilities
  - Bug Eater (Feeds exclusively on slime balls, magma balls, insect/bug-like mobs, slimes (entity) and magma cubes (entity, deals a heart of fire damage when consumed))
    - For 26.2: Also can eat Sulfur Cubes but will get poisoned with nausea, but will get a lot of saturation
  - Squishy (reduces fall and fly_into_wall damage)
  - Magnetic (nearby items come to player)

--------------------------------------------------------------------------------–--------------------------------------

## FROM TESTING PLAYER ABILITIES:
**Not yet coded**
- SQUISHY
- MAGNETIC
- PEARLING
- BUG_EATER

**Doesn't work (no changes get applied to player behavior):**
- CLIMBS_WALLS - Nothing happens (can't climb walls)
- WEAK_TO_DAMAGE - Nothing happens (same damage as normal)
- CANT_SWIM - Nothing happens (can swim as normal)

**Kinda work (works for the most part)**
- HUNTED_BY_FOX - Foxes are too scared to approach and attack unless the player is crouching
- FRIENDS_WITH_NATURE - Calms down neutral animals such as wolves, bees etc. but the tame roll chance still isn't always-taming
- CARNIVORE - Blocks foods not in diet, but eating animation still plays on servers (counter with nausea)
- VEGETARIAN - Blocks foods not in diet, but eating animation still plays on servers (counter with nausea)
- ONLY_EATS_SWEETS - Blocks foods not in diet, but eating animation still plays on servers (counter with nausea)
- GRASS_EATER - Blocks foods/blocks not in diet, but eating animation still plays on servers (counter with nausea)

**Do work (works as intended):**
- FIRE_IMMUNE
- FREEZE_IMMUNE
- FALL_IMMUNE
- AQUA_GRACE
- LIGHT
- SWIFT
- SLOW
- HOPPY
- DWARF
- TOUGH
- STRONG
- BREATHES_UNDERWATER
- CANT_BREATHE_AIR
- HYDROPHOBIC
- NIGHT_VISION
- HUNTED_BY_WOLF
- SCARES_CREEPERS
- SCARES_PHANTOMS
- BURNS_IN_DAYLIGHT
- IS_MONSTER
- PREDATORY
- GRASS_EATER
