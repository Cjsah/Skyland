package net.cjsah.skyland.event;

import lombok.Getter;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.RandomState;

@Getter
public record SkylandChunkGenerateEvent(
        WorldGenRegion level,
        StructureManager structureManager,
        RandomState random,
        ChunkAccess chunk
) {}
