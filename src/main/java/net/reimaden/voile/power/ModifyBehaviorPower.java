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

package net.reimaden.voile.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;
import net.reimaden.voile.Voile;

import java.util.function.Predicate;

public class ModifyBehaviorPower extends Power {

    private final Predicate<Entity> entityCondition;
    private final Predicate<Pair<Entity, Entity>> biEntityCondition;
    private final EntityBehavior desiredBehavior;

    public ModifyBehaviorPower(PowerType<?> type, LivingEntity player, EntityBehavior desiredBehavior,
                               Predicate<Entity> entityCondition, Predicate<Pair<Entity, Entity>> biEntityCondition) {
        super(type, player);
        this.entityCondition = entityCondition;
        this.biEntityCondition = biEntityCondition;
        this.desiredBehavior = desiredBehavior;
    }

    public boolean checkEntity(Entity mob) {
        return (this.entityCondition == null || this.entityCondition.test(mob)) && (this.biEntityCondition == null || this.biEntityCondition.test(new Pair<>(entity, mob)));
    }

    public EntityBehavior getDesiredBehavior() {
        return this.desiredBehavior;
    }

    public enum EntityBehavior {
        HOSTILE,
        NEUTRAL,
        PASSIVE
    }

    public static PowerFactory<Power> createFactory() {
        return new PowerFactory<>(
                Voile.id("modify_behavior"),
                new SerializableData()
                        .add("behavior", SerializableDataType.enumValue(ModifyBehaviorPower.EntityBehavior.class))
                        .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null),
                data -> (type, entity) -> new ModifyBehaviorPower(type, entity, data.get("behavior"), data.get("entity_condition"), data.get("bientity_condition"))
        ).allowCondition();
    }
}
