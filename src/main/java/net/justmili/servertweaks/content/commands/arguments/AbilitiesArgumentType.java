package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.justmili.servertweaks.content.abilities.AbilityManager;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.registry.AbilitiesRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AbilitiesArgumentType {
    public static StringArgumentType abilities() {
        return StringArgumentType.word();
    }

    public static Ability getAbility(CommandContext<CommandSourceStack> context, String argName) {
        return AbilitiesRegistry.byName(StringArgumentType.getString(context, argName));
    }

    public static CompletableFuture<Suggestions> suggestGrantable(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            ServerPlayer player = EntityArgument.getPlayer(context, "player");
            Set<Ability> has = AbilityManager.getAbilities(player);
            AbilitiesRegistry.getNames().stream()
                .filter(name -> !has.contains(AbilitiesRegistry.byName(name)))
                .filter(name -> name.toLowerCase().startsWith(builder.getRemaining().toLowerCase()))
                .forEach(builder::suggest);
        } catch (CommandSyntaxException ignored) {}
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> suggestRevokable(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            ServerPlayer player = EntityArgument.getPlayer(context, "player");
            AbilityManager.getAbilities(player).stream()
                .map(Ability::getName)
                .filter(name -> name.toLowerCase().startsWith(builder.getRemaining().toLowerCase()))
                .forEach(builder::suggest);
        } catch (CommandSyntaxException ignored) {}
        return builder.buildFuture();
    }
}