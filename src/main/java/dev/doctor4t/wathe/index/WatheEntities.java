package dev.doctor4t.wathe.index;

import dev.doctor4t.ratatouille.util.registrar.EntityTypeRegistrar;
import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.block.entity.SeatEntity;
import dev.doctor4t.wathe.entity.FirecrackerEntity;
import dev.doctor4t.wathe.entity.GrenadeEntity;
import dev.doctor4t.wathe.entity.NoteEntity;
import dev.doctor4t.wathe.entity.PlayerBodyEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public interface WatheEntities {
    EntityTypeRegistrar registrar = new EntityTypeRegistrar(Wathe.MOD_ID);

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
    EntityType<NoteEntity> NOTE = registrar.create("note", EntityType.Builder.create(NoteEntity::new, SpawnGroup.MISC)
            .dimensions(.45f, .45f)
            .maxTrackingRange(128)
    );

    static void initialize() {
        registrar.registerEntries();

        FabricDefaultAttributeRegistry.register(PLAYER_BODY, PlayerBodyEntity.createAttributes());
    }
}
