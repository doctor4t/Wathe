package dev.doctor4t.wathe.block;

import dev.doctor4t.wathe.cca.WorldBlackoutComponent;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public interface BlackoutBlock {
    int getDuration(Random random);

    default void init(@NotNull World world, WorldBlackoutComponent.BlackoutDetails detail) {
    }

    default void end(@NotNull World world, WorldBlackoutComponent.BlackoutDetails detail) {
    }

    default void tick(World world, WorldBlackoutComponent.BlackoutDetails detail) {
    }
}
