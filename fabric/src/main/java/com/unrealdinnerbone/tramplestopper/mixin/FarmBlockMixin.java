package com.unrealdinnerbone.tramplestopper.mixin;

import com.unrealdinnerbone.tramplestopper.TrampleConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public class FarmBlockMixin {

    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    public void onLandedUpon(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance, CallbackInfo ci) {
        if (TrampleConfig.CONFIG.get().type().getFunction().apply(entity)) {
            ci.cancel();
        }
    }
}
