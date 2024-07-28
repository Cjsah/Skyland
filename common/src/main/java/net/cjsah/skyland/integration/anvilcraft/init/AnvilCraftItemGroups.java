package net.cjsah.skyland.integration.anvilcraft.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.CreativeModeTab;

import static net.cjsah.skyland.integration.anvilcraft.AnvilcraftIntegration.REGISTRATE;

public class AnvilCraftItemGroups {

    public static final RegistryEntry<CreativeModeTab> TAB = REGISTRATE
        .defaultCreativeTab("anvilcraft_skyland", builder -> builder
            .icon(AnvilCraftItems.STONE_ANVIL::getDefaultInstance)
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(AnvilCraftItems.STONE_ANVIL);
                output.accept(AnvilCraftItems.MOSS);
                output.accept(AnvilCraftItems.PEBBLE);
                output.accept(AnvilCraftItems.BAMBOO_LEAVES);
            })
        )
        .register();

    public static void register() {
    }
}
