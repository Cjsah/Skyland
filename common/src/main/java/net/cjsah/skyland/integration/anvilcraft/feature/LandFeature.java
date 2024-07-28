package net.cjsah.skyland.integration.anvilcraft.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class LandFeature {
    public static void place(LevelAccessor level, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                level.setBlock(pos.offset(new BlockPos(x, -3, z)), Blocks.BEDROCK.defaultBlockState(), 3);
            }
        }
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                level.setBlock(pos.offset(new BlockPos(x, -2, z)), Blocks.DIRT.defaultBlockState(), 3);
                level.setBlock(pos.offset(new BlockPos(x, -1, z)), Blocks.DIRT.defaultBlockState(), 3);
            }
        }
        level.setBlock(pos.offset(new BlockPos(0, -2, 0)), Blocks.BUDDING_AMETHYST.defaultBlockState(), 3);
        level.setBlock(pos.offset(new BlockPos(0, 0, 0)), Blocks.BAMBOO_SAPLING.defaultBlockState(), 3);
    }
}
