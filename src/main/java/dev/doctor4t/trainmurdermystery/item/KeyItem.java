package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.block.SmallDoorBlock;
import dev.doctor4t.trainmurdermystery.block_entity.SmallDoorBlockEntity;
import dev.doctor4t.trainmurdermystery.util.AdventureUsable;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class KeyItem extends Item implements AdventureUsable {
    public KeyItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof SmallDoorBlock) {
            BlockPos lowerPos = state.get(SmallDoorBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();
            if (world.getBlockEntity(lowerPos) instanceof SmallDoorBlockEntity entity) {
                ItemStack mainHandStack = player.getMainHandStack();
                LoreComponent loreComponent = mainHandStack.get(DataComponentTypes.LORE);
                if (loreComponent != null) {
                    List<Text> lines = loreComponent.lines();
                    if (lines == null || lines.isEmpty()) {
                        return ActionResult.PASS;
                    }

                    // Sneaking creative player with key sets the door to require a key with the same name
                    if (player.isCreative() && player.isSneaking()) {
                        String roomName = lines.getFirst().getString();
                        entity.setKeyName(roomName);
                        return ActionResult.SUCCESS;
                    }
                }
            }

            return ActionResult.PASS;
        }
        return super.useOnBlock(context);
    }
}
