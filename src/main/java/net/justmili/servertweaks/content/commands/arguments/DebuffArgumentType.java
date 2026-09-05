package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.core.TypeRegistries;
import net.justmili.servertweaks.content.abilities.type.Debuff;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class DebuffArgumentType {
    public static IdentifierArgument debuffs() {
        return IdentifierArgument.id();
    }

    public static Debuff getDebuff(CommandContext<CommandSourceStack> context, String argName) throws CommandSyntaxException {
        var id = IdentifierArgument.getId(context, argName);
        var ability = TypeRegistries.getDebuffById(id);
        if (ability == null) throw new SimpleCommandExceptionType(Component.literal("Unknown player debuff: " + id)).create();
        return ability;
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(TypeRegistries.DEBUFFS.keySet(), builder);
    }
}