package net.justmili.servertweaks.variables;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.core.TypeCodecs;
import net.justmili.servertweaks.content.abilities.core.TypeRegistries;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.Debuff;
import net.justmili.servertweaks.content.abilities.type.Modifier;
import net.justmili.servertweaks.content.mechanics.features.AnvilRepair;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

import static net.justmili.corelibs.v1.utils.common.FdaUtil.*;

@SuppressWarnings({"NullableProblems"})
public final class PlayerVars {
    public static void init() {}

    public static final AttachmentType<Vec3> AFK_POS = createPersistent(id("afk_pos"), Vec3.ZERO, Vec3.CODEC);

    public static final AttachmentType<Boolean>
        IS_AFK = createPersistent(id("is_afk"), false, Codec.BOOL), // AFK-Command related
        SCALE_LOCKED = createPersistent(id("scale_locked"), false, Codec.BOOL), // Scale-Command related
        HAS_PICKED_PRESET = createPersistent(id("picked_ability_preset"), false, Codec.BOOL); // Player Abilities related

    public static final AttachmentType<Integer>
        SMP_PERM_LEVEL = createPersistent(id("smp_permission_level"), 0, Codec.INT),
        AFK_COOLDOWN = createPersistent(id("afk_cooldown"), Config.afkCommandCooldown.get(), Codec.INT),
        HURT_TICK = createTransient(id("hurt_tick"), -1),
        MILK_TICK = createTransient(id("milk_tick"), -1);

    public static final AttachmentType<AnvilRepair.RepairState> ANVIL_REPAIR_STATE =
        createTransient(id("anvil_repair_state"), AnvilRepair.RepairState.NONE);

    public static final AttachmentType<Set<Ability>> SYNCED_ABILITIES =
        createSynced(id("abilities"), Set.of(), TypeCodecs.streamSetOf(TypeCodecs.streamForType(TypeRegistries::getAbilityById)));
    public static final AttachmentType<Set<Debuff>> SYNCED_DEBUFFS =
        createSynced(id("debuffs"), Set.of(), TypeCodecs.streamSetOf(TypeCodecs.streamForType(TypeRegistries::getDebuffById)));
    public static final AttachmentType<Set<Modifier>> SYNCED_MODIFIERS =
        createSynced(id("modifiers"), Set.of(), TypeCodecs.streamSetOf(TypeCodecs.streamForType(TypeRegistries::getModifierById)));

    private static Identifier id(String path) {
        return ServerTweaks.asId(path);
    }
}