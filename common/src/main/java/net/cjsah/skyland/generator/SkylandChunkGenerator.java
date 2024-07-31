package net.cjsah.skyland.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import lombok.Getter;
import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.event.SkylandChunkGenerateEvent;
import net.cjsah.skyland.mixin.ChunkGeneratorAccessor;
import net.cjsah.skyland.mixin.NoiseChunkAccessor;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdStructure;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SkylandChunkGenerator extends ChunkGenerator {
    public static final Codec<SkylandChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((generator) -> generator.settings)
        ).apply(instance, instance.stable(SkylandChunkGenerator::new))
    );
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final Logger log = LoggerFactory.getLogger(SkylandChunkGenerator.class);

    @Getter
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
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunkAccess, StructureManager structureManager) {
        ChunkPos chunkPos = chunkAccess.getPos();
        SectionPos sectionPos = SectionPos.of(chunkPos, level.getMinSection());
        BlockPos blockPos = sectionPos.origin();

        Registry<Structure> structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Map<Integer, List<Structure>> stepMap = structureRegistry.stream().collect(Collectors.groupingBy(structure -> structure.step().ordinal()));
        List<FeatureSorter.StepFeatureData> steps = ((ChunkGeneratorAccessor)this).getFeaturesPerStep().get();
        WorldgenRandom random = new WorldgenRandom(new XoroshiroRandomSource(RandomSupport.generateUniqueSeed()));
        long seed = random.setDecorationSeed(level.getSeed(), blockPos.getX(), blockPos.getZ());

        Set<Holder<Biome>> biomes = new ObjectArraySet<>();
        ChunkPos.rangeClosed(sectionPos.chunk(), 1).forEach(pos -> {
            ChunkAccess chunk = level.getChunk(pos.x, pos.z);
            for (LevelChunkSection section : chunk.getSections()) {
                section.getBiomes().getAll(biomes::add);
            }
        });
        biomes.retainAll(this.biomeSource.possibleBiomes());

        int featureSize = steps.size();
        try {
            Registry<PlacedFeature> features = level.registryAccess().registryOrThrow(Registries.PLACED_FEATURE);
            int length = Math.max(GenerationStep.Decoration.values().length, featureSize);
            for (int step = 0; step < length; ++step) {
                if (structureManager.shouldGenerateStructures()) {
                    int index = 0;
                    List<Structure> structures = stepMap.getOrDefault(step, Collections.emptyList());
                    for (Structure structure : structures) {
                        random.setFeatureSeed(seed, index, step);
                        Supplier<String> supplier = () -> structureRegistry.getResourceKey(structure).map(Object::toString).orElseGet(structure::toString);
                        try {
                            if (structure instanceof StrongholdStructure) {
                                level.setCurrentlyGenerating(supplier);
                                BoundingBox box = ChunkGeneratorAccessor.invokeGetWritableArea(chunkAccess);
                                structureManager.startsForStructure(sectionPos, structure).forEach(structureStart -> {
                                    for (StructurePiece piece : structureStart.getPieces()) {
                                        if (piece.getType() == StructurePieceType.STRONGHOLD_PORTAL_ROOM && piece.getBoundingBox().intersects(box)) {
                                            new EndPortalStructure(piece).generate(level, box, random);
                                        }
                                    }
                                });
                            }
                        } catch (Exception exception) {
                            CrashReport crashReport = CrashReport.forThrowable(exception, "Feature placement");
                            crashReport.addCategory("Feature").setDetail("Description", supplier::get);
                            throw new ReportedException(crashReport);
                        }
                        ++index;
                    }
                }
                if (step >= featureSize) continue;
                IntArraySet intSet = new IntArraySet();
                for (Holder<Biome> holder : biomes) {
                    List<HolderSet<PlacedFeature>> featuresSet = ((ChunkGeneratorAccessor)this).getGenerationSettingsGetter().apply(holder).features();
                    if (step >= featuresSet.size()) continue;
                    HolderSet<PlacedFeature> feature = featuresSet.get(step);
                    FeatureSorter.StepFeatureData featureData = steps.get(step);
                    feature.stream().map(Holder::value).forEach(it -> intSet.add(featureData.indexMapping().applyAsInt(it)));
                }
                int[] array = intSet.toIntArray();
                Arrays.sort(array);
                FeatureSorter.StepFeatureData featureData = steps.get(step);
                for (int index : array) {
                    PlacedFeature feature = featureData.features().get(index);
                    Supplier<String> supplier = () -> features.getResourceKey(feature).map(Object::toString).orElseGet(feature::toString);
                    random.setFeatureSeed(seed, index, step);
                    try {
                        if (feature.feature().is(new ResourceLocation("end_gateway_return"))) {
                            level.setCurrentlyGenerating(supplier);
                            feature.placeWithBiomeCheck(level, this, random, blockPos);
                        } else if (feature.feature().is(new ResourceLocation("end_spike"))) {
                            level.setCurrentlyGenerating(supplier);
                            feature.placeWithBiomeCheck(level, this, random, blockPos);
                        }
                    } catch (Exception exception2) {
                        CrashReport crashReport2 = CrashReport.forThrowable(exception2, "Feature placement");
                        crashReport2.addCategory("Feature").setDetail("Description", supplier::get);
                        throw new ReportedException(crashReport2);
                    }
                }
            }
            level.setCurrentlyGenerating(null);
        } catch (Exception exception3) {
            CrashReport crashReport3 = CrashReport.forThrowable(exception3, "Biome decoration");
            crashReport3.addCategory("Generation").setDetail("CenterX", chunkPos.x).setDetail("CenterZ", chunkPos.z).setDetail("Seed", seed);
            throw new ReportedException(crashReport3);
        }

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

