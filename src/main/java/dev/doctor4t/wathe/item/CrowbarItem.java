package dev.doctor4t.wathe.item;

import dev.doctor4t.wathe.block_entity.DoorBlockEntity;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.index.WatheSounds;
import dev.doctor4t.wathe.util.AdventureUsable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CrowbarItem extends Item implements AdventureUsable {
    public CrowbarItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockEntity entity = world.getBlockEntity(context.getBlockPos());
        if (!(entity instanceof DoorBlockEntity)) entity = world.getBlockEntity(context.getBlockPos().down());
        PlayerEntity player = context.getPlayer();
        if (entity instanceof DoorBlockEntity door && !door.isBlasted() && player != null) {
            if (!player.isCreative()) player.getItemCooldownManager().set(this, 6000);
            world.playSound(null, context.getBlockPos(), WatheSounds.ITEM_CROWBAR_PRY, SoundCategory.BLOCKS, 2.5f, 1f);
            player.swingHand(Hand.MAIN_HAND, true);

            if (!player.isCreative()) {
                player.getItemCooldownManager().set(this, GameConstants.ITEM_COOLDOWNS.get(this));
            }

            door.blast();
        }
        return super.useOnBlock(context);
    }
}
