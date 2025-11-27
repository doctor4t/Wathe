package dev.doctor4t.trainmurdermystery.mixin.client.self;

import dev.doctor4t.trainmurdermystery.client.gui.screen.ingame.NoteScreen;
import dev.doctor4t.trainmurdermystery.item.NoteItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(NoteItem.class)
public class NoteItemMixin {

    @Inject(method = "use", at = @At("HEAD"))
    private void useClient(@NotNull World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!user.isSneaking()) {
            return;
        }
        MinecraftClient.getInstance().setScreen(new NoteScreen());
    }
}
