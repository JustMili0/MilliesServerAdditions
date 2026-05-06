package net.justmili.serveradditions.mixin;

import net.justmili.serveradditions.config.Config;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PistonStructureResolver.class)
public class BetterPushLimit {
    @ModifyConstant(method = "addBlockLine", constant = @Constant(intValue = 12))
    private int modifyPushLimit(int original) {
        return Config.pistonPushLimit.get();
    }
}
