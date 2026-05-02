package net.justmili.servertweaks.content.abilities.registry;

import net.justmili.servertweaks.content.commands.arguments.AbilitySetArgumentType.AbilitySet;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class AbilitySetsRegistry {
    public static final Map<String, AbilitySet> SETS = new LinkedHashMap<>();

    static {
        register(new AbilitySet(
            "custom",
            "\nContact any online staff that you'd like a custom set. Your chosen abilities, debuffs and ability modifiers "+
                "will be reviewed by staff and implemented if it's compliant with server's ability creation guidelines if there are any.",
            Set.of(),
            Set.of()
        ));
        register(new AbilitySet( // aka Cat
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
        register(new AbilitySet(
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
        register(new AbilitySet(
            "bunny",
            "Bunnies are swift and agile, giving them speed and ability to jump pretty high. " +
                "They can feed on fruits and vegetables as well as foliage such as grass or bushes but can't eat meat. " +
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
        register(new AbilitySet(
            "monster_generic",
            "Villagers are scared of monsters, in result they will run away. Iron Golems and Snow Golems will attack unprovoked as they see you as a threat, " +
                "but other monsters such as pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack another monster.",
            Set.of(
                AbilitiesRegistry.IS_MONSTER
            ),
            Set.of(
            )
        ));
        register(new AbilitySet(
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
        register(new AbilitySet(
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
        register(new AbilitySet(
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
        register(new AbilitySet( // TODO: TO BE REWRITTEN
            "husk",
            "Husks are undead monsters. Villagers run away from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack a Husk. " +
                "Husks don't burn in daylight, primarily reside in the desert and are immune to the heat of the hot sand under their feet, but freeze easily " +
                "and can not swim up in water but because they don't need air to live, they can breathe underwater. " +
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
        register(new AbilitySet( // aka Iron Golem
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
        register(new AbilitySet( // aka Golem but Human
            "knight",
            "",
            Set.of(
            ),
            Set.of(
            )
        ));
        register(new AbilitySet( // aka Nether Mob
            "imp",
            "",
            Set.of(
            ),
            Set.of(
            )
        ));
        register(new AbilitySet( // aka Chicken
            "galline",
            "",
            Set.of(
            ),
            Set.of(
            )
        ));
        register(new AbilitySet( // aka Fish
            "aquarian",
            "",
            Set.of(
            ),
            Set.of(
            )
        ));
        register(new AbilitySet( // aka Spider
            "arachnidian",
            "",
            Set.of(
            ),
            Set.of(
            )
        ));
        register(new AbilitySet( // aka Enderman
            "enderian",
            "",
            Set.of(
            ),
            Set.of(
            )
        ));
        register(new AbilitySet( // aka Frog
            "amphibian",
            "",
            Set.of(
            ),
            Set.of(
            )
        ));
        register(new AbilitySet(
            "slime",
            "",
            Set.of(
            ),
            Set.of(
            )
        ));
        register(new AbilitySet(
            "magma_cube",
            "",
            Set.of(
            ),
            Set.of(
            )
        ));
        register(new AbilitySet(
            "dwarf",
            "",
            Set.of(
            ),
            Set.of(
            )
        ));
    }

    public static void register(AbilitySet set) {
        SETS.put(set.name(), set);
    }

    public static Map<String, AbilitySet> getSets() {
        return SETS;
    }
}