/*
 * This file is part of Voile, a library mod for Minecraft.
 * Copyright (C) 2024  Maxmani
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

package net.reimaden.voile.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.reimaden.voile.Voile;

public class NearbyEntitiesCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {
        if (data.getInt("count") < 0) return false;

        if (!data.isPresent("entity_condition")) {
            return entity.getWorld().getEntitiesByClass(Entity.class, entity.getBoundingBox().expand(data.getFloat("distance")), predicate -> entity != predicate)
                    .size() >= data.getInt("count");
        }
        //noinspection unchecked
        int count = (int) entity.getWorld().getEntitiesByClass(Entity.class, entity.getBoundingBox().expand(data.getFloat("distance")), predicate -> entity != predicate)
                .stream().filter(target -> ((ConditionFactory<Entity>.Instance) data.get("entity_condition")).test(target)).count();
        return count >= data.getInt("count");
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
                Voile.id("nearby_entities"),
                new SerializableData()
                        .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("distance", SerializableDataTypes.FLOAT)
                        .add("count", SerializableDataTypes.INT, 1),
                NearbyEntitiesCondition::condition
        );
    }
}
