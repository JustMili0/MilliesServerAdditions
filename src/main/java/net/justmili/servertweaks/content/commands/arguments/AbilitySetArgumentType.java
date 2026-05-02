package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.ability.AbilityModifier;
import net.justmili.servertweaks.content.abilities.registry.AbilitySetsRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
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

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(AbilitySetsRegistry.getNames(), builder);
    }
}