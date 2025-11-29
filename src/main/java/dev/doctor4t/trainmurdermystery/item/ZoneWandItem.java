package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.index.TMMDataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ZoneWandItem extends Item {
    public ZoneWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        ItemStack stack = context.getStack();

        if (!world.isClient && player != null) {
            // Check Y-Level < 64
            if (pos.getY() < 64) {
                String zoneName = player.isSneaking() ? "B" : "A";
                player.sendMessage(
                        Text.literal("Zone " + zoneName + " can't be set... Under Block Limit! [y=64]")
                                .formatted(Formatting.RED),
                        true
                );
                return ActionResult.FAIL;
            }

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