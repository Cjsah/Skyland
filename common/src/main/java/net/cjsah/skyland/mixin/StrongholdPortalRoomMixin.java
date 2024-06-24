package net.cjsah.skyland.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StrongholdPieces.PortalRoom.class)
public class StrongholdPortalRoomMixin {
    @Redirect(
            method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getBoolean(Ljava/lang/String;)Z")
    )
    private boolean blockSpawnerPlace(CompoundTag instance, String string) {
        return true;
    }
}
