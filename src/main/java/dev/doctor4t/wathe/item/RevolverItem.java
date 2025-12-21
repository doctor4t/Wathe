package dev.doctor4t.wathe.item;

import dev.doctor4t.wathe.game.GameFunctions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class RevolverItem extends Item {
    public RevolverItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(@NotNull World world, @NotNull PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        return TypedActionResult.consume(stack);
    }

    public boolean hasBeenUsed(ItemStack stack, PlayerEntity user) {
        return false;
    }

    public static HitResult getGunTarget(PlayerEntity user, float range) {
        return ProjectileUtil.getCollision(user, entity -> entity instanceof PlayerEntity player && GameFunctions.isPlayerAliveAndSurvival(player), range);
    }

    public final HitResult getGunTarget(PlayerEntity user, ItemStack stack) {
        return getGunTarget(user, this.getRange(user, stack));
    }

    public float getRange(PlayerEntity user, ItemStack stack) {
        return 15f;
    }
}