package net.justmili.servertweaks.core.variables;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.justmili.servertweaks.config.Config;

import static net.justmili.libs.v1.utils.FdaApiUtil.createPersistentValue;
import static net.justmili.libs.v1.utils.FdaApiUtil.createValue;

@SuppressWarnings({"NullableProblems"})
public final class PlayerAttachments {
    public static void register() {
    }

    public static final AttachmentType<Boolean>
        SCALE_LOCKED = createPersistentValue("scale_locked", false, Codec.BOOL), // Scale-Command related
        IS_AFK = createPersistentValue("is_afk", false, Codec.BOOL), // AFK-Command related
        PICKED_PRESET = createPersistentValue("picked_ability_preset", false, Codec.BOOL); // Player Abilities related
    public static final AttachmentType<Double> // AFK Spot coords
        AFK_X = createPersistentValue("afk_x", 0.0, Codec.DOUBLE),
        AFK_Y = createPersistentValue("afk_y", 255.0, Codec.DOUBLE),
        AFK_Z = createPersistentValue("afk_z", 0.0, Codec.DOUBLE);
    public static final AttachmentType<Integer>
        AFK_COOLDOWN = createPersistentValue("afk_cooldown", Config.afkCommandCooldown.get(), Codec.INT),
        HURT_TICK = createValue("hurt_tick", -1, Codec.INT),
        MILK_TICK = createValue("milk_tick", -1, Codec.INT);
}
