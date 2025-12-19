package dev.doctor4t.wathe.util;

import dev.doctor4t.wathe.client.particle.HandParticle;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.concurrent.CopyOnWriteArrayList;

public class HandParticleManager {
    private final CopyOnWriteArrayList<HandParticle> particles = new CopyOnWriteArrayList<>();
    public static Vector3f vector;

    public void spawn(HandParticle p) {
        particles.add(p);
    }

    public void tick() {
        if (particles.isEmpty()) return;

        particles.forEach(handParticle -> {
            if (!handParticle.tick(1f)) particles.remove(handParticle);
        });
    }

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        vector = new Vector3f(0f, 0f, 1f);
        if (particles.isEmpty()) return;

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f model = entry.getPositionMatrix();

        Vector3f right = new Vector3f(1, 0, 0);
        Vector3f up = new Vector3f(0, 1, 0);

        for (HandParticle p : particles) {
            RenderLayer rl = p.renderLayerFactory.apply(p.texture);
            VertexConsumer consumer = vertexConsumers.getBuffer(rl);

            float half = p.size * 0.5f;
            Vector3f center = new Vector3f(p.x, p.y, p.z);

            float frameHeight = 1f / p.frames;
            int currentFrame;

            if (p.loop) {
                currentFrame = (int) ((p.age / p.maxAge) * p.frames) % p.frames;
            } else {
                currentFrame = Math.min((int) ((p.age / p.maxAge) * p.frames), p.frames - 1);
            }

            float v0 = frameHeight * currentFrame;
            float v1 = v0 + frameHeight;

            float u0 = 0f;
            float u1 = 1f;

            Vector3f c1 = new Vector3f(center).add(new Vector3f(right).mul(-half)).add(new Vector3f(up).mul(-half));
            Vector3f c2 = new Vector3f(center).add(new Vector3f(right).mul(-half)).add(new Vector3f(up).mul(half));
            Vector3f c3 = new Vector3f(center).add(new Vector3f(right).mul(half)).add(new Vector3f(up).mul(half));
            Vector3f c4 = new Vector3f(center).add(new Vector3f(right).mul(half)).add(new Vector3f(up).mul(-half));

            putVertex(consumer, model, c1, u0, v1, p);
            putVertex(consumer, model, c2, u0, v0, p);
            putVertex(consumer, model, c3, u1, v0, p);
            putVertex(consumer, model, c4, u1, v1, p);
        }
    }

    private static void putVertex(VertexConsumer consumer, Matrix4f model, Vector3f pos, float u, float v, HandParticle p) {
        float t = p.age / p.maxAge;

        int n = p.rColors.length;
        float r, g, b;
        if (n == 1) {
            r = p.rColors[0];
            g = p.gColors[0];
            b = p.bColors[0];
        } else {
            float scaled = t * (n - 1);
            int idx = (int) Math.floor(scaled);
            int next = Math.min(idx + 1, n - 1);
            float localT = scaled - idx;

            r = lerp(localT, p.rColors[idx], p.rColors[next]);
            g = lerp(localT, p.gColors[idx], p.gColors[next]);
            b = lerp(localT, p.bColors[idx], p.bColors[next]);
        }

        n = p.aColors.length;
        float a;
        if (n == 1) a = p.aColors[0];
        else {
            float scaled = t * (n - 1);
            int idx = (int) Math.floor(scaled);
            int next = Math.min(idx + 1, n - 1);
            float localT = scaled - idx;
            a = lerp(localT, p.aColors[idx], p.aColors[next]);
        }

        float displayR = r * a;
        float displayG = g * a;
        float displayB = b * a;

        consumer.vertex(model, pos.x, pos.y, pos.z)
                .color(displayR, displayG, displayB, a)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(p.light)
                .normal(0f, 1f, 0f);
    }

    private static float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }
}
