package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.cca.PlayerNoteComponent;
import dev.doctor4t.trainmurdermystery.entity.NoteEntity;
import dev.doctor4t.trainmurdermystery.index.TMMEntities;
import dev.doctor4t.trainmurdermystery.util.AdventureUsable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class NoteItem extends Item implements AdventureUsable {
    public NoteItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(@NotNull World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(@NotNull ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null || player.isSneaking()) return ActionResult.PASS;
        PlayerNoteComponent component = PlayerNoteComponent.KEY.get(player);
        if (!component.written) {
            player.sendMessage(Text.literal("I should write something first").withColor(MathHelper.hsvToRgb(0F, 1.0F, 0.6F)), true);
            return ActionResult.PASS;
        }
        World world = player.getWorld();
        if (world.isClient) return ActionResult.PASS;
        NoteEntity note = TMMEntities.NOTE.create(world);

        if (note == null) return ActionResult.PASS;

        switch (context.getSide()) {
            case DOWN -> {
                return ActionResult.PASS;
            }
            case UP -> note.setYaw(player.getHeadYaw());
            case NORTH, SOUTH, WEST, EAST -> note.setYaw(180f + (world.random.nextFloat() - .5f) * 30f);
        }

        Direction side = context.getSide();
        note.setDirection(side);
        note.setLines(component.text);
        Vec3d hitPos = context.getHitPos().add(context.getHitPos().subtract(player.getEyePos()).normalize().multiply(-.01f)).subtract(0, note.getHeight() / 2f, 0);
        note.setPosition(hitPos.getX(), hitPos.getY(), hitPos.getZ());
        world.spawnEntity(note);
        if (!player.isCreative()) player.getStackInHand(context.getHand()).decrement(1);
        return ActionResult.SUCCESS;
    }
}