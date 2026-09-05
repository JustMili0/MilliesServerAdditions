package net.justmili.servertweaks.content.abilities;

import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.content.abilities.core.TypeRegistries;
import net.justmili.servertweaks.content.abilities.type.Modifier;
import net.minecraft.resources.Identifier;

public class Modifiers {
    public static void init() {
    }

    public static final Modifier CAN_EAT_GOLDEN_FOOD;

    static {
        CAN_EAT_GOLDEN_FOOD = register(new Modifier(id("can_eat_golden_foods"), "Can Eat Golden Foods", false));
    }

    private static Identifier id(String id) {
        return ServerTweaks.asId(id);
    }

    private static Modifier register(Modifier modifier) {
        TypeRegistries.MODIFIERS.put(modifier.getId(), modifier);
        return modifier;
    }
}
