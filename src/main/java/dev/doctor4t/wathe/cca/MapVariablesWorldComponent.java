package dev.doctor4t.wathe.cca;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class MapVariablesWorldComponent implements AutoSyncedComponent {
    public static final ComponentKey<MapVariablesWorldComponent> KEY = ComponentRegistry.getOrCreate(Wathe.id("mapvariables"), MapVariablesWorldComponent.class);
    private final World world;

    public MapVariablesWorldComponent(World world) {
        this.world = world;
    }

    public void sync() {
        KEY.sync(this.world);
    }

    // Game areas
    PosWithOrientation spawnPos = new PosWithOrientation(-872.5f, 0f, -323f, 90f, 0f);
    PosWithOrientation spectatorSpawnPos = new PosWithOrientation(-68f, 133f, -535.5f, -90f, 15f);

    Box readyArea = new Box(-1017, -1, -363.75f, -813, 3, -357.25f);
    Vec3i playAreaOffset = new Vec3i(963, 121, -175);
    Box playArea = new Box(-140, 118, -535.5f - 15, 230, 200, -535.5f + 15);

    Box resetTemplateArea = new Box(-57, 64, -531, 177, 74, -541);
    Vec3i resetPasteOffset = new Vec3i(0, 55, 0);

    Box snowflakeCollider = new Box(-41.5, 126.0, -538.5, 169.5, 120, -532.5);

    public PosWithOrientation getSpawnPos() {
        return spawnPos;
    }

    public void setSpawnPos(PosWithOrientation spawnPos) {
        this.spawnPos = spawnPos;
        this.sync();
    }

    public PosWithOrientation getSpectatorSpawnPos() {
        return spectatorSpawnPos;
    }

    public void setSpectatorSpawnPos(PosWithOrientation spectatorSpawnPos) {
        this.spectatorSpawnPos = spectatorSpawnPos;
        this.sync();
    }

    public Box getReadyArea() {
        return readyArea;
    }

    public void setReadyArea(Box readyArea) {
        this.readyArea = readyArea;
        this.sync();
    }

    public Vec3i getPlayAreaOffset() {
        return playAreaOffset;
    }

    public void setPlayAreaOffset(Vec3i playAreaOffset) {
        this.playAreaOffset = playAreaOffset;
        this.sync();
    }

    public Box getPlayArea() {
        return playArea;
    }

    public void setPlayArea(Box playArea) {
        this.playArea = playArea;
        this.sync();
    }

    public Box getResetTemplateArea() {
        return resetTemplateArea;
    }

    public void setResetTemplateArea(Box resetTemplateArea) {
        this.resetTemplateArea = resetTemplateArea;
        this.sync();
    }

    public Vec3i getResetPasteOffset() {
        return resetPasteOffset;
    }

    public void setResetPasteOffset(Vec3i resetPasteOffset) {
        this.resetPasteOffset = resetPasteOffset;
        this.sync();
    }

    public Box getSnowflakeCollider() {
        return snowflakeCollider;
    }

    public void setSnowflakeCollider(Box snowflakeCollider) {
        this.snowflakeCollider = snowflakeCollider;
        this.sync();
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        this.spawnPos = getPosWithOrientationFromNbt(tag, "spawnPos");
        this.spectatorSpawnPos = getPosWithOrientationFromNbt(tag, "spectatorSpawnPos");
        this.readyArea = getBoxFromNbt(tag, "readyArea");
        this.playAreaOffset = getVec3iFromNbt(tag, "playAreaOffset");
        this.playArea = getBoxFromNbt(tag, "playArea");
        this.resetTemplateArea = getBoxFromNbt(tag, "resetTemplateArea");
        this.resetPasteOffset = getVec3iFromNbt(tag, "resetPasteOffset");
        if (tag.contains("snowflakeColliderMinX")) // make sure the map had this feature before setting it, otherwise the collider will be (0,0,0), (0,0,0)
            this.snowflakeCollider = getBoxFromNbt(tag, "snowflakeCollider");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        writePosWithOrientationToNbt(tag, this.spawnPos, "spawnPos");
        writePosWithOrientationToNbt(tag, this.spectatorSpawnPos, "spectatorSpawnPos");
        writeBoxToNbt(tag, this.readyArea, "readyArea");
        writeVec3iToNbt(tag, this.playAreaOffset, "playAreaOffset");
        writeBoxToNbt(tag, this.playArea, "playArea");
        writeBoxToNbt(tag, this.resetTemplateArea, "resetTemplateArea");
        writeVec3iToNbt(tag, this.resetPasteOffset, "resetPasteOffset");
        writeBoxToNbt(tag, this.snowflakeCollider, "snowflakeCollider");
    }

    public static class PosWithOrientation {
        public final Vec3d pos;
        public final float yaw;
        public final float pitch;

        public PosWithOrientation(Vec3d pos, float yaw, float pitch) {
            this.pos = pos;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        PosWithOrientation(double x, double y, double z, float yaw, float pitch) {
            this(new Vec3d(x, y, z), yaw, pitch);
        }

    }

    public static Vec3d getVec3dFromNbt(NbtCompound tag, String name) {
        return new Vec3d(tag.getDouble(name + "X"), tag.getDouble(name + "Y"), tag.getDouble(name + "Z"));
    }

    public void writeVec3dToNbt(NbtCompound tag, Vec3d vec3d, String name) {
        tag.putDouble(name + "X", vec3d.getX());
        tag.putDouble(name + "Y", vec3d.getY());
        tag.putDouble(name + "Z", vec3d.getZ());
    }

    public static Vec3i getVec3iFromNbt(NbtCompound tag, String name) {
        return new Vec3i(tag.getInt(name + "X"), tag.getInt(name + "Y"), tag.getInt(name + "Z"));
    }

    public void writeVec3iToNbt(NbtCompound tag, Vec3i vec3i, String name) {
        tag.putInt(name + "X", vec3i.getX());
        tag.putInt(name + "Y", vec3i.getY());
        tag.putInt(name + "Z", vec3i.getZ());
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

}
