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
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.reimaden.voile.power.PreventTamingPower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(targets = "net.minecraft.entity.passive.FoxEntity$MateGoal")
public abstract class FoxEntityMateGoalMixin extends AnimalMateGoal {

    public FoxEntityMateGoalMixin(AnimalEntity animal, double speed) {
        super(animal, speed);
    }

    @ModifyExpressionValue(method = "breed", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getUuid()Ljava/util/UUID;", ordinal = 0))
    private UUID voile$preventTrustedUUIDAddition(UUID original) {
        AnimalEntity fox = this.animal;
        if (fox == null) return original;
        ServerPlayerEntity player = fox.getLovingPlayer();

        List<PreventTamingPower> powers = PowerHolderComponent.getPowers(player, PreventTamingPower.class);
        Optional<PreventTamingPower> preventTamingPower = powers.stream().filter(power -> power.doesApply(fox)).findFirst();

        if (preventTamingPower.isPresent()) {
            preventTamingPower.get().executeAction(fox);
            return null;
        }

        return original;
    }

    @ModifyExpressionValue(method = "breed", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getUuid()Ljava/util/UUID;", ordinal = 1))
    private UUID voile$preventTrustedUUIDAdditionMate(UUID original) {
        AnimalEntity fox = this.mate;
        if (fox == null) return original;
        ServerPlayerEntity player = fox.getLovingPlayer();

        List<PreventTamingPower> powers = PowerHolderComponent.getPowers(player, PreventTamingPower.class);
        Optional<PreventTamingPower> preventTamingPower = powers.stream().filter(power -> power.doesApply(fox)).findFirst();

        if (preventTamingPower.isPresent()) {
            preventTamingPower.get().executeAction(fox);
            return null;
        }

        return original;
    }
}
