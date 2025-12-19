package dev.doctor4t.wathe.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class NoteEntity extends Entity {
    private static final TrackedData<Integer> DIRECTION = DataTracker.registerData(NoteEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<String> LINE1 = DataTracker.registerData(NoteEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> LINE2 = DataTracker.registerData(NoteEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> LINE3 = DataTracker.registerData(NoteEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> LINE4 = DataTracker.registerData(NoteEntity.class, TrackedDataHandlerRegistry.STRING);
    public final int seed;

    public NoteEntity(EntityType<?> type, World world) {
        super(type, world);
        this.seed = this.random.nextInt();
    }

    @Override
    public void tick() {
        super.tick();

        Supplier<Float> randomGiver = () -> (random.nextFloat() - .5f) * .2f;
        if (random.nextFloat() < .1f) {
            this.getWorld().addParticle(ParticleTypes.WAX_ON, this.getX() + randomGiver.get(), this.getY() + randomGiver.get() + this.getHeight() / 2f, this.getZ() + randomGiver.get(), 0, 0, 0);
        }
    }

    public String[] getLines() {
        return new String[]{
                this.dataTracker.get(LINE1),
                this.dataTracker.get(LINE2),
                this.dataTracker.get(LINE3),
                this.dataTracker.get(LINE4)
        };
    }

    public void setLines(String @NotNull [] lines) {
        if (lines.length > 0) this.dataTracker.set(LINE1, lines[0]);
        if (lines.length > 1) this.dataTracker.set(LINE2, lines[1]);
        if (lines.length > 2) this.dataTracker.set(LINE3, lines[2]);
        if (lines.length > 3) this.dataTracker.set(LINE4, lines[3]);
    }

    public @NotNull Direction getDirection() {
        return Direction.values()[this.dataTracker.get(DIRECTION)];
    }

    public void setDirection(@NotNull Direction direction) {
        this.dataTracker.set(DIRECTION, direction.getId());
    }

    @Override
    protected void initDataTracker(DataTracker.@NotNull Builder builder) {
        builder.add(DIRECTION, Direction.NORTH.getId());
        builder.add(LINE1, "");
        builder.add(LINE2, "");
        builder.add(LINE3, "");
        builder.add(LINE4, "");
    }

    @Override
    protected void writeCustomDataToNbt(@NotNull NbtCompound nbt) {
        nbt.putInt("Direction", this.dataTracker.get(DIRECTION));
        nbt.putString("Line1", this.dataTracker.get(LINE1));
        nbt.putString("Line2", this.dataTracker.get(LINE2));
        nbt.putString("Line3", this.dataTracker.get(LINE3));
        nbt.putString("Line4", this.dataTracker.get(LINE4));
    }

    @Override
    protected void readCustomDataFromNbt(@NotNull NbtCompound nbt) {
        if (nbt.contains("Direction")) this.dataTracker.set(DIRECTION, nbt.getInt("Direction"));
        if (nbt.contains("Line1")) this.dataTracker.set(LINE1, nbt.getString("Line1"));
        if (nbt.contains("Line2")) this.dataTracker.set(LINE2, nbt.getString("Line2"));
        if (nbt.contains("Line3")) this.dataTracker.set(LINE3, nbt.getString("Line3"));
        if (nbt.contains("Line4")) this.dataTracker.set(LINE4, nbt.getString("Line4"));
    }
}