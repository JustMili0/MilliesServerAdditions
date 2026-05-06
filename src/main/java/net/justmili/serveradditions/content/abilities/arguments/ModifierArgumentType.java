package net.justmili.serveradditions.content.abilities.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.serveradditions.content.abilities.registries.ModifierRegistry;
import net.justmili.serveradditions.content.abilities.type.AbilityModifier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.concurrent.CompletableFuture;

public class ModifierArgumentType {
    public static StringArgumentType modifier() {
        return StringArgumentType.word();
    }

    public static AbilityModifier getModifier(CommandContext<CommandSourceStack> context, String argName) {
        return ModifierRegistry.byName(StringArgumentType.getString(context, argName));
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(ModifierRegistry.getNames(), builder);
    }
}