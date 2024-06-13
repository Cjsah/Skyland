package net.cjsah.skyland.forge;

import dev.architectury.platform.forge.EventBuses;
import net.cjsah.skyland.Skyland;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Skyland.MOD_ID)
public class SkylandForge {
    public SkylandForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Skyland.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Skyland.init();
    }
}