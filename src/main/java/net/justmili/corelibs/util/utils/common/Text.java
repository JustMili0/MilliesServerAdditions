package net.justmili.corelibs.util.utils.common;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

public class Text {

    public static MutableComponent empty() {
        return Component.empty();
    }

    public static MutableComponent string(String text, Object... args) {
        return Component.literal(String.format(text, args));
    }

    public static MutableComponent obfuscate(String text, Object... args) {
        return Component.literal(String.format(text, args)).withStyle(ChatFormatting.OBFUSCATED);
    }

    public static MutableComponent toList(String text) {
        return Component.literal(text);
    }

    public static MutableComponent translate(String key, Object... args) {
        return Component.translatable(String.format(key, args));
    }

    public static MutableComponent translateSafe(String key, Object... args) {
        return Component.translatableEscape(key, args);
    }

    public static MutableComponent translateWithFallback(String key, String fallback) {
        return Component.translatableWithFallback(key, fallback, TranslatableContents.NO_ARGS);
    }

    public static MutableComponent translateWithFallback(String key, String fallback, Object... args) {
        return Component.translatableWithFallback(key, fallback, args);
    }
}
