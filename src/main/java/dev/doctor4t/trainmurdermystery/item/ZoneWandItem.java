package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.index.TMMDataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ZoneWandItem extends Item {
    public ZoneWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        ItemStack stack = context.getStack();

        if (!world.isClient && player != null) {
            if (player.isSneaking()) {
                // Shift + Right Click -> Set Pos B
                stack.set(TMMDataComponentTypes.POS_B, pos);
                player.sendMessage(Text.literal("§bPosition B set to: " + pos.toShortString()), true);
            } else {
                // Right Click -> Set Pos A
                stack.set(TMMDataComponentTypes.POS_A, pos);
                player.sendMessage(Text.literal("§6Position A set to: " + pos.toShortString()), true);
            }
        }

        return ActionResult.SUCCESS;
    }
}