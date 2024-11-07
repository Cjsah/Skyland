package net.cjsah.skyland.mixin;

import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(NaturalSpawner.class)
@Debug(export = true,print = true)
public class NaturalSpawnerMixin {
    @ModifyConstant(method = "isRightDistanceToPlayerAndSpawnPoint", constant = @Constant(doubleValue = 24.0))
    private static double injectedD( double value ) {
        return 4.0;
    }
    @ModifyConstant(method = "isRightDistanceToPlayerAndSpawnPoint", constant = @Constant(doubleValue = 576.0))
    private static double injectedSqrt( double value ) {
        return 16.0;
    }
}
