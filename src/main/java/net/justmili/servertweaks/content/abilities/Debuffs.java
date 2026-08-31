package net.justmili.servertweaks.content.abilities;

import net.justmili.libs.v1.utils.common.EntityUtil;
import net.justmili.servertweaks.ServerTweaks;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.justmili.servertweaks.content.abilities.core.AbilityRegistries;
import net.justmili.servertweaks.content.abilities.type.Debuff;
import net.justmili.servertweaks.content.abilities.type.TickingDebuff;
import net.justmili.servertweaks.mixin.accessors.FoxAccessor;
import net.justmili.servertweaks.registries.TagRegistry;
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
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;

import java.util.*;
import java.util.stream.Collectors;

import static net.justmili.libs.v1.utils.common.AttributeUtil.addOrUpdate;

public class Debuffs {
    public static void init() {
    }

    /// Extra Debuff variables
    public static final Identifier AR_SLOW_SPEED = ServerTweaks.asId("slow_speed");
    private static final Map<UUID, List<WrappedGoal>> STORED_GOALS = new HashMap<>();
    private static final List<EntityUtil.MobData> MONSTER_IGNORE = List.of(
        new EntityUtil.MobData(Pillager.class, 64.0, 0),
        new EntityUtil.MobData(Vindicator.class, 32.0, 0),
        new EntityUtil.MobData(Evoker.class, 16.0, 0),
        new EntityUtil.MobData(Witch.class, 16.0, 0),
        new EntityUtil.MobData(Zombie.class, 48.0, 0),
        new EntityUtil.MobData(Husk.class, 48.0, 0),
        new EntityUtil.MobData(Drowned.class, 48.0, 0),
        new EntityUtil.MobData(Skeleton.class, 24.0, 0),
        new EntityUtil.MobData(Parched.class, 24.0, 0),
        new EntityUtil.MobData(Slime.class, 16.0, 0)
    );
    private static final List<EntityUtil.MobData> MONSTER_FEAR = List.of(
        new EntityUtil.MobData(Villager.class, 16.0, 0)
    );
    private static final List<EntityUtil.MobData> MONSTER_AGGRO = List.of(
        new EntityUtil.MobData(IronGolem.class, 16.0, 0),
        new EntityUtil.MobData(SnowGolem.class, 24.0, 0)
    );
    private static final List<EntityUtil.MobData> PREDATORY_FEAR = List.of(
        new EntityUtil.MobData(Chicken.class, 8.0, 1.4),
        new EntityUtil.MobData(Parrot.class, 12.0, 1.25),
        new EntityUtil.MobData(Frog.class, 12.0, 2.0),
        new EntityUtil.MobData(Salmon.class, 6.0, 1.25),
        new EntityUtil.MobData(Pig.class, 8.0, 1.25)
    );

    public static final Debuff
        HEAT_SENSITIVE, COLD_SENSITIVE, SLOW,
        CANT_BREATHE_AIR, CANT_SWIM, HYDROPHOBIC,
        HUNTED_BY_FOX, HUNTED_BY_WOLF, WEAK_TO_DAMAGE, 
        BURNS_IN_DAYLIGHT, IS_MONSTER, PREDATORY, 
        CARNIVORE, VEGETARIAN, SACCHARIVORE, HERBIVORE, INSECTIVORE;

    static {
        HEAT_SENSITIVE = register(new HeatSensitive(id("heat_sensitive"), "Heat Sensitive", false));
        COLD_SENSITIVE = register(new ColdSensitive(id("cold_sensitive"), "Cold Sensitive", false));
        SLOW = register(new Slow(id("slow"), "Slow", false));
        CANT_BREATHE_AIR = register(new CantBreatheAir(id("cant_breathe_air"), "Can't Breathe Air", false));
        // TODO: Fix cant_swim, should entirely prevent upwards movement, atm only heavily slows down the player
        // Can't be a client-server or packet or whatever thing beause same thing happens in singleplayer
        CANT_SWIM = register(new Debuff(id("cant_swim"), "Can't Swim", true));
        HYDROPHOBIC = register(new Hydrophobic(id("hydrophobic"), "Hydrophobic", false));
        HUNTED_BY_FOX = register(new HuntedByFox(id("hunted_by_fox"), "Hunted By Foxes", false));
        HUNTED_BY_WOLF = register(new HuntedByWolf(id("hunted_by_wolf"), "Hunted By Wolves", false));
        WEAK_TO_DAMAGE = register(new Debuff(id("weak_to_damage"), "Weak to Damage", false));
        BURNS_IN_DAYLIGHT = register(new BurnsInDaylight(id("burns_in_daylight"), "Burns In Daylight", false));
        IS_MONSTER = register(new IsMonster(id("is_monster"), "Monster", false));
        PREDATORY = register(new Predatory(id("predatory"), "Predatory", false));
        CARNIVORE = register(new Debuff(id("carnivore"), "Carnivore", false));
        VEGETARIAN = register(new Debuff(id("vegetarian"), "Vegetarian", false));
        SACCHARIVORE = register(new Debuff(id("saccharivore"), "Saccharivore", false));
        HERBIVORE = register(new Debuff(id("herbivore"), "Herbivore", false));
        INSECTIVORE = register(new Debuff(id("insectivore"), "Insectivore", false));
    }

    private static Identifier id(String id) {
        return ServerTweaks.asId(id);
    }

    private static Debuff register(Debuff debuff) {
        AbilityRegistries.DEBUFFS.put(debuff.getId(), debuff);
        return debuff;
    }

    // Define ticking abilities
    static class HeatSensitive extends TickingDebuff {
        HeatSensitive(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (level.getGameTime() % 20 != 0) return;

            if (!level.getBiome(player.blockPosition()).is(TagRegistry.HOT_BIOMES)) return;
            if (!(level.canSeeSky(player.blockPosition())
                && level.getBrightness(LightLayer.SKY, player.blockPosition()) >= 8)
                || level.isDarkOutside()
                || level.isRainingAt(player.blockPosition())
                || player.isInWater()) return;

            player.hurtServer(level, level.damageSources().onFire(), 1f);
        }
    }

    static class ColdSensitive extends TickingDebuff {
        ColdSensitive(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (!level.getBiome(player.blockPosition()).is(TagRegistry.COLD_BIOMES)) return;
            // Completely cancel any effects if player has full leather armor.
            // Still applies freezing overlays but stops damage when armor isn't full leather. It is an intended side effect.
            if (player.getItemBySlot(EquipmentSlot.HEAD).is(Items.LEATHER_HELMET)
                && player.getItemBySlot(EquipmentSlot.CHEST).is(Items.LEATHER_CHESTPLATE)
                && player.getItemBySlot(EquipmentSlot.LEGS).is(Items.LEATHER_LEGGINGS)
                && player.getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS)) return;

            int targetTime = player.getTicksRequiredToFreeze() + 20;
            player.setTicksFrozen(targetTime);
            player.getEntityData().set(Entity.DATA_TICKS_FROZEN, targetTime, true);
        }
    }

    static class Slow extends TickingDebuff {
        Slow(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            float multiplier = AbilityProfilesUtil.has(player, Abilities.AQUATIC_GRACE) && player.isInWater()? -0.16f : -0.32f;
            var speed = player.getAttribute(Attributes.MOVEMENT_SPEED);

            addOrUpdate(speed, AR_SLOW_SPEED, multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }
    }

    static class CantBreatheAir extends TickingDebuff {
        CantBreatheAir(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            if (player.isInWater() || player.hasEffect(MobEffects.WATER_BREATHING)) {
                // Restore air when in water
                if (player.getAirSupply() < player.getMaxAirSupply())
                    player.setAirSupply(player.getAirSupply() + 4);
            } else {
                // Drain air on land
                player.setAirSupply(player.getAirSupply() - 1);
                if (player.getAirSupply() <= -20) {
                    player.setAirSupply(1);
                    player.hurtServer(level, level.damageSources().drown(), 1.0F);
                }
            }
        }
    }

    static class Hydrophobic extends TickingDebuff {
        Hydrophobic(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            boolean inWaterBlock = player.isInWater();
            boolean inWaterCauldron = level.getBlockState(player.blockPosition()).is(Blocks.WATER_CAULDRON);
            boolean hasHelmet = !player.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
            boolean inWetBiome = level.getBiome(player.blockPosition()).is(TagRegistry.HYDROPHOBIC_HELMET_EXCEPTIONS);
            boolean inRain = player.isInRain() && (!hasHelmet || inWetBiome);

            boolean inWater = inWaterBlock || inRain || inWaterCauldron;

            if (inWater && level.getGameTime() % 20 == 0) player.hurtServer(level, level.damageSources().magic(), 1.0F);
        }
    }

    static class HuntedByFox extends TickingDebuff {
        HuntedByFox(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (level.getGameTime() % 5 != 0) return;
            // TODO: Possibly turn into a goal mixin
            for (Fox fox : EntityUtil.getNearby(player, Fox.class, 12.0)) {
                var accessor = (FoxAccessor) fox;
                if (accessor.invokeTrusts(player)) continue;

                if (fox.getTarget() == null) fox.setTarget(player);
                if (fox.getTarget() == player && !accessor.invokeIsDefending()) accessor.invokeSetDefending(true);
            }
        }
    }

    static class HuntedByWolf extends TickingDebuff {
        HuntedByWolf(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (level.getGameTime() % 5 != 0) return;
            for (Wolf wolf : EntityUtil.getNearby(player, Wolf.class, 16.0)) {
                if (wolf.isTame()) continue;
                if (wolf.getTarget() == null) wolf.setTarget(player);
            }
        }
    }

    static class BurnsInDaylight extends TickingDebuff {
        BurnsInDaylight(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;
            if (!level.isBrightOutside() || !level.canSeeSky(player.blockPosition())) return;
            if (level.getBrightness(LightLayer.SKY, player.blockPosition()) <= 8) return;

            if (!player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) return;
            player.igniteForSeconds(2);
        }
    }

    static class IsMonster extends TickingDebuff {
        IsMonster(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            if (!player.gameMode.isSurvival()) return;

            // Ignore
            Set<UUID> stillNearby = new HashSet<>();
            EntityUtil.executeForNearby(player, MONSTER_IGNORE, (mob, data) -> {
                var uuid = mob.getUUID();
                stillNearby.add(uuid);

                if (!STORED_GOALS.containsKey(uuid)) {
                    List<WrappedGoal> removed = mob.targetSelector.getAvailableGoals()
                        .stream().filter(goal -> goal.getGoal() instanceof NearestAttackableTargetGoal<?>).collect(Collectors.toList());

                    removed.forEach(goal -> mob.targetSelector.removeGoal(goal.getGoal()));
                    STORED_GOALS.put(uuid, removed);

                    mob.setTarget(null);
                }
            });
            STORED_GOALS.entrySet().removeIf(entry -> {
                if (stillNearby.contains(entry.getKey())) return false;
                var entity = level.getEntity(entry.getKey());

                if (entity instanceof Mob mob) {
                    entry.getValue().forEach(goal -> mob.targetSelector.addGoal(goal.getPriority(), goal.getGoal()));
                }

                return true;
            });

            // Fear
            EntityUtil.executeForNearby(player, MONSTER_FEAR, (mob, data) ->
                mob.getNavigation().moveTo(
                    mob.getX() + (mob.getX() - player.getX()),
                    mob.getY(),
                    mob.getZ() + (mob.getZ() - player.getZ()),
                    data.runSpeed()
                )
            );

            // Attack
            EntityUtil.executeForNearby(player, MONSTER_AGGRO, (mob, data) -> {
                    if (mob.getTarget() != player) mob.setTarget(player);
                }
            );
        }
    }

    public static void restoreAllMonsterGoals(ServerLevel level) {
        STORED_GOALS.forEach((uuid, goals) -> {
            if (level.getEntity(uuid) instanceof Mob mob) {
                goals.forEach(goal -> mob.targetSelector.addGoal(goal.getPriority(), goal.getGoal()));
            }
        });
        STORED_GOALS.clear();
    }

    static class Predatory extends TickingDebuff {
        Predatory(Identifier id, String name, boolean requiresClient) {
            super(id, name, requiresClient);
        }

        @Override
        public void tick(ServerPlayer player, ServerLevel level) {
            EntityUtil.executeForNearby(player, PREDATORY_FEAR, (mob, data) ->
                mob.getNavigation().moveTo(
                    mob.getX() + (mob.getX() - player.getX()),
                    mob.getY(),
                    mob.getZ() + (mob.getZ() - player.getZ()),
                    data.runSpeed()
                )
            );
        }
    }
}
