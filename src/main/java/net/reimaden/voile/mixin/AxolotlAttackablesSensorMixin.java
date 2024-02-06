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

package net.reimaden.voile.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.AxolotlAttackablesSensor;
import net.reimaden.voile.power.ModifyBehaviorPower;
import net.reimaden.voile.util.BehaviorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AxolotlAttackablesSensor.class)
public abstract class AxolotlAttackablesSensorMixin {

    @ModifyExpressionValue(method = "matches", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/brain/sensor/AxolotlAttackablesSensor;isAlwaysHostileTo(Lnet/minecraft/entity/LivingEntity;)Z"))
    private boolean voile$markPlayerAsHostile(boolean original, LivingEntity entity, LivingEntity target) {
        BehaviorHelper behaviorHelper = new BehaviorHelper(target, entity);

        return original || behaviorHelper.behaviorMatches(ModifyBehaviorPower.EntityBehavior.HOSTILE);
    }
}
