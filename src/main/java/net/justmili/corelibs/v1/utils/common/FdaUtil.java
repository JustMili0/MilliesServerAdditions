package net.justmili.corelibs.v1.utils.common;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.function.UnaryOperator;

@SuppressWarnings({"NullableProblems"})
public class FdaUtil {
    public static boolean getBool(AttachmentTarget target, AttachmentType<Boolean> variable) {
        return get(target, variable, false);
    }

    public static String getString(AttachmentTarget target, AttachmentType<String> variable) {
        return get(target, variable, "ValueReturnedNull");
    }

    public static int getInt(AttachmentTarget target, AttachmentType<Integer> variable) {
        return get(target, variable, -1);
    }

    public static double getDouble(AttachmentTarget target, AttachmentType<Double> variable) {
        return get(target, variable, -1.0);
    }

    public static float getFloat(AttachmentTarget target, AttachmentType<Float> variable) {
        return get(target, variable, -1f);
    }

    public static long getLong(AttachmentTarget target, AttachmentType<Long> variable) {
        return get(target, variable, -1L);
    }

    // Generic value getter and setters
    public static <A> void set(AttachmentTarget target, AttachmentType<A> variable, A value) {
        target.setAttached(variable, value);
    }

    public static <A> A get(AttachmentTarget target, AttachmentType<A> variable, A defaultValue) {
        return target.getAttachedOrElse(variable, defaultValue);
    }

    /// Will silently throw and return nothing if target does not have the variable
    /// It is preferred to use get(target, variable, defaultValue)
    public static <A> A get(AttachmentTarget target, AttachmentType<A> variable) {
        return target.getAttachedOrThrow(variable);
    }

    public static <A> void remove(AttachmentTarget target, AttachmentType<A> variable) {
        target.removeAttached(variable);
    }

    public static <A> void modify(AttachmentTarget target, AttachmentType<A> variable, UnaryOperator<A> operator) {
        target.modifyAttached(variable, operator);
    }

    public static <A> boolean has(AttachmentTarget target, AttachmentType<A> variable) {
        return target.hasAttached(variable);
    }

    // Creates values that will clear after a restart
    public static <A> AttachmentType<A> createTransient(Identifier id, A defaultValue) {
        return AttachmentRegistry.create(id, builder -> builder.initializer(() -> defaultValue).copyOnDeath());
    }

    // Creates values that will not clear after a restart
    public static <A> AttachmentType<A> createPersistent(Identifier id, A defaultValue, Codec<A> codec) {
        return AttachmentRegistry.create(id, builder -> builder.initializer(() -> defaultValue).copyOnDeath().persistent(codec));
    }

    // Creates values synced to the player and clear after restart
    public static <A> AttachmentType<A> createSynced(Identifier id, A defaultValue, StreamCodec<? super RegistryFriendlyByteBuf, A> streamCodec) {
        return AttachmentRegistry.create(id, builder -> builder.initializer(() -> defaultValue).copyOnDeath().syncWith(streamCodec, AttachmentSyncPredicate.targetOnly()));
    }
}