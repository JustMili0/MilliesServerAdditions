package net.justmili.servertweaks.content.abilities;

import net.justmili.servertweaks.content.abilities.core.Registries;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;

public class Modifiers {
    public static final AbilityModifier CAN_EAT_GOLDEN_FOOD;

    public static void init() {}
    static {
        CAN_EAT_GOLDEN_FOOD = register(new AbilityModifier("can_eat_golden_foods"));
    }

    private static AbilityModifier register(AbilityModifier modifier) {
        Registries.MODIFIERS.put(modifier.getId(), modifier);
        return modifier;
    }
}
