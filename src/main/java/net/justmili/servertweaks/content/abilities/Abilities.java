package net.justmili.servertweaks.content.abilities;

import net.justmili.libs.v1.utils.common.EntityUtil;
import net.justmili.libs.v1.utils.common.EntityUtil.MobData;
import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.justmili.servertweaks.content.abilities.core.AbilityRegistries;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.TickingAbility;
import net.justmili.servertweaks.content.abilities.type.TickingDebuff;
import net.justmili.servertweaks.mixin.accessors.FoxAccessor;
import net.justmili.servertweaks.registries.TagRegistry;
import net.justmili.servertweaks.util.ScalerUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.skeleton.Parched;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.monster.zombie.Husk;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;

import java.util.*;
import java.util.stream.Collectors;

import static net.justmili.libs.v1.utils.common.AttributeUtil.*;

public class Abilities {
    public static void init() {
    }

    /// Extra Ability variables
    public static final Identifier AR_STRONG_HP = ServerTweaks.asId("strong_health");
    public static final Identifier AR_STRONG_DAMAGE = ServerTweaks.asId("strong_damage");

    public static final Ability
        FIRE_IMMUNE, LAVA_IMMUNE, HEAT_IMMUNE, FREEZE_IMMUNE, FALL_IMMUNE,
        LIGHT, SWIFT, HOPPY, DWARF, SQUISHY, MAGNETIC, TOUGH, STRONG,
        AQUATIC_GRACE, BREATHES_UNDERWATER, SCARES_CREEPERS, SCARES_PHANTOMS,
        CHILD_OF_NATURE, NIGHT_VISION, CLIMBS_WALLS, PEARLING, BOVID;

    static {
        FIRE_IMMUNE = register(new FireImmune(id("fire_immune"), "Fire Immune", false));
        LAVA_IMMUNE = register(new LavaImmune(id("lava_immune"), "Lava Immune", false));
        HEAT_IMMUNE = register(new Ability(id("heat_immune"), "Heat Immune", false));
        FREEZE_IMMUNE = register(new FreezeImmune(id("freeze_immune"), "Freeze Immune", false));
        FALL_IMMUNE = register(new FallImmune(id("fall_immune"), "Fall Immune", false));
        LIGHT = register(new Light(id("light"), "Lightweight", false));
        SWIFT = register(new Swift(id("swift"), "Swift", false));
        HOPPY = register(new Hoppy(id("hoppy"), "Hoppy", false));
        DWARF = register(new Dwarf(id("dwarf"), "Dwarf", false));
        SQUISHY = register(new Ability(id("squishy"), "Squishy", false));
        MAGNETIC = register(new Magnetic(id("magnetic"), "Magnetic", false));
        TOUGH = register(new Ability(id("tough"), "Tough", false));
        STRONG = register(new Strong(id("strong"), "Strong", false));
        AQUATIC_GRACE = register(new AquaticGrace(id("aquatic_grace"), "Aquatic Grace", false));
        BREATHES_UNDERWATER = register(new BreathesUnderwater(id("breathes_underwater"), "Breathes Underwater", false));
        SCARES_CREEPERS = register(new ScaresCreepers(id("scares_creepers"), "Scares Creepers", false));
        SCARES_PHANTOMS = register(new ScaresPhantoms(id("scares_phantoms"), "Scares Phantoms", false));
        CHILD_OF_NATURE = register(new ChildOfNature(id("child_of_nature"), "Child of Nature", false));
        NIGHT_VISION = register(new NightVision(id("night_vision"), "Night Vision", false));
        // TODO: Implement climbs_walls, should only work in survival, shouldn't work in water or when flying
        CLIMBS_WALLS = register(new Ability(id("climbs_walls"), "Climbs Walls", true));
        PEARLING = register(new Ability(id("pearling"), "Pearling", false));
        BOVID = register(new Ability(id("bovid"), "Bovid", false));
    }

    private static Identifier id(String id) {
        return ServerTweaks.asId(id);
    }

    private static Ability register(Ability ability) {
        AbilityRegistries.ABILITIES.put(ability.getId(), ability);
        return ability;
    }

    // Define ticking abilities
    static class FireImmune extends TickingAbility {
        FireImmune(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (player.isOnFire() && !level.getBlockState(player.blockPosition()).is(Blocks.FIRE) && !player.hasEffect(MobEffects.WEAKNESS)) player.extinguishFire();
        }
    }

    static class LavaImmune extends TickingAbility {
        LavaImmune(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (player.isOnFire() && !level.getBlockState(player.blockPosition()).is(Blocks.LAVA) && !player.hasEffect(MobEffects.WEAKNESS)) player.extinguishFire();
        }
    }

    static class FreezeImmune extends TickingAbility {
        public FreezeImmune(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.hasEffect(MobEffects.WEAKNESS) && player.isFreezing()) player.clearFreeze();
        }
    }

    static class FallImmune extends TickingAbility {
        FallImmune(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (!player.hasEffect(MobEffects.WEAKNESS) && player.hasEffect(MobEffects.SLOW_FALLING)) player.removeEffect(MobEffects.SLOW_FALLING);
        }
    }

    static class Light extends TickingAbility {
        Light(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (player.hasEffect(MobEffects.WEAKNESS)) return;
            if (player.getDeltaMovement().y < -0.4 && player.fallDistance > 3) EntityUtil.applyEffect(player, MobEffects.SLOW_FALLING, 60, 1);
            if (player.onGround()) player.removeEffect(MobEffects.SLOW_FALLING);
        }
    }

    static class Swift extends TickingAbility {
        Swift(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (player.isSprinting()) EntityUtil.applyEffect(player, MobEffects.SPEED, 30, 0);
        }
    }

    static class Hoppy extends TickingAbility {
        Hoppy(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            EntityUtil.applyEffect(player, MobEffects.JUMP_BOOST, 100, 0);
        }
    }

    static class Dwarf extends TickingAbility {
        Dwarf(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            var scale = ScalerUtil.getScale(player);
            if (scale != null && scale.getBaseValue() > 0.75) ScalerUtil.setScale(player, 0.75f);
            EntityUtil.applyEffect(player, MobEffects.HASTE, 100, 1);
        }
    }

    static class Magnetic extends TickingAbility {
        Magnetic(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            float r = 6f, rSq = r * r;
            var items = level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(r));

            for (ItemEntity item : items) {
                if (item.distanceToSqr(player) > rSq) continue;
                item.playerTouch(player);
            }
        }
    }

    static class Strong extends TickingAbility {
        Strong(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (level.getGameTime() % 5 != 0) return;

            var attack = get(player, Attributes.ATTACK_DAMAGE);
            var maxHp = get(player, Attributes.MAX_HEALTH);

            addOrUpdate(attack, AR_STRONG_DAMAGE, 3, AttributeModifier.Operation.ADD_VALUE);

            // Don't apply past this point
            if (!player.gameMode.isSurvival()) return;

            float min = 40f, max = 80f;
            float targetHp = Math.clamp(max - (player.getArmorValue() * 2), min, max);
            if (targetHp % 2 != 0) targetHp += 1;

            addOrReplace(maxHp, AR_STRONG_HP, targetHp - 20, AttributeModifier.Operation.ADD_VALUE);
            if (player.getHealth() > player.getMaxHealth()) player.setHealth(player.getMaxHealth());
        }
    }

    static class AquaticGrace extends TickingAbility {
        AquaticGrace(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.isInWater()) return;

            EntityUtil.applyEffect(player, MobEffects.CONDUIT_POWER, 100, 0);

            int num = player.hasEffect(MobEffects.POISON)? 1 : 2;
            if (level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.DEPTH_STRIDER)
                .map(e -> EnchantmentHelper.getItemEnchantmentLevel(e, player.getItemBySlot(EquipmentSlot.FEET)) > num)
                .orElse(false)) return; // Return before granting Dolphin's Grace if player has depth strider to prevent OP swimming speeds

            EntityUtil.applyEffect(player, MobEffects.DOLPHINS_GRACE, 100, 0);
        }
    }

    static class BreathesUnderwater extends TickingAbility {
        BreathesUnderwater(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            if (player.isInWater()) EntityUtil.applyEffect(player, MobEffects.WATER_BREATHING, 30, 0);
        }
    }

    static class ScaresCreepers extends TickingAbility {
        ScaresCreepers(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            for (Creeper creeper : EntityUtil.getNearby(player, Creeper.class, 10.0)) {
                creeper.setTarget(null);
                creeper.getNavigation().moveTo(
                    creeper.getX() + (creeper.getX() - player.getX()),
                    creeper.getY(),
                    creeper.getZ() + (creeper.getZ() - player.getZ()), 1.2);
            }
        }
    }

    static class ScaresPhantoms extends TickingAbility {
        ScaresPhantoms(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            for (Phantom phantom : EntityUtil.getNearby(player, Phantom.class, 16.0)) {
                phantom.setTarget(null);
                phantom.getNavigation().moveTo(
                    phantom.getX() + (phantom.getX() - player.getX()),
                    phantom.getY() + 8,
                    phantom.getZ() + (phantom.getZ() - player.getZ()), 1.2);
            }
        }
    }

    static class ChildOfNature extends TickingAbility {
        ChildOfNature(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (level.getGameTime() % 5 != 0) return;
            for (Fox fox : EntityUtil.getNearby(player, Fox.class, 12.0)) {
                var accessor = (FoxAccessor) fox;
                if (accessor.invokeTrusts(player)) continue;
                accessor.invokeAddTrustedEntity(player);
            }
            for (Wolf wolf : EntityUtil.getNearby(player, Wolf.class, 12.0)) {
                if (!wolf.isTame() && wolf.getTarget() == player) wolf.setTarget(null);
            }
        }
    }

    static class NightVision extends TickingAbility {
        NightVision(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (level.isDarkOutside()) EntityUtil.applyEffect(player, MobEffects.NIGHT_VISION, 320, 0);
        }
    }
}
