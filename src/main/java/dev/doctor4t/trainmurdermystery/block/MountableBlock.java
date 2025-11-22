package dev.doctor4t.trainmurdermystery.block;

import dev.doctor4t.trainmurdermystery.block.entity.SeatEntity;
import dev.doctor4t.trainmurdermystery.index.TMMEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class MountableBlock extends Block {

    public MountableBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player.isSneaking()) {
            return ActionResult.PASS;
        }
        if (player.getPos().subtract(pos.toCenterPos()).length() > 1.5f) {
            return ActionResult.PASS;
        }
        if (!(player.getMainHandStack().getItem() instanceof BlockItem blockItem)) {
            return ActionResult.PASS;
        }
        if (!(blockItem.getBlock() instanceof MountableBlock)) {
            return ActionResult.PASS;
        }
        final float radius = 1;
        if (!world.getEntitiesByClass(SeatEntity.class, Box.of(pos.toCenterPos(), radius, radius, radius), Entity::isAlive).isEmpty()) {
            return ActionResult.PASS;
        }
        if (world.isClient) {
            return ActionResult.success(true);
        }

        SeatEntity seatEntity = TMMEntities.SEAT.create(world);

        if (seatEntity == null) {
            return ActionResult.PASS;
        }

        Vec3d offset = this.getSitOffset(world, state, pos);
        Vec3d vec3d = Vec3d.of(pos).add(offset);
        seatEntity.refreshPositionAndAngles(vec3d.x, vec3d.y, vec3d.z, 0, 0);
        seatEntity.setSeatPos(pos);
        world.spawnEntity(seatEntity);
        player.startRiding(seatEntity);

        return ActionResult.success(false);
    }

    public abstract Vec3d getSitOffset(World world, BlockState state, BlockPos pos);
}
