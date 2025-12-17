package dev.doctor4t.trainmurdermystery.util;

import dev.doctor4t.trainmurdermystery.cca.WorldBlackoutComponent;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.TMMProperties;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public interface BlackoutBlockFunctions {
    interface Lights {
        static void init(@NotNull World world, WorldBlackoutComponent.BlackoutDetails detail) {
            BlockState state = world.getBlockState(detail.pos);
            if (!state.contains(Properties.LIT) || !state.contains(TMMProperties.ACTIVE)) return;
            detail.data = (byte) (state.get(Properties.LIT) ? -128 : 0);
            world.setBlockState(detail.pos, state.with(Properties.LIT, false).with(TMMProperties.ACTIVE, false));
//            playsound(detail, world, TMMSounds.BLOCK_BUTTON_TOGGLE_NO_POWER, 0.5f, 1f);
        }

        static void end(@NotNull World world, WorldBlackoutComponent.BlackoutDetails detail) {
            BlockState state = world.getBlockState(detail.pos);
            if (!state.contains(Properties.LIT) || !state.contains(TMMProperties.ACTIVE)) return;
//            world.setBlockState(detail.pos, state.with(Properties.LIT, (detail.data & -128) == -128).with(TMMProperties.ACTIVE, true));
            world.setBlockState(detail.pos, state.with(Properties.LIT, true).with(TMMProperties.ACTIVE, true));
            playsound(detail, world, TMMSounds.BLOCK_LIGHT_TOGGLE, 0.5f, 0.8f);
        }

        static void tick(World world, WorldBlackoutComponent.BlackoutDetails detail) {
//            if (detail.getTime() > 50) return;
            BlockState state = world.getBlockState(detail.pos);
            if (!state.contains(Properties.LIT) || !state.contains(TMMProperties.ACTIVE)) return;
            switch (detail.getTime()) {
//                case 0 -> {
//                    detail.end(world);
//                    return;
//                }
                case 3 -> blinkBlock(world, detail, state, false);
                case 7 -> blinkBlock(world, detail, state, true);
            }
            if (detail.getTime() > 20 && detail.getTime() < GameConstants.BLACKOUT_MIN_DURATION / 2) {
                int i = detail.data & 0x7F;
                if (i > 0) detail.data = (byte) ((detail.data & -128) | i - 1);
                if (i == 1) blinkBlock(world, detail, state, false);
                if (world.random.nextDouble() < .002) {
                    detail.data = (byte) ((detail.data & -128) | world.random.nextBetween(4, 8));
                    blinkBlock(world, detail, state, true);
                }
            }
        }

        private static void blinkBlock(World world, WorldBlackoutComponent.BlackoutDetails detail, BlockState state, boolean b) {
            world.setBlockState(detail.pos, state.with(Properties.LIT, b).with(TMMProperties.ACTIVE, b));
            playsound(detail, world, TMMSounds.BLOCK_LIGHT_TOGGLE, 0.1f, 1f);
        }
    }

    interface PrivacyGlasses {
        static int getDuration(Random random) {
            return GameConstants.getRandomBlackoutDuration(random) * 2 / 3;
        }

        static void init(@NotNull World world, WorldBlackoutComponent.BlackoutDetails detail) {
            BlockState state = world.getBlockState(detail.pos);
            if (!state.contains(TMMProperties.OPAQUE) || !state.contains(TMMProperties.ACTIVE)) return;
            detail.data = (byte) (state.get(TMMProperties.OPAQUE) ? 1 : 0);
            world.setBlockState(detail.pos, state.with(TMMProperties.OPAQUE, true).with(TMMProperties.ACTIVE, false));
//            playsound(detail, world, TMMSounds.BLOCK_PRIVACY_PANEL_TOGGLE, 0.5f, 1f);
        }

        static void end(@NotNull World world, WorldBlackoutComponent.BlackoutDetails detail) {
            BlockState state = world.getBlockState(detail.pos);
            if (!state.contains(TMMProperties.OPAQUE) || !state.contains(TMMProperties.ACTIVE)) return;
            world.setBlockState(detail.pos, state.with(TMMProperties.OPAQUE, (detail.data & 1) == 1).with(TMMProperties.ACTIVE, true));
            playsound(detail, world, TMMSounds.BLOCK_PRIVACY_PANEL_TOGGLE, 0.1f, 0.8f);
        }

        static void tick(World world, WorldBlackoutComponent.BlackoutDetails detail) {
            if (detail.getTime() == 0) detail.end(world);
        }
    }

    static void playsound(WorldBlackoutComponent.BlackoutDetails detail, World world, SoundEvent sound, float volume, float pitch) { // i have no idea why, but i spend 3 hours on this and this is the only way it works
        if (world instanceof ServerWorld serverWorld) for (ServerPlayerEntity player : serverWorld.getPlayers()) {
            float v = sound.getDistanceToTravel(volume);
            if (player.getPos().squaredDistanceTo(detail.pos.getX() + .5, detail.pos.getY() + .5, detail.pos.getZ() + .5) > v * v)
                continue;
            player.networkHandler.sendPacket(new PlaySoundS2CPacket(Registries.SOUND_EVENT.getEntry(sound), SoundCategory.BLOCKS, detail.pos.getX() + .5, detail.pos.getY() + .5, detail.pos.getZ() + .5, volume, pitch, player.getRandom().nextLong()));
        }
    }
}
