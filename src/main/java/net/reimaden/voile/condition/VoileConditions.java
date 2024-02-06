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

package net.reimaden.voile.condition;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import net.reimaden.voile.Voile;
import net.reimaden.voile.condition.bientity.ScoreboardCondition;
import net.reimaden.voile.condition.entity.MoonPhaseCondition;
import net.reimaden.voile.condition.entity.NearbyEntitiesCondition;
import net.reimaden.voile.condition.entity.PreciseOnBlockCondition;
import net.reimaden.voile.condition.item.CraftableCondition;
import net.reimaden.voile.condition.item.EnchantabilityCondition;

public class VoileConditions {

    public static void register() {
        Voile.LOGGER.debug("Registering condition types for " + Voile.MOD_ID);

        // Entity Conditions
        registerEntityCondition(MoonPhaseCondition.getFactory());
        registerEntityCondition(PreciseOnBlockCondition.getFactory());
        registerEntityCondition(NearbyEntitiesCondition.getFactory());

        // Bi-Entity Conditions
        registerBiEntityCondition(ScoreboardCondition.getFactory());

        // Item Conditions
        registerItemCondition(EnchantabilityCondition.getFactory());
        registerItemCondition(CraftableCondition.getFactory());
    }

    private static void registerEntityCondition(ConditionFactory<Entity> factory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, factory.getSerializerId(), factory);
    }

    private static void registerBiEntityCondition(ConditionFactory<Pair<Entity, Entity>> factory) {
        Registry.register(ApoliRegistries.BIENTITY_CONDITION, factory.getSerializerId(), factory);
    }

    private static void registerItemCondition(ConditionFactory<Pair<World, ItemStack>> factory) {
        Registry.register(ApoliRegistries.ITEM_CONDITION, factory.getSerializerId(), factory);
    }
}
