package dev.doctor4t.wathe.block;

import dev.doctor4t.wathe.block.entity.SeatEntity;
import dev.doctor4t.wathe.index.WatheEntities;
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
        float radius = 1;
        if (!player.isSneaking()
                && player.getPos().subtract(pos.toCenterPos()).length() <= 1.5f
                && !(player.getMainHandStack().getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof MountableBlock)
                && world.getEntitiesByClass(SeatEntity.class, Box.of(pos.toCenterPos(), radius, radius, radius), Entity::isAlive).isEmpty()) {

            if (world.isClient) {
                return ActionResult.success(true);
            }

            SeatEntity seatEntity = WatheEntities.SEAT.create(world);

            if (seatEntity == null) {
                return ActionResult.PASS;
            }

            Vec3d sitPos = this.getSitPos(world, state, pos);
            Vec3d vec3d = Vec3d.of(pos).add(sitPos);
            seatEntity.refreshPositionAndAngles(vec3d.x, vec3d.y, vec3d.z, 0, 0);
            seatEntity.setSeatPos(pos);
            world.spawnEntity(seatEntity);
            player.startRiding(seatEntity);

            return ActionResult.success(false);
        } else {
            return ActionResult.PASS;
        }
    }

    public abstract Vec3d getSitPos(World world, BlockState state, BlockPos pos);
}
