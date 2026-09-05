package net.justmili.servertweaks.content.abilities;

import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.content.abilities.core.TypeRegistries;
import net.justmili.servertweaks.content.abilities.type.Preset;
import net.minecraft.resources.Identifier;

import java.util.Set;

public class Presets {
    public static void init() {
    }

    static {
        register(new Preset(
            id("custom"),
            "Custom",
            "\nContact any online staff that you'd like a custom set. Your chosen abilities, debuffs and ability modifiers " +
                "will be reviewed by staff and implemented if it's compliant with server's ability creation guidelines if there are any.",
            Set.of(),
            Set.of(),
            Set.of()
        ));
        register(new Preset( // aka Cat
            id("feline"),
            "Feline",
            "\nFelines can mimic the hissing of a creeper and screeching of phantoms, scaring them away; they are also immune to fall damage" +
                "and are extra fast when sprinting. They are carnivores, meaning they can only eat meat, can't exactly swim up in water to not drown" +
                "and untamed wolves turn aggressive towards them unprovoked.",
            Set.of(
                Abilities.SCARES_CREEPERS,
                Abilities.SCARES_PHANTOMS,
                Abilities.FALL_IMMUNE,
                Abilities.SWIFT
            ),
            Set.of(
                Debuffs.HUNTED_BY_WOLF,
                Debuffs.CARNIVORE,
                Debuffs.CANT_SWIM
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset(
            id("dreamweaver"),
            "Dreamweaver",
            "\nDreamweavers are creatures that create dreams for their hosts, they can take an appearance of a cat-moth hybrid, sometimes humanoid; " +
                "though normally they don't have a physical form. " +
                "A dreamweaver scares away creepers due to their cat characteristics, scares away phantoms because they don't need to sleep, " +
                "since they again don't have a physical form and dreams are their domain.\n" +
                "They also are rather weak, taking 1.25x more damage than normal, can't swim up in water, can only eat sweets if not bound to a host, and animals love them.",
            Set.of(
                Abilities.SCARES_CREEPERS,
                Abilities.SCARES_PHANTOMS,
                Abilities.CHILD_OF_NATURE,
                Abilities.LIGHT
            ),
            Set.of(
                Debuffs.WEAK_TO_DAMAGE,
                Debuffs.SACCHARIVORE,
                Debuffs.CANT_SWIM
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
            // Ability set approved by Flufaye the dreamweaver Vtuber herself :3
        ));
        register(new Preset(
            id("bunny"),
            "Bunny",
            "\nBunnies are swift and agile, giving them speed and ability to jump pretty high. " +
                "They feed on fruits and vegetables as well as foliage such as grass or bushes but can not eat. " +
                "Bunnies also can't freeze in the cold thanks to their thick fur, " +
                "but they're also hunted by wildlife such as wolves and foxes.",
            Set.of(
                Abilities.FREEZE_IMMUNE,
                Abilities.HOPPY,
                Abilities.SWIFT
            ),
            Set.of(
                Debuffs.HUNTED_BY_WOLF,
                Debuffs.HUNTED_BY_FOX,
                Debuffs.VEGETARIAN,
                Debuffs.HERBIVORE
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset( // aka Wolf
            id("canine"),
            "Canine",
            "\nCanines are territorial and predatory animals, being attacked by other canines and scaring away smaller animals. " +
                "They are rather tough, not being able to be knocked back as much but also swift thanks to their heavy, muscular build." +
                "Canines also can't freeze in the cold thanks to their thick fur, " +
                "but only feed on meat of other animals as they are carnivores.",
            Set.of(
                Abilities.SWIFT,
                Abilities.TOUGH,
                Abilities.FREEZE_IMMUNE,
                Abilities.SCARES_SKELETONS
            ),
            Set.of(
                Debuffs.HUNTED_BY_WOLF,
                Debuffs.CARNIVORE,
                Debuffs.PREDATORY
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset(
            id("monster_generic"),
            "Monster (Generic)",
            "\nVillagers are scared of monsters, in result they will flee. Iron Golems and Snow Golems will attack unprovoked as they see you as a threat, " +
                "but other monsters such as pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack.",
            Set.of(
            ),
            Set.of(
                Debuffs.IS_MONSTER
            ),
            Set.of(
            )
        ));
        register(new Preset(
            id("undead_generic"),
            "Undead (Generic)",
            "\nThe undead are monsters. Villagers flee from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Undead also burn in daylight and can not swim up in water but because they don't need air to live, they can breathe underwater.",
            Set.of(
                Abilities.BREATHES_UNDERWATER
            ),
            Set.of(
                Debuffs.IS_MONSTER,
                Debuffs.BURNS_IN_DAYLIGHT,
                Debuffs.CANT_SWIM
            ),
            Set.of(
            )
        ));
        register(new Preset(
            id("zombie"),
            "Zombie",
            "\nZombies are undead monsters. Villagers flee from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Zombies also burn in daylight and can not swim up in water but because they don't need air to live, they can breathe underwater. " +
                "They also are rather slow and can only eat meat to sustain their hunger.",
            Set.of(
                Abilities.BREATHES_UNDERWATER
            ),
            Set.of(
                Debuffs.IS_MONSTER,
                Debuffs.BURNS_IN_DAYLIGHT,
                Debuffs.CANT_SWIM,
                Debuffs.SLOW,
                Debuffs.CARNIVORE
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset(
            id("drowned"),
            "Drowned",
            "\nDrowned are undead monsters. Villagers flee from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Drowned also burn in daylight, they live underwater and don't need air meaning they can breathe underwater " +
                "and are though to knock back due to them being heavier from all the water in their body." +
                "They also are rather slow, but can feed on pretty much anything like meat, vegetables and fruits or land and underwater foliage.",
            Set.of(
                Abilities.BREATHES_UNDERWATER,
                Abilities.TOUGH
            ),
            Set.of(
                Debuffs.IS_MONSTER,
                Debuffs.BURNS_IN_DAYLIGHT,
                Debuffs.SLOW,
                Debuffs.CARNIVORE,
                Debuffs.VEGETARIAN,
                Debuffs.HERBIVORE
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset(
            id("husk"),
            "Husk",
            "\nHusks are undead monsters. Villagers flee from them, Iron Golems and Snow Golems will attack unprovoked, but other monsters such as " +
                "pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Unlike most undead, Husks don't burn in daylight and are immune to heat, but are sensitive to the cold. " +
                "They can not swim up in water but because they don't need air to live, they can breathe underwater. " +
                "They also are rather slow and can only eat meat to sustain their hunger.",
            Set.of(
                Abilities.HEAT_IMMUNE
            ),
            Set.of(
                Debuffs.IS_MONSTER,
                Debuffs.SLOW,
                Debuffs.CARNIVORE,
                Debuffs.COLD_SENSITIVE
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset( // aka Iron Golem
            id("golem"),
            "Golem",
            "\nTough and strong, but slow and heavy but durable enough to survive any fall.",
            Set.of(
                Abilities.TOUGH,
                Abilities.STRONG,
                Abilities.FALL_IMMUNE
            ),
            Set.of(
                Debuffs.SLOW,
                Debuffs.CANT_SWIM
            ),
            Set.of(
            )
        ));
        register(new Preset( // aka Nether Mob
            id("netherborn"),
            "Netherborn",
            "\nNetherborn are creatures from the depths of the Nether. Native to it, they are immune to all hellish heat. " +
                "They are strong and tough, but are sensitive to cold and repulsed by water. Netherborn are also carnivores, " +
                "and their presence alone scares away Phantoms.",
            Set.of(
                Abilities.LAVA_IMMUNE,
                Abilities.FIRE_IMMUNE,
                Abilities.HEAT_IMMUNE,
                Abilities.STRONG,
                Abilities.TOUGH,
                Abilities.SCARES_PHANTOMS
            ),
            Set.of(
                Debuffs.HYDROPHOBIC,
                Debuffs.COLD_SENSITIVE,
                Debuffs.CARNIVORE
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset( // aka Chicken
            id("galline"),
            "Galline",
            "\nGallines are bird-like creatures. They are light and swift, they feed on plants and foliage, but are hunted by foxes in the wild.",
            Set.of(
                Abilities.LIGHT,
                Abilities.SWIFT
            ),
            Set.of(
                Debuffs.HUNTED_BY_FOX,
                Debuffs.VEGETARIAN,
                Debuffs.HERBIVORE
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset( // aka Fish
            id("aquarian"),
            "Aquarian",
            "\nAquarians are aquatic beings native to oceans. They are strong and gracefully fast underwater, " +
                "being able to breathe in water but suffocating on the surface. Aquarians can not be in hot climates, or they'll become fish soufflé." +
                "They primarily feed on meat and underwater or surface foliage.",
            Set.of(
                Abilities.AQUATIC_GRACE,
                Abilities.STRONG
            ),
            Set.of(
                Debuffs.CANT_BREATHE_AIR,
                Debuffs.HEAT_SENSITIVE,
                Debuffs.SLOW,
                Debuffs.CARNIVORE,
                Debuffs.HERBIVORE
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset( // aka Spider
            id("arachnidian"),
            "Arachnidian",
            "\nArachnidians are humanoid spider creatures. They are considered monsters, meaning Villagers will flee from them, " +
                "Iron Golems and Snow Golems will attack unprovoked, but pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Arachnidians are rather agile, meaning they can jump high and can scale any solid wall. " +
                "They are also strict carnivores, feeding only on the meat of their prey.",
            Set.of(
                Abilities.CLIMBS_WALLS,
                Abilities.WEAVER,
                Abilities.HOPPY
            ),
            Set.of(
                Debuffs.IS_MONSTER,
                Debuffs.CARNIVORE,
                Debuffs.PREDATORY
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset( // aka Enderman
            id("enderian"),
            "Enderian",
            "\nEnderians are creatures come from the far End islands. Due to their uncanny and creepy appearance, they are often seen as monsters, " +
                "meaning Villagers will flee from them, Iron Golems and Snow Golems will attack unprovoked, " +
                "but pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Enderians are aquagenic, getting hurt from getting in contact with water, though a helmet will protect them from rain. " +
                "They are also immune to cold, since they are native to the endless cold End and can infinitely use any Ender Pearls they get their hands on.",
            Set.of(
                Abilities.PEARLING,
                Abilities.FREEZE_IMMUNE
            ),
            Set.of(
                Debuffs.HYDROPHOBIC,
                Debuffs.IS_MONSTER
            ),
            Set.of(
            )
        ));
        register(new Preset( // aka Frog
            id("amphibian"),
            "Amphibian",
            "\nAmphibians are creatures native to swamps and lush wetlands. They are agile hoppers, able to jump quite high, " +
                "and are equally at home on land and underwater, being able to breathe in water and on surface. " +
                "Amphibians are carnivores with a very particular palate, feeding exclusively on bugs, slimes and magma cubes.",
            Set.of(
                Abilities.HOPPY,
                Abilities.BREATHES_UNDERWATER
            ),
            Set.of(
                Debuffs.INSECTIVORE,
                Debuffs.CARNIVORE
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset( // aka Slime
            id("gelatinous"),
            "Gelatinous",
            "\nThe Gelatinous are bouncy, slime-like creatures. They are monsters, meaning Villagers will flee from them, " +
                "Iron Golems and Snow Golems will attack unprovoked, but pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Their squishy body absorbs most of the impacts from falls and collisions, keeping them mostly unharmed. " +
                "Unexplainably they are carnivores, but also are sensitive to cold and freezing in cold biomes.",
            Set.of(
                Abilities.HOPPY,
                Abilities.SQUISHY
            ),
            Set.of(
                Debuffs.IS_MONSTER,
                Debuffs.CARNIVORE,
                Debuffs.COLD_SENSITIVE
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset( // aka Magma Cube
            id("pryogelatinous"),
            "Pryogelatinous",
            "\nThe Pryogelatinous are bouncy, magmatic slime-like creatures native to the Nether. They are monsters, meaning Villagers will flee from them, " +
                "Iron Golems and Snow Golems will attack unprovoked, but pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack. " +
                "Their squishy body absorbs most of the impacts from falls and collisions, keeping them mostly unharmed. " +
                "Like their gelatinous cousins they are unexplainably carnivores, but unlike them, they are immune to fire and heat, " +
                "though sensitive to cold and not entirely immune to lava.",
            Set.of(
                Abilities.FIRE_IMMUNE,
                Abilities.HEAT_IMMUNE,
                Abilities.HOPPY,
                Abilities.SQUISHY
            ),
            Set.of(
                Debuffs.IS_MONSTER,
                Debuffs.CARNIVORE,
                Debuffs.COLD_SENSITIVE
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new Preset(
            id("dwarf"),
            "Dwarf",
            "\nI AM A DWARF AND I'M DIGGING A HOLE, DIGGY DIGGY HOLE, DIGGY DIGGY HOLE!\n\n" +
                "(You get permanent haste 2, you're short, " +
                "you're immune to block heat like magma, not lava or fire, you're immune to freezing and " +
                "animals love you, and you're a vegetarian.)",
            Set.of(
                Abilities.DWARF,
                Abilities.HEAT_IMMUNE,
                Abilities.FREEZE_IMMUNE,
                Abilities.CHILD_OF_NATURE
            ),
            Set.of(
                Debuffs.VEGETARIAN
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
    }

    private static Identifier id(String id) {
        return ServerTweaks.asId(id);
    }

    public static void register(Preset preset) {
        TypeRegistries.PRESETS.put(preset.getId(), preset);
    }
}