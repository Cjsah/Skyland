package net.cjsah.skyland.integration.anvilcraft;

import dev.anvilcraft.lib.integration.Integration;
import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.integration.anvilcraft.event.SkylandChunkGenerateEventListener;

public class AnvilcraftIntegration implements Integration {
    @Override
    public void apply() {
        Skyland.EVENT_BUS.register(new SkylandChunkGenerateEventListener());
    }
}
