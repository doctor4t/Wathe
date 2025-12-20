package dev.doctor4t.trainmurdermystery.cca;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class PlayerWetComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    public static final ComponentKey<PlayerWetComponent> KEY = ComponentRegistry.getOrCreate(TMM.id("wet"), PlayerWetComponent.class);
    private final PlayerEntity player;

    public int wetTicks = -1;

    public PlayerWetComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("wetTicks", this.wetTicks);
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.wetTicks = tag.getInt("wetTicks");
    }

    @Override
    public void clientTick() {
        if (this.wetTicks >= 0) {
            this.wetTicks--;
            Random r = player.getRandom();
            Box b = player.getBoundingBox();
            player.getWorld().addParticle(ParticleTypes.FALLING_WATER,
                    MathHelper.lerp(r.nextDouble(), b.minX, b.maxX),
                    MathHelper.lerp(r.nextDouble(), b.minY, b.maxY),
                    MathHelper.lerp(r.nextDouble(), b.minZ, b.maxZ),
                    0, 0, 0
            );
        }
    }

    @Override
    public void serverTick() {
        if (this.wetTicks >= 0) {
            if (this.wetTicks % 10 == 0) KEY.sync(player);
            this.wetTicks--;
        }
    }

    public void makeWet() {
        this.wetTicks = GameConstants.SPRINKLER_GIVE_WET;
    }

    public boolean isUnderShower() {
        return this.wetTicks + 5 > GameConstants.SPRINKLER_GIVE_WET;
    }

    public void reset() {
        wetTicks = -1;
        KEY.sync(player);
    }

    public static PlayerWetComponent get(PlayerEntity player) {
        return KEY.get(player);
    }
}
