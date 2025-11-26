package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.entity.GrenadeEntity;
import dev.doctor4t.trainmurdermystery.index.TMMEntities;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class GrenadeItem extends Item {
    public GrenadeItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(@NotNull World world, @NotNull PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), TMMSounds.ITEM_GRENADE_THROW, SoundCategory.NEUTRAL, 0.5F, 1F + (world.random.nextFloat() - .5f) / 10f);
        if (!world.isClient) {
            GrenadeEntity grenade = new GrenadeEntity(TMMEntities.GRENADE, world);
            grenade.setOwner(user);
            grenade.setPos(user.getX(), user.getEyeY() - 0.1, user.getZ());
            grenade.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0.5F, 1.0F);
            world.spawnEntity(grenade);
        }

        if (!user.isCreative()) {
//            user.getItemCooldownManager().set(this, GameConstants.ITEM_COOLDOWNS.get(this));
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrementUnlessCreative(1, user);
        return TypedActionResult.success(itemStack, world.isClient());
    }
}