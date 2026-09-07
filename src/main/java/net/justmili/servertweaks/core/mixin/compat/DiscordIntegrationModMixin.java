package net.justmili.servertweaks.core.mixin.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.erdbeerbaerlp.dcintegration.architectury.util.MessageUtilsImpl;
import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration;
import de.erdbeerbaerlp.dcintegration.common.util.DiscordMessage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.justmili.servertweaks.config.Config;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "de.erdbeerbaerlp.dcintegration.architectury.DiscordIntegrationMod")
public class DiscordIntegrationModMixin {

    @WrapOperation(method = "handleChatMessage", at = @At(value = "INVOKE", target = "Lde/erdbeerbaerlp/dcintegration/common/DiscordIntegration;sendMessage(Lde/erdbeerbaerlp/dcintegration/common/util/DiscordMessage;Lnet/dv8tion/jda/api/entities/channel/middleman/MessageChannel;)V"))
    private static void servertweaks$respectNameFormatting(
        DiscordIntegration instance, DiscordMessage msg, MessageChannel channel, Operation<Void> original,
        @Local(argsOnly = true, name = "player") ServerPlayer player,
        @Local(name = "embed") MessageEmbed embed,
        @Local(name = "text") String text
    ) {
        if (!Config.enableDiscordIntegrationFix.get()) return;
        instance.sendMessage(MessageUtilsImpl.formatPlayerName(player), player.getUUID().toString(), new DiscordMessage(embed, text, true), channel);
    }
}