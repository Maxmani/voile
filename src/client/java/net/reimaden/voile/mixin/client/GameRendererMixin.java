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

package net.reimaden.voile.mixin.client;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.RotationAxis;
import net.reimaden.voile.power.FlipModelPower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow public abstract Camera getCamera();

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V", shift = At.Shift.AFTER))
    private void voile$flipView(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        Entity entity = this.getCamera().getFocusedEntity();
        if (entity == null) return;

        String string = Formatting.strip(entity.getName().getString());
        // Don't flip the entity's view if they would be flipped the right way up due to their name
        if ("Dinnerbone".equals(string) || "Grumm".equals(string)) return;

        List<FlipModelPower> powers = PowerHolderComponent.getPowers(entity, FlipModelPower.class);

        if (powers.stream().anyMatch(FlipModelPower::shouldFlipView)) {
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180f));
        }
    }
}
