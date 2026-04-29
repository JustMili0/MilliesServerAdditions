package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.registry.AbilityModifierRegistry;
import net.minecraft.commands.CommandSourceStack;

import java.util.concurrent.CompletableFuture;

public class ModifiersArgumentType {
    public static StringArgumentType modifier() {
        return StringArgumentType.word();
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        AbilityModifierRegistry.getNames().forEach(name -> {
            if (name.toLowerCase().startsWith(builder.getRemaining().toLowerCase()))
                builder.suggest(name);
        });
        return builder.buildFuture();
    }
}