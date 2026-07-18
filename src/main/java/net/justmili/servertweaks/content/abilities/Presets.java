package net.justmili.servertweaks.content.abilities;

import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.content.abilities.core.RegistryMaps;
import net.justmili.servertweaks.content.abilities.type.AbilityPreset;
import net.minecraft.resources.Identifier;

import java.util.Set;

public class Presets {
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
                Abilities.SCARES_CREEPERS,
                Abilities.SCARES_PHANTOMS,
                Abilities.HUNTED_BY_WOLF,
                Abilities.FALL_IMMUNE,
                Abilities.CARNIVORE,
                Abilities.CANT_SWIM,
                Abilities.SWIFT
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
                Abilities.SCARES_CREEPERS,
                Abilities.SCARES_PHANTOMS,
                Abilities.WEAK_TO_DAMAGE,
                Abilities.SACCHARIVORE,
                Abilities.CHILD_OF_NATURE,
                Abilities.CANT_SWIM
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
                Abilities.HUNTED_BY_WOLF,
                Abilities.HUNTED_BY_FOX,
                Abilities.VEGETARIAN,
                Abilities.HERBIVORE,
                Abilities.FREEZE_IMMUNE,
                Abilities.HOPPY,
                Abilities.SWIFT
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
        register(new AbilityPreset( // aka Wolf
            "canine",
            "\nCanines are territorial and predatory animals, being attacked by other canines and scaring away smaller animals. " +
                "They are rather tough, not being able to be knocked back as much but also swift thanks to their heavy, muscular build." +
                "Canines also can't freeze in the cold thanks to their thick fur, " +
                "but only feed on meat of other animals as they are carnivores.",
            Set.of(
                Abilities.HUNTED_BY_WOLF,
                Abilities.CARNIVORE,
                Abilities.SWIFT,
                Abilities.PREDATORY,
                Abilities.TOUGH,
                Abilities.FREEZE_IMMUNE
            ),
            Set.of()
        ));
        register(new AbilityPreset(
            "monster_generic",
            "\nVillagers are scared of monsters, in result they will flee. Iron Golems and Snow Golems will attack unprovoked as they see you as a threat, " +
                "but other monsters such as pillagers, zombies (and variants), skeletons (and variants), and slimes will not attack.",
            Set.of(
                Abilities.IS_MONSTER
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
                Abilities.IS_MONSTER,
                Abilities.BURNS_IN_DAYLIGHT,
                Abilities.CANT_SWIM,
                Abilities.BREATHES_UNDERWATER
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
                Abilities.IS_MONSTER,
                Abilities.BURNS_IN_DAYLIGHT,
                Abilities.CANT_SWIM,
                Abilities.SLOW,
                Abilities.CARNIVORE,
                Abilities.BREATHES_UNDERWATER
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
                Abilities.IS_MONSTER,
                Abilities.BURNS_IN_DAYLIGHT,
                Abilities.SLOW,
                Abilities.CARNIVORE,
                Abilities.VEGETARIAN,
                Abilities.HERBIVORE,
                Abilities.BREATHES_UNDERWATER,
                Abilities.TOUGH
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
                Abilities.IS_MONSTER,
                Abilities.SLOW,
                Abilities.CARNIVORE,
                Abilities.COLD_SENSITIVE,
                Abilities.HEAT_IMMUNE
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Iron Golem
            "golem",
            "\nTough and strong, but slow and heavy but durable enough to survive any fall.",
            Set.of(
                Abilities.TOUGH,
                Abilities.STRONG,
                Abilities.SLOW,
                Abilities.CANT_SWIM,
                Abilities.FALL_IMMUNE
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
                Abilities.LAVA_IMMUNE,
                Abilities.FIRE_IMMUNE,
                Abilities.HEAT_IMMUNE,
                Abilities.STRONG,
                Abilities.TOUGH,
                Abilities.HYDROPHOBIC,
                Abilities.COLD_SENSITIVE,
                Abilities.CARNIVORE,
                Abilities.SCARES_PHANTOMS
            ),
            Set.of(
            )
        ));
        register(new AbilityPreset( // aka Chicken
            "galline",
            "\nGallines are bird-like creatures. They are light and swift, they feed on plants and foliage, but are hunted by foxes in the wild.",
            Set.of(
                Abilities.HUNTED_BY_FOX,
                Abilities.VEGETARIAN,
                Abilities.HERBIVORE,
                Abilities.LIGHT,
                Abilities.SWIFT
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
                Abilities.AQUA_GRACE,
                Abilities.BREATHES_UNDERWATER,
                Abilities.CANT_BREATHE_AIR,
                Abilities.CARNIVORE,
                Abilities.HERBIVORE,
                Abilities.STRONG,
                Abilities.HEAT_SENSITIVE
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
                Abilities.CLIMBS_WALLS,
                Abilities.IS_MONSTER,
                Abilities.CARNIVORE,
                Abilities.HOPPY,
                Abilities.PREDATORY
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
                Abilities.PEARLING,
                Abilities.HYDROPHOBIC,
                Abilities.IS_MONSTER,
                Abilities.FREEZE_IMMUNE
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
                Abilities.INSECTIVORE,
                Abilities.CARNIVORE,
                Abilities.HOPPY,
                Abilities.BREATHES_UNDERWATER
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
                Abilities.HOPPY,
                Abilities.IS_MONSTER,
                Abilities.CARNIVORE,
                Abilities.COLD_SENSITIVE,
                Abilities.SQUISHY
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
                Abilities.FIRE_IMMUNE,
                Abilities.HEAT_IMMUNE,
                Abilities.HOPPY,
                Abilities.IS_MONSTER,
                Abilities.CARNIVORE,
                Abilities.COLD_SENSITIVE,
                Abilities.SQUISHY
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
                Abilities.DWARF,
                Abilities.HEAT_IMMUNE,
                Abilities.FREEZE_IMMUNE,
                Abilities.CHILD_OF_NATURE,
                Abilities.VEGETARIAN
            ),
            Set.of(
                Modifiers.CAN_EAT_GOLDEN_FOOD
            )
        ));
    }

    public static void register(AbilityPreset preset) {
        RegistryMaps.PRESETS.put(preset.getId(), preset);
    }
}