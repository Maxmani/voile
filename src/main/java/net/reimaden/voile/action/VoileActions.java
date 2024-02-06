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

package net.reimaden.voile.action;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.reimaden.voile.Voile;
import net.reimaden.voile.action.bientity.StoreDataAction;
import net.reimaden.voile.action.entity.ApplyRandomEffectAction;
import net.reimaden.voile.action.entity.ChangeResourceWithStatusEffectsAction;
import net.reimaden.voile.action.entity.DisableShieldAction;

public class VoileActions {

    public static void register() {
        Voile.LOGGER.debug("Registering action types for " + Voile.MOD_ID);

        // Entity Actions
        registerEntityAction(ChangeResourceWithStatusEffectsAction.getFactory());
        registerEntityAction(DisableShieldAction.getFactory());
        registerEntityAction(ApplyRandomEffectAction.getFactory());

        // Bi-Entity Actions
        registerBiEntityAction(StoreDataAction.getFactory());
    }

    private static void registerEntityAction(ActionFactory<Entity> factory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, factory.getSerializerId(), factory);
    }

    private static void registerBiEntityAction(ActionFactory<Pair<Entity, Entity>> factory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, factory.getSerializerId(), factory);
    }
}
