package net.cjsah.skyland.mixin.integration.anvilcraft;

import net.cjsah.skyland.integration.anvilcraft.AnvilcraftIntegration;
import net.cjsah.skyland.integration.anvilcraft.init.AnvilCraftItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;

@Mixin(AxeItem.class)
abstract class AnvilCraft_AxeItemMixin {
    @SuppressWarnings("unchecked")
    @Inject(method = "useOn", at = @At("RETURN"), cancellable = true)
    public void useOn(@NotNull UseOnContext arg, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = arg.getLevel();
        BlockPos blockpos = arg.getClickedPos();
        Player player = arg.getPlayer();
        BlockState blockstate = level.getBlockState(blockpos);
        ItemStack itemstack = arg.getItemInHand();
        Optional<Block> optional = Optional.ofNullable(AnvilcraftIntegration.MOSS_MAP.getOrDefault(blockstate.getBlock(), null));
        if (optional.isPresent()) {
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
            }
            Block block = optional.get();
            BlockState state = block.defaultBlockState();
            for (Map.Entry<Property<?>, Comparable<?>> entry : blockstate.getValues().entrySet()) {
                if (!state.hasProperty(entry.getKey())) continue;
                // noinspection rawtypes
                state = state.setValue((Property) entry.getKey(), (Comparable) entry.getValue());
            }
            level.setBlock(blockpos, state, 11);
            if (!level.isClientSide()) {
                Vec3 pos = blockpos.above().getCenter();
                ItemStack stack = new ItemStack(AnvilCraftItems.MOSS, 1);
                ItemEntity entity = new ItemEntity(level, pos.x, pos.y, pos.z, stack);
                level.addFreshEntity(entity);
            }
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, state));
            if (player != null) {
                level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f);
                itemstack.hurtAndBreak(1, player, arg2 -> arg2.broadcastBreakEvent(arg.getHand()));
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
    }
}
