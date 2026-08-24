package net.justmili.servertweaks.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.network.packets.ClientboundModCheckPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentItemMixin {

    @ModifyExpressionValue(method = "releaseUsing", at = @At(value = "CONSTANT", args = "intValue=10"))
    private int servertweaks$fasterRechargeWithRiptide(int original, ItemStack stack, Level level, LivingEntity entity, int remainingTime) {
        if (!Config.fasterRiptideCharge.get()) return original;
        if (entity instanceof Player player && EnchantmentHelper.getTridentSpinAttackStrength(stack, player) > 0.0F) return 5;
        return original;
    }

    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;push(DDD)V", shift = At.Shift.AFTER))
    private void servertweaks$syncRiptideVelocity(ItemStack stack, Level level, LivingEntity entity, int remainingTime, CallbackInfoReturnable<Boolean> cir) {
        if (!Config.fasterRiptideCharge.get()) return;
        if (entity instanceof ServerPlayer player && !ServerPlayNetworking.canSend(player, ClientboundModCheckPacket.TYPE)) {
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }
}