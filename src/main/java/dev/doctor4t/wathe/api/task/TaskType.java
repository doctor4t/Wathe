package dev.doctor4t.wathe.api.task;

import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TaskType {
    Map<Identifier, TaskType> TYPES = new HashMap<>();

    Identifier getId();

    TrainTask createTask();

    TrainTask getFromNbt(NbtCompound nbt);

    static TaskType register(Identifier id, @NotNull Supplier<TrainTask> creator, @NotNull Function<NbtCompound, TrainTask> fromNbt) {
        return register(new PlayerMoodComponent.TaskTypeImpl(id, creator, fromNbt));
    }

    static TaskType register(TaskType type) {
        TYPES.put(type.getId(), type);
        return type;
    }
}
