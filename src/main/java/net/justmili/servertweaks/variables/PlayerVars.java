package net.justmili.servertweaks.variables;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

import static net.justmili.libs.v1.utils.FdaUtil.create;
import static net.justmili.libs.v1.utils.FdaUtil.createPersistent;

@SuppressWarnings({"NullableProblems"})
public final class PlayerVars {
    public static void register() {}

    public static final AttachmentType<BlockPos> AFK_POS = createPersistent(id("afk_pos"), BlockPos.containing(0, 255, 0), BlockPos.CODEC);

    public static final AttachmentType<Boolean>
        IS_AFK = createPersistent(id("is_afk"), false, Codec.BOOL), // AFK-Command related
        SCALE_LOCKED = createPersistent(id("scale_locked"), false, Codec.BOOL), // Scale-Command related
        HAS_PICKED_PRESET = createPersistent(id("picked_ability_preset"), false, Codec.BOOL); // Player Abilities related

    public static final AttachmentType<Integer>
        AFK_COOLDOWN = createPersistent(id("afk_cooldown"), Config.afkCommandCooldown.get(), Codec.INT),
        HURT_TICK = create(id("hurt_tick"), -1),
        MILK_TICK = create(id("milk_tick"), -1);

    private static Identifier id(String path) {
        return ServerTweaks.asId(path);
    }
}
