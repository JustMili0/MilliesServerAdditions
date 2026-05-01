package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.AbilityManager;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.ability.AbilityModifier;
import net.justmili.servertweaks.content.abilities.registry.AbilitiesRegistry;
import net.justmili.servertweaks.content.abilities.registry.AbilityModifierRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModifiersArgumentType {
    public static StringArgumentType modifier() {
        return StringArgumentType.word();
    }

    public static AbilityModifier getModifier(CommandContext<CommandSourceStack> context, String argName) {
        return AbilityModifierRegistry.byName(StringArgumentType.getString(context, argName));
    }

    public static CompletableFuture<Suggestions> suggestGrantable(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            ServerPlayer player = EntityArgument.getPlayer(context, "player");
            Set<AbilityModifier> has = AbilityManager.getModifiers(player);
            AbilityModifierRegistry.getNames().stream()
                .filter(name -> !has.contains(AbilityModifierRegistry.byName(name)))
                .filter(name -> name.toLowerCase().startsWith(builder.getRemaining().toLowerCase()))
                .forEach(builder::suggest);
        } catch (CommandSyntaxException ignored) {}
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestRevokable(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            ServerPlayer player = EntityArgument.getPlayer(context, "player");
            AbilityManager.getModifiers(player).stream()
                .map(AbilityModifier::getName)
                .filter(name -> name.toLowerCase().startsWith(builder.getRemaining().toLowerCase()))
                .forEach(builder::suggest);
        } catch (CommandSyntaxException ignored) {}
        return builder.buildFuture();
    }
}