package dev.doctor4t.trainmurdermystery.index;

import dev.doctor4t.ratatouille.util.registrar.EntityTypeRegistrar;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.block.entity.SeatEntity;
import dev.doctor4t.trainmurdermystery.entity.FirecrackerEntity;
import dev.doctor4t.trainmurdermystery.entity.GrenadeEntity;
import dev.doctor4t.trainmurdermystery.entity.PlayerBodyEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public interface TMMEntities {
    EntityTypeRegistrar registrar = new EntityTypeRegistrar(TMM.MOD_ID);

    EntityType<SeatEntity> SEAT = registrar.create("seat", EntityType.Builder.create(SeatEntity::new, SpawnGroup.MISC)
            .dimensions(1f, 1f)
            .maxTrackingRange(128)
            .disableSummon()
    );
    EntityType<PlayerBodyEntity> PLAYER_BODY = registrar.create("player_body", EntityType.Builder.create(PlayerBodyEntity::new, SpawnGroup.MISC)
            .dimensions(1f, 0.25f)
            .maxTrackingRange(128)
            .disableSummon()
    );
    EntityType<FirecrackerEntity> FIRECRACKER = registrar.create("firecracker", EntityType.Builder.create(FirecrackerEntity::new, SpawnGroup.MISC)
            .dimensions(.2f, .2f)
            .maxTrackingRange(128)
    );
    EntityType<GrenadeEntity> GRENADE = registrar.create("grenade", EntityType.Builder.create(GrenadeEntity::new, SpawnGroup.MISC)
            .dimensions(.2f, .2f)
            .maxTrackingRange(128)
    );

    static void initialize() {
        registrar.registerEntries();

        FabricDefaultAttributeRegistry.register(PLAYER_BODY, PlayerBodyEntity.createAttributes());
    }
}
