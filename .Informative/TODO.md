# TODO:
- Finish writing presets  [!!!]
- Fix discarding containers on entities
- Fix abilities
  - Fix hp resetting to 10 hearts with STRONG after relog
  - Fix not being able to place any edible but placeable foods with diets other than vegetarian  [!]
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
- BUG_EATER

**Doesn't work (no changes get applied to player behavior):**
- CLIMBS_WALLS - Nothing happens (can't climb walls)
- WEAK_TO_DAMAGE - Nothing happens (same damage as normal)
- CANT_SWIM - Nothing happens (can swim as normal)

**Kinda work (works for the most part)**
- HUNTED_BY_FOX - Foxes are too scared to approach and attack unless the player is crouching
- FRIENDS_WITH_NATURE - Calms down neutral animals such as wolves, bees etc. but the tame roll chance still isn't always-taming
- CARNIVORE - Nausea doesn't apply when trying to consume foods that aren't included in player's diet
- VEGETARIAN - Nausea doesn't apply when trying to consume foods that aren't included in player's diet
- ONLY_EATS_SWEETS - Nausea doesn't apply when trying to consume foods that aren't included in player's diet
- GRASS_EATER - Nausea doesn't apply when trying to consume foods that aren't included in player's diet

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
- PEARLING
- PREDATORY
- GRASS_EATER
