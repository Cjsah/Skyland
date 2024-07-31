package net.cjsah.skyland.generator;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class SkylandStructures {
    protected final StructurePiece piece;
    protected final Rotation rotation;
    protected final Mirror mirror;

    @SuppressWarnings("DataFlowIssue")
    public SkylandStructures(StructurePiece piece) {
        this.piece = piece;
        this.rotation = Objects.requireNonNullElse(piece.getRotation(), Rotation.NONE);
        this.mirror = Objects.requireNonNullElse(piece.getMirror(), Mirror.NONE);
    }

    public abstract void generate(ServerLevelAccessor level, BoundingBox bounds, RandomSource random);

    protected void setBlock(ServerLevelAccessor level, BlockState block, int x, int y, int z, BoundingBox box) {
        BlockPos.MutableBlockPos pos = this.getAbsolutePos(x, y, z);
        if (!box.isInside(pos)) return;
        if (this.mirror != Mirror.NONE) block = block.mirror(this.mirror);
        if (this.rotation != Rotation.NONE) block = block.rotate(this.rotation);
        level.setBlock(pos, block, Block.UPDATE_CLIENTS);
    }

    protected void setBlockEntity(ServerLevelAccessor level, BlockState block, int x, int y, int z, BoundingBox box, Consumer<BlockEntity> blockEntityFactory) {
        BlockPos.MutableBlockPos pos = this.getAbsolutePos(x, y, z);
        if (!box.isInside(pos)) return;
        level.setBlock(pos, block, Block.UPDATE_CLIENTS);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        blockEntityFactory.accept(blockEntity);
    }

    @SuppressWarnings("SameParameterValue")
    protected void fillBlock(ServerLevelAccessor level, BlockState block, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BoundingBox box) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    this.setBlock(level, block, x, y, z, box);
                }
            }
        }
    }

    protected BlockPos.MutableBlockPos getAbsolutePos(int x, int y, int z) {
        BoundingBox box = this.piece.getBoundingBox();
        return new BlockPos.MutableBlockPos(
                applyXTransform(x, z, box),
                box.minY() + y,
                applyZTransform(x, z, box));
    }

    private int applyXTransform(int x, int z, BoundingBox boundingBox) {
        if ((rotation == Rotation.NONE && mirror != Mirror.FRONT_BACK)
                || (rotation == Rotation.CLOCKWISE_180 && mirror == Mirror.FRONT_BACK)) {
            return boundingBox.minX() + x;
        } else if (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180) {
            return boundingBox.maxX() - x;
        } else if ((rotation == Rotation.COUNTERCLOCKWISE_90 && mirror != Mirror.LEFT_RIGHT)
                || (rotation == Rotation.CLOCKWISE_90 && mirror == Mirror.LEFT_RIGHT)) {
            return boundingBox.minX() + z;
        } else {
            return boundingBox.maxX() - z;
        }
    }

    private int applyZTransform(int x, int z, BoundingBox boundingBox) {
        if ((rotation == Rotation.NONE && mirror != Mirror.LEFT_RIGHT)
                || (rotation == Rotation.CLOCKWISE_180 && mirror == Mirror.LEFT_RIGHT)) {
            return boundingBox.minZ() + z;
        } else if (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180) {
            return boundingBox.maxZ() - z;
        } else if ((rotation == Rotation.CLOCKWISE_90 && mirror != Mirror.FRONT_BACK)
                || (rotation == Rotation.COUNTERCLOCKWISE_90 && mirror == Mirror.LEFT_RIGHT)) {
            return boundingBox.minZ() + x;
        } else {
            return boundingBox.maxZ() - x;
        }
    }
}
