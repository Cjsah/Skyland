package net.cjsah.skyland.mixin;

import com.mojang.serialization.Codec;
import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.generator.SkylandChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGenerators;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerators.class)
public class RegisterChunkGeneratorMixin {
    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void register(Registry<Codec<? extends ChunkGenerator>> registry, CallbackInfoReturnable<Codec<? extends ChunkGenerator>> cir) {
        Registry.register(registry, Skyland.SKYLAND_ID, SkylandChunkGenerator.CODEC);
    }
}
