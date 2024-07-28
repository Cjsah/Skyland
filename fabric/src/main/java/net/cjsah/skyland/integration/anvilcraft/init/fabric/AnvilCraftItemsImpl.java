package net.cjsah.skyland.integration.anvilcraft.init.fabric;

import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.integration.anvilcraft.init.AnvilCraftItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.Map;

public class AnvilCraftItemsImpl extends AnvilCraftItems {
    public static void register() {
        for (Map.Entry<String, Item> entry : AnvilCraftItems.ITEM_MAP.entrySet()) {
            Registry.register(BuiltInRegistries.ITEM, Skyland.of(entry.getKey()), entry.getValue());
        }
        AnvilCraftItems.addComposter(0.3f, MOSS);
        AnvilCraftItems.addComposter(0.7f, BAMBOO_LEAVES);
    }
}
