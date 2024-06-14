package net.cjsah.skyland.generator;

import net.minecraft.world.level.levelgen.DensityFunctions;

import java.util.Arrays;


public enum BeardifierMarker implements DensityFunctions.BeardifierOrMarker {
    INSTANCE;

    public double compute(FunctionContext context) {
        return 0.0;
    }

    public void fillArray(double[] array, ContextProvider contextProvider) {
        Arrays.fill(array, 0.0);
    }

    public double minValue() {
        return 0.0;
    }

    public double maxValue() {
        return 0.0;
    }
}
