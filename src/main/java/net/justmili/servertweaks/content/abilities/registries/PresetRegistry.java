package net.justmili.servertweaks.content.abilities.registries;

import net.justmili.servertweaks.content.abilities.arguments.PresetArgumentType.AbilityPreset;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PresetRegistry {
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
                AbilityRegistry.SCARES_CREEPERS,
                AbilityRegistry.SCARES_PHANTOMS,
                AbilityRegistry.HUNTED_BY_WOLF,
                AbilityRegistry.FALL_IMMUNE,
                AbilityRegistry.CARNIVORE,
                AbilityRegistry.CANT_SWIM,
                AbilityRegistry.SWIFT
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
                AbilityRegistry.SCARES_CREEPERS,
                AbilityRegistry.SCARES_PHANTOMS,
                AbilityRegistry.WEAK_TO_DAMAGE,
                AbilityRegistry.SACCHARIVORE,
                AbilityRegistry.CHILD_OF_NATURE,
                AbilityRegistry.CANT_SWIM
            ),
            Set.of()
            // Ability set approved by Flufaye the dreamweaver Vtuber herself :3
        ));
        register(new AbilityPreset(
            "bunny",
            "\nBunnies are swift and agile, giving them speed and ability to jump pretty high. " +
                "They feed on fruits and vegetables as well as foliage such as grass or bushes but can not eat. " +
                "Bunnies also can't freeze in the cold thanks to their thick fur, " +
                "but they're also hunted by wildlife such as wolves and foxes.",
            Set.of(
                AbilityRegistry.HUNTED_BY_WOLF,
                AbilityRegistry.HUNTED_BY_FOX,
                AbilityRegistry.VEGETARIAN,
                AbilityRegistry.HERBIVORE,
                AbilityRegistry.FREEZE_IMMUNE,
                AbilityRegistry.HOPPY,
                AbilityRegistry.SWIFT
            ),
            Set.of(
                ModifierRegistry.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new AbilityPreset( // aka Wolf
            "canine",
            "\nCanines are territorial and predatory animals, being attacked by other canines and scaring away smaller animals. " +
                "They are rather tough, not being able to be knocked back as much but also swift thanks to their heavy, muscular build." +
                "Canines also can't freeze in the cold thanks to their thick fur, " +
                "but only feed on meat of other animals as they are carnivores.",
            Set.of(
                AbilityRegistry.HUNTED_BY_WOLF,
                AbilityRegistry.CARNIVORE,
                AbilityRegistry.SWIFT,
                AbilityRegistry.PREDATORY,
                AbilityRegistry.TOUGH,
                AbilityRegistry.FREEZE_IMMUNE
            ),
            Set.of()
        ));
        register(new AbilityPreset(
            "monster_generic",
            "\nVillagers are scared of monsters, in result they will flee. Iron Golems and Snow Golems will attack unprovoked as they see you as a threat, " +
                "but other monsters such as pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack.",
            Set.of(
                AbilityRegistry.IS_MONSTER
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset(
            "undead_generic",
            "\nThe undead are monsters. Villagers flee from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Undead also burn in daylight and can not swim up in water but because they don't need air to live, they can breathe underwater.",
            Set.of(
                AbilityRegistry.IS_MONSTER,
                AbilityRegistry.BURNS_IN_DAYLIGHT,
                AbilityRegistry.CANT_SWIM,
                AbilityRegistry.BREATHES_UNDERWATER
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset(
            "zombie",
            "\nZombies are undead monsters. Villagers flee from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Zombies also burn in daylight and can not swim up in water but because they don't need air to live, they can breathe underwater. " +
                "They also are rather slow and can only eat meat to sustain their hunger.",
            Set.of(
                AbilityRegistry.IS_MONSTER,
                AbilityRegistry.BURNS_IN_DAYLIGHT,
                AbilityRegistry.CANT_SWIM,
                AbilityRegistry.SLOW,
                AbilityRegistry.CARNIVORE,
                AbilityRegistry.BREATHES_UNDERWATER
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset(
            "drowned",
            "\nDrowned are undead monsters. Villagers flee from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Drowned also burn in daylight, they live underwater and don't need air meaning they can breathe underwater " +
                "and are though to knock back due to them being heavier from all the water in their body." +
                "They also are rather slow, but can feed on pretty much anything like meat, vegetables and fruits or land and underwater foliage.",
            Set.of(
                AbilityRegistry.IS_MONSTER,
                AbilityRegistry.BURNS_IN_DAYLIGHT,
                AbilityRegistry.SLOW,
                AbilityRegistry.CARNIVORE,
                AbilityRegistry.VEGETARIAN,
                AbilityRegistry.HERBIVORE,
                AbilityRegistry.BREATHES_UNDERWATER,
                AbilityRegistry.TOUGH
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset(
            "husk",
            "\nHusks are undead monsters. Villagers flee from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Unlike most undead, Husks don't burn in daylight and are immune to heat, but are sensitive to the cold. " +
                "They can not swim up in water but because they don't need air to live, they can breathe underwater. " +
                "They also are rather slow and can only eat meat to sustain their hunger.",
            Set.of(
                AbilityRegistry.IS_MONSTER,
                AbilityRegistry.SLOW,
                AbilityRegistry.CARNIVORE,
                AbilityRegistry.COLD_SENSITIVE,
                AbilityRegistry.HEAT_IMMUNE
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Iron Golem
            "golem",
            "\nTough and strong, but slow and heavy but durable enough to survive any fall.",
            Set.of(
                AbilityRegistry.TOUGH,
                AbilityRegistry.STRONG,
                AbilityRegistry.SLOW,
                AbilityRegistry.CANT_SWIM,
                AbilityRegistry.FALL_IMMUNE
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Nether Mob
            "netherborn",
            "\nNetherborn are creatures from the depths of the Nether. Native to it, they are immune to all hellish heat. " +
                "They are strong and tough, but are sensitive to cold and repulsed by water. Netherborn are also carnivores, " +
                "and their presence alone scares away Phantoms.",
            Set.of(
                AbilityRegistry.LAVA_IMMUNE,
                AbilityRegistry.FIRE_IMMUNE,
                AbilityRegistry.HEAT_IMMUNE,
                AbilityRegistry.STRONG,
                AbilityRegistry.TOUGH,
                AbilityRegistry.HYDROPHOBIC,
                AbilityRegistry.COLD_SENSITIVE,
                AbilityRegistry.CARNIVORE,
                AbilityRegistry.SCARES_PHANTOMS
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Chicken
            "galline",
            "\nGallines are bird-like creatures. They are light and swift, they feed on plants and foliage, but are hunted by foxes in the wild.",
            Set.of(
                AbilityRegistry.HUNTED_BY_FOX,
                AbilityRegistry.VEGETARIAN,
                AbilityRegistry.HERBIVORE,
                AbilityRegistry.LIGHT,
                AbilityRegistry.SWIFT
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Fish
            "aquarian",
            "\nAquarians are aquatic beings native to oceans. They are strong and gracefully fast underwater, " +
                "being able to breathe in water but suffocating on the surface. Aquarians can not be in hot climates, or they'll become fish soufflé." +
                "They primarily feed on meat and underwater or surface foliage.",
            Set.of(
                AbilityRegistry.AQUA_GRACE,
                AbilityRegistry.BREATHES_UNDERWATER,
                AbilityRegistry.CANT_BREATHE_AIR,
                AbilityRegistry.CARNIVORE,
                AbilityRegistry.HERBIVORE,
                AbilityRegistry.STRONG,
                AbilityRegistry.HEAT_SENSITIVE
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Spider
            "arachnidian",
            "\nArachnidians are humanoid spider creatures. They are considered monsters, meaning Villagers will flee from them, " +
                "Iron Golems and Snow Golems will attack unprovoked, but pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Arachnidians are rather agile, meaning they can jump high and can scale any solid wall. " +
                "They are also strict carnivores, feeding only on the meat of their prey.",
            Set.of(
                AbilityRegistry.CLIMBS_WALLS,
                AbilityRegistry.IS_MONSTER,
                AbilityRegistry.CARNIVORE,
                AbilityRegistry.HOPPY,
                AbilityRegistry.PREDATORY
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Enderman
            "enderian",
            "\nEnderians are creatures come from the far End islands. Due to their uncanny and creepy appearance, they are often seen as monsters, " +
                "meaning Villagers will flee from them, Iron Golems and Snow Golems will attack unprovoked, " +
                "but pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Enderians are aquagenic, getting hurt from getting in contact with water, though a helmet will protect them from rain. " +
                "They are also immune to cold, since they are native to the endless cold End and can infinitely use any Ender Pearls they get their hands on.",
                Set.of(
                AbilityRegistry.PEARLING,
                AbilityRegistry.HYDROPHOBIC,
                AbilityRegistry.IS_MONSTER,
                AbilityRegistry.FREEZE_IMMUNE
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Frog
            "amphibian",
            "\nAmphibians are creatures native to swamps and lush wetlands. They are agile hoppers, able to jump quite high, " +
                "and are equally at home on land and underwater, being able to breathe in water and on surface. " +
                "Amphibians are carnivores with a very particular palate, feeding exclusively on bugs, slimes and magma cubes.",
            Set.of(
                AbilityRegistry.INSECTIVORE,
                AbilityRegistry.CARNIVORE,
                AbilityRegistry.HOPPY,
                AbilityRegistry.BREATHES_UNDERWATER
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Slime
            "gelatinous",
            "\nThe Gelatinous are bouncy, slime-like creatures. They are monsters, meaning Villagers will flee from them, " +
                "Iron Golems and Snow Golems will attack unprovoked, but pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Their squishy body absorbs most of the impacts from falls and collisions, keeping them mostly unharmed. " +
                "Unexplainably they are carnivores, but also are sensitive to cold and freezing in cold biomes.",
            Set.of(
                AbilityRegistry.HOPPY,
                AbilityRegistry.IS_MONSTER,
                AbilityRegistry.CARNIVORE,
                AbilityRegistry.COLD_SENSITIVE,
                AbilityRegistry.SQUISHY
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Magma Cube
            "pryogelatinous",
            "\nThe Pryogelatinous are bouncy, magmatic slime-like creatures native to the Nether. They are monsters, meaning Villagers will flee from them, " +
                "Iron Golems and Snow Golems will attack unprovoked, but pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Their squishy body absorbs most of the impacts from falls and collisions, keeping them mostly unharmed. " +
                "Like their gelatinous cousins they are unexplainably carnivores, but unlike them, they are immune to fire and heat, " +
                "though sensitive to cold and not entirely immune to lava.",
            Set.of(
                AbilityRegistry.FIRE_IMMUNE,
                AbilityRegistry.HEAT_IMMUNE,
                AbilityRegistry.HOPPY,
                AbilityRegistry.IS_MONSTER,
                AbilityRegistry.CARNIVORE,
                AbilityRegistry.COLD_SENSITIVE,
                AbilityRegistry.SQUISHY
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset(
            "dwarf",
            "\nI AM A DWARF AND I'M DIGGING A HOLE, DIGGY DIGGY HOLE, DIGGY DIGGY HOLE!\n\n" +
                "(You get permanent haste 2, you're short, " +
                "you're immune to block heat like magma, not lava or fire, you're immune to freezing and " +
                "animals love you, and you're a vegetarian.)",
            Set.of(
                AbilityRegistry.DWARF,
                AbilityRegistry.HEAT_IMMUNE,
                AbilityRegistry.FREEZE_IMMUNE,
                AbilityRegistry.CHILD_OF_NATURE,
                AbilityRegistry.VEGETARIAN
            ),
            Set.of(
                ModifierRegistry.CAN_EAT_GOLDEN_FOOD
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
        return PresetRegistry.getSets().keySet();
    }
}