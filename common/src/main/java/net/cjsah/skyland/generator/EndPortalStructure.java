package net.cjsah.skyland.generator;

import net.cjsah.skyland.SkylandConfig;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public class EndPortalStructure extends SkylandStructures {

    public EndPortalStructure(StructurePiece piece) {
        super(piece);
    }

    @Override
    public void generate(ServerLevelAccessor level, BoundingBox box, RandomSource random) {
        boolean complete = true;

        complete &= this.portal(level, box, random, Direction.SOUTH, 4, 3, 3);
        complete &= this.portal(level, box, random, Direction.SOUTH, 4, 3, 3);
        complete &= this.portal(level, box, random, Direction.SOUTH, 5, 3, 3);
        complete &= this.portal(level, box, random, Direction.SOUTH, 6, 3, 3);
        complete &= this.portal(level, box, random, Direction.NORTH, 4, 3, 7);
        complete &= this.portal(level, box, random, Direction.NORTH, 5, 3, 7);
        complete &= this.portal(level, box, random, Direction.NORTH, 6, 3, 7);
        complete &= this.portal(level, box, random, Direction.EAST, 3, 3, 4);
        complete &= this.portal(level, box, random, Direction.EAST, 3, 3, 5);
        complete &= this.portal(level, box, random, Direction.EAST, 3, 3, 6);
        complete &= this.portal(level, box, random, Direction.WEST, 7, 3, 4);
        complete &= this.portal(level, box, random, Direction.WEST, 7, 3, 5);
        complete &= this.portal(level, box, random, Direction.WEST, 7, 3, 6);

        if (complete) {
            this.fillBlock(level, Blocks.END_PORTAL.defaultBlockState(), 4, 3, 4, 6, 3, 6, box);
        }

        if (SkylandConfig.GenSliverFishSpawner) {
            this.setBlockEntity(level, Blocks.SPAWNER.defaultBlockState(), 5, 3, 9, box, be -> {
                if (be instanceof SpawnerBlockEntity blockEntity) {
                    blockEntity.setEntityId(EntityType.SILVERFISH, random);
                }
            });
        }

    }

    private boolean portal(ServerLevelAccessor level, BoundingBox box, RandomSource random, Direction direction, int x, int y, int z) {
        boolean hasEye = random.nextFloat() > 0.9f;
        BlockState block = Blocks.END_PORTAL_FRAME.defaultBlockState()
                .setValue(EndPortalFrameBlock.FACING, direction)
                .setValue(EndPortalFrameBlock.HAS_EYE, hasEye);
        this.setBlock(level, block, x, y, z, box);
        return hasEye;
    }

}
