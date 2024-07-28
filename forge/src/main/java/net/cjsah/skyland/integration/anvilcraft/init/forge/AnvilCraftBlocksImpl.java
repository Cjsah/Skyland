package net.cjsah.skyland.integration.anvilcraft.init.forge;

import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.integration.anvilcraft.init.AnvilCraftBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Map;

public class AnvilCraftBlocksImpl extends AnvilCraftBlocks {
    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(AnvilCraftBlocksImpl::registerBlock);
    }

    public static void registerBlock(RegisterEvent event) {
        for (Map.Entry<String, Block> entry : AnvilCraftBlocks.BLOCK_MAP.entrySet()) {
            event.register(Registries.BLOCK, Skyland.of(entry.getKey()), entry::getValue);
        }
    }
}
