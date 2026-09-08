package dev.doctor4t.wathe.item;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.index.WatheCosmetics;
import dev.doctor4t.wathe.index.WatheSounds;
import dev.doctor4t.wathe.network.KnifeStabPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.*;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class KnifeItem extends Item implements ItemWithSkin {

    /**
     * the registry ID of the knife item
     */
    public static final Identifier ITEM_ID = Wathe.id("knife");

    public KnifeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, @NotNull PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        user.playSound(WatheSounds.ITEM_KNIFE_PREPARE, 1.0f, 1.0f);
        return TypedActionResult.consume(itemStack);
    }


    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user.isSpectator()) {
            return;
        }

        if (remainingUseTicks >= this.getMaxUseTime(stack, user) - 10 || !(user instanceof PlayerEntity attacker) || !world.isClient)
            return;
        HitResult collision = getKnifeTarget(attacker);
        if (collision instanceof EntityHitResult entityHitResult) {
            Entity target = entityHitResult.getEntity();
            ClientPlayNetworking.send(new KnifeStabPayload(target.getId()));
        }
    }

    public static HitResult getKnifeTarget(PlayerEntity user) {
        return ProjectileUtil.getCollision(user, entity -> entity instanceof PlayerEntity player && GameFunctions.isPlayerAliveAndSurvival(player), 3f);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 100;
    }

    @Override
    public WatheCosmetics.ItemSkinsManager getSkinManager() {
        return WatheCosmetics.KNIFE_SKINS_MANAGER;
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
