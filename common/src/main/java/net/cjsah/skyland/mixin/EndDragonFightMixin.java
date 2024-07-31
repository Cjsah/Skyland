package net.cjsah.skyland.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndDragonFight.class)
public class EndDragonFightMixin {

    @Redirect(method = "spawnExitPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getHeightmapPos(Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/BlockPos;"))
    private BlockPos changeEndPortalPos(ServerLevel instance, Heightmap.Types types, BlockPos blockPos) {
        return blockPos.atY(64);
    }
}
