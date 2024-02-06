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
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.reimaden.voile.Voile;

public class DisableShieldAction {

    public static void action(SerializableData.Instance data, Entity entity) {
        if (entity == null) return;

        // Can only disable shields on players
        if (!(entity instanceof PlayerEntity player)) return;

        // Make sure the player is actually using a shield
        if (player.getActiveItem().isIn(ConventionalItemTags.SHIELDS)) {
            player.disableShield(true);
        }
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
                Voile.id("disable_shield"),
                new SerializableData(),
                DisableShieldAction::action
        );
    }
}
