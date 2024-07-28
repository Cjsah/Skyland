package net.cjsah.skyland.integration.anvilcraft.event;

import dev.anvilcraft.lib.event.SubscribeEvent;
import net.cjsah.skyland.event.SkylandChunkGenerateEvent;
import net.cjsah.skyland.integration.anvilcraft.feature.LandFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.storage.LevelData;
import org.jetbrains.annotations.NotNull;

public class SkylandChunkGenerateEventListener {
    @SubscribeEvent
    public void generate(@NotNull SkylandChunkGenerateEvent event) {
        WorldGenRegion level = event.getLevel();
        LevelData levelData = event.getLevel().getLevelData();
        ChunkPos pos = event.getChunk().getPos();
        BlockPos spawnPos = new BlockPos(levelData.getXSpawn(), levelData.getYSpawn(), levelData.getZSpawn());
        ChunkPos spawnChunk = new ChunkPos(spawnPos);
        if (pos.x != spawnChunk.x || pos.z != spawnChunk.z) return;
        LandFeature.place(level, spawnPos);
    }
}
