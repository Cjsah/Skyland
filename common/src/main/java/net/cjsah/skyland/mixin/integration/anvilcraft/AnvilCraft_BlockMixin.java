package net.cjsah.skyland.mixin.integration.anvilcraft;

import net.cjsah.skyland.integration.anvilcraft.AnvilcraftIntegration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Block.class)
abstract class AnvilCraft_BlockMixin extends BlockBehaviour {
    public AnvilCraft_BlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "isRandomlyTicking", at = @At("RETURN"), cancellable = true)
    private void isRandomTicking(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        for (Map.Entry<Block, Block> entry : AnvilcraftIntegration.MOSS_MAP.entrySet()) {
            if (!blockState.is(entry.getValue())) continue;
            cir.setReturnValue(true);
            break;
        }
    }

    @SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
    @Override
    public void tick(
        @NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos,
        @NotNull RandomSource random
    ) {
        if (level.getBrightness(LightLayer.SKY, pos) > 7) return;
        Holder<Biome> biome = level.getBiome(pos);
        Biome value = biome.isBound() ? biome.value() : null;
        if ((value == null || value.climateSettings.downfall() < 0.8) && !level.isRainingAt(pos)) return;
        Block block = null;
        for (Map.Entry<Block, Block> entry : AnvilcraftIntegration.MOSS_MAP.entrySet()) {
            if (!state.is(entry.getValue())) continue;
            block = entry.getKey();
            break;
        }
        if (block == null) return;
        BlockState blockState = block.defaultBlockState();
        for (Map.Entry<Property<?>, Comparable<?>> entry : state.getValues().entrySet()) {
            if (blockState.hasProperty(entry.getKey())) {
                blockState = blockState.setValue((Property) entry.getKey(), (Comparable) entry.getValue());
            }
        }
        level.setBlockAndUpdate(pos, blockState);
    }
}
