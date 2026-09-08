package dev.doctor4t.wathe.item;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.client.particle.HandParticle;
import dev.doctor4t.wathe.client.render.WatheRenderLayers;
import dev.doctor4t.wathe.client.util.WatheItemTooltips;
import dev.doctor4t.wathe.index.WatheDataComponentTypes;
import dev.doctor4t.wathe.network.GunShootPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.List;

public class DerringerItem extends RevolverItem implements AttackUseItem {
    public DerringerItem(Settings settings) {
        super(settings);
    }

    @Override
    public void triggerAttackUseClient(PlayerEntity player) {
        ItemStack stack = player.getMainHandStack();

        if (!player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            boolean used = stack.getOrDefault(WatheDataComponentTypes.USED, false);

            HitResult collision = getGunTarget(player);
            if (collision instanceof EntityHitResult entityHitResult) {
                Entity target = entityHitResult.getEntity();
                ClientPlayNetworking.send(new GunShootPayload(target.getId()));
            } else {
                ClientPlayNetworking.send(new GunShootPayload(-1));
            }
            if (!used) {
                player.setPitch(player.getPitch() - 4);
                this.spawnHandParticle();
            }
        }
    }

    public void spawnHandParticle() {
        HandParticle handParticle = new HandParticle()
                .setTexture(Wathe.id("textures/particle/gunshot.png"))
                .setPos(0.1f, 0.2f, -0.2f)
                .setMaxAge(3)
                .setSize(0.5f)
                .setVelocity(0f, 0f, 0f)
                .setLight(15, 15)
                .setAlpha(1f, 0.1f)
                .setRenderLayer(WatheRenderLayers::additive);
        WatheClient.handParticleManager.spawn(handParticle);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Boolean used = stack.getOrDefault(WatheDataComponentTypes.USED, false);
        if (used) {
            tooltip.add(Text.translatable("tip.derringer.used").withColor(WatheItemTooltips.COOLDOWN_COLOR));
        }

        super.appendTooltip(stack, context, tooltip, type);
    }
}
