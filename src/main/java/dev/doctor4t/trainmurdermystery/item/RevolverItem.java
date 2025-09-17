package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.TrainMurderMystery;
import dev.doctor4t.trainmurdermystery.block.SmallDoorBlock;
import dev.doctor4t.trainmurdermystery.block_entity.SmallDoorBlockEntity;
import dev.doctor4t.trainmurdermystery.client.TrainMurderMysteryClient;
import dev.doctor4t.trainmurdermystery.client.particle.HandParticle;
import dev.doctor4t.trainmurdermystery.game.GameLoop;
import dev.doctor4t.trainmurdermystery.index.TMMDataComponentTypes;
import dev.doctor4t.trainmurdermystery.index.TrainMurderMysterySounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.*;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class RevolverItem extends Item {
    public RevolverItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return shoot(world, user, hand);
    }

    private @NotNull TypedActionResult<ItemStack> shoot(World world, PlayerEntity user, Hand hand) {
        ItemStack stackInHand = user.getStackInHand(hand);
        Integer bullets = stackInHand.get(TMMDataComponentTypes.BULLETS);

        if (bullets == null) {
            bullets = 6;
        }

        if (!world.isClient) {
            world.playSound(null, user.getX(), user.getEyeY(), user.getZ(), TrainMurderMysterySounds.ITEM_REVOLVER_CLICK, SoundCategory.PLAYERS, 0.5f, 1f + world.random.nextFloat() * .1f - .05f);
            if (bullets > 0) {
                world.playSound(null, user.getX(), user.getEyeY(), user.getZ(), TrainMurderMysterySounds.ITEM_REVOLVER_SHOOT, SoundCategory.PLAYERS, 5f, 1f + world.random.nextFloat() * .1f - .05f);
                user.getItemCooldownManager().set(this, 20);
                if (!user.isCreative()) stackInHand.set(TMMDataComponentTypes.BULLETS, bullets-1);

                HitResult collision = ProjectileUtil.getCollision(user, entity -> entity.isAlive() && entity.isAttackable(), 20f);
                if (collision instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof PlayerEntity killedPlayer && TrainMurderMystery.isPlayerAliveAndSurvival(killedPlayer)) {
                    GameLoop.killPlayer(killedPlayer, true);
                }
                return TypedActionResult.consume(user.getStackInHand(hand));
            } else {
                return TypedActionResult.fail(user.getStackInHand(hand));
            }
        } else {
            if (bullets > 0) {
                user.setPitch(user.getPitch() - 4);

                HandParticle particle_animated = new HandParticle(0.1f, 0.275f, -0.2f,
                        0, 0, 0,
                        0.5f, 8,
                        TrainMurderMystery.id("textures/particle/gunshot.png"),
                        17, false);
                TrainMurderMysteryClient.handParticleManager.spawn(particle_animated);

                return TypedActionResult.consume(user.getStackInHand(hand));
            } else {
                return TypedActionResult.fail(user.getStackInHand(hand));
            }
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        TypedActionResult<ItemStack> shoot = shoot(world, player, context.getHand());

        if (shoot.getResult() == ActionResult.CONSUME && state.getBlock() instanceof SmallDoorBlock) {
            BlockPos lowerPos = state.get(SmallDoorBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();
            if (world.getBlockEntity(lowerPos) instanceof SmallDoorBlockEntity entity) {
                entity.blast();
            }
        }
        return shoot.getResult();
    }
}
