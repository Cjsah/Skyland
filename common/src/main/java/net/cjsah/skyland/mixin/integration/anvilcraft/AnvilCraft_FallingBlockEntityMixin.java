package net.cjsah.skyland.mixin.integration.anvilcraft;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.event.entity.AnvilFallOnLandEvent;
import net.cjsah.skyland.integration.anvilcraft.init.AnvilCraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FallingBlockEntity.class)
abstract class AnvilCraft_FallingBlockEntityMixin extends Entity {

    @Shadow
    private BlockState blockState;

    @Shadow
    private boolean cancelDrop;
    @Unique
    private float anvilcraft_skyland$fallDistance;

    public AnvilCraft_FallingBlockEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "causeFallDamage",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"
        )
    )
    public void damage(float f, float g, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = this.blockState.is(AnvilCraftBlocks.STONE_ANVIL);
        if (bl && this.random.nextFloat() < 0.667f) {
            this.cancelDrop = true;
        }
    }


    @SuppressWarnings("resource")
    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            ordinal = 0,
            target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;level()Lnet/minecraft/world/level/Level;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD)
    private void anvilPerFallOnGround(CallbackInfo ci, Block block) {
        if (this.level().isClientSide()) return;
        if (this.onGround()) return;
        this.anvilcraft_skyland$fallDistance = this.fallDistance;
    }

    @SuppressWarnings({"UnreachableCode", "resource"})
    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            ordinal = 10,
            target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;level()Lnet/minecraft/world/level/Level;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void anvilFallOnGround(CallbackInfo ci, Block block, BlockPos blockPos) {
        if (this.level().isClientSide()) return;
        if (!this.blockState.is(AnvilCraftBlocks.STONE_ANVIL)) return;
        AnvilFallOnLandEvent event = new AnvilFallOnLandEvent(this.level(), blockPos, (FallingBlockEntity) (Object) this, anvilcraft_skyland$fallDistance);
        AnvilCraft.EVENT_BUS.post(event);
        boolean isAnvilDamage = event.isAnvilDamage();
        if (isAnvilDamage) {
            BlockState state = AnvilBlock.damage(this.blockState);
            if (state != null) this.level().setBlockAndUpdate(blockPos, state);
            else {
                this.level().setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                if (!this.isSilent()) this.level().levelEvent(1029, this.getOnPos(), 0);
                this.cancelDrop = true;
            }
        }
    }
}
