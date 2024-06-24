package net.cjsah.skyland.mixin;

import net.cjsah.skyland.Config;
import net.cjsah.skyland.util.ClassMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StructureStart.class)
public class StructureStartMixin {
    @Redirect(
            method = "placeInChunk",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/StructurePiece;postProcess(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/core/BlockPos;)V")
    )
    private void blockFeature(StructurePiece instance, WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
        if (instance instanceof TemplateStructurePiece piece) {
            for (ClassMatcher matcher : Config.PassedStructures) {
                if (matcher.match(instance.getClass(), ((TemplateStructurePieceAccessor)piece).getTemplateName())) {
                    instance.postProcess(level, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, pos);
                }
            }
        }
    }
}
