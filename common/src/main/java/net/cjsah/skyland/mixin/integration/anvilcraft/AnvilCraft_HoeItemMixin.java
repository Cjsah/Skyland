package net.cjsah.skyland.mixin.integration.anvilcraft;

import dev.dubhe.anvilcraft.init.ModItems;
import net.cjsah.skyland.integration.anvilcraft.init.AnvilCraftItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoeItem.class)
abstract class AnvilCraft_HoeItemMixin {
    @Inject
        (method = "useOn",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V"
            )
        )
    public void useOn(@NotNull UseOnContext arg, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack item = arg.getItemInHand();
        if (item.is(Items.WOODEN_HOE) || item.is(ModItems.AMETHYST_HOE.get())) return;
        Vec3 pos = arg.getClickedPos().above().getCenter();
        Level level = arg.getLevel();
        RandomSource random = level.random;
        if (!level.isClientSide() && random.nextFloat() < 0.7) {
            ItemStack stack = new ItemStack(AnvilCraftItems.PEBBLE, 1);
            ItemEntity entity = new ItemEntity(level, pos.x, pos.y, pos.z, stack);
            level.addFreshEntity(entity);
        }
    }
}
