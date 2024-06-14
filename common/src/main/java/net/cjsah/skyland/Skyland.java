package net.cjsah.skyland;

import net.minecraft.resources.ResourceLocation;

public class Skyland
{
	public static final String MOD_ID = "skyland";
	public static final ResourceLocation SKYLAND_ID = Skyland.of("skyland");

	public static ResourceLocation of(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

	public static void init() {
		
	}
}
