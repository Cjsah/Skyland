package net.cjsah.skyland.mixin;

import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(NoiseChunk.class)
public interface NoiseChunkAccessor {
    @Accessor
    int getCellWidth();
    @Accessor
    int getCellHeight();
    @Invoker
    BlockState invokeGetInterpolatedState();

    @Invoker
    Climate.Sampler invokeCachedClimateSampler(NoiseRouter noiseRouter, List<Climate.ParameterPoint> points);

}
