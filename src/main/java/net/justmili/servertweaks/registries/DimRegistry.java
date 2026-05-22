package net.justmili.servertweaks.registries;

import net.justmili.servertweaks.ServerTweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class DimRegistry {
    public static final ResourceKey<Level> BANISHMENT = ResourceKey.create(Registries.DIMENSION, ServerTweaks.asResource("banishment"));

    public static void register() {}
}
