package net.cjsah.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.structures.EndCityStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndCityStructure.class)
public abstract class EndCityStructureMixin extends Structure {
    protected EndCityStructureMixin(StructureSettings structureSettings) {
        super(structureSettings);
    }

    @Redirect(method = "findGenerationPoint", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/structures/EndCityStructure;getLowestYIn5by5BoxOffset7Blocks(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Lnet/minecraft/world/level/block/Rotation;)Lnet/minecraft/core/BlockPos;"))
    private BlockPos modifyY(EndCityStructure instance, Structure.GenerationContext context, Rotation rotation) {
        ChunkPos chunkpos = context.chunkPos();
        int x = chunkpos.getBlockX(7);
        int z = chunkpos.getBlockZ(7);
        boolean generate = context.random().next(10) % 10 == 0;
        return new BlockPos(x, generate ? 64 : 0, z);
    }
}
