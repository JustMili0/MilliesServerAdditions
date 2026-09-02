package net.justmili.libs.v1.utils.common;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class NbtUtil {
    private NbtUtil() {
    }

    public static CustomDataBuilder custom() {
        return new CustomDataBuilder();
    }

    public static final class CustomDataBuilder {
        private final CompoundTag tag = new CompoundTag();

        private CustomDataBuilder() {
        }

        public CustomDataBuilder addBool(String key, boolean value) {
            tag.putBoolean(key, value);
            return this;
        }

        public CustomDataBuilder addInt(String key, int value) {
            tag.putInt(key, value);
            return this;
        }

        public CustomDataBuilder addLong(String key, long value) {
            tag.putLong(key, value);
            return this;
        }

        public CustomDataBuilder addDouble(String key, double value) {
            tag.putDouble(key, value);
            return this;
        }

        public CustomDataBuilder addFloat(String key, float value) {
            tag.putFloat(key, value);
            return this;
        }

        public CustomDataBuilder addString(String key, String value) {
            tag.putString(key, value);
            return this;
        }

        public ItemStack applyToStack(ItemStack stack) {
            CustomData.update(DataComponents.CUSTOM_DATA, stack, existing -> existing.merge(tag));
            return stack;
        }

        public ItemStack overwriteStack(ItemStack stack) {
            CustomData.set(DataComponents.CUSTOM_DATA, stack, tag);
            return stack;
        }
    }

    public static boolean getBool(ItemStack stack, String key, boolean orElse) {
        return tagOf(stack).getBooleanOr(key, orElse);
    }

    public static int getInt(ItemStack stack, String key, int orElse) {
        return tagOf(stack).getIntOr(key, orElse);
    }

    public static long getLong(ItemStack stack, String key, long orElse) {
        return tagOf(stack).getLongOr(key, orElse);
    }

    public static double getDouble(ItemStack stack, String key, double orElse) {
        return tagOf(stack).getDoubleOr(key, orElse);
    }

    public static float getFloat(ItemStack stack, String key, float orElse) {
        return tagOf(stack).getFloatOr(key, orElse);
    }

    public static String getString(ItemStack stack, String key, String orElse) {
        return tagOf(stack).getStringOr(key, orElse);
    }

    public static boolean has(ItemStack stack, String key) {
        var data = stack.get(DataComponents.CUSTOM_DATA);
        return data != null && !data.isEmpty() && data.copyTag().contains(key);
    }

    private static CompoundTag tagOf(ItemStack stack) {
        var data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? new CompoundTag() : data.copyTag();
    }

    public static <T> ItemStack set(ItemStack stack, DataComponentType<T> type, T value) {
        stack.set(type, value);
        return stack;
    }

    public static <T> T get(ItemStack stack, DataComponentType<T> type, T orElse) {
        return stack.getOrDefault(type, orElse);
    }

    public static <T> T get(ItemStack stack, DataComponentType<T> type) {
        return stack.get(type);
    }

    public static <T> boolean has(ItemStack stack, DataComponentType<T> type) {
        return stack.has(type);
    }

    public static <T> void remove(ItemStack stack, DataComponentType<T> type) {
        stack.remove(type);
    }
}