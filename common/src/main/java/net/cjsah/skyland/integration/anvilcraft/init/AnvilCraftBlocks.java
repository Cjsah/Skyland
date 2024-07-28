package net.cjsah.skyland.integration.anvilcraft.init;

import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.integration.anvilcraft.block.StoneAnvilBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.HashMap;
import java.util.Map;

public class AnvilCraftBlocks {
    private static final Map<String, Block> BLOCK_MAP = new HashMap<>();

    public static final StoneAnvilBlock STONE_ANVIL = register("stone_anvil", new StoneAnvilBlock(BlockBehaviour.Properties.of()));

    public static void register() {
        for (Map.Entry<String, Block> entry : AnvilCraftBlocks.BLOCK_MAP.entrySet()) {
            Registry.register(BuiltInRegistries.BLOCK, Skyland.of(entry.getKey()), entry.getValue());
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> T register(String id, T item) {
        AnvilCraftBlocks.BLOCK_MAP.put(id, item);
        return item;
    }
}
