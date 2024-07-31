package net.cjsah.skyland.mixin;

import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.generator.SkylandChunkGenerator;
import net.cjsah.skyland.init.ModConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(
        method = "setInitialSpawn",
        locals = LocalCapture.CAPTURE_FAILHARD,
        at =
        @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/ServerLevelData;setSpawn(Lnet/minecraft/core/BlockPos;F)V",
            ordinal = 1,
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private static void generateSpawnPlatform(
        ServerLevel level,
        ServerLevelData levelData,
        boolean bonusChest,
        boolean debugWorld,
        CallbackInfo ci,
        ServerChunkCache serverChunkManager,
        ChunkPos spawnChunk,
        int spawnHeight
    ) {
        ServerChunkCache chunkManager = level.getChunkSource();
        ChunkGenerator chunkGenerator = chunkManager.getGenerator();
        if (!(chunkGenerator instanceof SkylandChunkGenerator)) return;
        BlockPos worldSpawn = spawnChunk.getMiddleBlockPosition(spawnHeight);

        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0));
        random.setLargeFeatureSeed(level.getSeed(), spawnChunk.x, spawnChunk.z);

        Holder.Reference<ConfiguredFeature<?, ?>> spawnPlatformFeature = level.registryAccess()
            .registryOrThrow(Registries.CONFIGURED_FEATURE)
            .getHolderOrThrow(ModConfiguredFeatures.SPAWN_PLATFORM);

        if (!spawnPlatformFeature.value().place(level, chunkGenerator, random, worldSpawn)) {
            Skyland.LOGGER.error("Couldn't generate spawn platform");
        }

        ci.cancel();
    }
}
