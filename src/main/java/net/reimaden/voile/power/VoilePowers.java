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

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;
import net.reimaden.voile.Voile;

public class VoilePowers {

    public static void register() {
        Voile.LOGGER.debug("Registering power types for " + Voile.MOD_ID);

        registerPower(ZombieArmsPower::createFactory);
        registerPower(ModifyBehaviorPower::createFactory);
        registerPower(FlipModelPower::createFactory);
        registerPower(ModifyScalePower::createFactory);
        registerPower(ModifyFootstepSoundPower::createFactory);
        registerPower(ModifyHurtSoundPower::createFactory);
        registerPower(ModifyDeathSoundPower::createFactory);
        registerPower(EnchantmentVulnerabilityPower::createFactory);
        registerPower(ReverseInstantEffectsPower::createFactory);
        registerPower(InstantEffectImmunityPower::createFactory);
        registerPower(ConvertEntityPower::createFactory);
        registerPower(ModifyDivergencePower::createFactory);
        registerPower(PreventSprintingParticlesPower::createFactory);
        registerPower(DisableShieldsPower::createFactory);
        registerPower(ActionOnBlockPower::createFactory);
        registerPower(PreventItemSlowdownPower::createFactory);
        registerPower(WaterBreathingPower::createFactory);
        registerPower(PreventTamingPower::createFactory);
        registerPower(PreventFlyingKickPower::createFactory);
    }

    private static void registerPower(PowerFactory<?> factory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, factory.getSerializerId(), factory);
    }

    private static void registerPower(PowerFactorySupplier<?> factorySupplier) {
        registerPower(factorySupplier.createFactory());
    }
}
