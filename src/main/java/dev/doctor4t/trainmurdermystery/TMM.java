package dev.doctor4t.trainmurdermystery;

import dev.doctor4t.trainmurdermystery.block.DoorPartBlock;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.command.*;
import dev.doctor4t.trainmurdermystery.command.argument.TMMGameModeArgumentType;
import dev.doctor4t.trainmurdermystery.command.argument.TimeOfDayArgumentType;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.*;
import dev.doctor4t.trainmurdermystery.networking.*;
import dev.doctor4t.trainmurdermystery.util.*;
import dev.upcraft.datasync.api.DataSyncAPI;
import dev.upcraft.datasync.api.util.Entitlements;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TMM implements ModInitializer {
    public static final String MOD_ID = "trainmurdermystery";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final boolean DEVELOPMENT = FabricLoader.getInstance().isDevelopmentEnvironment();
    public static final Identifier COMMAND_ACCESS = id("commandaccess");

    public static @NotNull Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        // Init constants
        GameConstants.init();

        // Registry initializers
        TMMDataComponentTypes.initialize();
        TMMSounds.initialize();
        TMMEntities.initialize();
        TMMBlocks.initialize();
        TMMItems.initialize();
        TMMBlockEntities.initialize();
        TMMParticles.initialize();

        // Register command argument types
        ArgumentTypeRegistry.registerArgumentType(id("timeofday"), TimeOfDayArgumentType.class, ConstantArgumentSerializer.of(TimeOfDayArgumentType::timeofday));
        ArgumentTypeRegistry.registerArgumentType(id("gamemode"), TMMGameModeArgumentType.class, ConstantArgumentSerializer.of(TMMGameModeArgumentType::gamemode));

        // Register commands
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            GiveRoomKeyCommand.register(dispatcher);
            StartCommand.register(dispatcher);
            StopCommand.register(dispatcher);
            EnableWeightsCommand.register(dispatcher);
            CheckWeightsCommand.register(dispatcher);
            ResetWeightsCommand.register(dispatcher);
            SetVisualCommand.register(dispatcher);
            ForceRoleCommand.register(dispatcher);
//            UpdateDoorsCommand.register(dispatcher);
            SetTimerCommand.register(dispatcher);
            SetMoneyCommand.register(dispatcher);
            SetBoundCommand.register(dispatcher);
            AutoStartCommand.register(dispatcher);
            LockToSupportersCommand.register(dispatcher);
        }));

        // server lock to supporters
        ServerPlayerEvents.JOIN.register(player -> {
            DataSyncAPI.refreshAllPlayerData(player.getUuid()).thenRunAsync(() -> {
                // check if player is supporter now, if not kick
                if (GameWorldComponent.KEY.get(player.getWorld()).isLockedToSupporters() && !TMM.isSupporter(player)) {
                    player.networkHandler.disconnect(Text.literal("Server is reserved to doctor4t supporters."));
                }
            }, player.getWorld().getServer());
        });

        PayloadTypeRegistry.playS2C().register(ShootMuzzleS2CPayload.ID, ShootMuzzleS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PoisonOverlayS2CPayload.ID, PoisonOverlayS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(GunDropS2CPayload.ID, GunDropS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TaskCompleteS2CPayload.ID, TaskCompleteS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AnnounceWelcomeS2CPayload.ID, AnnounceWelcomeS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AnnounceEndingS2CPayload.ID, AnnounceEndingS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(KnifeStabC2SPayload.ID, KnifeStabC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(GunShootC2SPayload.ID, GunShootC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(StoreBuyC2SPayload.ID, StoreBuyC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(NoteEditC2SPayload.ID, NoteEditC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(KnifeStabC2SPayload.ID, new KnifeStabC2SPayload.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(GunShootC2SPayload.ID, new GunShootC2SPayload.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(StoreBuyC2SPayload.ID, new StoreBuyC2SPayload.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(NoteEditC2SPayload.ID, new NoteEditC2SPayload.Receiver());

        Scheduler.init();
    }

    public static boolean isSkyVisibleAdjacent(@NotNull Entity player) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable();
        BlockPos playerPos = BlockPos.ofFloored(player.getEyePos());
        for (int x = -1; x <= 1; x += 2) {
            for (int z = -1; z <= 1; z += 2) {
                blockPos.set(playerPos.getX() + x, playerPos.getY(), playerPos.getZ() + z);
                if (player.getWorld().isSkyVisible(blockPos)) {
                    // uh is this correct - SkyNotTheLimit
                    return !(player.getWorld().getBlockState(playerPos).getBlock() instanceof DoorPartBlock);
                }
            }
        }
        return false;
    }

    public static boolean isExposedToWind(@NotNull Entity player) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable();
        BlockPos playerPos = BlockPos.ofFloored(player.getEyePos());
        for (int x = 0; x <= 10; x++) {
            blockPos.set(playerPos.getX() - x, player.getEyePos().getY(), playerPos.getZ());
            if (!player.getWorld().isSkyVisible(blockPos)) {
                return false;
            }
        }
        return true;
    }

    public static int executeSupporterCommand(ServerCommandSource source, Runnable runnable) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            return 0;
        }

        if (isSupporter(player) || DEVELOPMENT) {
            runnable.run();
            return 1;
        }

        player.sendMessage(Text.translatable("commands.supporter_only"));
        return 0;
    }

    public static @NotNull Boolean isSupporter(PlayerEntity player) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment())
            return true;

        Optional<Entitlements> entitlements = Entitlements.token().get(player.getUuid());
        return entitlements.map(value -> value.keys().stream().anyMatch(identifier -> identifier.equals(COMMAND_ACCESS))).orElse(false);
    }
}