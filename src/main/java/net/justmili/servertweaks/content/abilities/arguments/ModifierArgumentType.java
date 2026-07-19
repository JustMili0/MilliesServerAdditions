package net.justmili.servertweaks.content.abilities.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.core.Registries;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class ModifierArgumentType {
    public static IdentifierArgument modifier() {
        return IdentifierArgument.id();
    }

    public static AbilityModifier getModifier(CommandContext<CommandSourceStack> context, String argName) throws CommandSyntaxException {
        var id = IdentifierArgument.getId(context, argName);
        var modifier = Registries.byModifierId(id);
        if (modifier == null) throw new SimpleCommandExceptionType(Component.literal("Unknown ability modifier: " + id)).create();
        return modifier;
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Registries.getAsString(Registries.MODIFIERS), builder);
    }
}