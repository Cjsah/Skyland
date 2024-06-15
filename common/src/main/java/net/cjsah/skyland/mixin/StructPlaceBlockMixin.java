package net.cjsah.skyland.mixin;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(StructurePiece.class)
public class StructPlaceBlockMixin {

    @Unique
    private static final List<Block> Skyland$KeepBlocks;

    static {
        Skyland$KeepBlocks = List.of(
                Blocks.AIR, Blocks.VOID_AIR, Blocks.CAVE_AIR, // 空气
                Blocks.END_PORTAL_FRAME // 末地传送门
        );
    }

    @Inject(method = "placeBlock", at = @At("HEAD"), cancellable = true)
    private void placeBlock(WorldGenLevel worldGenLevel, BlockState blockState, int i, int j, int k, BoundingBox boundingBox, CallbackInfo ci) {
        if (!Skyland$KeepBlocks.contains(blockState.getBlock())) {
            ci.cancel();
        }
    }

}
