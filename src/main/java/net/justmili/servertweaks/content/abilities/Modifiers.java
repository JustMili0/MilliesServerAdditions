package net.justmili.servertweaks.content.abilities;

import net.justmili.servertweaks.content.abilities.core.RegistryMaps;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;

public class Modifiers {
    public static final AbilityModifier CAN_EAT_GOLDEN_FOOD;

    static {
        CAN_EAT_GOLDEN_FOOD = register(new AbilityModifier("can_eat_golden_foods"));
    }

    private static AbilityModifier register(AbilityModifier modifier) {
        RegistryMaps.MODIFIERS.put(modifier.getId(), modifier);
        return modifier;
    }
}
