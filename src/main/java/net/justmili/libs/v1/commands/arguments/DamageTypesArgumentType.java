package net.justmili.libs.v1.commands.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypesArgumentType {
    public static ResourceArgument<DamageType> damageTypes(CommandBuildContext context) {
        return ResourceArgument.resource(context, Registries.DAMAGE_TYPE);
    }

    public static Holder.Reference<DamageType> getType(CommandContext<CommandSourceStack> context, String argName) throws CommandSyntaxException {
        return ResourceArgument.getResource(context, argName, Registries.DAMAGE_TYPE);
    }

    public static ResourceKey<DamageType> getTypeId(CommandContext<CommandSourceStack> context, String argName) throws CommandSyntaxException {
        return getType(context, argName).key();
    }
}