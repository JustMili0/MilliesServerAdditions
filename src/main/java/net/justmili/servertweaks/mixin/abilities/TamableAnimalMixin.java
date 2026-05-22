package net.justmili.servertweaks.mixin.abilities;

import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.DataManager;
import net.justmili.servertweaks.content.abilities.registries.AbilityRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TamableAnimal.class)
public class TamableAnimalMixin {
    // 100% tame chance for CHILD_OF_NATURE (TO FIX)
    @Inject(method = "tame", at = @At("HEAD"), cancellable = true)
    private void tame(Player player, CallbackInfo ci) {
        if (!(Config.playerAbilities.get())) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (!DataManager.has(serverPlayer, AbilityRegistry.CHILD_OF_NATURE)) return;
        TamableAnimal self = (TamableAnimal) (Object) this;
        self.setOwner(player);
        self.setTame(true, true);
        ci.cancel();
    }
}