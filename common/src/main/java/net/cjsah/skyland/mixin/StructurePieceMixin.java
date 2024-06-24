package net.cjsah.skyland.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(StructurePiece.class)
public class StructurePieceMixin {
    @Unique
    private static final List<Class<?>> skyland$passedStructs;

    static {
        skyland$passedStructs = List.of(
                MineshaftPieces.MineShaftCorridor.class,
                StrongholdPieces.Library.class,
                StrongholdPieces.ChestCorridor.class,
                StrongholdPieces.RoomCrossing.class
        );
    }

    @Inject(
            method = "createChest(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/resources/ResourceLocation;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void passChestPlace(WorldGenLevel worldGenLevel, BoundingBox boundingBox, RandomSource randomSource, int i, int j, int k, ResourceLocation resourceLocation, CallbackInfoReturnable<Boolean> cir) {
//        if (Test.test(skyland$passedStructs, this.getClass())) {
//            cir.cancel();
//        }
//
        if (skyland$passedStructs.contains(this.getClass())) {
            cir.setReturnValue(false);
        }
    }

}
