package net.cjsah.skyland.forge;

import dev.architectury.platform.forge.EventBuses;
import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.init.ModFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

@Mod(Skyland.MOD_ID)
public class SkylandForge {
    public SkylandForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Skyland.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Skyland.init();
        MinecraftForge.EVENT_BUS.addListener(SkylandForge::register);
    }

    public static void register(@NotNull RegisterEvent event) {
        event.register(Registries.FEATURE, Skyland.of("locatable_structure"), () -> ModFeatures.LOCATABLE_STRUCTURE);
        event.register(Registries.FEATURE, Skyland.of("spawn_platform"), () -> ModFeatures.SPAWN_PLATFORM);
    }
}