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

package net.reimaden.voile.condition.bientity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Pair;
import net.reimaden.voile.Voile;

public class ScoreboardCondition {

    public static boolean condition(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {
        Entity actor = actorAndTarget.getLeft();
        Entity target = actorAndTarget.getRight();

        if (actor == null || target == null) return false;

        String actorName = actor instanceof PlayerEntity playerEntity ? playerEntity.getName().getString() : actor.getUuidAsString();
        String targetName = target instanceof PlayerEntity playerEntity ? playerEntity.getName().getString() : target.getUuidAsString();

        Scoreboard scoreboard = actor.getWorld().getScoreboard();
        ScoreboardObjective actorObjective = scoreboard.getNullableObjective(data.getString("actor_objective"));
        ScoreboardObjective targetObjective = scoreboard.getNullableObjective(data.getString("target_objective"));

        if (scoreboard.playerHasObjective(actorName, actorObjective) && scoreboard.playerHasObjective(targetName, targetObjective)) {
            Comparison comparison = data.get("comparison");
            return comparison.compare(scoreboard.getPlayerScore(actorName, actorObjective).getScore(), scoreboard.getPlayerScore(targetName, targetObjective).getScore());
        }

        return false;
    }

    public static ConditionFactory<Pair<Entity, Entity>> getFactory() {
        return new ConditionFactory<>(
                Voile.id("scoreboard"),
                new SerializableData()
                        .add("actor_objective", SerializableDataTypes.STRING)
                        .add("target_objective", SerializableDataTypes.STRING)
                        .add("comparison", ApoliDataTypes.COMPARISON, Comparison.EQUAL),
                ScoreboardCondition::condition
        );
    }
}
