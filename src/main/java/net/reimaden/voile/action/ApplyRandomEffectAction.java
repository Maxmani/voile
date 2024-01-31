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

package net.reimaden.voile.action;

import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.stream.Collectors;

public class ApplyRandomEffectAction {

    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity living)) return;

        if (!entity.getWorld().isClient()) {
            // Get all status effects with the specified category
            List<StatusEffect> effects = Registries.STATUS_EFFECT.stream()
                    .filter(effect -> effect.getCategory().equals(data.get("category")))
                    .collect(Collectors.toList());

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
}
