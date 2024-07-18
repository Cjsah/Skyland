package net.cjsah.skyland;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Skyland {
    public static final String MOD_ID = "anvilcraft_skyland";
    public static final ResourceLocation SKYLAND_ID = Skyland.of("skyland");

    public static @NotNull ResourceLocation of(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void init() {

    }
}
