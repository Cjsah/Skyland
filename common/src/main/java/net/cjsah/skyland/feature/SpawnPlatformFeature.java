package net.cjsah.skyland.feature;

import com.mojang.serialization.Codec;
import net.cjsah.skyland.feature.configuration.SpawnPlatformFeatureConfiguration;
import net.cjsah.skyland.init.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

public class SpawnPlatformFeature extends Feature<SpawnPlatformFeatureConfiguration> {
    public SpawnPlatformFeature(Codec<SpawnPlatformFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<SpawnPlatformFeatureConfiguration> context) {
        SpawnPlatformFeatureConfiguration config = context.config();
        BlockPos origin = config.spawnRelative() ? context.origin().atY(0) : BlockPos.ZERO;
        return ModFeatures.LOCATABLE_STRUCTURE.place(config.platformConfig(), context.level(), context.chunkGenerator(), context.random(), origin);
    }
}
