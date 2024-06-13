package net.cjsah.skyland.fabric;

import net.cjsah.skyland.Skyland;
import net.fabricmc.api.ModInitializer;

public class SkylandFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Skyland.init();
    }
}