package net.cjsah.skyland.integration.anvilcraft;

import com.tterrag.registrate.Registrate;
import dev.anvilcraft.lib.integration.Integration;
import dev.dubhe.anvilcraft.api.registry.AnvilCraftRegistrate;
import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.integration.anvilcraft.event.SkylandChunkGenerateEventListener;
import net.cjsah.skyland.integration.anvilcraft.init.AnvilCraftBlocks;
import net.cjsah.skyland.integration.anvilcraft.init.AnvilCraftItemGroups;
import net.cjsah.skyland.integration.anvilcraft.init.AnvilCraftItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class AnvilcraftIntegration implements Integration {
    public static final Map<Block, Block> MOSS_MAP = new HashMap<>() {{
        this.put(Blocks.MOSSY_COBBLESTONE, Blocks.COBBLESTONE);
        this.put(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.COBBLESTONE_SLAB);
        this.put(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.COBBLESTONE_STAIRS);
        this.put(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.COBBLESTONE_WALL);
        this.put(Blocks.MOSSY_STONE_BRICKS, Blocks.STONE_BRICKS);
        this.put(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.STONE_BRICK_SLAB);
        this.put(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.STONE_BRICK_STAIRS);
        this.put(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.STONE_BRICK_STAIRS);
    }};
    public static final Registrate REGISTRATE = AnvilCraftRegistrate.create(Skyland.MOD_ID);

    @Override
    public void apply() {
        AnvilCraftBlocks.register();
        AnvilCraftItems.register();
        AnvilCraftItemGroups.register();
        Skyland.EVENT_BUS.register(new SkylandChunkGenerateEventListener());
        REGISTRATE.register();
    }
}
