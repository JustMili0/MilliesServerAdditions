package net.justmili.libs.v1.utils;

import net.minecraft.resources.Identifier;

public class ResourceUtil {
    public static Identifier parse(String modId, String path) {
        return Identifier.fromNamespaceAndPath(modId, path);
    }
    public static Identifier asPath(String path) {
        return Identifier.parse(path);
    }
    public static Identifier asMinecraft(String path) {
        return parse("minecraft", path);
    }
    public static Identifier asFabric(String path) {
        return parse("fabric", path);
    }

    public static Identifier asBlockPath(String path) {
        return asPath("minecraft:block/"+path);
    }
    public static Identifier asItemPath(String path) {
        return asPath("minecraft:item/"+path);
    }
}
