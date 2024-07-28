package net.cjsah.skyland.mixin;

import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldOpenFlows.class)
public class WorldOpenFlowsMixin {
    @ModifyVariable(
        method = "loadLevel",
        at = @At("HEAD"),
        argsOnly = true,
        index = 4
    )
    private boolean removeAdviceOnLoad(boolean original) {
        return false;
    }

    @ModifyVariable(
        method = "confirmWorldCreation",
        at = @At("HEAD"),
        argsOnly = true,
        index = 4
    )
    private static boolean removeAdviceOnCreation(boolean original) {
        return true;
    }
}
