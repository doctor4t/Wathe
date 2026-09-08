package dev.doctor4t.wathe.item;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.client.particle.HandParticle;
import dev.doctor4t.wathe.client.render.WatheRenderLayers;
import dev.doctor4t.wathe.index.WatheCosmetics;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.Vec3d;

public class RevolverItem extends PistolItem implements SkinnedItem {
    public static final Vec3d DEFAULT_FLASH_OFFSET = new Vec3d(0.1f, 0.275f, -0.25f);

    public RevolverItem(Settings settings) {
        super(settings);
    }

    @Override
    public WatheCosmetics.ItemSkinsManager getSkinManager() {
        return WatheCosmetics.REVOLVER_SKINS_MANAGER;
    }

    public void spawnHandParticle(ItemStack stack) {
        Vec3d flashOffset = WatheCosmetics.getSkin(this.getSkinManager(), stack).flashOffset;
        if (flashOffset == null) {
            flashOffset = DEFAULT_FLASH_OFFSET;
        }
        HandParticle handParticle = new HandParticle()
                .setTexture(Wathe.id("textures/particle/gunshot.png"))
                .setPos((float) flashOffset.getX(), (float) flashOffset.getY(), (float) flashOffset.getZ())
                .setMaxAge(3)
                .setSize(0.5f)
                .setVelocity(0f, 0f, 0f)
                .setLight(15, 15)
                .setAlpha(1f, 0.1f)
                .setYRot(MinecraftClient.getInstance().player.getRandom().nextFloat() * 360f)
                .setRenderLayer(WatheRenderLayers::additive);
        WatheClient.handParticleManager.spawn(handParticle);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && otherStack.isEmpty()) {
            if (Wathe.isSupporter(player)) {
                WatheCosmetics.ItemSkinsManager.Skin currentSkin = WatheCosmetics.getSkin(this.getSkinManager(), stack);
                WatheCosmetics.setSkin(player, stack, this.getSkinManager().getNext(currentSkin).getName());
            }

            return true;
        } else return false;
    }
}
