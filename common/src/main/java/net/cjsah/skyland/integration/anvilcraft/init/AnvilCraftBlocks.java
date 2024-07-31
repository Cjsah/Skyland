package net.cjsah.skyland.integration.anvilcraft.init;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.cjsah.skyland.integration.anvilcraft.block.StoneAnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.HashMap;
import java.util.Map;

public class AnvilCraftBlocks {
    protected static final Map<String, Block> BLOCK_MAP = new HashMap<>();

    public static final StoneAnvilBlock STONE_ANVIL = register("stone_anvil", new StoneAnvilBlock(BlockBehaviour.Properties.of()));

    @ExpectPlatform
    public static void register() {
        throw new AssertionError();
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> T register(String id, T item) {
        AnvilCraftBlocks.BLOCK_MAP.put(id, item);
        return item;
    }
}
