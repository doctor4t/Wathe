package dev.doctor4t.wathe.client;

import com.google.common.collect.Maps;
import dev.doctor4t.ratatouille.client.util.OptionLocker;
import dev.doctor4t.ratatouille.client.util.ambience.AmbienceUtil;
import dev.doctor4t.ratatouille.client.util.ambience.BackgroundAmbience;
import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.WatheConfig;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import dev.doctor4t.wathe.cca.TrainWorldComponent;
import dev.doctor4t.wathe.client.gui.RoundTextRenderer;
import dev.doctor4t.wathe.client.gui.StoreRenderer;
import dev.doctor4t.wathe.client.gui.TimeRenderer;
import dev.doctor4t.wathe.client.model.WatheModelLayers;
import dev.doctor4t.wathe.client.model.item.KnifeModelLoadingPlugin;
import dev.doctor4t.wathe.client.render.block_entity.PlateBlockEntityRenderer;
import dev.doctor4t.wathe.client.render.block_entity.SmallDoorBlockEntityRenderer;
import dev.doctor4t.wathe.client.render.block_entity.WheelBlockEntityRenderer;
import dev.doctor4t.wathe.client.render.entity.FirecrackerEntityRenderer;
import dev.doctor4t.wathe.client.render.entity.HornBlockEntityRenderer;
import dev.doctor4t.wathe.client.render.entity.NoteEntityRenderer;
import dev.doctor4t.wathe.client.util.WatheItemTooltips;
import dev.doctor4t.wathe.entity.FirecrackerEntity;
import dev.doctor4t.wathe.entity.NoteEntity;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.index.*;
import dev.doctor4t.wathe.util.*;
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
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
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
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class WatheClient implements ClientModInitializer {
    private static float soundLevel = 0f;
    public static HandParticleManager handParticleManager;
    public static Map<PlayerEntity, Vec3d> particleMap;
    private static boolean prevGameRunning;
    public static GameWorldComponent gameComponent;
    public static TrainWorldComponent trainComponent;
    public static PlayerMoodComponent moodComponent;

    public static final Map<UUID, PlayerListEntry> PLAYER_ENTRIES_CACHE = Maps.newHashMap();

    public static KeyBinding instinctKeybind;
    public static float prevInstinctLightLevel = -.04f;
    public static float instinctLightLevel = -.04f;

    public static boolean shouldDisableHudAndDebug() {
        MinecraftClient client = MinecraftClient.getInstance();
        return (client == null || (client.player != null && !client.player.isCreative() && !client.player.isSpectator()));
    }

    @Override
    public void onInitializeClient() {
        // Load config
        WatheConfig.init(Wathe.MOD_ID, WatheConfig.class);

        // Initialize ScreenParticle
        handParticleManager = new HandParticleManager();
        particleMap = new HashMap<>();

        // Register particle factories
        WatheParticles.registerFactories();

        // Entity renderer registration
        EntityRendererRegistry.register(WatheEntities.SEAT, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(WatheEntities.FIRECRACKER, FirecrackerEntityRenderer::new);
        EntityRendererRegistry.register(WatheEntities.GRENADE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(WatheEntities.NOTE, NoteEntityRenderer::new);

        // Register entity model layers
        WatheModelLayers.initialize();

        // Custom Baked Models
        ModelLoadingPlugin.register(new KnifeModelLoadingPlugin());

        // Block render layers
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                WatheBlocks.STAINLESS_STEEL_VENT_HATCH,
                WatheBlocks.DARK_STEEL_VENT_HATCH,
                WatheBlocks.TARNISHED_GOLD_VENT_HATCH,
                WatheBlocks.METAL_SHEET_WALKWAY,
                WatheBlocks.STAINLESS_STEEL_LADDER,
                WatheBlocks.COCKPIT_DOOR,
                WatheBlocks.METAL_SHEET_DOOR,
                WatheBlocks.GOLDEN_GLASS_PANEL,
                WatheBlocks.CULLING_GLASS,
                WatheBlocks.STAINLESS_STEEL_WALKWAY,
                WatheBlocks.DARK_STEEL_WALKWAY,
                WatheBlocks.PANEL_STRIPES,
                WatheBlocks.RAIL_BEAM,
                WatheBlocks.TRIMMED_RAILING_POST,
                WatheBlocks.DIAGONAL_TRIMMED_RAILING,
                WatheBlocks.TRIMMED_RAILING,
                WatheBlocks.TRIMMED_EBONY_STAIRS,
                WatheBlocks.WHITE_LOUNGE_COUCH,
                WatheBlocks.WHITE_OTTOMAN,
                WatheBlocks.WHITE_TRIMMED_BED,
                WatheBlocks.BLUE_LOUNGE_COUCH,
                WatheBlocks.GREEN_LOUNGE_COUCH,
                WatheBlocks.BAR_STOOL,
                WatheBlocks.WALL_LAMP,
                WatheBlocks.SMALL_BUTTON,
                WatheBlocks.ELEVATOR_BUTTON,
                WatheBlocks.STAINLESS_STEEL_SPRINKLER,
                WatheBlocks.GOLD_SPRINKLER,
                WatheBlocks.GOLD_ORNAMENT,
                WatheBlocks.WHEEL,
                WatheBlocks.RUSTED_WHEEL,
                WatheBlocks.BARRIER_PANEL,
                WatheBlocks.FOOD_PLATTER,
                WatheBlocks.DRINK_TRAY,
                WatheBlocks.LIGHT_BARRIER,
                WatheBlocks.HORN
        );
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
                WatheBlocks.RHOMBUS_GLASS,
                WatheBlocks.PRIVACY_GLASS_PANEL,
                WatheBlocks.CULLING_BLACK_HULL,
                WatheBlocks.CULLING_WHITE_HULL,
                WatheBlocks.HULL_GLASS,
                WatheBlocks.RHOMBUS_HULL_GLASS
        );

        // Custom block models
        CustomModelProvider customModelProvider = new CustomModelProvider();
        ModelLoadingPlugin.register(customModelProvider);

        // Block Entity Renderers
        BlockEntityRendererFactories.register(
                WatheBlockEntities.SMALL_GLASS_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(Wathe.id("textures/entity/small_glass_door.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                WatheBlockEntities.SMALL_WOOD_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(Wathe.id("textures/entity/small_wood_door.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                WatheBlockEntities.ANTHRACITE_STEEL_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(Wathe.id("textures/entity/anthracite_steel_door.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                WatheBlockEntities.KHAKI_STEEL_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(Wathe.id("textures/entity/khaki_steel_door.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                WatheBlockEntities.MAROON_STEEL_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(Wathe.id("textures/entity/maroon_steel_door.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                WatheBlockEntities.MUNTZ_STEEL_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(Wathe.id("textures/entity/muntz_steel_door.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                WatheBlockEntities.NAVY_STEEL_DOOR,
                ctx -> new SmallDoorBlockEntityRenderer(Wathe.id("textures/entity/navy_steel_door.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                WatheBlockEntities.WHEEL,
                ctx -> new WheelBlockEntityRenderer(Wathe.id("textures/entity/wheel.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                WatheBlockEntities.RUSTED_WHEEL,
                ctx -> new WheelBlockEntityRenderer(Wathe.id("textures/entity/rusted_wheel.png"), ctx)
        );
        BlockEntityRendererFactories.register(
                WatheBlockEntities.BEVERAGE_PLATE,
                PlateBlockEntityRenderer::new
        );
        BlockEntityRendererFactories.register(WatheBlockEntities.HORN, HornBlockEntityRenderer::new);

        // Ambience
        AmbienceUtil.registerBackgroundAmbience(new BackgroundAmbience(WatheSounds.AMBIENT_TRAIN_INSIDE, player -> isTrainMoving() && !Wathe.isSkyVisibleAdjacent(player), 20));
        AmbienceUtil.registerBackgroundAmbience(new BackgroundAmbience(WatheSounds.AMBIENT_TRAIN_OUTSIDE, player -> isTrainMoving() && Wathe.isSkyVisibleAdjacent(player), 20));
        AmbienceUtil.registerBackgroundAmbience(new BackgroundAmbience(WatheSounds.AMBIENT_PSYCHO_DRONE, player -> gameComponent.isPsychoActive(), 20));
//        AmbienceUtil.registerBlockEntityAmbience(WatheBlockEntities.SPRINKLER, new BlockEntityAmbience(WatheSounds.BLOCK_SPRINKLER_RUN, 0.5f, blockEntity -> blockEntity instanceof SprinklerBlockEntity sprinklerBlockEntity && sprinklerBlockEntity.isPowered(), 20));

        // Caching components
        ClientTickEvents.START_WORLD_TICK.register(clientWorld -> {
            gameComponent = GameWorldComponent.KEY.get(clientWorld);
            trainComponent = TrainWorldComponent.KEY.get(clientWorld);
            moodComponent = PlayerMoodComponent.KEY.get(MinecraftClient.getInstance().player);
        });

        // Lock options
        OptionLocker.overrideOption("gamma", 0d);
        OptionLocker.overrideOption("renderDistance", getLockedRenderDistance(WatheConfig.ultraPerfMode)); // mfw 15 fps on a 3050 - Cup // haha 🫵 brokie - RAT // buy me a better one then - Cup // okay nvm I fixed it I was actually rendering a lot of empty chunks we didn't need my bad LMAO - RAT
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
        WatheItemTooltips.addTooltips();

        ClientTickEvents.START_WORLD_TICK.register(clientWorld -> {
            prevInstinctLightLevel = instinctLightLevel;
            // instinct night vision
            if (WatheClient.isInstinctEnabled()) {
                instinctLightLevel += .1f;
            } else {
                instinctLightLevel -= .1f;
            }
            instinctLightLevel = MathHelper.clamp(instinctLightLevel, -.04f, .5f);

            // Cache player entries
            for (AbstractClientPlayerEntity player : clientWorld.getPlayers()) {
                ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
                if (networkHandler != null) {
                    PLAYER_ENTRIES_CACHE.put(player.getUuid(), networkHandler.getPlayerListEntry(player.getUuid()));
                }
            }
            if (!prevGameRunning && gameComponent.isRunning()) {
                MinecraftClient.getInstance().player.getInventory().selectedSlot = 8;
            }
            prevGameRunning = gameComponent.isRunning();

            // Fade sound with game start / stop fade
            GameWorldComponent component = GameWorldComponent.KEY.get(clientWorld);
            if (component.getFade() > 0) {
                MinecraftClient.getInstance().getSoundManager().updateSoundVolume(SoundCategory.MASTER, MathHelper.map(component.getFade(), 0, GameConstants.FADE_TIME, soundLevel, 0));
            } else {
                MinecraftClient.getInstance().getSoundManager().updateSoundVolume(SoundCategory.MASTER, soundLevel);
                soundLevel = MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER);
            }

            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                StoreRenderer.tick();
                TimeRenderer.tick();
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            WatheClient.handParticleManager.tick();
            RoundTextRenderer.tick();
        });

        ClientPlayNetworking.registerGlobalReceiver(ShootMuzzleS2CPayload.ID, new ShootMuzzleS2CPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(PoisonUtils.PoisonOverlayPayload.ID, new PoisonUtils.PoisonOverlayPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(GunDropPayload.ID, new GunDropPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(AnnounceWelcomePayload.ID, new AnnounceWelcomePayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(AnnounceEndingPayload.ID, new AnnounceEndingPayload.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(TaskCompletePayload.ID, new TaskCompletePayload.Receiver());

        // Instinct keybind
        instinctKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + Wathe.MOD_ID + ".instinct",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "category." + Wathe.MOD_ID + ".keybinds"
        ));
    }

    public static TrainWorldComponent getTrainComponent() {
        return trainComponent;
    }

    public static float getTrainSpeed() {
        return trainComponent.getSpeed();
    }

    public static boolean isTrainMoving() {
        return trainComponent != null && trainComponent.getSpeed() > 0;
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

    public static boolean isPlayerSpectatingOrCreative() {
        return GameFunctions.isPlayerSpectatingOrCreative(MinecraftClient.getInstance().player);
    }

    public static boolean isKiller() {
        return gameComponent != null && gameComponent.canUseKillerFeatures(MinecraftClient.getInstance().player);
    }

    public static int getInstinctHighlight(Entity target) {
        if (!isInstinctEnabled()) return -1;
//        if (target instanceof PlayerBodyEntity) return 0x606060;
        if (target instanceof ItemEntity || target instanceof NoteEntity || target instanceof FirecrackerEntity)
            return 0xDB9D00;
        if (target instanceof PlayerEntity player) {
            if (GameFunctions.isPlayerSpectatingOrCreative(player)) return -1;
            if (gameComponent.canUseKillerFeatures(player)) return MathHelper.hsvToRgb(0F, 1.0F, 0.6F);
            if (gameComponent.isInnocent(player)) {
                float mood = PlayerMoodComponent.KEY.get(target).getMood();
                if (mood < GameConstants.DEPRESSIVE_MOOD_THRESHOLD) {
                    return 0x171DC6;
                } else if (mood < GameConstants.MID_MOOD_THRESHOLD) {
                    return 0x1FAFAF;
                } else {
                    return 0x4EDD35;
                }
            }
            if (isPlayerSpectatingOrCreative()) return 0xFFFFFF;
        }
        return -1;
    }

    public static boolean isInstinctEnabled() {
        return instinctKeybind.isPressed() && ((isKiller() && isPlayerAliveAndInSurvival()) || isPlayerSpectatingOrCreative());
    }

    public static int getLockedRenderDistance(boolean ultraPerfMode) {
        return ultraPerfMode ? 2 : 32;
    }
}
