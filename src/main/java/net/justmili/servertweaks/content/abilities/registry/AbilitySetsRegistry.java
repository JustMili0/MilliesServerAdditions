package net.justmili.servertweaks.content.abilities.registry;

import net.justmili.servertweaks.content.commands.arguments.AbilitySetArgumentType.AbilityPreset;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AbilitySetsRegistry {
    public static final Map<String, AbilityPreset> REGISTRY = new HashMap<>();

    static {
        register(new AbilityPreset(
            "custom",
            "\nContact any online staff that you'd like a custom set. Your chosen abilities, debuffs and ability modifiers "+
                "will be reviewed by staff and implemented if it's compliant with server's ability creation guidelines if there are any.",
            Set.of(),
            Set.of()
        ));
        register(new AbilityPreset( // aka Cat
            "feline",
            "\nFelines can mimic the hissing of a creeper and screeching of phantoms, scaring them away; they are also immune to fall damage"+
                "and are extra fast when sprinting. They are carnivores, meaning they can only eat meat, can't exactly swim up in water to not drown"+
                "and untamed wolves turn aggressive towards them unprovoked.",
            Set.of(
                AbilitiesRegistry.SCARES_CREEPERS,
                AbilitiesRegistry.SCARES_PHANTOMS,
                AbilitiesRegistry.HUNTED_BY_WOLF,
                AbilitiesRegistry.FALL_IMMUNE,
                AbilitiesRegistry.CARNIVORE,
                AbilitiesRegistry.CANT_SWIM,
                AbilitiesRegistry.SWIFT
            ),
            Set.of()
        ));
        register(new AbilityPreset(
            "dreamweaver",
            "\nDreamweavers are creatures that create dreams for their hosts, they can take an appearance of a cat-moth hybrid, sometimes humanoid; "+
                "though normally they don't have a physical form. "+
                "A dreamweaver scares away creepers due to their cat characteristics, scares away phantoms because they don't need to sleep, " +
                "since they again don't have a physical form and dreams are their domain.\n"+
                "They also are rather weak, taking 1.25x more damage than normal, can't swim up in water, can only eat sweets if not bound to a host, and animals love them.",
            Set.of(
                AbilitiesRegistry.SCARES_CREEPERS,
                AbilitiesRegistry.SCARES_PHANTOMS,
                AbilitiesRegistry.WEAK_TO_DAMAGE,
                AbilitiesRegistry.ONLY_EATS_SWEETS,
                AbilitiesRegistry.FRIENDS_WITH_NATURE,
                AbilitiesRegistry.CANT_SWIM
            ),
            Set.of()
            // Ability set approved by Flufaye the dreamweaver Vtuber herself :3
        ));
        register(new AbilityPreset(
            "bunny",
            "Bunnies are swift and agile, giving them speed and ability to jump pretty high. " +
                "They feed on fruits and vegetables as well as foliage such as grass or bushes but can not eat. " +
                "Bunnies also can't freeze in the cold thanks to their thick fur, " +
                "but they're also hunted by wildlife such as wolves and foxes.",
            Set.of(
                AbilitiesRegistry.HUNTED_BY_WOLF,
                AbilitiesRegistry.HUNTED_BY_FOX,
                AbilitiesRegistry.VEGETARIAN,
                AbilitiesRegistry.GRASS_EATER,
                AbilitiesRegistry.FREEZE_IMMUNE,
                AbilitiesRegistry.HOPPY,
                AbilitiesRegistry.SWIFT
            ),
            Set.of(
                AbilityModifierRegistry.ADD_GOLD_FOODS_TO_DIET
            )
        ));
        register(new AbilityPreset(
            "monster_generic",
            "Villagers are scared of monsters, in result they will run away. Iron Golems and Snow Golems will attack unprovoked as they see you as a threat, " +
                "but other monsters such as pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack another monster.",
            Set.of(
                AbilitiesRegistry.IS_MONSTER
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset(
            "undead_generic",
            "The undead are monsters. Villagers run away from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack another monster. " +
                "Undead also burn in daylight and can not swim up in water but because they don't need air to live, they can breathe underwater.",
            Set.of(
                AbilitiesRegistry.IS_MONSTER,
                AbilitiesRegistry.BURNS_IN_DAYLIGHT,
                AbilitiesRegistry.CANT_SWIM,
                AbilitiesRegistry.BREATHES_UNDERWATER
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset(
            "zombie",
            "Zombies are undead monsters. Villagers run away from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack a Zombie. " +
                "Zombies also burn in daylight and can not swim up in water but because they don't need air to live, they can breathe underwater. " +
                "They also are rather slow and can only eat meat to sustain their hunger.",
            Set.of(
                AbilitiesRegistry.IS_MONSTER,
                AbilitiesRegistry.BURNS_IN_DAYLIGHT,
                AbilitiesRegistry.CANT_SWIM,
                AbilitiesRegistry.SLOW,
                AbilitiesRegistry.CARNIVORE,
                AbilitiesRegistry.BREATHES_UNDERWATER
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset(
            "drowned",
            "Drowned are undead monsters. Villagers run away from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack a Drowned. " +
                "Drowned also burn in daylight, they live underwater and don't need air meaning they can breathe underwater " +
                "and are though to knock back due to them being heavier from all the water in their body." +
                "They also are rather slow, but can feed on pretty much anything like meat, vegetables and fruits or land and underwater foliage.",
            Set.of(
                AbilitiesRegistry.IS_MONSTER,
                AbilitiesRegistry.BURNS_IN_DAYLIGHT,
                AbilitiesRegistry.SLOW,
                AbilitiesRegistry.CARNIVORE,
                AbilitiesRegistry.VEGETARIAN,
                AbilitiesRegistry.GRASS_EATER,
                AbilitiesRegistry.BREATHES_UNDERWATER,
                AbilitiesRegistry.TOUGH
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset(
            "husk",
            "Husks are undead monsters. Villagers run away from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack a Husk. " +
                "Unlike most undead, Husks don't burn in daylight and are immune to heat, but are sensitive to the cold. " +
                "They can not swim up in water but because they don't need air to live, they can breathe underwater. " +
                "They also are rather slow and can only eat meat to sustain their hunger.",
            Set.of(
                AbilitiesRegistry.IS_MONSTER,
                AbilitiesRegistry.SLOW,
                AbilitiesRegistry.CARNIVORE,
                AbilitiesRegistry.COLD_SENSITIVE,
                AbilitiesRegistry.HEAT_IMMUNE
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Iron Golem
            "golem",
            "Tough and strong, but slow and heavy but durable enough to survive any fall.",
            Set.of(
                AbilitiesRegistry.TOUGH,
                AbilitiesRegistry.STRONG,
                AbilitiesRegistry.SLOW,
                AbilitiesRegistry.CANT_SWIM,
                AbilitiesRegistry.FALL_IMMUNE
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Nether Mob
            "netherborn",
            "Description in Progress",
            Set.of(
                AbilitiesRegistry.LAVA_IMMUNE,
                AbilitiesRegistry.FIRE_IMMUNE,
                AbilitiesRegistry.HEAT_IMMUNE,
                AbilitiesRegistry.STRONG,
                AbilitiesRegistry.TOUGH,
                AbilitiesRegistry.HYDROPHOBIC,
                AbilitiesRegistry.COLD_SENSITIVE,
                AbilitiesRegistry.CARNIVORE,
                AbilitiesRegistry.SCARES_PHANTOMS
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Chicken
            "galline",
            "Description in Progress",
            Set.of(
                AbilitiesRegistry.HUNTED_BY_FOX,
                AbilitiesRegistry.VEGETARIAN,
                AbilitiesRegistry.GRASS_EATER,
                AbilitiesRegistry.LIGHT,
                AbilitiesRegistry.SWIFT
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Fish
            "aquarian",
            "Description in Progress",
            Set.of(
                AbilitiesRegistry.AQUA_GRACE,
                AbilitiesRegistry.BREATHES_UNDERWATER,
                AbilitiesRegistry.CANT_BREATHE_AIR,
                AbilitiesRegistry.CARNIVORE,
                AbilitiesRegistry.GRASS_EATER,
                AbilitiesRegistry.STRONG,
                AbilitiesRegistry.HEAT_SENSITIVE
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Spider
            "arachnidian",
            "Description in Progress",
            Set.of(
                AbilitiesRegistry.CLIMBS_WALLS,
                AbilitiesRegistry.IS_MONSTER,
                AbilitiesRegistry.CARNIVORE,
                AbilitiesRegistry.HOPPY
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Enderman
            "enderian",
            "Description in Progress",
            Set.of(
                AbilitiesRegistry.PEARLING,
                AbilitiesRegistry.HYDROPHOBIC,
                AbilitiesRegistry.IS_MONSTER,
                AbilitiesRegistry.FREEZE_IMMUNE
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Frog
            "amphibian",
            "Description in Progress",
            Set.of(
                AbilitiesRegistry.BUG_EATER,
                AbilitiesRegistry.CARNIVORE,
                AbilitiesRegistry.HOPPY,
                AbilitiesRegistry.BREATHES_UNDERWATER
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Slime
            "gelatinous",
            "Description in Progress",
            Set.of(
                AbilitiesRegistry.HOPPY,
                AbilitiesRegistry.IS_MONSTER,
                AbilitiesRegistry.CARNIVORE,
                AbilitiesRegistry.COLD_SENSITIVE,
                AbilitiesRegistry.SQUISHY
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Magma Cube
            "pryogelatinous",
            "Description in Progress",
            Set.of(
                AbilitiesRegistry.FIRE_IMMUNE,
                AbilitiesRegistry.HEAT_IMMUNE,
                AbilitiesRegistry.HOPPY,
                AbilitiesRegistry.IS_MONSTER,
                AbilitiesRegistry.CARNIVORE,
                AbilitiesRegistry.COLD_SENSITIVE,
                AbilitiesRegistry.SQUISHY
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset(
            "dwarf",
            "Description in Progress",
            Set.of(
                AbilitiesRegistry.DWARF,
                AbilitiesRegistry.HEAT_IMMUNE,
                AbilitiesRegistry.FRIENDS_WITH_NATURE,
                AbilitiesRegistry.VEGETARIAN
            ),
            Set.of(
                AbilityModifierRegistry.ADD_GOLD_FOODS_TO_DIET
            )
        ));
    }

    public static void register(AbilityPreset set) {
        REGISTRY.put(set.name(), set);
    }

    public static Map<String, AbilityPreset> getSets() {
        return REGISTRY;
    }
    public static Set<String> getNames() {
        return AbilitySetsRegistry.getSets().keySet();
    }
}