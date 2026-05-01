package net.justmili.servertweaks.content.commands.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.justmili.servertweaks.content.abilities.ability.AbilityModifier;
import net.justmili.servertweaks.content.abilities.registry.AbilityModifierRegistry;
import net.minecraft.commands.CommandSourceStack;

public class ModifiersArgumentType {
    public static StringArgumentType modifier() {
        return StringArgumentType.word();
    }

    public static AbilityModifier getModifier(CommandContext<CommandSourceStack> context, String argName) {
        return AbilityModifierRegistry.byName(StringArgumentType.getString(context, argName));
    }
}