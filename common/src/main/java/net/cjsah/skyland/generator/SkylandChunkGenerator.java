package net.cjsah.skyland.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
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
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
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
    public CompletableFuture<ChunkAccess> createBiomes(Executor executor, RandomState randomState, Blender blender, StructureManager structureManager, ChunkAccess chunk) {
        return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("init_biomes", () -> {
            this.doCreateBiomes(blender, randomState, chunk);
            return chunk;
        }), Util.backgroundExecutor());
    }

    private void doCreateBiomes(Blender blender, RandomState random, ChunkAccess chunk) {
        NoiseChunk noiseChunk = chunk.getOrCreateNoiseChunk((chunkAccess) -> this.  createNoiseChunk(chunkAccess, blender, random));
        BiomeResolver biomeResolver = BelowZeroRetrogen.getBiomeResolver(blender.getBiomeResolver(this.biomeSource), chunk);
        chunk.fillBiomesFromNoise(biomeResolver, ((NoiseChunkAccessor) noiseChunk).invokeCachedClimateSampler(random.router(), this.settings.value().spawnTarget()));
    }

    private NoiseChunk createNoiseChunk(ChunkAccess chunk, Blender blender, RandomState random) {
        return NoiseChunk.forChunk(chunk, random, forStructuresInChunk(), this.settings.value(), this.globalFluidPicker, blender);
    }

    public static Beardifier forStructuresInChunk() {
        ObjectList<Beardifier.Rigid> rigids = ObjectLists.emptyList();
        ObjectList<JigsawJunction> junctions = ObjectLists.emptyList();
        return new Beardifier(rigids.iterator(), junctions.iterator());
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState random) {
        return level.getMinBuildHeight();
    }

    @Override
    public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {}

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState random, StructureManager structureManager, ChunkAccess chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState random) {
        return new NoiseColumn(height.getMinBuildHeight(), new BlockState[0]);
    }

    @Override
    public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState random, ChunkAccess chunk) {
    }

    @Override
    public void applyCarvers(WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion level) {
    }
}

