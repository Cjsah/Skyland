package net.cjsah.skyland.init;

import net.cjsah.skyland.feature.LocatableStructureFeature;
import net.cjsah.skyland.feature.configuration.LocatableStructureFeatureConfiguration;
import net.cjsah.skyland.feature.SpawnPlatformFeature;
import net.cjsah.skyland.feature.configuration.SpawnPlatformFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;

public class ModFeatures {
    public static final Feature<LocatableStructureFeatureConfiguration> LOCATABLE_STRUCTURE =
        new LocatableStructureFeature(LocatableStructureFeatureConfiguration.CODEC);

    public static final Feature<SpawnPlatformFeatureConfiguration> SPAWN_PLATFORM =
        new SpawnPlatformFeature(SpawnPlatformFeatureConfiguration.CODEC);
}
