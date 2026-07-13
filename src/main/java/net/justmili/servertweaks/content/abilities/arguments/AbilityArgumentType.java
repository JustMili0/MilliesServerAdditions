package net.justmili.servertweaks.content.abilities.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.registries.AbilityRegistry;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;

public class AbilityArgumentType {
    public static StringArgumentType abilities() {
        return StringArgumentType.word();
    }

    public static Ability getAbility(CommandContext<CommandSourceStack> context, String argName) {
        Identifier id = Identifier.tryParse(StringArgumentType.getString(context, argName));
        if (id == null) return null;
        return AbilityRegistry.byId(id);
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(AbilityRegistry.getIds().stream().map(Identifier::toString), builder);
    }
}