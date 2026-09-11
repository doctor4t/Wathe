package dev.doctor4t.wathe.api.task;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public interface TrainTask {
    default void tick(@NotNull PlayerEntity player) {
    }

    boolean isFulfilled(PlayerEntity player);

    TaskType getType();

    default void writeCustomDataToNbt(NbtCompound nbt) {
    }
}
