package net.cjsah.skyland.event;

import lombok.Getter;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.RandomState;

@Getter
public class SkylandChunkGenerateEvent {
    private final WorldGenRegion level;
    private final StructureManager structureManager;
    private final RandomState random;
    private final ChunkAccess chunk;

    public SkylandChunkGenerateEvent(WorldGenRegion level, StructureManager structureManager, RandomState random, ChunkAccess chunk) {
        this.level = level;
        this.structureManager = structureManager;
        this.random = random;
        this.chunk = chunk;
    }
}
