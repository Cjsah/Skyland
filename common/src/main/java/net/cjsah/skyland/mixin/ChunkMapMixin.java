package net.cjsah.skyland.mixin;

import net.cjsah.skyland.generator.SkylandChunkGenerator;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {

    @Shadow
    private ChunkGenerator generator;

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;dummy()Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;"
            )
    )
    private NoiseGeneratorSettings skylandSettings() {
        if (this.generator instanceof SkylandChunkGenerator skylandChunkGenerator) {
            return skylandChunkGenerator.getSettings().value();
        }
        return NoiseGeneratorSettings.dummy();
    }


}
