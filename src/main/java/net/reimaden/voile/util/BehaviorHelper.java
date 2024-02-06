/*
 * This file is part of Voile, a library mod for Minecraft.
 * Copyright (C) 2023-2024  Maxmani
 *
 * Voile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Voile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Voile.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.reimaden.voile.util;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.reimaden.voile.mixin.MobEntityAccessor;
import net.reimaden.voile.power.ModifyBehaviorPower;

import java.util.List;

/**
 * Helper class for applying behavior modifications to entities through the Modify Behavior power.
 */
public class BehaviorHelper {

    private final Entity mob;
    private final List<ModifyBehaviorPower> powers;

    /**
     * @param user The entity that has the Modify Behavior power.
     * @param mob The entity that is being modified.
     */
    public BehaviorHelper(Entity user, Entity mob) {
        this.mob = mob;
        this.powers = PowerHolderComponent.getPowers(user, ModifyBehaviorPower.class);
    }
    
    /**
     * Checks if the Modify Behavior power's conditions apply to the mob.
     * @return True if the Modify Behavior power's conditions apply to the mob.
     */
    public boolean checkEntity() {
        this.powers.removeIf(power -> !power.checkEntity(this.mob));
        return !powers.isEmpty();
    }

    /**
     * Checks if the Modify Behavior power's desired behavior matches the given behavior.
     * @param entityBehavior The behavior to check against.
     * @return True if the Modify Behavior power's desired behavior matches the given behavior.
     */
    public boolean behaviorMatches(ModifyBehaviorPower.EntityBehavior entityBehavior) {
        if (this.powers.isEmpty()) return false;
        return this.powers.stream().findFirst().get().getDesiredBehavior().equals(entityBehavior);
    }

    /**
     * Checks if the Modify Behavior power's desired behavior is not hostile.
     * @return True if either neutral or passive.
     */
    public boolean neutralOrPassive() {
        return this.behaviorMatches(ModifyBehaviorPower.EntityBehavior.NEUTRAL) || this.behaviorMatches(ModifyBehaviorPower.EntityBehavior.PASSIVE);
    }

    /**
     * Checks if the Modify Behavior power's desired behavior is not neutral.
     * @return True if either hostile or passive.
     */
    public boolean hostileOrPassive() {
        return this.behaviorMatches(ModifyBehaviorPower.EntityBehavior.HOSTILE) || this.behaviorMatches(ModifyBehaviorPower.EntityBehavior.PASSIVE);
    }

    /**
     * Checks if the Modify Behavior power's desired behavior is not neutral or passive, but also isn't explicitly hostile.
     * @return True if neither neutral nor passive is set.
     */
    public boolean neitherNeutralNorPassive() {
        return !this.behaviorMatches(ModifyBehaviorPower.EntityBehavior.NEUTRAL) && !this.behaviorMatches(ModifyBehaviorPower.EntityBehavior.PASSIVE);
    }

    /**
     * Adds a new target goal to the mob that targets players with a Modify Behavior power that has the desired behavior set to hostile.
     * @param mob The mob to add the target goal to.
     */
    public static void targetPlayer(MobEntity mob) {
        ((MobEntityAccessor) mob).targetSelector().add(1, new ActiveTargetGoal<>(mob, PlayerEntity.class, false, livingEntity -> {
            BehaviorHelper behaviorHelper = new BehaviorHelper(livingEntity, mob);

            if (behaviorHelper.checkEntity()) {
                return behaviorHelper.behaviorMatches(ModifyBehaviorPower.EntityBehavior.HOSTILE);
            }

            return false;
        }));
    }
}
