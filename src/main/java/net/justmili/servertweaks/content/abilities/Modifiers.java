package net.justmili.servertweaks.content.abilities;

import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.content.abilities.core.Registries;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import net.minecraft.resources.Identifier;

public class Modifiers {
    public static void init() {}

    public static final AbilityModifier CAN_EAT_GOLDEN_FOOD;

    static {
        CAN_EAT_GOLDEN_FOOD = register(new AbilityModifier(id("can_eat_golden_foods")));
    }

    private static Identifier id(String id) {
        return ServerTweaks.asResource(id);
    }
    private static AbilityModifier register(AbilityModifier modifier) {
        Registries.MODIFIERS.put(modifier.getId(), modifier);
        return modifier;
    }
}
