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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DamageToggle {
    private static final Map<ResourceKey<DamageType>, Boolean> DISABLED_TYPES = new HashMap<>();
    private static boolean eventRegistered = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        if (!eventRegistered) {
            eventRegistered = true;
            ServerLivingEntityEvents.ALLOW_DAMAGE.register((LivingEntity _, DamageSource source, float _) -> {
                    for (Map.Entry<ResourceKey<DamageType>, Boolean> entry : DISABLED_TYPES.entrySet()) {
                        if (!entry.getValue()) continue;
                        if (source.is(entry.getKey())) return false;
                    }
                    return true;
                }
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
        var status = shouldDisable? "Disabled" : "Enabled";
        DISABLED_TYPES.put(key, shouldDisable);
        CommandUtil.sendOk(source, status + " Damage Type " + key.identifier());
        return 1;
    }

    static int getStatus(CommandSourceStack source, ResourceKey<DamageType> key) {
        var status = DISABLED_TYPES.getOrDefault(key, false) ? "disabled" : "enabled";
        CommandUtil.sendOk(source, "Damage Type " + key.identifier() + " is currently " + status);
        return 1;
    }

    static int enableAll(CommandSourceStack source) {
        DISABLED_TYPES.clear();
        CommandUtil.sendOk(source, "Enabled all existing Damage Types");
        return 1;
    }

    static int disableAll(CommandSourceStack source) {
        var registry = source.getServer().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        registry.listElementIds().forEach(key -> DISABLED_TYPES.put(key, true));
        CommandUtil.sendOk(source, "Disabled all existing Damage Types");
        return 1;
    }

    static int listDisabled(CommandSourceStack source) {
        var registry = source.getServer().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        List<String> list = DISABLED_TYPES.entrySet().stream().filter(Map.Entry::getValue)
            .map(entry -> entry.getKey().identifier().toString()).toList();

        String message;
        if (list.isEmpty()) {
            message = "No existing Damage Types are currently disabled";
        } else if (list.size() >= registry.size()) {
            message = "All existing Damage Types are currently disabled";
        } else {
            message = "Disabled damage types: " + String.join(", ", list);
        }

        CommandUtil.sendOk(source, message);
        return 1;
    }
}