package net.cjsah.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Feature.class)
public class FeatureMixin {

    @Unique
    private static final List<Class<?>> skyland$passedFeatures;

    static {
        skyland$passedFeatures = List.of(
                SpikeFeature.class,
                EndPodiumFeature.class
        );
    }

    @Inject(
            method = "place(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void blockPlace(FeatureConfiguration featureConfiguration, WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (!skyland$passedFeatures.contains(this.getClass())) {
            cir.setReturnValue(false);
        }
    }
}
