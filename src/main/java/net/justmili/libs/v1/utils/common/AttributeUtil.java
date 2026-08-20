package net.justmili.libs.v1.utils.common;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributeUtil {
    public static AttributeModifier create(Identifier id, double value, AttributeModifier.Operation operation) {
        return new AttributeModifier(id, value, operation);
    }
    public static AttributeInstance get(LivingEntity entity, Holder<Attribute> attribute) {
        return entity.getAttribute(attribute);
    }
    public static double getValue(LivingEntity entity, Holder<Attribute> attribute) {
        return get(entity, attribute).getValue();
    }

    public static void addTransient(AttributeInstance instance, AttributeModifier modifier) {
        if (instance == null) return;
        instance.addTransientModifier(modifier);
    }
    public static void addPermanent(AttributeInstance instance, AttributeModifier modifier) {
        if (instance == null) return;
        instance.addPermanentModifier(modifier);
    }
    public static void addTransient(AttributeInstance instance, Identifier id, double value, AttributeModifier.Operation operation) {
        if (instance == null) return;
        addTransient(instance, create(id, value, operation));
    }
    public static void addPermanent(AttributeInstance instance, Identifier id, double value, AttributeModifier.Operation operation) {
        if (instance == null) return;
        addPermanent(instance, create(id, value, operation));
    }

    public static void addOrUpdate(AttributeInstance instance, AttributeModifier modifier) {
        if (instance == null) return;
        instance.addOrUpdateTransientModifier(modifier);
    }
    public static void addOrReplace(AttributeInstance instance, AttributeModifier modifier) {
        if (instance == null) return;
        instance.addOrReplacePermanentModifier(modifier);
    }
    public static void addOrUpdate(AttributeInstance instance, Identifier id, double value, AttributeModifier.Operation operation) {
        if (instance == null) return;
        instance.addOrUpdateTransientModifier(create(id, value, operation));
    }
    public static void addOrReplace(AttributeInstance instance, Identifier id, double value, AttributeModifier.Operation operation) {
        if (instance == null) return;
        instance.addOrReplacePermanentModifier(create(id, value, operation));
    }
}
