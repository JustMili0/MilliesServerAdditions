package net.justmili.servertweaks.mixin.abilities;

import net.justmili.servertweaks.config.Config;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {
    // Non-Ability but I'm not making another file for this
    @Inject(method = "tick", at = @At("HEAD"))
    private void checkNoAiName(CallbackInfo ci) {
        if (!Config.noAiNameTags.get()) return;
        Mob mob = (Mob) (Object) this;
        if (mob.level().isClientSide()) return;

        var name = mob.getCustomName();
        boolean shouldBeNoAi = name != null && name.getString().equals("NoAI");

        if ((mob instanceof TamableAnimal || mob instanceof AbstractVillager) && (mob.isNoAi() != shouldBeNoAi)) {
            mob.setNoAi(shouldBeNoAi);
        }
    }
}