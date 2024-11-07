package net.cjsah.skyland.mixin;

import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BambooSaplingBlock.class)
public class BambooSaplingBlockMixin {
    @Redirect(method = "isValidBonemealTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z", opcode = Opcodes.GETFIELD))
    private boolean isAirOrBedrock(BlockState blockState) {
        return blockState.equals(Blocks.AIR.defaultBlockState()) || blockState.equals(Blocks.BEDROCK.defaultBlockState());
    }
}
