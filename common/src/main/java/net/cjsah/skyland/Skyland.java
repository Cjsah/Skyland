package net.cjsah.skyland;

import dev.anvilcraft.lib.event.EventManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Skyland {
    public static final Logger LOGGER = LoggerFactory.getLogger(Skyland.class);
    public static final String MOD_ID = "anvilcraft_skyland";
    public static final ResourceLocation SKYLAND_ID = Skyland.of("skyland");
    public static final EventManager EVENT_BUS = new EventManager();

    public static @NotNull ResourceLocation of(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void init() {

    }
}
