package net.cjsah.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Blocks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BambooStalkBlock.class)
public class BambooStalkBlockMixin {
    @Redirect(method = "performBonemeal",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;isEmptyBlock(Lnet/minecraft/core/BlockPos;)Z",opcode = Opcodes.GETFIELD))
    private boolean isEmptyBlock( ServerLevel level, BlockPos blockPos ){
        return level.getBlockState( blockPos ).equals( Blocks.AIR.defaultBlockState() ) || level.getBlockState( blockPos ).equals( Blocks.BEDROCK.defaultBlockState() );
    }
}
