package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.ability.AbilityModifier;
import net.justmili.servertweaks.content.abilities.registry.AbilitySetsRegistry;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AbilitySetArgumentType {
    public record AbilitySet(String name, String description, Set<Ability> abilities, Set<AbilityModifier> modifiers) {
    }

    public static StringArgumentType setSelect() {
        return StringArgumentType.word();
    }

    public static @Nullable AbilitySet getSet(String name) {
        return AbilitySetsRegistry.getSets().get(name.toLowerCase());
    }

    public static Set<String> getNames() {
        return AbilitySetsRegistry.getSets().keySet();
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        AbilitySetsRegistry.SETS.keySet().forEach(name -> {
            if (name.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) builder.suggest(name);
        });
        return builder.buildFuture();
    }
}