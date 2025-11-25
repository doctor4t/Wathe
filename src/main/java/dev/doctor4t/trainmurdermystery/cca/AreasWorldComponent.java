package dev.doctor4t.trainmurdermystery.cca;

import dev.doctor4t.trainmurdermystery.TMM;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class AreasWorldComponent implements AutoSyncedComponent {
    public static final ComponentKey<AreasWorldComponent> KEY = ComponentRegistry.getOrCreate(TMM.id("areas"), AreasWorldComponent.class);
    private final World world;

    public static class PosWithOrientation {
        public final Vec3d pos;
        public final float yaw;
        public final float pitch;

        PosWithOrientation(Vec3d pos, float yaw, float pitch) {
            this.pos = pos;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        PosWithOrientation(double x, double y, double z, float yaw, float pitch) {
            this(new Vec3d(x, y, z), yaw, pitch);
        }

    }
    public static Vec3d getVec3dFromNbt(NbtCompound tag, String name) {
        return new Vec3d(tag.getDouble(name + "X"), tag.getFloat(name + "Y"), tag.getDouble(name + "Z"));
    }

    public void writeVec3dToNbt(NbtCompound tag, Vec3d vec3d, String name) {
        tag.putDouble(name + "X", vec3d.getX());
        tag.putDouble(name + "Y", vec3d.getY());
        tag.putDouble(name + "Z", vec3d.getZ());
    }

    public static PosWithOrientation getPosWithOrientationFromNbt(NbtCompound tag, String name) {
        return new PosWithOrientation(tag.getDouble(name + "X"), tag.getFloat(name + "Y"), tag.getDouble(name + "Z"), tag.getFloat(name + "Yaw"), tag.getFloat(name + "Pitch"));
    }

    public void writePosWithOrientationToNbt(NbtCompound tag, PosWithOrientation posWithOrientation, String name) {
        tag.putDouble(name + "X", posWithOrientation.pos.getX());
        tag.putDouble(name + "Y", posWithOrientation.pos.getY());
        tag.putDouble(name + "Z", posWithOrientation.pos.getZ());
        tag.putDouble(name + "Yaw", posWithOrientation.yaw);
        tag.putDouble(name + "Pitch", posWithOrientation.pitch);
    }

    public static Box getBoxFromNbt(NbtCompound tag, String name) {
        return new Box(tag.getDouble(name + "MinX"), tag.getFloat(name + "MinY"), tag.getDouble(name + "MinZ"), tag.getDouble(name + "MaxX"), tag.getFloat(name + "MaxY"), tag.getDouble(name + "MaxZ"));
    }

    public void writeBoxToNbt(NbtCompound tag, Box box, String name) {
        tag.putDouble(name + "MinX", box.minX);
        tag.putDouble(name + "MinY", box.minY);
        tag.putDouble(name + "MinZ", box.minZ);
        tag.putDouble(name + "MaxX", box.maxX);
        tag.putDouble(name + "MaxY", box.maxY);
        tag.putDouble(name + "MaxZ", box.maxZ);
    }

    // Game areas
    PosWithOrientation spawnPos = new PosWithOrientation(-872.5f, 0f, -323f, 90f, 0f);
    PosWithOrientation spectatorSpawnPos = new PosWithOrientation(-68f, 133f, -535.5f, -90f, 15f);

    Box readyArea = new Box(-1017, -1, -363.5f, -813, 3, -357.5f);
    Vec3d playAreaOffset = new Vec3d(963, 121, -175);
    Box playArea = new Box(-140, 118, -535.5f - 15, 230, 200, -535.5f + 15);

    Box resetTemplateArea = new Box(-57, 64, -531, 177, 74, -541);
    Box resetPasteArea = resetTemplateArea.offset(0, 55, 0);

    public PosWithOrientation getSpawnPos() {
        return spawnPos;
    }

    public void setSpawnPos(PosWithOrientation spawnPos) {
        this.spawnPos = spawnPos;
    }

    public PosWithOrientation getSpectatorSpawnPos() {
        return spectatorSpawnPos;
    }

    public void setSpectatorSpawnPos(PosWithOrientation spectatorSpawnPos) {
        this.spectatorSpawnPos = spectatorSpawnPos;
    }

    public Box getReadyArea() {
        return readyArea;
    }

    public void setReadyArea(Box readyArea) {
        this.readyArea = readyArea;
    }

    public Vec3d getPlayAreaOffset() {
        return playAreaOffset;
    }

    public void setPlayAreaOffset(Vec3d playAreaOffset) {
        this.playAreaOffset = playAreaOffset;
    }

    public Box getPlayArea() {
        return playArea;
    }

    public void setPlayArea(Box playArea) {
        this.playArea = playArea;
    }

    public Box getResetTemplateArea() {
        return resetTemplateArea;
    }

    public void setResetTemplateArea(Box resetTemplateArea) {
        this.resetTemplateArea = resetTemplateArea;
    }

    public Box getResetPasteArea() {
        return resetPasteArea;
    }

    public void setResetPasteArea(Box resetPasteArea) {
        this.resetPasteArea = resetPasteArea;
    }

    public AreasWorldComponent(World world) {
        this.world = world;
    }

    public void sync() {
        KEY.sync(this.world);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        this.spawnPos = getPosWithOrientationFromNbt(tag, "spawnPos");
        this.spectatorSpawnPos = getPosWithOrientationFromNbt(tag, "spectatorSpawnPos");

        this.readyArea = getBoxFromNbt(tag, "readyArea");
        this.playAreaOffset = getVec3dFromNbt(tag, "playAreaOffset");
        this.playArea = getBoxFromNbt(tag, "playArea");

        this.resetTemplateArea = getBoxFromNbt(tag, "resetTemplateArea");
        this.resetPasteArea = getBoxFromNbt(tag, "resetPasteArea");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        writePosWithOrientationToNbt(tag, this.spawnPos, "spawnPos");
        writePosWithOrientationToNbt(tag, this.spectatorSpawnPos, "spectatorSpawnPos");

        writeBoxToNbt(tag, this.readyArea, "readyArea");
        writeVec3dToNbt(tag, this.playAreaOffset, "playAreaOffset");
        writeBoxToNbt(tag, this.playArea, "playArea");

        writeBoxToNbt(tag, this.resetTemplateArea, "resetTemplateArea");
        writeBoxToNbt(tag, this.resetPasteArea, "resetPasteArea");
    }
}
