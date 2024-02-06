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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import net.reimaden.voile.Voile;

public class ModifyHurtSoundPower extends ModifySoundPower {

    public ModifyHurtSoundPower(PowerType<?> type, LivingEntity living, SerializableData.Instance data) {
        super(type, living, data);
    }

    @Override
    protected void playSound(Entity entity, SoundEvent soundEvent, float volume, float pitch) {
        Random random = entity instanceof LivingEntity living ? living.getRandom() : entity.getWorld().getRandom();
        entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                soundEvent, entity.getSoundCategory(), volume, (random.nextFloat() - random.nextFloat()) * 0.2f + pitch);
    }

    public static PowerFactory<Power> createFactory() {
        return new PowerFactory<>(
                Voile.id("modify_hurt_sound"),
                ModifySoundPower.getSerializableData(),
                data -> (type, entity) -> new ModifyHurtSoundPower(type, entity, data)
        ).allowCondition();
    }
}
