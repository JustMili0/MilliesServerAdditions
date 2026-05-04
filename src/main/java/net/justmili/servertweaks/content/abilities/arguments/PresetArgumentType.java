package net.justmili.servertweaks.content.abilities.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.registries.PresetRegistry;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PresetArgumentType {
    public record AbilityPreset(String name, String description, Set<Ability> abilities, Set<AbilityModifier> modifiers) {
    }

    public static StringArgumentType setSelect() {
        return StringArgumentType.word();
    }

    public static @Nullable AbilityPreset getSet(String name) {
        return PresetRegistry.getSets().get(name.toLowerCase());
    }

    public static CompletableFuture<Suggestions> suggest(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(PresetRegistry.getNames(), builder);
    }
}