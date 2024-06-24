package net.cjsah.skyland;

import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdPieces;

import java.util.List;

public class Test {
    public static boolean test(List<Class<?>> classList, Class<?> clazz) {
        List<Class<?>> classes = List.of(
                MineshaftPieces.MineShaftCorridor.class,
                StrongholdPieces.Library.class,
                StrongholdPieces.ChestCorridor.class,
                StrongholdPieces.RoomCrossing.class
        );

        if (classes.contains(clazz)) {
            return true;
        }
        System.out.println(clazz);
        return false;
    }

}
