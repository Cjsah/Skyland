package net.cjsah.skyland.mixin;

import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(NoiseChunk.class)
public interface NoiseChunkAccessor {
    @Invoker
    Climate.Sampler invokeCachedClimateSampler(NoiseRouter noiseRouter, List<Climate.ParameterPoint> points);

}
