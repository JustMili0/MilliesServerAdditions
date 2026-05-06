package net.justmili.serveradditions.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.justmili.serveradditions.core.util.CommandUtil;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DamageToggle {
    private static final Map<Identifier, Boolean> damageDisabled = new HashMap<>();
    private static boolean eventRegistered = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection environment) {
        if (!eventRegistered) {
            eventRegistered = true;
            ServerLivingEntityEvents.ALLOW_DAMAGE.register(
                (LivingEntity entity, DamageSource source, float amount) -> {
                    for (Map.Entry<Identifier, Boolean> entry : damageDisabled.entrySet()) {
                        if (!entry.getValue()) continue;
                        if (source.is(ResourceKey.create(Registries.DAMAGE_TYPE, entry.getKey()))) return false;
                    }
                    return true;
                }
            );
        }

        dispatcher.register(
            Commands.literal("damagetoggle")
                .requires(src -> CommandUtil.hasPerms(src, 2))
                .then(Commands.literal("listDisabled")
                    .executes(context -> {
                        List<String> disabled = damageDisabled.entrySet().stream()
                            .filter(Map.Entry::getValue).map(entry -> entry.getKey().toString()).toList();
                        CommandUtil.sendSucc(context.getSource(), disabled.isEmpty()
                            ? "No damage types are currently disabled."
                            : "Disabled damage types: " + String.join(", ", disabled));
                        return 1;
                    })
                )
                .then(Commands.literal("enableAll")
                    .executes(context -> {
                        damageDisabled.replaceAll((id, bool) -> false);
                        CommandUtil.sendSucc(context.getSource(), "All damage types enabled.");
                        return 1;
                    })
                )
                .then(Commands.literal("disableAll")
                    .executes(context -> {
                        damageDisabled.replaceAll((id, bool) -> true);
                        CommandUtil.sendSucc(context.getSource(), "All damage types disabled.");
                        return 1;
                    })
                )
                .then(Commands.literal("enable")
                    .then(Commands.argument("type", IdentifierArgument.id())
                        .suggests(DamageToggle::suggestDamageTypes)
                        .executes(context -> setDamage(context, false)))
                )
                .then(Commands.literal("disable")
                    .then(Commands.argument("type", IdentifierArgument.id())
                        .suggests(DamageToggle::suggestDamageTypes)
                        .executes(context -> setDamage(context, true)))
                )
                .then(Commands.literal("get")
                    .then(Commands.argument("type", IdentifierArgument.id())
                        .suggests(DamageToggle::suggestDamageTypes)
                        .executes(DamageToggle::getStatus))
                )
        );
    }

    private static CompletableFuture<Suggestions> suggestDamageTypes(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        Registry<DamageType> registry = context.getSource().getServer().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        return SharedSuggestionProvider.suggest(registry.keySet().stream().map(Identifier::toString), builder);
    }

    private static int setDamage(CommandContext<CommandSourceStack> context, boolean disable) {
        Identifier id = IdentifierArgument.getId(context, "type");
        damageDisabled.put(id, disable);
        CommandUtil.sendSucc(context.getSource(), "Damage type '" + id + "' is now " + (disable ? "disabled" : "enabled"));
        return 1;
    }

    private static int getStatus(CommandContext<CommandSourceStack> context) {
        Identifier id = IdentifierArgument.getId(context, "type");
        CommandUtil.sendSucc(context.getSource(), "Damage type '" + id + "' is " + (damageDisabled.getOrDefault(id, false) ? "disabled" : "enabled"));
        return 1;
    }
}