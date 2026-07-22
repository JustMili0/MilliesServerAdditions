package net.justmili.servertweaks.content.mechanics.features;

import net.minecraft.core.Rotations;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class ArmedArmorStands {
    public static void onEntityLoad(Entity entity) {
        if (!(entity instanceof ArmorStand armorStand)) return;

        // {ShowArms:1b,Pose:{LeftArm:[0f,0f,-5f],RightArm:[0f,0f,5f]}} but extra
        if (armorStand.getName().getString().equalsIgnoreCase("display") && !armorStand.showArms()) {
            armorStand.setShowArms(true);
            armorStand.setLeftArmPose(new Rotations(-5, 0, -5));
            armorStand.setRightArmPose(new Rotations(-10, 0, 5));
        }
    }
}
