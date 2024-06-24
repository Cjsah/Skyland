package net.cjsah.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalPiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RuinedPortalPiece.class)
public class RuinedPortalPieceMixin {
    @Inject(
            method = "postProcess",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece;spreadNetherrack(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/LevelAccessor;)V"),
            cancellable = true
    )
    private void blockPlaceStruct(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfo ci) {
        ci.cancel();
    }
}
