package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.entity.GrenadeEntity;
import dev.doctor4t.trainmurdermystery.index.TMMEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GrenadeItem extends Item {
    public GrenadeItem(Item.Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.NEUTRAL, 0.5F, 0.8F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!world.isClient) {
            GrenadeEntity grenade = new GrenadeEntity(TMMEntities.GRENADE, world);
            grenade.setOwner(user);
            grenade.setPos(user.getX(), user.getEyeY() - 0.1, user.getZ());
            grenade.setItem(itemStack);
            grenade.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 0.35F, 1.0F);
            world.spawnEntity(grenade);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrementUnlessCreative(1, user);
        return TypedActionResult.success(itemStack, world.isClient());
    }
}