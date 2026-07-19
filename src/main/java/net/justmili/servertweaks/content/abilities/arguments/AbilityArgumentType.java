package net.justmili.servertweaks.content.abilities.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.core.Registries;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class AbilityArgumentType {
    public static IdentifierArgument abilities() {
        return IdentifierArgument.id();
    }

    public static Ability getAbility(CommandContext<CommandSourceStack> context, String argName) throws CommandSyntaxException {
        var id = IdentifierArgument.getId(context, argName);
        var ability = Registries.byAbilityId(id);
        if (ability == null) throw new SimpleCommandExceptionType(Component.literal("Unknown player ability: " + id)).create();
        return ability;
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Registries.getAsString(Registries.ABILITIES), builder);
    }
}