package net.cjsah.skyland.mixin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.cjsah.skyland.Test;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public class TestCommandMixin {

    @Unique
    private static final Logger skyLand$log = LoggerFactory.getLogger(TestCommandMixin.class);
    @Shadow
    @Final
    private CommandDispatcher<CommandSourceStack> dispatcher;


    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(Commands.CommandSelection commandSelection, CommandBuildContext commandBuildContext, CallbackInfo ci) {
        this.dispatcher.register(Commands.literal("sky").then(Commands.literal("t").executes(context -> {
            skyLand$log.info("{}", Test.cache);
            return Command.SINGLE_SUCCESS;
        })));
    }
}
