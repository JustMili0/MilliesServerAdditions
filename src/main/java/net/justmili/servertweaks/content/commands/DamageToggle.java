package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.justmili.libs.v1.utils.common.CommandUtil;
import net.justmili.libs.v1.commands.arguments.DamageTypesArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DamageToggle {
    private static final Map<ResourceKey<DamageType>, Boolean> damageDisabled = new HashMap<>();
    private static boolean eventRegistered = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        if (!eventRegistered) {
            eventRegistered = true;
            ServerLivingEntityEvents.ALLOW_DAMAGE.register((LivingEntity _, DamageSource source, float _) -> {
                    for (Map.Entry<ResourceKey<DamageType>, Boolean> entry : damageDisabled.entrySet()) {
                        if (!entry.getValue()) continue;
                        if (source.is(entry.getKey())) return false;
                    }
                    return true;
                }
            );
        }

        dispatcher.register(Commands.literal("damagetoggle")
            .requires(src -> CommandUtil.hasPerms(src, 2))

            .then(Commands.literal("enable")
                .then(Commands.argument("type", DamageTypesArgumentType.damageTypes(buildContext))
                    .executes(context -> setStatus(context, false))))
            .then(Commands.literal("disable")
                .then(Commands.argument("type", DamageTypesArgumentType.damageTypes(buildContext))
                    .executes(context -> setStatus(context, true))))
            .then(Commands.literal("get")
                .then(Commands.argument("type", DamageTypesArgumentType.damageTypes(buildContext))
                    .executes(DamageToggle::getStatus)))

            .then(Commands.literal("enableAll")
                .executes(DamageToggle::enableAll))
            .then(Commands.literal("disableAll")
                .executes(DamageToggle::disableAll))

            .then(Commands.literal("listDisabled")
                .executes(DamageToggle::listDisabled))
        );
    }

    private static int setStatus(CommandContext<CommandSourceStack> context, boolean disable) throws CommandSyntaxException {
        var key = DamageTypesArgumentType.getTypeId(context, "type");
        damageDisabled.put(key, disable);
        CommandUtil.sendOk(context.getSource(), "Damage type '" + key.identifier() + "' is now " + (disable ? "disabled" : "enabled"), false);
        return 1;
    }

    private static int getStatus(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var key = DamageTypesArgumentType.getTypeId(context, "type");
        var status = damageDisabled.getOrDefault(key, false) ? "disabled" : "enabled";
        CommandUtil.sendOk(context.getSource(), "Damage type '" + key.identifier() + "' is " + status);
        return 1;
    }

    private static int enableAll(CommandContext<CommandSourceStack> context) {
        damageDisabled.replaceAll((id, bool) -> false);
        CommandUtil.sendOk(context.getSource(), "All damage types enabled.");
        return 1;
    }

    private static int disableAll(CommandContext<CommandSourceStack> context) {
        damageDisabled.replaceAll((_, _) -> true);
        CommandUtil.sendOk(context.getSource(), "All damage types disabled.");
        return 1;
    }

    private static int listDisabled(CommandContext<CommandSourceStack> context) {
        List<String> list = damageDisabled.entrySet().stream().filter(Map.Entry::getValue)
            .map(entry -> entry.getKey().identifier().toString()).toList();
        CommandUtil.sendOk(context.getSource(), list.isEmpty() ? "No damage types are currently disabled" : "Disabled damage types: " + String.join(", ", list));
        return 1;
    }
}