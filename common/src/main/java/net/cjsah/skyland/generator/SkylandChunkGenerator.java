package net.cjsah.skyland.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.event.SkylandChunkGenerateEvent;
import net.cjsah.skyland.mixin.NoiseChunkAccessor;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SkylandChunkGenerator extends ChunkGenerator {
    public static final Codec<SkylandChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((generator) -> generator.settings)
        ).apply(instance, instance.stable(SkylandChunkGenerator::new))
    );
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    private final Holder<NoiseGeneratorSettings> settings;
    private final Aquifer.FluidPicker globalFluidPicker;

    public SkylandChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource);
        this.settings = settings;
        Aquifer.FluidStatus air = new Aquifer.FluidStatus(0, AIR);
        this.globalFluidPicker = (x, y, z) -> air;
    }

    @Override
    protected @NotNull Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    public Holder<NoiseGeneratorSettings> getSettings() {
        return this.settings;
    }

    @Override
    public int getGenDepth() {
        return this.settings.value().noiseSettings().height();
    }

    @Override
    public int getSeaLevel() {
        return this.settings.value().seaLevel();
    }

    @Override
    public int getMinY() {
        return this.settings.value().noiseSettings().minY();
    }

    @Override
    public @NotNull CompletableFuture<ChunkAccess> createBiomes(
        @NotNull Executor executor, @NotNull RandomState randomState, @NotNull Blender blender,
        @NotNull StructureManager structureManager, @NotNull ChunkAccess chunk
    ) {
        return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("init_biomes", () -> {
            this.doCreateBiomes(blender, randomState, chunk);
            return chunk;
        }), Util.backgroundExecutor());
    }

    private void doCreateBiomes(@NotNull Blender blender, @NotNull RandomState random, @NotNull ChunkAccess chunk) {
        NoiseChunk noiseChunk = chunk.getOrCreateNoiseChunk((chunkAccess) -> this.createNoiseChunk(chunkAccess, blender, random));
        BiomeResolver biomeResolver = BelowZeroRetrogen.getBiomeResolver(blender.getBiomeResolver(this.biomeSource), chunk);
        chunk.fillBiomesFromNoise(biomeResolver, ((NoiseChunkAccessor) noiseChunk).invokeCachedClimateSampler(random.router(), this.settings.value().spawnTarget()));
    }

    private @NotNull NoiseChunk createNoiseChunk(ChunkAccess chunk, Blender blender, RandomState random) {
        return NoiseChunk.forChunk(chunk, random, forStructuresInChunk(), this.settings.value(), this.globalFluidPicker, blender);
    }

    public static @NotNull Beardifier forStructuresInChunk() {
        ObjectList<Beardifier.Rigid> rigids = ObjectLists.emptyList();
        ObjectList<JigsawJunction> junctions = ObjectLists.emptyList();
        return new Beardifier(rigids.iterator(), junctions.iterator());
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.@NotNull Types type, @NotNull LevelHeightAccessor level, @NotNull RandomState random) {
        return level.getMinBuildHeight();
    }

    @Override
    public void addDebugScreenInfo(@NotNull List<String> info, @NotNull RandomState random, @NotNull BlockPos pos) {
    }

    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(
        @NotNull Executor executor, @NotNull Blender blender, @NotNull RandomState random,
        @NotNull StructureManager structureManager, @NotNull ChunkAccess chunk
    ) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public @NotNull NoiseColumn getBaseColumn(int x, int z, @NotNull LevelHeightAccessor height, @NotNull RandomState random) {
        return new NoiseColumn(height.getMinBuildHeight(), new BlockState[0]);
    }

    @Override
    public void buildSurface(
        @NotNull WorldGenRegion level, @NotNull StructureManager structureManager, @NotNull RandomState random,
        @NotNull ChunkAccess chunk
    ) {
        Skyland.EVENT_BUS.post(new SkylandChunkGenerateEvent(level, structureManager, random, chunk));
    }

    @Override
    public void applyCarvers(
        @NotNull WorldGenRegion level, long seed, @NotNull RandomState random, @NotNull BiomeManager biomeManager,
        @NotNull StructureManager structureManager, @NotNull ChunkAccess chunk, GenerationStep.@NotNull Carving step
    ) {
    }

    @Override
    public void spawnOriginalMobs(@NotNull WorldGenRegion level) {
    }
}

