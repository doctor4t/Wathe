package dev.doctor4t.trainmurdermystery.client;

import com.google.common.collect.Maps;
import dev.doctor4t.ratatouille.client.util.OptionLocker;
import dev.doctor4t.ratatouille.client.util.ambience.AmbienceUtil;
import dev.doctor4t.ratatouille.client.util.ambience.BackgroundAmbience;
import dev.doctor4t.trainmurdermystery.TrainMurderMystery;
import dev.doctor4t.trainmurdermystery.cca.TrainMurderMysteryComponents;
import dev.doctor4t.trainmurdermystery.cca.WorldGameComponent;
import dev.doctor4t.trainmurdermystery.client.model.TrainMurderMysteryEntityModelLayers;
import dev.doctor4t.trainmurdermystery.client.render.block_entity.SmallDoorBlockEntityRenderer;
import dev.doctor4t.trainmurdermystery.client.util.TMMItemTooltips;
import dev.doctor4t.trainmurdermystery.index.*;
import dev.doctor4t.trainmurdermystery.util.HandParticleManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class TrainMurderMysteryClient implements ClientModInitializer {
    public static HandParticleManager handParticleManager;
    private static float trainSpeed;
    private static boolean prevGameRunning;
    private static WorldGameComponent GAME_COMPONENT;

    public static final Map<UUID, PlayerListEntry> PLAYER_ENTRIES_CACHE = Maps.newHashMap();

    public static KeyBinding instinctKeybind;

    public static boolean shouldDisableHudAndDebug() {
        MinecraftClient client = MinecraftClient.getInstance();
        return (client == null || (client.player != null && !client.player.isCreative() && !client.player.isSpectator()));
    }

    @Override
    public void onInitializeClient() {
        //Initialize ScreenParticle
        handParticleManager = new HandParticleManager();

        // Register particle factories
        TrainMurderMysteryParticles.registerFactories();

        // Entity renderer registration
        EntityRendererRegistry.register(TrainMurderMysteryEntities.SEAT, EmptyEntityRenderer::new);

        // Register entity model layers
        TrainMurderMysteryEntityModelLayers.initialize();

        // Block render layers
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                TrainMurderMysteryBlocks.STAINLESS_STEEL_VENT_HATCH,
                TrainMurderMysteryBlocks.DARK_STEEL_VENT_HATCH,
                TrainMurderMysteryBlocks.TARNISHED_GOLD_VENT_HATCH,
                TrainMurderMysteryBlocks.METAL_SHEET_WALKWAY,
                TrainMurderMysteryBlocks.STAINLESS_STEEL_LADDER,
                TrainMurderMysteryBlocks.COCKPIT_DOOR,
                TrainMurderMysteryBlocks.METAL_SHEET_DOOR,
                TrainMurderMysteryBlocks.GOLDEN_GLASS_PANEL,
                TrainMurderMysteryBlocks.CULLING_GLASS,
                TrainMurderMysteryBlocks.STAINLESS_STEEL_WALKWAY,
                TrainMurderMysteryBlocks.DARK_STEEL_WALKWAY,
                TrainMurderMysteryBlocks.PANEL_STRIPES,
                TrainMurderMysteryBlocks.TRIMMED_RAILING_POST,
                TrainMurderMysteryBlocks.DIAGONAL_TRIMMED_RAILING,
                TrainMurderMysteryBlocks.TRIMMED_RAILING,
                TrainMurderMysteryBlocks.TRIMMED_EBONY_STAIRS,
                TrainMurderMysteryBlocks.WHITE_LOUNGE_COUCH,
                TrainMurderMysteryBlocks.WHITE_OTTOMAN,
                TrainMurderMysteryBlocks.WHITE_TRIMMED_BED,
                TrainMurderMysteryBlocks.BLUE_LOUNGE_COUCH,
                TrainMurderMysteryBlocks.GREEN_LOUNGE_COUCH,
                TrainMurderMysteryBlocks.BAR_STOOL,
                TrainMurderMysteryBlocks.WALL_LAMP,
                TrainMurderMysteryBlocks.SMALL_BUTTON,
                TrainMurderMysteryBlocks.ELEVATOR_BUTTON,
                TrainMurderMysteryBlocks.STAINLESS_STEEL_SPRINKLER,
                TrainMurderMysteryBlocks.GOLD_SPRINKLER,
                TrainMurderMysteryBlocks.GOLD_ORNAMENT);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
                TrainMurderMysteryBlocks.RHOMBUS_GLASS,
                TrainMurderMysteryBlocks.PRIVACY_GLASS_PANEL,
                TrainMurderMysteryBlocks.CULLING_BLACK_HULL,
                TrainMurderMysteryBlocks.CULLING_WHITE_HULL,
                TrainMurderMysteryBlocks.HULL_GLASS,
                TrainMurderMysteryBlocks.RHOMBUS_HULL_GLASS);

        // Custom block models
        CustomModelProvider customModelProvider = new CustomModelProvider();
        ModelLoadingPlugin.register(customModelProvider);

        // Block Entity Renderers
        BlockEntityRendererFactories.register(
                TrainMurderMysteryBlockEntities.SMALL_GLASS_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(TrainMurderMystery.id("textures/entity/small_glass_door.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                TrainMurderMysteryBlockEntities.SMALL_WOOD_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(TrainMurderMystery.id("textures/entity/small_wood_door.png"), ctx)
        );

        // Ambience
        AmbienceUtil.registerBackgroundAmbience(new BackgroundAmbience(TrainMurderMysterySounds.AMBIENT_TRAIN_INSIDE, player -> isTrainMoving() && !isSkyVisibleAdjacent(player), 20));
        AmbienceUtil.registerBackgroundAmbience(new BackgroundAmbience(TrainMurderMysterySounds.AMBIENT_TRAIN_OUTSIDE, player -> isTrainMoving() && isSkyVisibleAdjacent(player), 20));

        // Caching components
        ClientTickEvents.START_WORLD_TICK.register(clientWorld -> {
            trainSpeed = TrainMurderMysteryComponents.TRAIN.get(clientWorld).getTrainSpeed();
            GAME_COMPONENT = TrainMurderMysteryComponents.GAME.get(clientWorld);
        });

        // Lock options
        OptionLocker.overrideOption("gamma", 0d);
        OptionLocker.overrideOption("renderDistance", 32);
        OptionLocker.overrideOption("showSubtitles", false);
        OptionLocker.overrideOption("autoJump", false);
        OptionLocker.overrideSoundCategoryVolume("music", 0.0);
        OptionLocker.overrideSoundCategoryVolume("record", 0.1);
        OptionLocker.overrideSoundCategoryVolume("weather", 1.0);
        OptionLocker.overrideSoundCategoryVolume("block", 1.0);
        OptionLocker.overrideSoundCategoryVolume("hostile", 1.0);
        OptionLocker.overrideSoundCategoryVolume("neutral", 1.0);
        OptionLocker.overrideSoundCategoryVolume("player", 1.0);
        OptionLocker.overrideSoundCategoryVolume("ambient", 1.0);
        OptionLocker.overrideSoundCategoryVolume("voice", 1.0);

        // Item tooltips
        TMMItemTooltips.addTooltips();

        // Cache player entries + select last slot at start of game
        ClientTickEvents.START_WORLD_TICK.register(clientWorld -> {
            for (AbstractClientPlayerEntity player : clientWorld.getPlayers()) {
                if (!PLAYER_ENTRIES_CACHE.containsKey(player.getUuid())) {
                    PLAYER_ENTRIES_CACHE.put(player.getUuid(), MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(player.getUuid()));
                }
            }

            if (!prevGameRunning && GAME_COMPONENT.isRunning()) {
                MinecraftClient.getInstance().player.getInventory().selectedSlot = 8;
            }
            prevGameRunning = GAME_COMPONENT.isRunning();
        });

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            TrainMurderMysteryClient.handParticleManager.tick();
        });

        // Instinct keybind
        instinctKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key."+TrainMurderMystery.MOD_ID+".instinct",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "category."+TrainMurderMystery.MOD_ID+".keybinds"
        ));
    }

    public static boolean isSkyVisibleAdjacent(ClientPlayerEntity player) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos playerPos = BlockPos.ofFloored(player.getEyePos());
        for (int x = -1; x <= 1; x+=2) {
            for (int z = -1; z <= 1; z+=2) {
                mutable.set(playerPos.getX() + x, playerPos.getY(), playerPos.getZ() + z);
                if (player.clientWorld.isSkyVisible(mutable)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isExposedToWind(ClientPlayerEntity player) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos playerPos = BlockPos.ofFloored(player.getEyePos());

        for (int x = 0; x <= 10; x++) {
            mutable.set(playerPos.getX() - x, player.getEyePos().getY(), playerPos.getZ());
            if (!player.clientWorld.isSkyVisible(mutable)) {
                return false;
            }
        }

        return true;
    }

    public static float getTrainSpeed() {
        return trainSpeed;
    }

    public static boolean isTrainMoving() {
        return trainSpeed > 0;
    }

    public static class CustomModelProvider implements ModelLoadingPlugin {

        private final Map<Identifier, UnbakedModel> modelIdToBlock = new Object2ObjectOpenHashMap<>();
        private final Set<Identifier> withInventoryVariant = new HashSet<>();

        public void register(Block block, UnbakedModel model) {
            this.register(Registries.BLOCK.getId(block), model);
        }

        public void register(Identifier id, UnbakedModel model) {
            this.modelIdToBlock.put(id, model);
        }

        public void markInventoryVariant(Block block) {
            this.markInventoryVariant(Registries.BLOCK.getId(block));
        }

        public void markInventoryVariant(Identifier id) {
            this.withInventoryVariant.add(id);
        }

        @Override
        public void onInitializeModelLoader(Context ctx) {
            ctx.modifyModelOnLoad().register((model, context) -> {
                ModelIdentifier topLevelId = context.topLevelId();
                if (topLevelId == null) {
                    return model;
                }
                Identifier id = topLevelId.id();
                if (topLevelId.getVariant().equals("inventory") && !this.withInventoryVariant.contains(id)) {
                    return model;
                }
                if (this.modelIdToBlock.containsKey(id)) {
                    return this.modelIdToBlock.get(id);
                }
                return model;
            });
        }
    }

    public static boolean isPlayerAliveAndInSurvival() {
        return TrainMurderMystery.isPlayerAliveAndSurvival(MinecraftClient.getInstance().player);
    }

    public static boolean isHitman() {
        return GAME_COMPONENT.getHitmen().contains(MinecraftClient.getInstance().player.getUuid());
    }

    public static boolean isDetective() {
        return GAME_COMPONENT.getDetectives().contains(MinecraftClient.getInstance().player.getUuid());
    }

    public static boolean isPassenger() {
        return !isHitman() && !isDetective();
    }

    public static List<UUID> getTargets() {
        return GAME_COMPONENT.getTargets();
    }

    public static boolean shouldInstinctHighlight(Entity entityToHighlight) {
        return isInstinctEnabled() && entityToHighlight instanceof PlayerEntity player && TrainMurderMystery.isPlayerAliveAndSurvival(player);
    }
    public static boolean isInstinctEnabled() {
        return TrainMurderMysteryClient.instinctKeybind.isPressed() && TrainMurderMysteryClient.isHitman() && TrainMurderMysteryClient.isPlayerAliveAndInSurvival();
    }
}
