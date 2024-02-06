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
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.reimaden.voile.Voile;

public class PreciseOnBlockCondition {

    // Copy of Apoli's On Block condition with more precision

    public static boolean condition(SerializableData.Instance data, Entity entity) {
        //noinspection unchecked
        return entity.isOnGround() &&
                (!data.isPresent("block_condition") || (((ConditionFactory<CachedBlockPosition>.Instance) data.get("block_condition")).test(
                        new CachedBlockPosition(entity.getWorld(), BlockPos.ofFloored(entity.getX(), entity.getBoundingBox().minY - 0.0000001D, entity.getZ()), true))));
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
                Voile.id("precise_on_block"),
                new SerializableData()
                        .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
                PreciseOnBlockCondition::condition
        );
    }
}
