package net.cjsah.skyland.integration.anvilcraft.event;

import dev.anvilcraft.lib.event.SubscribeEvent;
import net.cjsah.skyland.event.SkylandChunkGenerateEvent;
import net.cjsah.skyland.integration.anvilcraft.feature.LandFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.storage.LevelData;
import org.jetbrains.annotations.NotNull;

public class SkylandChunkGenerateEventListener {
    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public void generate(@NotNull SkylandChunkGenerateEvent event) {
//        WorldGenRegion level = event.getLevel();
//        ServerLevel serverLevel = event.getLevel().getLevel();
//        if (serverLevel.dimension() != ServerLevel.OVERWORLD) return;
//        BlockPos spawnPos = new BlockPos(8, 64, 8);
//        LevelData levelData = level.getLevelData();
//        ChunkPos pos = event.getChunk().getPos();
//        if (pos.x == levelData.getXSpawn() && pos.z == levelData.getYSpawn()) {
//            serverLevel.setDefaultSpawnPos(spawnPos, 0.0f);
//        }
//        if (pos.x != 0 || pos.z != 0) return;
//        LandFeature.place(level, spawnPos);
    }
}
