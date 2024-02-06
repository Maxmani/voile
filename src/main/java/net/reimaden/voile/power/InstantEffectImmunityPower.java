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
import net.minecraft.entity.effect.StatusEffect;
import net.reimaden.voile.Voile;

import java.util.HashSet;

public class InstantEffectImmunityPower extends Power {

    private final HashSet<StatusEffect> effects = new HashSet<>();
    private final boolean inverted;

    public InstantEffectImmunityPower(PowerType<?> type, LivingEntity entity, boolean inverted) {
        super(type, entity);
        this.inverted = inverted;
    }

    public InstantEffectImmunityPower(PowerType<?> type, LivingEntity living, SerializableData.Instance data) {
        this(type, living, data.getBoolean("inverted"));
        if (data.isPresent("effect")) this.effects.add(data.get("effect"));
        if (data.isPresent("effects")) this.effects.addAll(data.get("effects"));
    }

    public boolean containsEffect(StatusEffect effect) {
        return this.inverted ^ this.effects.contains(effect);
    }

    public static PowerFactory<Power> createFactory() {
        return new PowerFactory<>(
                Voile.id("instant_effect_immunity"),
                new SerializableData()
                        .add("effect", SerializableDataTypes.STATUS_EFFECT, null)
                        .add("effects", SerializableDataTypes.STATUS_EFFECTS, null)
                        .add("inverted", SerializableDataTypes.BOOLEAN, false),
                data -> (type, entity) -> new InstantEffectImmunityPower(type, entity, data)
        ).allowCondition();
    }
}
