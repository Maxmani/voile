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

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.reimaden.voile.Voile;

public class ModifyDivergencePower extends Power {

    private final float divergence;

    public ModifyDivergencePower(PowerType<?> type, LivingEntity entity, float divergence) {
        super(type, entity);
        this.divergence = divergence;
    }

    public float getDivergence() {
        return this.divergence;
    }

    public static PowerFactory<Power> createFactory() {
        return new PowerFactory<>(
                Voile.id("modify_divergence"),
                new SerializableData()
                        .add("divergence", SerializableDataTypes.FLOAT),
                data -> (type, entity) -> new ModifyDivergencePower(type, entity, data.getFloat("divergence"))
        ).allowCondition();
    }
}
