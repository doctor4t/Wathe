package dev.doctor4t.wathe.item;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.client.particle.HandParticle;
import dev.doctor4t.wathe.client.render.WatheRenderLayers;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.index.WatheCosmetics;
import dev.doctor4t.wathe.index.WatheDataComponentTypes;
import dev.doctor4t.wathe.network.GunShootPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class RevolverItem extends Item implements ItemWithSkin, AttackUseItem {
    /**
     * the registry ID of the revolver item
     */
    public static final Identifier ITEM_ID = Wathe.id("revolver");

    public RevolverItem(Settings settings) {
        super(settings);
    }

    @Override
    public void triggerAttackUseClient(ClientPlayerEntity player) {
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
                spawnHandParticle();
            }
        }
    }

    @Override
    public void triggerAttackUseServer(ServerPlayerEntity player) {

    }

    public static void spawnHandParticle() {
        HandParticle handParticle = new HandParticle()
                .setTexture(Wathe.id("textures/particle/gunshot.png"))
                .setPos(0.1f, 0.275f, -0.25f)
                .setMaxAge(3)
                .setSize(0.5f)
                .setVelocity(0f, 0f, 0f)
                .setLight(15, 15)
                .setAlpha(1f, 0.1f)
                .setRenderLayer(WatheRenderLayers::additive);
        WatheClient.handParticleManager.spawn(handParticle);
    }

    public static HitResult getGunTarget(PlayerEntity user) {
        return ProjectileUtil.getCollision(user, entity -> entity instanceof PlayerEntity player && GameFunctions.isPlayerAliveAndSurvival(player), 15f);
    }

    @Override
    public WatheCosmetics.ItemSkinsManager getSkinManager() {
        return WatheCosmetics.REVOLVER_SKINS_MANAGER;
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && otherStack.isEmpty()) {
            if (Wathe.isSupporter(player)) {
                WatheCosmetics.ItemSkinsManager.Skin currentSkin = this.getSkinManager().fromString(WatheCosmetics.getSkin(stack));
                WatheCosmetics.setSkin(player, stack, this.getSkinManager().getNext(currentSkin).getName());
            }

            return true;
        } else return false;
    }
}