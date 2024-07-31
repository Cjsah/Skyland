package net.cjsah.skyland.init;

import net.cjsah.skyland.Skyland;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SPAWN_PLATFORM = feature("spawn_platform");

    @SuppressWarnings("SameParameterValue")
    private static @NotNull ResourceKey<ConfiguredFeature<?, ?>> feature(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Skyland.of(path));
    }
}
