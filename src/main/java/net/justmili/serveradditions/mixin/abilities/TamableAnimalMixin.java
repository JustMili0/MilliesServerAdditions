package net.justmili.serveradditions.mixin.abilities;

import net.justmili.serveradditions.config.Config;
import net.justmili.serveradditions.content.abilities.DataManager;
import net.justmili.serveradditions.content.abilities.registries.AbilityRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TamableAnimal.class)
public class TamableAnimalMixin {
    // 100% tame chance for FRIENDS_WITH_NATURE (TO FIX)
    @Inject(method = "tame", at = @At("HEAD"), cancellable = true)
    private void tame(Player player, CallbackInfo ci) {
        if (!(Config.playerAbilities.get())) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (!DataManager.has(serverPlayer, AbilityRegistry.FRIENDS_WITH_NATURE)) return;
        TamableAnimal self = (TamableAnimal) (Object) this;
        self.setOwner(player);
        self.setTame(true, true);
        ci.cancel();
    }
}