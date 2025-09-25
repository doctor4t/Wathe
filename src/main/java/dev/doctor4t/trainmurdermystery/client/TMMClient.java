package dev.doctor4t.trainmurdermystery.client;

import com.google.common.collect.Maps;
import dev.doctor4t.ratatouille.client.util.OptionLocker;
import dev.doctor4t.ratatouille.client.util.ambience.AmbienceUtil;
import dev.doctor4t.ratatouille.client.util.ambience.BackgroundAmbience;
import dev.doctor4t.ratatouille.client.util.ambience.BlockEntityAmbience;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.block_entity.SprinklerBlockEntity;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.TMMComponents;
import dev.doctor4t.trainmurdermystery.cca.TrainWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.StoreRenderer;
import dev.doctor4t.trainmurdermystery.client.model.TMMModelLayers;
import dev.doctor4t.trainmurdermystery.client.render.block_entity.DrinkPlateBlockEntityRenderer;
import dev.doctor4t.trainmurdermystery.client.render.block_entity.PlateBlockEntityRenderer;
import dev.doctor4t.trainmurdermystery.client.render.block_entity.SmallDoorBlockEntityRenderer;
import dev.doctor4t.trainmurdermystery.client.render.block_entity.WheelBlockEntityRenderer;
import dev.doctor4t.trainmurdermystery.client.render.entity.FirecrackerEntityRenderer;
import dev.doctor4t.trainmurdermystery.client.util.TMMItemTooltips;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.*;
import dev.doctor4t.trainmurdermystery.util.HandParticleManager;
import dev.doctor4t.trainmurdermystery.util.PoisonUtils;
import dev.doctor4t.trainmurdermystery.util.ShootMuzzleS2CPayload;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class TMMClient implements ClientModInitializer {
    private static float soundLevel = 0f;
    public static HandParticleManager handParticleManager;
    public static Map<PlayerEntity, Vec3d> particleMap;
    private static boolean prevGameRunning;
    public static GameWorldComponent gameComponent;
    public static TrainWorldComponent trainComponent;

    public static final Map<UUID, PlayerListEntry> PLAYER_ENTRIES_CACHE = Maps.newHashMap();

    public static KeyBinding instinctKeybind;

    public static boolean shouldDisableHudAndDebug() {
        var client = MinecraftClient.getInstance();
        return (client == null || (client.player != null && !client.player.isCreative() && !client.player.isSpectator()));
    }

    @Override
    public void onInitializeClient() {
        // Initialize ScreenParticle
        handParticleManager = new HandParticleManager();
        particleMap = new HashMap<>();

        // Register particle factories
        TMMParticles.registerFactories();

        // Entity renderer registration
        EntityRendererRegistry.register(TMMEntities.SEAT, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(TMMEntities.FIRECRACKER, FirecrackerEntityRenderer::new);
        EntityRendererRegistry.register(TMMEntities.GRENADE, FlyingItemEntityRenderer::new);

        // Register entity model layers
        TMMModelLayers.initialize();

        // Block render layers
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                TMMBlocks.STAINLESS_STEEL_VENT_HATCH,
                TMMBlocks.DARK_STEEL_VENT_HATCH,
                TMMBlocks.TARNISHED_GOLD_VENT_HATCH,
                TMMBlocks.METAL_SHEET_WALKWAY,
                TMMBlocks.STAINLESS_STEEL_LADDER,
                TMMBlocks.COCKPIT_DOOR,
                TMMBlocks.METAL_SHEET_DOOR,
                TMMBlocks.GOLDEN_GLASS_PANEL,
                TMMBlocks.CULLING_GLASS,
                TMMBlocks.STAINLESS_STEEL_WALKWAY,
                TMMBlocks.DARK_STEEL_WALKWAY,
                TMMBlocks.PANEL_STRIPES,
                TMMBlocks.RAIL_BEAM,
                TMMBlocks.TRIMMED_RAILING_POST,
                TMMBlocks.DIAGONAL_TRIMMED_RAILING,
                TMMBlocks.TRIMMED_RAILING,
                TMMBlocks.TRIMMED_EBONY_STAIRS,
                TMMBlocks.WHITE_LOUNGE_COUCH,
                TMMBlocks.WHITE_OTTOMAN,
                TMMBlocks.WHITE_TRIMMED_BED,
                TMMBlocks.BLUE_LOUNGE_COUCH,
                TMMBlocks.GREEN_LOUNGE_COUCH,
                TMMBlocks.BAR_STOOL,
                TMMBlocks.WALL_LAMP,
                TMMBlocks.SMALL_BUTTON,
                TMMBlocks.ELEVATOR_BUTTON,
                TMMBlocks.STAINLESS_STEEL_SPRINKLER,
                TMMBlocks.GOLD_SPRINKLER,
                TMMBlocks.GOLD_ORNAMENT,
                TMMBlocks.WHEEL,
                TMMBlocks.BARRIER_PANEL,
                TMMBlocks.FOOD_PLATTER,
                TMMBlocks.DRINK_TRAY
        );
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
                TMMBlocks.RHOMBUS_GLASS,
                TMMBlocks.PRIVACY_GLASS_PANEL,
                TMMBlocks.CULLING_BLACK_HULL,
                TMMBlocks.CULLING_WHITE_HULL,
                TMMBlocks.HULL_GLASS,
                TMMBlocks.RHOMBUS_HULL_GLASS
        );

        // Custom block models
        CustomModelProvider customModelProvider = new CustomModelProvider();
        ModelLoadingPlugin.register(customModelProvider);

        // Block Entity Renderers
        BlockEntityRendererFactories.register(
                TMMBlockEntities.SMALL_GLASS_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(TMM.id("textures/entity/small_glass_door.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                TMMBlockEntities.SMALL_WOOD_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(TMM.id("textures/entity/small_wood_door.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                TMMBlockEntities.SMALL_TRAIN_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(TMM.id("textures/entity/small_train_door.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                TMMBlockEntities.WHEEL,
                ctx -> new WheelBlockEntityRenderer(TMM.id("textures/entity/wheel.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                TMMBlockEntities.PLATE,
                PlateBlockEntityRenderer::new
        );
        BlockEntityRendererFactories.register(
                TMMBlockEntities.DRINK_PLATE,
                DrinkPlateBlockEntityRenderer::new
        );

        // Ambience
        AmbienceUtil.registerBackgroundAmbience(new BackgroundAmbience(TMMSounds.AMBIENT_TRAIN_INSIDE, player -> isTrainMoving() && !TMM.isSkyVisibleAdjacent(player), 20));
        AmbienceUtil.registerBackgroundAmbience(new BackgroundAmbience(TMMSounds.AMBIENT_TRAIN_OUTSIDE, player -> isTrainMoving() && TMM.isSkyVisibleAdjacent(player), 20));
        AmbienceUtil.registerBackgroundAmbience(new BackgroundAmbience(TMMSounds.AMBIENT_PSYCHO_DRONE, player -> gameComponent.isPsychoActive(), 20));
        AmbienceUtil.registerBlockEntityAmbience(TMMBlockEntities.SPRINKLER, new BlockEntityAmbience(TMMSounds.BLOCK_SPRINKLER_RUN, 0.5f, blockEntity -> blockEntity instanceof SprinklerBlockEntity sprinklerBlockEntity && sprinklerBlockEntity.isPowered(), 10));

        // Caching components
        ClientTickEvents.START_WORLD_TICK.register(clientWorld -> {
            gameComponent = TMMComponents.GAME.get(clientWorld);
            trainComponent = TMMComponents.TRAIN.get(clientWorld);
        });

        // Lock options
        OptionLocker.overrideOption("gamma", 0d);
        OptionLocker.overrideOption("renderDistance", 32); // mfw 15 fps on a 3050 - Cup // haha 🫵 brokie - RAT // buy me a better one then - Cup // okay nvm I fixed it I was actually rendering a lot of empty chunks we didn't need my bad LMAO - RAT
        OptionLocker.overrideOption("showSubtitles", false);
        OptionLocker.overrideOption("autoJump", false);
        OptionLocker.overrideOption("renderClouds", CloudRenderMode.OFF);
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

        ClientTickEvents.START_WORLD_TICK.register(clientWorld -> {
            // Cache player entries
            for (AbstractClientPlayerEntity player : clientWorld.getPlayers()) {
                if (!PLAYER_ENTRIES_CACHE.containsKey(player.getUuid())) {
                    PLAYER_ENTRIES_CACHE.put(player.getUuid(), MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(player.getUuid()));
                }
            }
            prevGameRunning = gameComponent.isRunning();

            // Fade sound with game start / stop fade
            GameWorldComponent component = TMMComponents.GAME.get(clientWorld);
            if (component.getFade() > 0) {
                MinecraftClient.getInstance().getSoundManager().updateSoundVolume(SoundCategory.MASTER, MathHelper.map(component.getFade(), 0, GameConstants.FADE_TIME, soundLevel, 0));
            } else {
                MinecraftClient.getInstance().getSoundManager().updateSoundVolume(SoundCategory.MASTER, soundLevel);
                soundLevel = MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER);
            }

            var player = MinecraftClient.getInstance().player;
            if (player != null) StoreRenderer.tick();
        });

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            TMMClient.handParticleManager.tick();
        });

        ClientPlayNetworking.registerGlobalReceiver(ShootMuzzleS2CPayload.ID, new ShootMuzzleS2CPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(PoisonUtils.PoisonOverlayPayload.ID, new PoisonUtils.PoisonOverlayPayload.Receiver());

        // Instinct keybind
        instinctKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key."+ TMM.MOD_ID+".instinct",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "category."+ TMM.MOD_ID+".keybinds"
        ));
    }

    public static float getTrainSpeed() {
        return trainComponent.getTrainSpeed();
    }

    public static boolean isTrainMoving() {
        return trainComponent != null && trainComponent.getTrainSpeed() > 0;
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
        return GameFunctions.isPlayerAliveAndSurvival(MinecraftClient.getInstance().player);
    }

    public static boolean isHitman() {
        return gameComponent.getHitmen().contains(MinecraftClient.getInstance().player.getUuid());
    }

    public static boolean shouldInstinctHighlight(Entity entityToHighlight) {
        return isInstinctEnabled() && entityToHighlight instanceof PlayerEntity player && GameFunctions.isPlayerAliveAndSurvival(player);
    }
    public static boolean isInstinctEnabled() {
        return TMMClient.instinctKeybind.isPressed() && TMMClient.isHitman() && TMMClient.isPlayerAliveAndInSurvival();
    }
}
