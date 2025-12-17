package dev.doctor4t.trainmurdermystery.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.MeCommand;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.SayCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ChatCommandsMixins {
    @Mixin(SayCommand.class)
    public static class SayCommandMixin {
        @Inject(method = "register", at = @At("HEAD"), cancellable = true)
        private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
            ci.cancel();
        }
    }

    @Mixin(MessageCommand.class)
    public static class MessageCommandMixin {
        @Inject(method = "register", at = @At("HEAD"), cancellable = true)
        private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
            ci.cancel();
        }
    }

    @Mixin(MeCommand.class)
    public static class MeCommandMixin {
        @Inject(method = "register", at = @At("HEAD"), cancellable = true)
        private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
            ci.cancel();
        }
    }
}