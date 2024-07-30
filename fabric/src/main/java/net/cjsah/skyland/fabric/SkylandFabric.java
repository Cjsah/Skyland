package net.cjsah.skyland.fabric;

import net.cjsah.skyland.Skyland;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SkylandFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Skyland.init();
        SkylandFabric.loadResource();
    }

    public static void loadResource() {
        ModContainer skyland = FabricLoader.getInstance().getModContainer(Skyland.MOD_ID)
            .orElseThrow(() -> new IllegalStateException("Skyland's ModContainer couldn't be found!"));
        ResourceLocation packId = Skyland.of("anvilcraft_skyland");
        ResourceManagerHelper.registerBuiltinResourcePack(
            packId, skyland,
            Component.translatable("data_pack.anvilcraft_skyland.description"),
            ResourcePackActivationType.NORMAL
        );
    }
}