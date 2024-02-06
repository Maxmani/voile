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

package net.reimaden.voile.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;
import net.reimaden.voile.Voile;

import java.util.List;
import java.util.stream.Collectors;

public class ApplyRandomEffectAction {

    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity living)) return;

        if (!entity.getWorld().isClient()) {
            List<StatusEffect> effects;
            if (data.isPresent("category")) {
                // Filter status effects by category if "category" is present
                effects = Registries.STATUS_EFFECT.stream()
                        .filter(effect -> effect.getCategory().equals(data.get("category")))
                        .collect(Collectors.toList());
            } else {
                // Otherwise, get all status effects
                effects = Registries.STATUS_EFFECT.stream().collect(Collectors.toList());
            }

            // If there are any effects to filter, remove them from the list
            if (data.isPresent("filtered_effects")) {
                List<StatusEffect> filteredEffects = data.get("filtered_effects");
                effects.removeAll(filteredEffects);
            }

            if (!effects.isEmpty()) {
                Random random = living.getWorld().getRandom();

                // Get a random status effect from the list and apply it to the entity
                StatusEffect randomEffect = effects.get(random.nextInt(effects.size()));
                living.addStatusEffect(new StatusEffectInstance(
                        randomEffect,
                        data.getInt("duration"),
                        data.getInt("amplifier"),
                        data.getBoolean("is_ambient"),
                        data.getBoolean("show_particles"),
                        data.getBoolean("show_icon")
                ));
            }
        }
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
                Voile.id("apply_random_effect"),
                new SerializableData()
                        .add("category", SerializableDataType.enumValue(StatusEffectCategory.class), null)
                        .add("duration", SerializableDataTypes.INT, 100)
                        .add("amplifier", SerializableDataTypes.INT, 0)
                        .add("is_ambient", SerializableDataTypes.BOOLEAN, false)
                        .add("show_particles", SerializableDataTypes.BOOLEAN, true)
                        .add("show_icon", SerializableDataTypes.BOOLEAN, true)
                        .add("filtered_effects", SerializableDataType.list(SerializableDataTypes.STATUS_EFFECT), null),
                ApplyRandomEffectAction::action
        );
    }
}
