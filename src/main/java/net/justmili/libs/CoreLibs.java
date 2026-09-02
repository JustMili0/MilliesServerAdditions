package net.justmili.libs;

import net.justmili.libs.v1.utils.common.ResourceUtil;
import net.justmili.libs.v1.utils.server.ServerUtil;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreLibs {
    public static final String MODID = "corelibs";
    public static final Logger LOGGER = LoggerFactory.getLogger(CoreLibs.class);

    public static void init() {
    }

    public static Identifier asId(String path) {
        return ResourceUtil.parse(MODID, path);
    }
}