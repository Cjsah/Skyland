package net.cjsah.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndPodiumFeature.class)
public class EndPodiumFeatureMixin {

    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;origin()Lnet/minecraft/core/BlockPos;"))
    private BlockPos changePos(FeaturePlaceContext<NoneFeatureConfiguration> instance) {
        return new BlockPos(0, 64, 0);
    }
}
