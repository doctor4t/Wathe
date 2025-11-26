package dev.doctor4t.trainmurdermystery.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    private World world;

    @WrapMethod(method = "collidesWith")
    protected boolean tmm$solid(Entity other, Operation<Boolean> original) {
        if (GameWorldComponent.KEY.get(this.world).isRunning()) {
            Entity self = (Entity) (Object) this;
            if (self instanceof PlayerEntity && other instanceof PlayerEntity) return true;
        }
        return original.call(other);
    }
}