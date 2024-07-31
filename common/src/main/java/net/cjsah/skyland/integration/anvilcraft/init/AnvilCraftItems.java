package net.cjsah.skyland.integration.anvilcraft.init;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AnvilCraftItems {
    protected static final Map<String, Item> ITEM_MAP = new HashMap<>();
    public static final FoodProperties MOSS_FOOD = new FoodProperties.Builder().nutrition(1).saturationMod(2.0f).fast().build();

    public static final Item MOSS = register("moss", new Item(new Item.Properties().food(MOSS_FOOD)));
    public static final Item PEBBLE = register("pebble", new Item(new Item.Properties()));
    public static final Item BAMBOO_LEAVES = register("bamboo_leaves", new Item(new Item.Properties()));
    public static final BlockItem STONE_ANVIL = register("stone_anvil", new BlockItem(AnvilCraftBlocks.STONE_ANVIL, new Item.Properties()));

    @ExpectPlatform
    public static void register() {
        throw new AssertionError();
    }

    @SuppressWarnings("SameParameterValue")
    protected static void addComposter(float f, @NotNull ItemLike itemLike) {
        ComposterBlock.COMPOSTABLES.put(itemLike.asItem(), f);
    }

    private static <T extends Item> T register(String id, T item) {
        AnvilCraftItems.ITEM_MAP.put(id, item);
        return item;
    }
}
