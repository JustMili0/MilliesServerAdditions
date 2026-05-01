package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.ability.AbilityModifier;
import net.justmili.servertweaks.content.abilities.registry.AbilitiesRegistry;
import net.justmili.servertweaks.content.abilities.registry.AbilityModifierRegistry;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AbilitySetArgumentType {
    public record AbilitySet(String name, String description, Set<Ability> abilities, Set<AbilityModifier> modifiers) {
    }

    private static final Map<String, AbilitySet> SETS = new LinkedHashMap<>();

    static {
        register(new AbilitySet(
            "custom",
            "\nContact any online staff that you'd like a custom set. Your chosen abilities, debuffs and ability modifiers "+
                "will be reviewed by staff and implemented if it's compliant with server's ability creation guidelines if there are any.",
            Set.of(),
            Set.of()
        ));
        register(new AbilitySet(
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
                "A dreamweaver scares away creepers due to their cat characteristics, scares away phantoms they don't need to sleep, " +
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
            "",
            Set.of(
                AbilitiesRegistry.HUNTED_BY_WOLF,
                AbilitiesRegistry.HUNTED_BY_FOX,
                AbilitiesRegistry.VEGETARIAN,
                AbilitiesRegistry.GRASS_EATER,
                AbilitiesRegistry.HOPPY,
                AbilitiesRegistry.SWIFT
            ),
            Set.of(
                AbilityModifierRegistry.ADD_GOLD_FOODS_TO_DIET
            )
        ));
    }
    /* TODO: IDEAS
    bunny
    monster_generic
    undead_generic
    zombie
    husk
    frozen (husk but cold)
    golem
    knight
    imp
    chicken
    aquarian
    spider
    enderian
    amphibian
    slime
    magma cube (slime but fire resistant and cold sensitive)
    dwarf
     */

    private static void register(AbilitySet set) {
        SETS.put(set.name(), set);
    }

    public static StringArgumentType setSelect() {
        return StringArgumentType.word();
    }

    public static @Nullable AbilitySet getSet(String name) {
        return SETS.get(name.toLowerCase());
    }

    public static Set<String> getNames() {
        return SETS.keySet();
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        SETS.keySet().forEach(name -> {
            if (name.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) builder.suggest(name);
        });
        return builder.buildFuture();
    }
}