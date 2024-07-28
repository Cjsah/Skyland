package net.cjsah.skyland.integration.anvilcraft.init.fabric;

import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.integration.anvilcraft.init.AnvilCraftBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class AnvilCraftBlocksImpl extends AnvilCraftBlocks {
    public static void register() {
        for (Map.Entry<String, Block> entry : AnvilCraftBlocks.BLOCK_MAP.entrySet()) {
            Registry.register(BuiltInRegistries.BLOCK, Skyland.of(entry.getKey()), entry.getValue());
        }
    }
}
