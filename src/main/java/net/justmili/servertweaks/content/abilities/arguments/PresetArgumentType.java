package net.justmili.servertweaks.content.abilities.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.core.Registries;
import net.justmili.servertweaks.content.abilities.type.AbilityPreset;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class PresetArgumentType {
    public static IdentifierArgument preset() {
        return IdentifierArgument.id();
    }

    public static AbilityPreset getPreset(CommandContext<CommandSourceStack> context, String argName) throws CommandSyntaxException {
        var id = IdentifierArgument.getId(context, argName);
        var preset = Registries.byPresetId(id);
        if (preset == null) throw new SimpleCommandExceptionType(Component.literal("Unknown abilities preset: " + id)).create();
        return preset;
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Registries.getAsString(Registries.PRESETS), builder);
    }
}