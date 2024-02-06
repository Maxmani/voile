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

package net.reimaden.voile.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.VariantHolder;
import net.minecraft.entity.ai.goal.UntamedActiveTargetGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.reimaden.voile.power.ModifyBehaviorPower;
import net.reimaden.voile.util.BehaviorHelper;
import net.reimaden.voile.util.TameUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntity implements VariantHolder<CatVariant> {

    protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void voile$markPlayerAsTarget(CallbackInfo ci) {
        this.targetSelector.add(1, new UntamedActiveTargetGoal<>(this, PlayerEntity.class, false, livingEntity -> {
            BehaviorHelper behaviorHelper = new BehaviorHelper(livingEntity, this);

            if (behaviorHelper.checkEntity()) {
                return behaviorHelper.behaviorMatches(ModifyBehaviorPower.EntityBehavior.HOSTILE);
            }

            return false;
        }));
    }

    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    private void voile$makeCatHiss(CallbackInfoReturnable<SoundEvent> cir) {
        if (this.getTarget() != null && this.getTarget().isPlayer()) {
            cir.setReturnValue(SoundEvents.ENTITY_CAT_HISS);
        }
    }

    @ModifyExpressionValue(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private int voile$preventTaming(int original, PlayerEntity player) {
        return TameUtil.preventTaming(original, player, this);
    }
}
