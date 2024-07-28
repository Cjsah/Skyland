package net.cjsah.skyland.integration.anvilcraft.init.forge;

import net.cjsah.skyland.Skyland;
import net.cjsah.skyland.integration.anvilcraft.init.AnvilCraftItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Map;

public class AnvilCraftItemsImpl extends AnvilCraftItems {
    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(AnvilCraftItemsImpl::registerBlock);
        AnvilCraftItems.addComposter(0.3f, MOSS);
        AnvilCraftItems.addComposter(0.7f, BAMBOO_LEAVES);
    }

    public static void registerBlock(RegisterEvent event) {
        for (Map.Entry<String, Item> entry : AnvilCraftItems.ITEM_MAP.entrySet()) {
            event.register(Registries.ITEM, Skyland.of(entry.getKey()), entry::getValue);
        }
    }
}
