package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.registry.AbilitiesRegistry;
import net.minecraft.commands.CommandSourceStack;

public class AbilitiesArgumentType {
    public static StringArgumentType abilities() {
        return StringArgumentType.word();
    }

    public static Ability getAbility(CommandContext<CommandSourceStack> context, String argName) {
        return AbilitiesRegistry.byName(StringArgumentType.getString(context, argName));
    }
}