package dev.doctor4t.trainmurdermystery.client.util;

import dev.doctor4t.trainmurdermystery.TMMConfig;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;

public class AlwaysVisibleFrustum extends Frustum {

    @SuppressWarnings("unused")
    public AlwaysVisibleFrustum(Matrix4f positionMatrix, Matrix4f projectionMatrix) {
        super(positionMatrix, projectionMatrix);
    }

    public AlwaysVisibleFrustum(Frustum frustum) {
        super(frustum);
    }

    @Override
    public boolean isVisible(Box box) {
        if (!TMMClient.isTrainMoving()) {
            return super.isVisible(box);
        }

        if (TMMConfig.ultraPerfMode) {
            return super.isVisible(box) && box.getCenter().getY() < 148 && box.getCenter().getY() > 112;
        }

        return box.getCenter().getY() < 148 && box.getCenter().getY() > -64;
    }
}