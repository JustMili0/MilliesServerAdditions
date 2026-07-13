package net.justmili.servertweaks.content.abilities.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.registries.ModifierRegistry;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModifierArgumentType {
    public static StringArgumentType modifier() {
        return StringArgumentType.word();
    }

    public static AbilityModifier getModifier(CommandContext<CommandSourceStack> context, String argName) {
        Identifier id = Identifier.tryParse(StringArgumentType.getString(context, argName));
        if (id == null) return null;
        return ModifierRegistry.byId(id);
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(ModifierRegistry.getIds().stream().map(Identifier::toString), builder);
    }
}