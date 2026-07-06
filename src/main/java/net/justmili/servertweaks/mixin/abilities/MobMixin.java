package net.justmili.servertweaks.mixin.abilities;

import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.DataManager;
import net.justmili.servertweaks.content.abilities.data.PlayerContext;
import net.justmili.servertweaks.content.abilities.registries.AbilityRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {
    // CHILD_OF_NATURE
    @Inject(method = "tick", at = @At("TAIL"))
    private void clearTargetIfFriendly(CallbackInfo ci) {
        if (!(Config.playerAbilities.get())) return;
        Mob self = (Mob) (Object) this;
        LivingEntity target = self.getTarget();
        if (!(target instanceof ServerPlayer player)) return;
        if (!DataManager.has(player, AbilityRegistry.CHILD_OF_NATURE)) return;

        if (self instanceof TamableAnimal tamed && tamed.isTame()) return;
        self.setTarget(null);
    }

    // Non-Ability but I'm not making another file for this
    @Inject(method = "tick", at = @At("HEAD"))
    private void checkNoAiName(CallbackInfo ci) {
        if (!Config.noAiNameTags.get()) return;
        Mob mob = (Mob) (Object) this;
        if (mob.level().isClientSide()) return;

        Component name = mob.getCustomName();
        boolean shouldBeNoAi = name != null && name.getString().equals("NoAI");

        if ((mob instanceof TamableAnimal || mob instanceof AbstractVillager) && (mob.isNoAi() != shouldBeNoAi)) {
            mob.setNoAi(shouldBeNoAi);
        }
    }

    // Taming context for CHILD_OF_NATURE
    @Inject(method = "interact", at = @At("HEAD"))
    private void setContext(Player player, InteractionHand hand, Vec3 location, CallbackInfoReturnable<InteractionResult> cir) {
        if (!(Config.playerAbilities.get())) return;
        if ((Object) this instanceof TamableAnimal) PlayerContext.set(player);
    }

    @Inject(method = "interact", at = @At("RETURN"))
    private void clearContext(Player player, InteractionHand hand, Vec3 location, CallbackInfoReturnable<InteractionResult> cir) {
        if (!(Config.playerAbilities.get())) return;
        if ((Object) this instanceof TamableAnimal) PlayerContext.set(null);
    }
}