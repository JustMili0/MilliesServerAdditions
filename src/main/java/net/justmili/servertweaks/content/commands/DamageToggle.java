package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.justmili.libs.v1.commands.arguments.DamageTypesArgumentType;
import net.justmili.libs.v1.utils.common.CommandUtil;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DamageToggle {
    private static final Set<ResourceKey<DamageType>> DISABLED_TYPES = new HashSet<>();
    private static boolean eventRegistered = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        if (!eventRegistered) {
            eventRegistered = true;
            ServerLivingEntityEvents.ALLOW_DAMAGE.register((LivingEntity _, DamageSource source, float _) ->
                !DISABLED_TYPES.contains(source.typeHolder().unwrapKey().orElse(null))
            );
        }

        dispatcher.register(Commands.literal("damagetoggle").requires(src -> CommandUtil.hasPerms(src, 2))

            .then(Commands.literal("enable").then(Commands.argument("type", DamageTypesArgumentType.damageTypes(buildContext))
                .executes(context -> setStatus(
                    context.getSource(), DamageTypesArgumentType.getTypeKey(context, "type"), false))))

            .then(Commands.literal("disable").then(Commands.argument("type", DamageTypesArgumentType.damageTypes(buildContext))
                .executes(context -> setStatus(
                    context.getSource(), DamageTypesArgumentType.getTypeKey(context, "type"), true))))

            .then(Commands.literal("get").then(Commands.argument("type", DamageTypesArgumentType.damageTypes(buildContext))
                .executes(context -> getStatus(
                    context.getSource(), DamageTypesArgumentType.getTypeKey(context, "type")))))

            .then(Commands.literal("enableAll").executes(context -> enableAll(context.getSource())))
            .then(Commands.literal("disableAll").executes(context -> disableAll(context.getSource())))
            .then(Commands.literal("listDisabled").executes(context -> listDisabled(context.getSource())))
        );
    }

    static int setStatus(CommandSourceStack source, ResourceKey<DamageType> key, boolean shouldDisable) {
        if (shouldDisable) {
            DISABLED_TYPES.add(key);
        } else {
            DISABLED_TYPES.remove(key);
        }
        CommandUtil.sendOk(source, (shouldDisable ? "Disabled" : "Enabled") + " Damage Type " + key.identifier());
        return 1;
    }

    static int getStatus(CommandSourceStack source, ResourceKey<DamageType> key) {
        CommandUtil.sendOk(source, "Damage Type " + key.identifier() + " is currently " + (DISABLED_TYPES.contains(key) ? "disabled" : "enabled"));
        return 1;
    }

    static int enableAll(CommandSourceStack source) {
        DISABLED_TYPES.clear();
        CommandUtil.sendOk(source, "Enabled all existing Damage Types");
        return 1;
    }

    static int disableAll(CommandSourceStack source) {
        var registry = source.getServer().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        registry.listElementIds().forEach(DISABLED_TYPES::add);
        CommandUtil.sendOk(source, "Disabled all existing Damage Types");
        return 1;
    }

    static int listDisabled(CommandSourceStack source) {
        var registry = source.getServer().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        var message = DISABLED_TYPES.isEmpty() ? "No existing Damage Types are currently disabled"
            : DISABLED_TYPES.size() >= registry.size() ? "All existing Damage Types are currently disabled"
            : "Disabled damage types: " + DISABLED_TYPES.stream().map(key -> key.identifier().toString()).collect(Collectors.joining(", "));
        CommandUtil.sendOk(source, message);
        return 1;
    }
}