package dev.doctor4t.trainmurdermystery.cca;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

public class TMMComponents implements WorldComponentInitializer, EntityComponentInitializer, ScoreboardComponentInitializer {
    @Override
    public void registerWorldComponentFactories(@NotNull WorldComponentFactoryRegistry registry) {
        registry.register(TrainWorldComponent.KEY, TrainWorldComponent::new);
        registry.register(GameWorldComponent.KEY, GameWorldComponent::new);
        registry.register(AreasWorldComponent.KEY, AreasWorldComponent::new);
        registry.register(WorldBlackoutComponent.KEY, WorldBlackoutComponent::new);
        registry.register(GameTimeComponent.KEY, GameTimeComponent::new);
        registry.register(AutoStartComponent.KEY, AutoStartComponent::new);
        registry.register(GameRoundEndComponent.KEY, GameRoundEndComponent::new);
    }

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, PlayerMoodComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(PlayerMoodComponent::new);
        registry.beginRegistration(PlayerEntity.class, PlayerShopComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(PlayerShopComponent::new);
        registry.beginRegistration(PlayerEntity.class, PlayerPoisonComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(PlayerPoisonComponent::new);
        registry.beginRegistration(PlayerEntity.class, PlayerPsychoComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(PlayerPsychoComponent::new);
        registry.beginRegistration(PlayerEntity.class, PlayerNoteComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(PlayerNoteComponent::new);
    }

    @Override
    public void registerScoreboardComponentFactories(@NotNull ScoreboardComponentFactoryRegistry registry) {
        registry.registerScoreboardComponent(ScoreboardRoleSelectorComponent.KEY, ScoreboardRoleSelectorComponent::new);
    }
}