package net.justmili.serveradditions.content.abilities.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.serveradditions.content.abilities.registries.AbilityRegistry;
import net.justmili.serveradditions.content.abilities.type.Ability;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.concurrent.CompletableFuture;

public class AbilityArgumentType {
    public static StringArgumentType abilities() {
        return StringArgumentType.word();
    }

    public static Ability getAbility(CommandContext<CommandSourceStack> context, String argName) {
        return AbilityRegistry.byName(StringArgumentType.getString(context, argName));
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(AbilityRegistry.getNames(), builder);
    }
}