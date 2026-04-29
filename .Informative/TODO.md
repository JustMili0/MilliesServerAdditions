TODO:
- Fix discarding containers on entities
- Fix abilities
  - Fix nausea appliance when eating non-diet foods
  - Fix kinda-working abilities
  - Fix non-working abilities
- Add a command for Player Abilities management
  - Any Player - can pick any pre-made sets of abilities and debuffs up to 3, for more a moderator will need to be contacted
  - Moderators - Can freely add and remove abilities on the fly without having to restart the server
    - File would be updated and reloaded
- Add new abilities
  - Pearling (Shift+RC to throw a pear in the direction the player is looking)
  - Predatory (Cows, Pigs, Sheep and Salmon will run/swim away from the player, but not so fast it's impossible for the player to catch up)
  - Bug Eater (Feeds exclusively on slime balls, magma balls, insect/bug-like mobs, slimes (entity) and magma cubes (entity, deals a heart of fire damage when consumed))
    - For 26.2: Also can eat Sulfur Cubes but will get poisoned with nausea, but will get a lot of saturation
  - Squishy (reduces fall and fly_into_wall damage)
  - Magnetic (nearby items come to player)
  - Sticky (magnetic but cool slime visuals)

--------------------------------------------------------------------------------–--------------------------------------

FROM TESTING PLAYER ABILITIES:
Doesn't work (no changes get applied to player behavior):
- CLIMBS_WALLS - Nothing happens (can't climb walls)
- WEAK_TO_DAMAGE - Nothing happens (same damage as normal)
- CANT_SWIM - Nothing happens (can swim as normal)

Kinda work (works for the most part)
- BURNS_IN_DAYLIGHT - Player doesn't take burning damage without a helmet in water (to fix, should take tiny bit of damage in water if they don't have a helmet)
- HUNTED_BY_FOX - Foxes are too scared to approach and attack unless the player is crouching
- FRIENDS_WITH_NATURE - Calms down neutral animals such as wolves, bees etc. but the tame roll chance still isn't always-taming
- CARNIVORE - Blocks foods not in diet, but eating animation still plays on servers (counter with nausea)
- VEGETARIAN - Blocks foods not in diet, but eating animation still plays on servers (counter with nausea)
- ONLY_EATS_SWEETS - Blocks foods not in diet, but eating animation still plays on servers (counter with nausea)

Do work (works as intended):
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
- IS_MONSTER
- GRASS_EATER
