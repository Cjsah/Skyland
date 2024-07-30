package net.cjsah.skyland.mixin.plugin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class SkylandMixinPlugin implements IMixinConfigPlugin {
    private static boolean hasAnvilCraft = false;

    @SuppressWarnings("SameParameterValue")
    private boolean isLoaded(String clazz) {
        return SkylandMixinPlugin.class.getClassLoader().getResource(clazz) != null;
    }

    @Override
    public void onLoad(String mixinPackage) {
        hasAnvilCraft = this.isLoaded("dev/dubhe/anvilcraft/AnvilCraft.class");
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!hasAnvilCraft && mixinClassName.startsWith("AnvilCraft_")) return false;
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
