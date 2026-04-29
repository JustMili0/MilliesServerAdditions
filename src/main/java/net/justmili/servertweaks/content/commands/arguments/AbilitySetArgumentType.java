package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.ability.AbilityModifier;
import net.justmili.servertweaks.content.abilities.registry.AbilitiesRegistry;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AbilitySetArgumentType {
    public record AbilitySet(String name, String description, Set<Ability> abilities, Set<AbilityModifier> modifiers) {}
    private static final Map<String, AbilitySet> SETS = new LinkedHashMap<>();

    static {
        register(new AbilitySet(
            "custom",
            "\nContact any online staff that you'd like a custom set. Your chosen abilities, debuffs and ability modifiers " +
                "will be reviewed by staff and implemented if it's compliant with server's ability creation guidelines if there are any.",
            Set.of(),
            Set.of()
        ));
        register(new AbilitySet(
            "feline",
            "\nThe Feline set will make you scare away creepers and phantoms, will grant you speed 1 effect when sprinting, and you won't take fall damage " +
                "but you'll only be able to eat meat, untamed wolves will attack you unprovoked, and you can't swim in water.",
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
    }
    /* TODO: IDEAS
    dreamweaver
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
    aquatic
    spider
    enderian
    toad
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