package net.cjsah.skyland;

import net.cjsah.skyland.util.ClassMatcher;
import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;

import java.util.List;

public class Config {
    public static final List<ClassMatcher> PassedStructures;

    static {
        PassedStructures = List.of(
                new ClassMatcher(EndCityPieces.EndCityPiece.class, "ship"::equals)
        );
    }

}
