package net.justmili.corelibs;

import net.justmili.corelibs.v1.utils.common.ResourceUtil;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreLibs {
    // Package temporarily renamed to net.justmili.mlibs so classes that I have in my other mods don't conflict with these
    public static final String MODID = "corelibs";
    public static final Logger LOGGER = LoggerFactory.getLogger(CoreLibs.class);

    public static void init() {
    }

    public static Identifier asId(String path) {
        return ResourceUtil.parse(MODID, path);
    }
}