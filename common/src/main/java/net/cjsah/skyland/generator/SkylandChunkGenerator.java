package net.cjsah.skyland.generator;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.skyland.mixin.NoiseChunkAccessor;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
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
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;
import java.util.Set;
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
            this.doCreateBiomes(blender, randomState, structureManager, chunk);
            return chunk;
        }), Util.backgroundExecutor());
    }

    private void doCreateBiomes(Blender blender, RandomState random, StructureManager structureManager, ChunkAccess chunk) {
        NoiseChunk noiseChunk = chunk.getOrCreateNoiseChunk((chunkAccess) -> this.createNoiseChunk(chunkAccess, structureManager, blender, random));
        BiomeResolver biomeResolver = BelowZeroRetrogen.getBiomeResolver(blender.getBiomeResolver(this.biomeSource), chunk);
        chunk.fillBiomesFromNoise(biomeResolver, ((NoiseChunkAccessor) noiseChunk).invokeCachedClimateSampler(random.router(), this.settings.value().spawnTarget()));
    }

    private NoiseChunk createNoiseChunk(ChunkAccess chunk, StructureManager structureManager, Blender blender, RandomState random) {
        return NoiseChunk.forChunk(chunk, random, Beardifier.forStructuresInChunk(structureManager, chunk.getPos()), this.settings.value(), this.globalFluidPicker, blender);
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState random) {
        return level.getMinBuildHeight();
    }

    @Override
    public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {}

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState random, StructureManager structureManager, ChunkAccess chunk) {
        NoiseSettings noiseSettings = this.settings.value().noiseSettings().clampToHeightAccessor(chunk.getHeightAccessorForGeneration());
        int i = noiseSettings.minY();
        int j = Mth.floorDiv(i, noiseSettings.getCellHeight());
        int k = Mth.floorDiv(noiseSettings.height(), noiseSettings.getCellHeight());
        if (k <= 0) {
            return CompletableFuture.completedFuture(chunk);
        } else {
            int l = chunk.getSectionIndex(k * noiseSettings.getCellHeight() - 1 + i);
            int m = chunk.getSectionIndex(i);
            Set<LevelChunkSection> set = Sets.newHashSet();

            for (int n = l; n >= m; --n) {
                LevelChunkSection levelChunkSection = chunk.getSection(n);
                levelChunkSection.acquire();
                set.add(levelChunkSection);
            }

            return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("wgen_fill_noise", () -> this.doFill(blender, structureManager, random, chunk, j, k)), Util.backgroundExecutor()).whenCompleteAsync((chunkAccess, throwable) -> {

                for (LevelChunkSection levelChunkSection : set) {
                    levelChunkSection.release();
                }

            }, executor);
        }
    }

    private ChunkAccess doFill(Blender blender, StructureManager structureManager, RandomState random, ChunkAccess chunk, int minCellY, int cellCountY) {
        NoiseChunk noiseChunk = chunk.getOrCreateNoiseChunk((chunkAccess) -> this.createNoiseChunk(chunkAccess, structureManager, blender, random));
        Heightmap heightmap = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap heightmap2 = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        ChunkPos chunkPos = chunk.getPos();
        int i = chunkPos.getMinBlockX();
        int j = chunkPos.getMinBlockZ();
        Aquifer aquifer = noiseChunk.aquifer();
        noiseChunk.initializeForFirstCellX();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int k = ((NoiseChunkAccessor) noiseChunk).getCellWidth();
        int l = ((NoiseChunkAccessor) noiseChunk).getCellHeight();
        int m = 16 / k;
        int n = 16 / k;

        for (int o = 0; o < m; ++o) {
            noiseChunk.advanceCellX(o);

            for (int p = 0; p < n; ++p) {
                int q = chunk.getSectionsCount() - 1;
                LevelChunkSection levelChunkSection = chunk.getSection(q);

                for (int r = cellCountY - 1; r >= 0; --r) {
                    noiseChunk.selectCellYZ(r, p);

                    for (int s = l - 1; s >= 0; --s) {
                        int t = (minCellY + r) * l + s;
                        int u = t & 15;
                        int v = chunk.getSectionIndex(t);
                        if (q != v) {
                            q = v;
                            levelChunkSection = chunk.getSection(v);
                        }

                        double d = (double) s / (double) l;
                        noiseChunk.updateForY(t, d);

                        for (int w = 0; w < k; ++w) {
                            int x = i + o * k + w;
                            int y = x & 15;
                            double e = (double) w / (double) k;
                            noiseChunk.updateForX(x, e);

                            for (int z = 0; z < k; ++z) {
                                int aa = j + p * k + z;
                                int ab = aa & 15;
                                double f = (double) z / (double) k;
                                noiseChunk.updateForZ(aa, f);
                                BlockState blockState = ((NoiseChunkAccessor) noiseChunk).invokeGetInterpolatedState();
                                if (blockState == null) {
                                    blockState = this.settings.value().defaultBlock();
                                }

                                if (blockState != AIR && !SharedConstants.debugVoidTerrain(chunk.getPos())) {
//                                    levelChunkSection.setBlockState(y, u, ab, blockState, false);
//                                    heightmap.update(y, t, ab, blockState);
//                                    heightmap2.update(y, t, ab, blockState);
//                                    if (aquifer.shouldScheduleFluidUpdate() && !blockState.getFluidState().isEmpty()) {
//                                        mutableBlockPos.set(x, t, aa);
//                                        chunk.markPosForPostprocessing(mutableBlockPos);
//                                    }
                                }
                            }
                        }
                    }
                }
            }

            noiseChunk.swapSlices();
        }

        noiseChunk.stopInterpolation();
        return chunk;
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

