package dev.doctor4t.wathe.datagen;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.block.*;
import dev.doctor4t.wathe.block.property.CouchArms;
import dev.doctor4t.wathe.index.WatheBlocks;
import dev.doctor4t.wathe.index.WatheItems;
import dev.doctor4t.wathe.item.KnifeItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.data.client.*;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class WatheModelGen extends FabricModelProvider {

    protected static final Model THICK_BAR = new Model(
            Optional.of(Wathe.id("block/template_thick_bar")),
            Optional.empty(),
            TextureKey.TEXTURE);
    protected static final Model THICK_BAR_TOP = new Model(
            Optional.of(Wathe.id("block/template_thick_bar_top")),
            Optional.of("_top"),
            TextureKey.TEXTURE);
    protected static final Model THICK_BAR_BOTTOM = new Model(
            Optional.of(Wathe.id("block/template_thick_bar_bottom")),
            Optional.of("_bottom"),
            TextureKey.TEXTURE);
    private static final TextureKey SPYGLASS_KEY = TextureKey.of("spyglass");
    private static final TextureKey SHELF_KEY = TextureKey.of("shelf");
    private static final Model BRANCH_FACE = template(
            "block/template_branch_face", "_face", TextureKey.SIDE
    );
    private static final Model BRANCH_FACE_HORIZONTAL = template(
            "block/template_branch_face_horizontal", "_face_horizontal", TextureKey.SIDE
    );
    private static final Model BRANCH_FRONT = template(
            "block/template_branch_front", "_front", TextureKey.TOP, TextureKey.SIDE
    );
    private static final Model BRANCH_BACK = template(
            "block/template_branch_back", "_back", TextureKey.TOP, TextureKey.SIDE
    );
    private static final Model BRANCH_INVENTORY = template(
            "block/template_branch_inventory", "_inventory", TextureKey.TOP, TextureKey.SIDE
    );
    private static final Model VENT_SHAFT_SIDE = template(
            "block/template_vent_shaft_side", TextureKey.SIDE, TextureKey.INSIDE
    );
    private static final Model VENT_SHAFT_SIDE_VERTICAL = template(
            "block/template_vent_shaft_side_vertical", "_side_vertical", TextureKey.SIDE, TextureKey.INSIDE
    );
    private static final Model VENT_SHAFT_SIDE_OPENING = template(
            "block/template_vent_shaft_side_opening", "_side_opening", TextureKey.SIDE
    );
    private static final Model VENT_SHAFT_INVENTORY = template(
            "block/template_vent_shaft_inventory", "_inventory", TextureKey.SIDE, TextureKey.END, TextureKey.INSIDE
    );
    private static final Model WALKWAY_TOP = template(
            "block/template_walkway_top", "_top", TextureKey.SIDE, TextureKey.TOP
    );
    private static final Model WALKWAY_BOTTOM = template(
            "block/template_walkway_bottom", "_bottom", TextureKey.SIDE, TextureKey.TOP
    );
    private static final Model LOUNGE_COUCH_LEFT = template(
            "block/template_lounge_couch_left", "_left", TextureKey.TEXTURE
    );
    private static final Model LOUNGE_COUCH_RIGHT = template(
            "block/template_lounge_couch_right", "_right", TextureKey.TEXTURE
    );
    private static final Model LOUNGE_COUCH_SINGLE = template(
            "block/template_lounge_couch_single", "_single", TextureKey.TEXTURE
    );
    private static final Model LOUNGE_COUCH_NO_ARMS = template(
            "block/template_lounge_couch_no_arms", "_no_arms", TextureKey.TEXTURE
    );
    private static final Model TRIMMED_STAIR_SUPPORT = template(
            "block/template_trimmed_stair_support", "_support", TextureKey.TEXTURE
    );
    private static final Model TRIMMED_STAIRS = template(
            "block/template_trimmed_stairs", TextureKey.TEXTURE
    );
    private static final Model PANEL = template(
            "block/template_panel", TextureKey.ALL
    );
    private static final Model LADDER = template(
            Identifier.ofVanilla("block/ladder"), TextureKey.TEXTURE, TextureKey.PARTICLE
    );
    private static final Model TRIMMED_LANTERN_FLOOR = template(
            "block/template_trimmed_lantern_floor", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE
    );
    private static final Model TRIMMED_LANTERN_CEILING = template(
            "block/template_trimmed_lantern_ceiling", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE
    );
    private static final Model TRIMMED_LANTERN_WALL = template(
            "block/template_trimmed_lantern_wall", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE
    );
    private static final Model WALL_LAMP_FLOOR = template(
            "block/template_wall_lamp_floor", TextureKey.TEXTURE
    );
    private static final Model WALL_LAMP_CEILING = template(
            "block/template_wall_lamp_ceiling", TextureKey.TEXTURE
    );
    private static final Model WALL_LAMP_WALL = template(
            "block/template_wall_lamp_wall", TextureKey.TEXTURE
    );
    private static final Model CARGO_BOX = template(
            "block/template_cargo_box", TextureKey.TOP, TextureKey.SIDE, TextureKey.BOTTOM
    );
    private static final Model CARGO_BOX_OPEN = template(
            "block/template_cargo_box_open", "_open", TextureKey.TOP, TextureKey.SIDE, TextureKey.BOTTOM
    );
    private static final Model CABINET_OPEN = template(
            "block/template_cabinet_open", "_open", TextureKey.FRONT, TextureKey.SIDE, TextureKey.WALL, SHELF_KEY
    );
    private static final Model GLASS_PANEL = template(
            "block/template_glass_panel", TextureKey.PANE, TextureKey.EDGE
    );
    private static final Model LEATHER_COUCH_LEFT = template(
            "block/template_leather_couch_left", "_left", TextureKey.TEXTURE
    );
    private static final Model LEATHER_COUCH_RIGHT = template(
            "block/template_leather_couch_right", "_right", TextureKey.TEXTURE
    );
    private static final Model LEATHER_COUCH = template(
            "block/template_leather_couch", TextureKey.TEXTURE
    );
    private static final Model LEATHER_COUCH_MIDDLE = template(
            "block/template_leather_couch_middle", "_middle", TextureKey.TEXTURE
    );
    private static final Model ORNAMENT_R0 = template(
            "block/template_ornament_r0", TextureKey.TEXTURE
    );
    private static final Model ORNAMENT_R90 = template(
            "block/template_ornament_r90", TextureKey.TEXTURE
    );
    private static final Model ORNAMENT_R180 = template(
            "block/template_ornament_r180", TextureKey.TEXTURE
    );
    private static final Model ORNAMENT_R270 = template(
            "block/template_ornament_r270", TextureKey.TEXTURE
    );
    private static final Model LEDGE = template(
            "block/template_ledge", TextureKey.TEXTURE
    );
    private static final Model BAR = template(
            "block/template_bar", TextureKey.TEXTURE
    );
    private static final Model BAR_TOP = template(
            "block/template_bar_top", "_top", TextureKey.TEXTURE
    );
    private static final Model BAR_BOTTOM = template(
            "block/template_bar_bottom", "_bottom", TextureKey.TEXTURE
    );
    private static final Model TRIMMED_BED_FOOT = template(
            "block/template_trimmed_bed_foot", "_foot", TextureKey.TEXTURE
    );
    private static final Model TRIMMED_BED_HEAD = template(
            "block/template_trimmed_bed_head", "_head", TextureKey.TEXTURE
    );
    private static final Model TRIMMED_BED_INVENTORY = template(
            "block/template_trimmed_bed_inventory", "_inventory", TextureKey.TEXTURE
    );
    private static final Model SPRINKLER = template(
            "block/template_sprinkler", TextureKey.TEXTURE, TextureKey.PARTICLE
    );
    private static final Model VENT_HATCH = template(
            "block/template_vent_hatch", TextureKey.TEXTURE
    );
    private static final Model VENT_HATCH_OPEN = template(
            "block/template_vent_hatch_open", "_open", TextureKey.TEXTURE
    );


    private final Map<Block, TexturedModel> uniqueModels = ImmutableMap.<Block, TexturedModel>builder()
            .build();

    public WatheModelGen(FabricDataOutput output) {
        super(output);
    }

    private static Model template(Identifier parent, @Nullable String variant, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(parent), Optional.ofNullable(variant), requiredTextureKeys);
    }

    private static Model template(Identifier parent, TextureKey... requiredTextureKeys) {
        return template(parent, null, requiredTextureKeys);
    }

    private static Model template(String parentName, @Nullable String variant, TextureKey... requiredTextureKeys) {
        return template(Wathe.id(parentName), variant, requiredTextureKeys);
    }

    private static Model template(String parentName, TextureKey... requiredTextureKeys) {
        return template(Wathe.id(parentName), requiredTextureKeys);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        this.registerVentShaft(generator, WatheBlocks.STAINLESS_STEEL_VENT_SHAFT);
        this.registerVentHatch(generator, WatheBlocks.STAINLESS_STEEL_VENT_HATCH);
        this.registerVentShaft(generator, WatheBlocks.DARK_STEEL_VENT_SHAFT);
        this.registerVentHatch(generator, WatheBlocks.DARK_STEEL_VENT_HATCH);
        this.registerVentShaft(generator, WatheBlocks.TARNISHED_GOLD_VENT_SHAFT);
        this.registerVentHatch(generator, WatheBlocks.TARNISHED_GOLD_VENT_HATCH);
        this.registerFamily(generator, WatheBlocks.Family.TARNISHED_GOLD);
        this.registerFamily(generator, WatheBlocks.Family.GOLD);
        this.registerFamily(generator, WatheBlocks.Family.PRISTINE_GOLD);
        generator.registerAxisRotated(WatheBlocks.TARNISHED_GOLD_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        generator.registerAxisRotated(WatheBlocks.GOLD_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        generator.registerAxisRotated(WatheBlocks.PRISTINE_GOLD_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        this.registerFamily(generator, WatheBlocks.Family.METAL_SHEET);
        generator.registerDoor(WatheBlocks.COCKPIT_DOOR);
        this.registerWalkway(generator, WatheBlocks.METAL_SHEET_WALKWAY);
        this.registerLadder(generator, WatheBlocks.STAINLESS_STEEL_LADDER);
        this.registerFamily(generator, WatheBlocks.Family.STAINLESS_STEEL);
        this.registerWalkway(generator, WatheBlocks.STAINLESS_STEEL_WALKWAY);
        this.registerBranch(generator, WatheBlocks.STAINLESS_STEEL_BRANCH, WatheBlocks.STAINLESS_STEEL);
        generator.registerAxisRotated(WatheBlocks.STAINLESS_STEEL_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        this.registerFamily(generator, WatheBlocks.Family.DARK_STEEL);
        this.registerWalkway(generator, WatheBlocks.DARK_STEEL_WALKWAY);
        this.registerBranch(generator, WatheBlocks.DARK_STEEL_BRANCH, WatheBlocks.DARK_STEEL);
        generator.registerAxisRotated(WatheBlocks.DARK_STEEL_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
        generator.registerSimpleCubeAll(WatheBlocks.RHOMBUS_GLASS);
        this.registerGlassPanel(generator, WatheBlocks.GOLDEN_GLASS_PANEL);
        this.registerCullingGlass(generator);
        this.registerFamily(generator, WatheBlocks.Family.MARBLE);
        this.registerFamily(generator, WatheBlocks.Family.MARBLE_TILE);
        this.registerFamily(generator, WatheBlocks.Family.DARK_MARBLE);
        generator.registerSouthDefaultHorizontalFacing(TexturedModel.TEMPLATE_GLAZED_TERRACOTTA, WatheBlocks.MARBLE_MOSAIC);
        this.registerFamily(generator, WatheBlocks.Family.WHITE_HULL);
        this.registerCulledBlock(generator, WatheBlocks.CULLING_WHITE_HULL, WatheBlocks.WHITE_HULL);
        this.registerFamily(generator, WatheBlocks.Family.BLACK_HULL);
        this.registerCulledBlock(generator, WatheBlocks.CULLING_BLACK_HULL, WatheBlocks.BLACK_HULL);
        this.registerFamily(generator, WatheBlocks.Family.BLACK_HULL_SHEET);
        this.registerFamily(generator, WatheBlocks.Family.MAHOGANY);
        this.registerFamily(generator, WatheBlocks.Family.MAHOGANY_HERRINGBONE);
        this.registerFamily(generator, WatheBlocks.Family.SMOOTH_MAHOGANY);
        this.registerPanel(generator, WatheBlocks.MAHOGANY_PANEL, WatheBlocks.SMOOTH_MAHOGANY);
        this.registerCabinet(generator, WatheBlocks.MAHOGANY_CABINET);
        this.registerVariedBookshelf(generator, WatheBlocks.MAHOGANY_BOOKSHELF, WatheBlocks.MAHOGANY_PLANKS);
        this.registerFamily(generator, WatheBlocks.Family.BUBINGA);
        this.registerFamily(generator, WatheBlocks.Family.BUBINGA_HERRINGBONE);
        this.registerFamily(generator, WatheBlocks.Family.SMOOTH_BUBINGA);
        this.registerPanel(generator, WatheBlocks.BUBINGA_PANEL, WatheBlocks.SMOOTH_BUBINGA);
        this.registerCabinet(generator, WatheBlocks.BUBINGA_CABINET);
        this.registerVariedBookshelf(generator, WatheBlocks.BUBINGA_BOOKSHELF, WatheBlocks.BUBINGA_PLANKS);
        this.registerFamily(generator, WatheBlocks.Family.EBONY);
        this.registerFamily(generator, WatheBlocks.Family.EBONY_HERRINGBONE);
        this.registerFamily(generator, WatheBlocks.Family.SMOOTH_EBONY);
        this.registerPanel(generator, WatheBlocks.EBONY_PANEL, WatheBlocks.SMOOTH_EBONY);
        this.registerCabinet(generator, WatheBlocks.EBONY_CABINET);
        this.registerTrimmedStairs(generator, WatheBlocks.TRIMMED_EBONY_STAIRS);
        this.registerVariedBookshelf(generator, WatheBlocks.EBONY_BOOKSHELF, WatheBlocks.EBONY_PLANKS);
        this.registerBranch(generator, WatheBlocks.OAK_BRANCH, Blocks.OAK_LOG);
        this.registerBranch(generator, WatheBlocks.SPRUCE_BRANCH, Blocks.SPRUCE_LOG);
        this.registerBranch(generator, WatheBlocks.BIRCH_BRANCH, Blocks.BIRCH_LOG);
        this.registerBranch(generator, WatheBlocks.JUNGLE_BRANCH, Blocks.JUNGLE_LOG);
        this.registerBranch(generator, WatheBlocks.ACACIA_BRANCH, Blocks.ACACIA_LOG);
        this.registerBranch(generator, WatheBlocks.DARK_OAK_BRANCH, Blocks.DARK_OAK_LOG);
        this.registerBranch(generator, WatheBlocks.MANGROVE_BRANCH, Blocks.MANGROVE_LOG);
        this.registerBranch(generator, WatheBlocks.CHERRY_BRANCH, Blocks.CHERRY_LOG);
        this.registerPole(generator, WatheBlocks.BAMBOO_POLE, Blocks.BAMBOO_BLOCK);
        this.registerBranch(generator, WatheBlocks.CRIMSON_STIPE, Blocks.CRIMSON_STEM);
        this.registerBranch(generator, WatheBlocks.WARPED_STIPE, Blocks.WARPED_STEM);
        this.registerBranch(generator, WatheBlocks.STRIPPED_OAK_BRANCH, Blocks.STRIPPED_OAK_LOG);
        this.registerBranch(generator, WatheBlocks.STRIPPED_SPRUCE_BRANCH, Blocks.STRIPPED_SPRUCE_LOG);
        this.registerBranch(generator, WatheBlocks.STRIPPED_BIRCH_BRANCH, Blocks.STRIPPED_BIRCH_LOG);
        this.registerBranch(generator, WatheBlocks.STRIPPED_JUNGLE_BRANCH, Blocks.STRIPPED_JUNGLE_LOG);
        this.registerBranch(generator, WatheBlocks.STRIPPED_ACACIA_BRANCH, Blocks.STRIPPED_ACACIA_LOG);
        this.registerBranch(generator, WatheBlocks.STRIPPED_DARK_OAK_BRANCH, Blocks.STRIPPED_DARK_OAK_LOG);
        this.registerBranch(generator, WatheBlocks.STRIPPED_MANGROVE_BRANCH, Blocks.STRIPPED_MANGROVE_LOG);
        this.registerBranch(generator, WatheBlocks.STRIPPED_CHERRY_BRANCH, Blocks.STRIPPED_CHERRY_LOG);
        this.registerPole(generator, WatheBlocks.STRIPPED_BAMBOO_POLE, Blocks.STRIPPED_BAMBOO_BLOCK);
        this.registerBranch(generator, WatheBlocks.STRIPPED_CRIMSON_STIPE, Blocks.STRIPPED_CRIMSON_STEM);
        this.registerBranch(generator, WatheBlocks.STRIPPED_WARPED_STIPE, Blocks.STRIPPED_WARPED_STEM);
        this.registerHorizontalAxisBlock(generator, WatheBlocks.PANEL_STRIPES, false);
        this.registerHorizontalAxisBlock(generator, WatheBlocks.RAIL_BEAM, true);
        this.registerRailing(generator, WatheBlocks.TRIMMED_RAILING, WatheBlocks.TRIMMED_RAILING_POST, WatheBlocks.DIAGONAL_TRIMMED_RAILING);
        this.registerCargoBox(generator, WatheBlocks.CARGO_BOX);
        this.registerLoungeCouch(generator, WatheBlocks.WHITE_LOUNGE_COUCH);
        generator.registerNorthDefaultHorizontalRotation(WatheBlocks.WHITE_OTTOMAN);
        this.registerBed(generator, WatheBlocks.WHITE_TRIMMED_BED);
        this.registerBed(generator, WatheBlocks.RED_TRIMMED_BED);
        this.registerLoungeCouch(generator, WatheBlocks.BLUE_LOUNGE_COUCH);
        this.registerLoungeCouch(generator, WatheBlocks.GREEN_LOUNGE_COUCH);
        this.registerLeatherCouch(generator, WatheBlocks.RED_LEATHER_COUCH);
        this.registerLeatherCouch(generator, WatheBlocks.BROWN_LEATHER_COUCH);
        this.registerLeatherCouch(generator, WatheBlocks.BEIGE_LEATHER_COUCH);
        generator.registerSimpleState(WatheBlocks.COFFEE_TABLE);
        generator.registerSimpleState(WatheBlocks.BAR_TABLE);
        generator.registerSimpleState(WatheBlocks.BAR_STOOL);
        this.registerBar(generator, WatheBlocks.GOLD_BAR);
        this.registerLedge(generator, WatheBlocks.GOLD_LEDGE, WatheBlocks.GOLD_BAR);
        this.registerBar(generator, WatheBlocks.STAINLESS_STEEL_BAR);
        this.registerTrimmedLantern(generator, WatheBlocks.TRIMMED_LANTERN, false);
        this.registerWallLamp(generator, WatheBlocks.WALL_LAMP, false);
        this.registerNeonPillar(generator, WatheBlocks.NEON_PILLAR, false);
        this.registerNeonTube(generator, WatheBlocks.NEON_TUBE, false);
        this.registerSprinkler(generator, WatheBlocks.STAINLESS_STEEL_SPRINKLER, WatheBlocks.STAINLESS_STEEL);
        this.registerSprinkler(generator, WatheBlocks.GOLD_SPRINKLER, WatheBlocks.GOLD);
        this.registerButton(generator, WatheBlocks.SMALL_BUTTON);
        this.registerButton(generator, WatheBlocks.ELEVATOR_BUTTON);
        this.registerOrnament(generator, WatheBlocks.GOLD_ORNAMENT);
        this.registerParticleBlockWithItemSprite(generator, WatheBlocks.SMALL_WOOD_DOOR, WatheBlocks.SMOOTH_EBONY);
        this.registerParticleBlockWithItemSprite(generator, WatheBlocks.SMALL_GLASS_DOOR, WatheBlocks.TARNISHED_GOLD_PILLAR);

        this.registerHullGlass(generator, WatheBlocks.HULL_GLASS);
        generator.registerSimpleCubeAll(WatheBlocks.RHOMBUS_HULL_GLASS);
        this.registerPrivacyGlassPanel(generator, WatheBlocks.PRIVACY_GLASS_PANEL);

        registerFancySteel(generator, WatheBlocks.ANTHRACITE_STEEL, WatheBlocks.SMOOTH_ANTHRACITE_STEEL, WatheBlocks.ANTHRACITE_STEEL_TILES, WatheBlocks.ANTHRACITE_STEEL_PANEL, WatheBlocks.ANTHRACITE_STEEL_TILES_PANEL, WatheBlocks.SMOOTH_ANTHRACITE_STEEL_PANEL, WatheBlocks.ANTHRACITE_STEEL_DOOR, WatheBlocks.Family.SMOOTH_ANTHRACITE_STEEL);
        registerFancySteel(generator, WatheBlocks.KHAKI_STEEL, WatheBlocks.SMOOTH_KHAKI_STEEL, WatheBlocks.KHAKI_STEEL_TILES, WatheBlocks.KHAKI_STEEL_PANEL, WatheBlocks.KHAKI_STEEL_TILES_PANEL, WatheBlocks.SMOOTH_KHAKI_STEEL_PANEL, WatheBlocks.KHAKI_STEEL_DOOR, WatheBlocks.Family.SMOOTH_KHAKI_STEEL);
        registerFancySteel(generator, WatheBlocks.MAROON_STEEL, WatheBlocks.SMOOTH_MAROON_STEEL, WatheBlocks.MAROON_STEEL_TILES, WatheBlocks.MAROON_STEEL_PANEL, WatheBlocks.MAROON_STEEL_TILES_PANEL, WatheBlocks.SMOOTH_MAROON_STEEL_PANEL, WatheBlocks.MAROON_STEEL_DOOR, WatheBlocks.Family.SMOOTH_MAROON_STEEL);
        registerFancySteel(generator, WatheBlocks.MUNTZ_STEEL, WatheBlocks.SMOOTH_MUNTZ_STEEL, WatheBlocks.MUNTZ_STEEL_TILES, WatheBlocks.MUNTZ_STEEL_PANEL, WatheBlocks.MUNTZ_STEEL_TILES_PANEL, WatheBlocks.SMOOTH_MUNTZ_STEEL_PANEL, WatheBlocks.MUNTZ_STEEL_DOOR, WatheBlocks.Family.SMOOTH_MUNTZ_STEEL);
        registerFancySteel(generator, WatheBlocks.NAVY_STEEL, WatheBlocks.SMOOTH_NAVY_STEEL, WatheBlocks.NAVY_STEEL_TILES, WatheBlocks.NAVY_STEEL_PANEL, WatheBlocks.NAVY_STEEL_TILES_PANEL, WatheBlocks.SMOOTH_NAVY_STEEL_PANEL, WatheBlocks.NAVY_STEEL_DOOR, WatheBlocks.Family.SMOOTH_NAVY_STEEL);

        this.registerParticleBlockWithItemSprite(generator, WatheBlocks.WHEEL, WatheBlocks.DARK_STEEL);
        this.registerParticleBlockWithItemSprite(generator, WatheBlocks.RUSTED_WHEEL, WatheBlocks.DARK_STEEL);
        generator.registerSimpleCubeAll(WatheBlocks.RED_MOQUETTE);
        generator.registerSimpleCubeAll(WatheBlocks.BROWN_MOQUETTE);
        generator.registerSimpleCubeAll(WatheBlocks.BLUE_MOQUETTE);
        generator.registerSimpleState(WatheBlocks.FOOD_PLATTER);
        generator.registerNorthDefaultHorizontalRotation(WatheBlocks.DRINK_TRAY);
        this.registerPanel(generator, WatheBlocks.BARRIER_PANEL, TextureMap.getId(WatheBlocks.BARRIER_PANEL));

        generator.registerBuiltinWithParticle(WatheBlocks.LIGHT_BARRIER, WatheBlocks.LIGHT_BARRIER.asItem());
        generator.registerItemModel(WatheBlocks.LIGHT_BARRIER.asItem());
        generator.registerNorthDefaultHorizontalRotation(WatheBlocks.HORN);
        generator.registerItemModel(WatheBlocks.CHIMNEY.asItem());
    }

    private void registerFancySteel(BlockStateModelGenerator generator, Block block, Block smooth, Block tiles, Block panel, Block tilesPanel, Block smoothPanel, Block door, BlockFamily family) {
        generator.registerSimpleCubeAll(block);
        this.registerPanel(generator, panel, block);
        generator.registerSimpleCubeAll(tiles);
        this.registerPanel(generator, tilesPanel, tiles);
        this.registerFamily(generator, family);
        this.registerPanel(generator, smoothPanel, smooth);
        this.registerParticleBlockWithItemSprite(generator, door, block);
    }

    public static final Model SMALL_ITEM = item("small_item", TextureKey.LAYER0);

    private static Model item(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(Wathe.id("item/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    public static final Model KNIFE_TEMPLATE = model("item/template_knife", "_in_hand", TextureKey.LAYER0);

    private static Model model(String parent, @Nullable String variant, TextureKey... keys) {
        return new Model(Optional.of(Wathe.id(parent)), Optional.ofNullable(variant), keys);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(WatheItems.POISON_VIAL, SMALL_ITEM);
        generator.register(WatheItems.SCORPION, SMALL_ITEM);
        generator.register(WatheItems.OLD_FASHIONED, SMALL_ITEM);
        generator.register(WatheItems.MOJITO, SMALL_ITEM);
        generator.register(WatheItems.MARTINI, SMALL_ITEM);
        generator.register(WatheItems.COSMOPOLITAN, SMALL_ITEM);
        generator.register(WatheItems.CHAMPAGNE, SMALL_ITEM);
        generator.register(WatheItems.GRENADE, SMALL_ITEM);
        generator.register(WatheItems.THROWN_GRENADE, SMALL_ITEM);
        generator.register(WatheItems.FIRECRACKER, SMALL_ITEM);

        for (KnifeItem.Skin value : KnifeItem.Skin.values()) {
            registerTemplateWeapon(KNIFE_TEMPLATE, value == KnifeItem.Skin.DEFAULT ? null : value.getName(), WatheItems.KNIFE, generator);
        }
    }

    private void registerBuiltinModel(Item item, ItemModelGenerator generator) {
        generator.writer.accept(ModelIds.getItemModelId(item), new SimpleModelSupplier(Identifier.of("builtin/entity")));
    }

    private void registerTemplateWeapon(Model templateModel, @Nullable String name, Item item, ItemModelGenerator generator) {
        this.registerTemplateWeaponHandheld(templateModel, name, item, generator);
        this.registerTemplateWeaponInventory(templateModel, name, item, generator);
    }

    private void registerTemplateWeaponHandheld(Model templateModel, @Nullable String name, Item item, ItemModelGenerator generator) {
        registerTemplateWeaponHandheld(templateModel, name, Registries.ITEM.getId(item), generator);
    }

    private void registerTemplateWeaponHandheld(Model templateModel, @Nullable String name, Identifier itemId, ItemModelGenerator generator) {
        Identifier handheldModelName = (name == null ? getItemSubId(itemId, "_in_hand") : getItemSubId(itemId, "_" + name + "_in_hand"));
        Identifier handheldTexture = (name == null ? getItemId(itemId) : getItemSubId(itemId, "_" + name));

        templateModel.upload(handheldModelName, TextureMap.layer0(handheldTexture), generator.writer); // this is the actual handheld model
    }

    private void registerTemplateWeaponInventory(Model templateModel, @Nullable String name, Item item, ItemModelGenerator generator) {
        registerTemplateWeaponInventory(templateModel, name, Registries.ITEM.getId(item), generator);
    }

    private void registerTemplateWeaponInventory(Model templateModel, @Nullable String name, Identifier itemId, ItemModelGenerator generator) {
        Identifier inventoryTexture = (name == null ? getItemSubId(itemId, "") : getItemSubId(itemId, "_" + name));
        registerTemplateWeaponInventory(templateModel, name, itemId, inventoryTexture, generator);
    }

    private void registerTemplateWeaponInventory(Model templateModel, @Nullable String name, Identifier itemModelId, Identifier inventoryTexture, ItemModelGenerator generator) {
        Identifier inventoryModelName = (name == null ? getItemSubId(itemModelId, "") : getItemSubId(itemModelId, "_" + name));

        KNIFE_TEMPLATE.upload(inventoryModelName, TextureMap.layer0(inventoryTexture), generator.writer); // this is actually the inventory model
    }

    public static Identifier getItemId(Identifier itemId) {
        return itemId.withPrefixedPath("item/");
    }

    public static Identifier getItemSubId(Identifier itemId, String suffix) {
        return itemId.withPath(path -> "item/" + path + suffix);
    }

    private BlockStateVariant variant() {
        return BlockStateVariant.create();
    }

    private <T> BlockStateVariant variant(VariantSetting<T> variantSetting, T value) {
        return this.variant().put(variantSetting, value);
    }

    private <T> BlockStateVariant variant(Identifier model, VariantSetting<T> variantSetting, T value) {
        return this.model(model).put(variantSetting, value);
    }

    private BlockStateVariant model(Identifier model) {
        return this.variant(VariantSettings.MODEL, model);
    }

    private void registerBranch(BlockStateModelGenerator generator, Block branch, Block log) {
        this.registerBranch(generator, branch, log, TextureMap.getSubId(branch, "_top"));
    }

    private void registerPole(BlockStateModelGenerator generator, Block pole, Block block) {
        this.registerBranch(generator, pole, block, TextureMap.getSubId(block, "_top"));
    }

    private void registerBranch(BlockStateModelGenerator generator, Block branch, Block log, Identifier topTexture) {
        TextureMap faceMap = new TextureMap().put(TextureKey.SIDE, TextureMap.getId(log));
        TextureMap map = faceMap.copyAndAdd(TextureKey.TOP, topTexture);
        Identifier face = BRANCH_FACE.upload(branch, faceMap, generator.modelCollector);
        Identifier faceHorizontal = BRANCH_FACE_HORIZONTAL.upload(branch, faceMap, generator.modelCollector);
        Identifier front = BRANCH_FRONT.upload(branch, map, generator.modelCollector);
        Identifier back = BRANCH_BACK.upload(branch, map, generator.modelCollector);
        Identifier inventory = BRANCH_INVENTORY.upload(branch, map, generator.modelCollector);
        generator.registerParentedItemModel(branch, inventory);
        MultipartBlockStateSupplier blockStateSupplier = MultipartBlockStateSupplier.create(branch);
        for (Direction side : Direction.values())
            this.addBranchSide(blockStateSupplier, side, face, faceHorizontal, front, back);
        generator.blockStateCollector.accept(blockStateSupplier);
    }

    private BlockStateVariant rotateBranchSide(BlockStateVariant variant, Direction side) {
        return switch (side.getAxis()) {
            case X -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
            case Y -> variant.put(VariantSettings.X, VariantSettings.Rotation.R270);
            case Z -> variant;
        };
    }

    private void addBranchSide(MultipartBlockStateSupplier blockStateSupplier, Direction side, Identifier face, Identifier faceHorizontal, Identifier front, Identifier back) {
        BooleanProperty sideProperty = BranchBlock.FACING_PROPERTIES.get(side);
        BooleanProperty horizontalProperty1 = BranchBlock.FACING_PROPERTIES.get(side.getAxis().isVertical() ? Direction.EAST : side.rotateYClockwise());
        BooleanProperty horizontalProperty2 = BranchBlock.FACING_PROPERTIES.get(side.getAxis().isVertical() ? Direction.WEST : side.rotateYCounterclockwise());
        boolean isFront = side.getDirection() == (side.getAxis().equals(Direction.Axis.Z) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
        blockStateSupplier.with(
                When.create().set(sideProperty, true),
                this.rotateBranchSide(BlockStateVariant.create().put(VariantSettings.MODEL, isFront ? front : back), side)
        ).with(
                When.create().set(sideProperty, false).set(horizontalProperty1, false).set(horizontalProperty2, false),
                this.rotateForFace(BlockStateVariant.create().put(VariantSettings.MODEL, face), side, false)
        ).with(
                When.create().set(sideProperty, false).set(horizontalProperty1, true).set(horizontalProperty2, false),
                this.rotateForFace(BlockStateVariant.create().put(VariantSettings.MODEL, face), side, false)
        ).with(
                When.create().set(sideProperty, false).set(horizontalProperty1, false).set(horizontalProperty2, true),
                this.rotateForFace(BlockStateVariant.create().put(VariantSettings.MODEL, face), side, false)
        ).with(
                When.create().set(sideProperty, false).set(horizontalProperty1, true).set(horizontalProperty2, true),
                this.rotateForFace(BlockStateVariant.create().put(VariantSettings.MODEL, faceHorizontal), side, false)
        );
    }

    private BlockStateVariant rotateForFace(BlockStateVariant variant, Direction direction, boolean uvlock) {
        if (uvlock) {
            variant.put(VariantSettings.UVLOCK, true);
        }
        switch (direction) {
            case EAST -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
            case SOUTH -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R180);
            case WEST -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R270);
            case UP -> variant.put(VariantSettings.X, VariantSettings.Rotation.R270);
            case DOWN -> variant.put(VariantSettings.X, VariantSettings.Rotation.R90);
        }
        return variant;
    }

    private BlockStateVariant rotateForFace(BlockStateVariant variant, Direction direction) {
        return this.rotateForFace(variant, direction, false);
    }

    private void registerVentShaft(BlockStateModelGenerator generator, Block block) {
        TextureMap openingTexture = new TextureMap().put(TextureKey.SIDE, TextureMap.getSubId(block, "_opening"));
        TextureMap sideTexture = new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"))
                .put(TextureKey.INSIDE, TextureMap.getSubId(block, "_inside"));
        TextureMap junctionTexture = new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_junction"))
                .put(TextureKey.INSIDE, TextureMap.getSubId(block, "_junction_inside"));
        TextureMap inventoryTexture = new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"))
                .put(TextureKey.INSIDE, TextureMap.getSubId(block, "_inside"))
                .put(TextureKey.END, TextureMap.getSubId(block, "_opening"));
        Identifier openingModel = VENT_SHAFT_SIDE_OPENING.upload(block, openingTexture, generator.modelCollector);
        Identifier sideModel = VENT_SHAFT_SIDE.upload(block, "_side", sideTexture, generator.modelCollector);
        Identifier verticalModel = VENT_SHAFT_SIDE_VERTICAL.upload(block, sideTexture, generator.modelCollector);
        Identifier junctionModel = VENT_SHAFT_SIDE.upload(block, "_junction", junctionTexture, generator.modelCollector);

        Identifier inventoryModel = VENT_SHAFT_INVENTORY.upload(block, inventoryTexture, generator.modelCollector);
        generator.registerParentedItemModel(block, inventoryModel);

        MultipartBlockStateSupplier blockStateSupplier = MultipartBlockStateSupplier.create(block);
        for (Direction direction : Direction.values()) {
            boolean horizontalAxis = direction.getAxis().isHorizontal();
            boolean isUp = direction == Direction.UP;
            Direction leftDirection = horizontalAxis ? direction.rotateYClockwise() : Direction.EAST;
            Direction topDirection = horizontalAxis ? Direction.UP : isUp ? Direction.SOUTH : Direction.NORTH;

            BooleanProperty property = ConnectingBlock.FACING_PROPERTIES.get(direction);
            BooleanProperty left = ConnectingBlock.FACING_PROPERTIES.get(leftDirection);
            BooleanProperty right = ConnectingBlock.FACING_PROPERTIES.get(leftDirection.getOpposite());
            BooleanProperty top = ConnectingBlock.FACING_PROPERTIES.get(topDirection);
            BooleanProperty bottom = ConnectingBlock.FACING_PROPERTIES.get(topDirection.getOpposite());

            When whenSide = When.create().set(property, true);
            When whenVertical = When.allOf(When.create().set(top, false), When.create().set(bottom, false));
            When whenNotVertical = When.allOf(When.create().set(top, true), When.create().set(bottom, true));
            When whenHorizontal = When.allOf(When.create().set(left, false), When.create().set(right, false));
            When whenNotHorizontal = When.allOf(When.create().set(left, true), When.create().set(right, true));

            this.addVentSide(blockStateSupplier, direction, openingModel, When.create().set(property, false));
            this.addVentSide(blockStateSupplier, direction, sideModel, When.allOf(whenSide, whenHorizontal, whenNotVertical));
            this.addVentSide(blockStateSupplier, direction, verticalModel, When.allOf(whenSide, whenVertical, whenNotHorizontal));
            this.addVentSide(blockStateSupplier, direction, junctionModel, When.allOf(whenSide, When.anyOf(
                    When.create().set(top, true).set(bottom, false).set(left, false).set(right, false),
                    When.create().set(top, false).set(bottom, true).set(left, false).set(right, false),
                    When.create().set(top, false).set(bottom, false).set(left, true).set(right, false),
                    When.create().set(top, false).set(bottom, false).set(left, false).set(right, true),
                    When.create().set(top, false).set(bottom, false).set(left, false).set(right, false),
                    When.allOf(
                            When.anyOf(When.create().set(top, true), When.create().set(bottom, true)),
                            When.anyOf(When.create().set(left, true), When.create().set(right, true))
                    )
            )));

        }
        generator.blockStateCollector.accept(blockStateSupplier);
    }

    private void addVentSide(MultipartBlockStateSupplier blockStateSupplier, Direction direction, Identifier model, When when) {
        blockStateSupplier.with(when, this.rotateForFace(BlockStateVariant.create().put(VariantSettings.MODEL, model), direction, false));
    }

    private void registerItemParticleBlock(BlockStateModelGenerator generator, Block block) {
        Identifier model = Models.PARTICLE.upload(ModelIds.getBlockModelId(block), TextureMap.particle(block.asItem()), generator.modelCollector);
        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, model));
    }

    private void registerParticleBlockWithItemSprite(BlockStateModelGenerator generator, Block block, Block particleBlock) {
        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), TextureMap.layer0(block.asItem()), generator.modelCollector);
        this.registerParticleBlock(generator, block, particleBlock);
    }

    private void registerParticleBlock(BlockStateModelGenerator generator, Block block, Block particleBlock) {
        Identifier model = Models.PARTICLE.upload(ModelIds.getBlockModelId(block), TextureMap.particle(particleBlock), generator.modelCollector);
        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, model));
    }

    private void registerFamily(BlockStateModelGenerator generator, BlockFamily family) {
        TexturedModel texturedModel = this.uniqueModels.getOrDefault(family.getBaseBlock(), TexturedModel.CUBE_ALL.get(family.getBaseBlock()));
        generator.new BlockTexturePool(texturedModel.getTextures()).base(family.getBaseBlock(), texturedModel.getModel()).family(family);
    }

    private void registerWalkway(BlockStateModelGenerator generator, Block block) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"))
                .put(TextureKey.TOP, TextureMap.getId(block));
        Identifier top = WALKWAY_TOP.upload(block, "_top", textureMap, generator.modelCollector);
        Identifier bottom = WALKWAY_BOTTOM.upload(block, "_bottom", textureMap, generator.modelCollector);
        generator.registerParentedItemModel(block, bottom);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(WalkwayBlock.HALF)
                        .register(BlockHalf.TOP, BlockStateVariant.create().put(VariantSettings.MODEL, top))
                        .register(BlockHalf.BOTTOM, BlockStateVariant.create().put(VariantSettings.MODEL, bottom))));
    }

    private void registerCouch(BlockStateModelGenerator generator, Block block, Model left, Model right, Model single, Model noArms) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TEXTURE, TextureMap.getId(block));
        Identifier leftModel = left.upload(block, textureMap, generator.modelCollector);
        Identifier rightModel = right.upload(block, textureMap, generator.modelCollector);
        Identifier singleModel = single.upload(block, textureMap, generator.modelCollector);
        Identifier noArmsModel = noArms.upload(block, textureMap, generator.modelCollector);
        generator.registerParentedItemModel(block, singleModel);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(CouchBlock.ARMS)
                        .register(CouchArms.LEFT, BlockStateVariant.create().put(VariantSettings.MODEL, leftModel))
                        .register(CouchArms.RIGHT, BlockStateVariant.create().put(VariantSettings.MODEL, rightModel))
                        .register(CouchArms.SINGLE, BlockStateVariant.create().put(VariantSettings.MODEL, singleModel))
                        .register(CouchArms.NO_ARMS, BlockStateVariant.create().put(VariantSettings.MODEL, noArmsModel)))
                .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
    }

    private void registerLoungeCouch(BlockStateModelGenerator generator, Block block) {
        this.registerCouch(generator, block, LOUNGE_COUCH_LEFT, LOUNGE_COUCH_RIGHT, LOUNGE_COUCH_SINGLE, LOUNGE_COUCH_NO_ARMS);
    }

    private void registerLeatherCouch(BlockStateModelGenerator generator, Block block) {
        this.registerCouch(generator, block, LEATHER_COUCH_LEFT, LEATHER_COUCH_RIGHT, LEATHER_COUCH, LEATHER_COUCH_MIDDLE);
    }

    private void registerTrimmedStairs(BlockStateModelGenerator generator, Block block) {
        TextureMap supportTexture = TextureMap.texture(ModelIds.getBlockSubModelId(block, "_support"));
        TextureMap singleTexture = TextureMap.texture(ModelIds.getBlockSubModelId(block, "_single"));
        TextureMap leftTexture = TextureMap.texture(ModelIds.getBlockSubModelId(block, "_left"));
        TextureMap rightTexture = TextureMap.texture(ModelIds.getBlockSubModelId(block, "_right"));
        TextureMap middleTexture = TextureMap.texture(ModelIds.getBlockSubModelId(block, "_middle"));
        Identifier support = TRIMMED_STAIR_SUPPORT.upload(block, supportTexture, generator.modelCollector);
        Identifier single = TRIMMED_STAIRS.upload(block, "_single", singleTexture, generator.modelCollector);
        Identifier left = TRIMMED_STAIRS.upload(block, "_left", leftTexture, generator.modelCollector);
        Identifier right = TRIMMED_STAIRS.upload(block, "_right", rightTexture, generator.modelCollector);
        Identifier middle = TRIMMED_STAIRS.upload(block, "_middle", middleTexture, generator.modelCollector);
        MultipartBlockStateSupplier blockStateSupplier = MultipartBlockStateSupplier.create(block);
        for (Direction direction : Direction.Type.HORIZONTAL) {
            blockStateSupplier.with(When.create()
                            .set(TrimmedStairsBlock.FACING, direction)
                            .set(TrimmedStairsBlock.LEFT, false)
                            .set(TrimmedStairsBlock.RIGHT, false),
                    this.rotateForFace(this.model(middle), direction)
            );
            blockStateSupplier.with(When.create()
                            .set(TrimmedStairsBlock.FACING, direction)
                            .set(TrimmedStairsBlock.LEFT, true)
                            .set(TrimmedStairsBlock.RIGHT, false),
                    this.rotateForFace(this.model(left), direction)
            );
            blockStateSupplier.with(When.create()
                            .set(TrimmedStairsBlock.FACING, direction)
                            .set(TrimmedStairsBlock.LEFT, false)
                            .set(TrimmedStairsBlock.RIGHT, true),
                    this.rotateForFace(this.model(right), direction)
            );
            blockStateSupplier.with(When.create()
                            .set(TrimmedStairsBlock.FACING, direction)
                            .set(TrimmedStairsBlock.LEFT, true)
                            .set(TrimmedStairsBlock.RIGHT, true),
                    this.rotateForFace(this.model(single), direction)
            );
            blockStateSupplier.with(When.create()
                            .set(TrimmedStairsBlock.FACING, direction)
                            .set(TrimmedStairsBlock.SUPPORT, true),
                    this.rotateForFace(this.model(support), direction)
            );
        }
        generator.registerParentedItemModel(block, single);
        generator.blockStateCollector.accept(blockStateSupplier);
    }

    private void registerLiquid(BlockStateModelGenerator generator, Item item, Identifier texture) {
        // Leaves base model has a tintIndex of 0 for all faces
        Models.LEAVES.upload(ModelIds.getItemModelId(item), TextureMap.all(texture), generator.modelCollector);
    }

    private void registerColumn(BlockStateModelGenerator generator, Block block) {
        TextureMap textureMap = TextureMap.sideEnd(block);
        Identifier model = Models.CUBE_COLUMN.upload(block, textureMap, generator.modelCollector);
        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, model));
    }

    private void registerVariedBookshelf(BlockStateModelGenerator generator, Block block, Block planks) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.END, TextureMap.getId(planks))
                .put(TextureKey.SIDE, TextureMap.getId(block));
        TextureMap altTextureMap = new TextureMap()
                .put(TextureKey.END, TextureMap.getId(planks))
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_alt"));
        Identifier model = Models.CUBE_COLUMN.upload(block, textureMap, generator.modelCollector);
        Identifier altModel = Models.CUBE_COLUMN.upload(block, "_alt", altTextureMap, generator.modelCollector);
        List<Identifier> models = List.of(model, altModel);
        generator.registerParentedItemModel(block, model);
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, BlockStateModelGenerator.buildBlockStateVariants(models, variant -> variant).toArray(BlockStateVariant[]::new))
        );
    }

    private void registerCabinet(BlockStateModelGenerator generator, Block block) {
        Identifier sideTexture = TextureMap.getSubId(block, "_side");
        TextureMap closedTexture = new TextureMap()
                .put(TextureKey.SIDE, sideTexture)
                .put(TextureKey.TOP, sideTexture)
                .put(TextureKey.FRONT, TextureMap.getSubId(block, "_front"));
        TextureMap openTexture = new TextureMap()
                .put(TextureKey.SIDE, sideTexture)
                .put(SHELF_KEY, TextureMap.getSubId(block, "_front_open_shelf"))
                .put(TextureKey.WALL, TextureMap.getSubId(block, "_wall"))
                .put(TextureKey.FRONT, TextureMap.getSubId(block, "_front_open"));
        Identifier closedModel = Models.ORIENTABLE.upload(block, closedTexture, generator.modelCollector);
        Identifier openModel = CABINET_OPEN.upload(block, openTexture, generator.modelCollector);
        generator.registerParentedItemModel(block, closedModel);
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block)
                        .coordinate(BlockStateModelGenerator.createBooleanModelMap(CabinetBlock.OPEN, openModel, closedModel))
                        .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
        );
    }

    private void registerPanel(BlockStateModelGenerator generator, Block block, Block textureBlock) {
        registerPanel(generator, block, TextureMap.getId(textureBlock));
    }

    private void registerPanel(BlockStateModelGenerator generator, Block block, Identifier texture) {
        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), TextureMap.layer0(texture), generator.modelCollector);
        Identifier model = PANEL.upload(block, TextureMap.all(texture), generator.modelCollector);
        MultipartBlockStateSupplier blockStateSupplier = MultipartBlockStateSupplier.create(block);
        When.PropertyCondition propertyCondition = When.create();
        BlockStateModelGenerator.CONNECTION_VARIANT_FUNCTIONS.stream().map(Pair::getFirst)
                .forEach(property -> propertyCondition.set(property, false));

        for (Pair<BooleanProperty, Function<Identifier, BlockStateVariant>> pair : BlockStateModelGenerator.CONNECTION_VARIANT_FUNCTIONS) {
            BooleanProperty facingProperty = pair.getFirst();
            BlockStateVariant variant = pair.getSecond().apply(model);
            blockStateSupplier.with(When.create().set(facingProperty, true), variant);
            blockStateSupplier.with(propertyCondition, variant);
        }

        generator.blockStateCollector.accept(blockStateSupplier);
    }

    private BlockStateVariant rotateForAxis(BlockStateVariant variant, Direction.Axis axis) {
        return switch (axis) {
            case X -> variant.put(VariantSettings.Y, VariantSettings.Rotation.R270);
            case Y -> variant.put(VariantSettings.X, VariantSettings.Rotation.R90);
            case Z -> variant;
        };
    }

    private void registerBar(BlockStateModelGenerator generator, Block block) {
        TextureMap textureMap = TextureMap.texture(block);
        Identifier model = BAR.upload(block, textureMap, generator.modelCollector);
        Identifier topModel = BAR_TOP.upload(block, textureMap, generator.modelCollector);
        Identifier bottomModel = BAR_BOTTOM.upload(block, textureMap, generator.modelCollector);
        generator.registerItemModel(block.asItem());
        MultipartBlockStateSupplier blockStateSupplier = MultipartBlockStateSupplier.create(block);
        for (Direction.Axis axis : Direction.Axis.values()) {
            blockStateSupplier.with(
                    When.create().set(BarBlock.AXIS, axis),
                    this.rotateForAxis(this.model(model), axis)
            ).with(
                    When.create().set(BarBlock.AXIS, axis).set(BarBlock.TOP, true),
                    this.rotateForAxis(this.model(topModel), axis)
            ).with(
                    When.create().set(BarBlock.AXIS, axis).set(BarBlock.BOTTOM, true),
                    this.rotateForAxis(this.model(bottomModel), axis)
            );
        }
        generator.blockStateCollector.accept(blockStateSupplier);
    }

    private void registerLedge(BlockStateModelGenerator generator, Block block, Block barBlock) {
        TextureMap textureMap = TextureMap.texture(barBlock);
        Identifier model = LEDGE.upload(block, textureMap, generator.modelCollector);
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(LedgeBlock.FACING)
                        .register(Direction.NORTH, this.model(model))
                        .register(Direction.EAST, this.model(model).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                        .register(Direction.SOUTH, this.model(model).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                        .register(Direction.WEST, this.model(model).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                )
        );
        generator.registerItemModel(block.asItem());
    }

    private void registerLadder(BlockStateModelGenerator generator, Block block) {
        TextureMap textureMap = TextureMap.texture(block).put(TextureKey.PARTICLE, TextureMap.getId(block));
        LADDER.upload(block, textureMap, generator.modelCollector);
        generator.registerItemModel(block);
        generator.registerNorthDefaultHorizontalRotation(block);
    }

    private void registerTrimmedLantern(BlockStateModelGenerator generator, Block block, boolean hasEmergencyVariant) {
        TextureMap litTexture = new TextureMap()
                .put(TextureKey.TOP, TextureMap.getSubId(block, "_top"))
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side_lit"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(block, "_bottom_lit"));
        TextureMap unlitTexture = new TextureMap()
                .put(TextureKey.TOP, TextureMap.getSubId(block, "_top"))
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side_unlit"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(block, "_bottom_unlit"));
        Identifier litCeiling = TRIMMED_LANTERN_CEILING.upload(block, "_ceiling_lit", litTexture, generator.modelCollector);
        Identifier litWall = TRIMMED_LANTERN_WALL.upload(block, "_wall_lit", litTexture, generator.modelCollector);
        Identifier litFloor = TRIMMED_LANTERN_FLOOR.upload(block, "_floor_lit", litTexture, generator.modelCollector);
        Identifier unlitCeiling = TRIMMED_LANTERN_CEILING.upload(block, "_ceiling_unlit", unlitTexture, generator.modelCollector);
        Identifier unlitWall = TRIMMED_LANTERN_WALL.upload(block, "_wall_unlit", unlitTexture, generator.modelCollector);
        Identifier unlitFloor = TRIMMED_LANTERN_FLOOR.upload(block, "_floor_unlit", unlitTexture, generator.modelCollector);

        TextureMap unpoweredTexture = new TextureMap()
                .put(TextureKey.TOP, TextureMap.getSubId(block, "_top"))
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side_emergency"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(block, "_bottom_emergency"));
        Identifier unpoweredCeiling = hasEmergencyVariant ? TRIMMED_LANTERN_CEILING.upload(block, "_ceiling_emergency", unpoweredTexture, generator.modelCollector) : unlitCeiling;
        Identifier unpoweredWall = hasEmergencyVariant ? TRIMMED_LANTERN_WALL.upload(block, "_wall_emergency", unpoweredTexture, generator.modelCollector) : unlitWall;
        Identifier unpoweredFloor = hasEmergencyVariant ? TRIMMED_LANTERN_FLOOR.upload(block, "_floor_emergency", unpoweredTexture, generator.modelCollector) : unlitFloor;

        generator.registerParentedItemModel(block, unlitFloor);
        VariantsBlockStateSupplier blockStateSupplier = VariantsBlockStateSupplier.create(block);
        blockStateSupplier.coordinate(BlockStateVariantMap.create(TrimmedLanternBlock.FACING, TrimmedLanternBlock.ACTIVE, TrimmedLanternBlock.LIT)
                // powered
                .register(Direction.NORTH, true, false, this.model(unlitWall))
                .register(Direction.EAST, true, false, this.rotateForFace(this.model(unlitWall), Direction.EAST, false))
                .register(Direction.SOUTH, true, false, this.rotateForFace(this.model(unlitWall), Direction.SOUTH, false))
                .register(Direction.WEST, true, false, this.rotateForFace(this.model(unlitWall), Direction.WEST, false))
                .register(Direction.UP, true, false, this.model(unlitFloor))
                .register(Direction.DOWN, true, false, this.model(unlitCeiling))
                .register(Direction.NORTH, true, true, this.model(litWall))
                .register(Direction.EAST, true, true, this.rotateForFace(this.model(litWall), Direction.EAST, false))
                .register(Direction.SOUTH, true, true, this.rotateForFace(this.model(litWall), Direction.SOUTH, false))
                .register(Direction.WEST, true, true, this.rotateForFace(this.model(litWall), Direction.WEST, false))
                .register(Direction.UP, true, true, this.model(litFloor))
                .register(Direction.DOWN, true, true, this.model(litCeiling))
                // unpowered
                .register(Direction.NORTH, false, false, this.model(unpoweredWall))
                .register(Direction.EAST, false, false, this.rotateForFace(this.model(unpoweredWall), Direction.EAST, false))
                .register(Direction.SOUTH, false, false, this.rotateForFace(this.model(unpoweredWall), Direction.SOUTH, false))
                .register(Direction.WEST, false, false, this.rotateForFace(this.model(unpoweredWall), Direction.WEST, false))
                .register(Direction.UP, false, false, this.model(unpoweredFloor))
                .register(Direction.DOWN, false, false, this.model(unpoweredCeiling))
                .register(Direction.NORTH, false, true, this.model(unpoweredWall))
                .register(Direction.EAST, false, true, this.rotateForFace(this.model(unpoweredWall), Direction.EAST, false))
                .register(Direction.SOUTH, false, true, this.rotateForFace(this.model(unpoweredWall), Direction.SOUTH, false))
                .register(Direction.WEST, false, true, this.rotateForFace(this.model(unpoweredWall), Direction.WEST, false))
                .register(Direction.UP, false, true, this.model(unpoweredFloor))
                .register(Direction.DOWN, false, true, this.model(unpoweredCeiling))
        );
        generator.blockStateCollector.accept(blockStateSupplier);
    }

    private BlockStateVariantMap wallMountedVariantMap(Identifier model) {
        return BlockStateVariantMap.create(WallMountedBlock.FACING, WallMountedBlock.FACE).register((facing, face) -> {
            if (face == BlockFace.WALL) {
                return this.rotateForFace(this.model(model), facing, false);
            }
            return this.rotateForFace(this.rotateForFace(
                            this.model(model), face == BlockFace.FLOOR ? Direction.UP : Direction.DOWN, false),
                    facing.getOpposite(), false);
        });
    }

    private BlockStateVariantMap wallMountedVariantMap(BooleanProperty booleanProperty, Identifier trueModel, Identifier falseModel) {
        return BlockStateVariantMap.create(WallMountedBlock.FACING, WallMountedBlock.FACE, booleanProperty).register((facing, face, bl) -> {
            Identifier model = bl ? trueModel : falseModel;
            if (face == BlockFace.WALL) {
                return this.rotateForFace(this.model(model), facing, false);
            }
            return this.rotateForFace(this.rotateForFace(
                            this.model(model), face == BlockFace.FLOOR ? Direction.UP : Direction.DOWN, false),
                    facing.getOpposite(), false);
        });
    }

    private void registerSprinkler(BlockStateModelGenerator generator, Block block, Block particleBlock) {
        Identifier model = SPRINKLER.upload(block, TextureMap.texture(block).put(TextureKey.PARTICLE, TextureMap.getId(particleBlock)), generator.modelCollector);
        generator.registerParentedItemModel(block, model);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(this.wallMountedVariantMap(model)));
    }

    private void registerWallLamp(BlockStateModelGenerator generator, Block block, boolean hasEmergencyVariant) {
        TextureMap litTexture = TextureMap.texture(TextureMap.getSubId(block, "_lit"));
        TextureMap unlitTexture = TextureMap.texture(TextureMap.getSubId(block, "_unlit"));
        Identifier litCeiling = WALL_LAMP_CEILING.upload(block, "_ceiling_lit", litTexture, generator.modelCollector);
        Identifier litWall = WALL_LAMP_WALL.upload(block, "_wall_lit", litTexture, generator.modelCollector);
        Identifier litFloor = WALL_LAMP_FLOOR.upload(block, "_floor_lit", litTexture, generator.modelCollector);
        Identifier unlitCeiling = WALL_LAMP_CEILING.upload(block, "_ceiling_unlit", unlitTexture, generator.modelCollector);
        Identifier unlitWall = WALL_LAMP_WALL.upload(block, "_wall_unlit", unlitTexture, generator.modelCollector);
        Identifier unlitFloor = WALL_LAMP_FLOOR.upload(block, "_floor_unlit", unlitTexture, generator.modelCollector);

        TextureMap unpoweredTexture = hasEmergencyVariant ? TextureMap.texture(TextureMap.getSubId(block, "_emergency")) : unlitTexture;
        Identifier unpoweredCeiling = hasEmergencyVariant ? WALL_LAMP_CEILING.upload(block, "_ceiling_emergency", unpoweredTexture, generator.modelCollector) : unlitCeiling;
        Identifier unpoweredWall = hasEmergencyVariant ? WALL_LAMP_WALL.upload(block, "_wall_emergency", unpoweredTexture, generator.modelCollector) : unlitWall;
        Identifier unpoweredFloor = hasEmergencyVariant ? WALL_LAMP_FLOOR.upload(block, "_floor_emergency", unpoweredTexture, generator.modelCollector) : unlitFloor;

        generator.registerParentedItemModel(block, unlitFloor);
        VariantsBlockStateSupplier blockStateSupplier = VariantsBlockStateSupplier.create(block);
        blockStateSupplier.coordinate(BlockStateVariantMap.create(WallLampBlock.FACING, WallLampBlock.ACTIVE, WallLampBlock.LIT)
                // powered
                .register(Direction.NORTH, true, false, this.model(unlitWall))
                .register(Direction.EAST, true, false, this.rotateForFace(this.model(unlitWall), Direction.EAST, false))
                .register(Direction.SOUTH, true, false, this.rotateForFace(this.model(unlitWall), Direction.SOUTH, false))
                .register(Direction.WEST, true, false, this.rotateForFace(this.model(unlitWall), Direction.WEST, false))
                .register(Direction.UP, true, false, this.model(unlitFloor))
                .register(Direction.DOWN, true, false, this.model(unlitCeiling))
                .register(Direction.NORTH, true, true, this.model(litWall))
                .register(Direction.EAST, true, true, this.rotateForFace(this.model(litWall), Direction.EAST, false))
                .register(Direction.SOUTH, true, true, this.rotateForFace(this.model(litWall), Direction.SOUTH, false))
                .register(Direction.WEST, true, true, this.rotateForFace(this.model(litWall), Direction.WEST, false))
                .register(Direction.UP, true, true, this.model(litFloor))
                .register(Direction.DOWN, true, true, this.model(litCeiling))
                // unpowered
                .register(Direction.NORTH, false, false, this.model(unpoweredWall))
                .register(Direction.EAST, false, false, this.rotateForFace(this.model(unpoweredWall), Direction.EAST, false))
                .register(Direction.SOUTH, false, false, this.rotateForFace(this.model(unpoweredWall), Direction.SOUTH, false))
                .register(Direction.WEST, false, false, this.rotateForFace(this.model(unpoweredWall), Direction.WEST, false))
                .register(Direction.UP, false, false, this.model(unpoweredFloor))
                .register(Direction.DOWN, false, false, this.model(unpoweredCeiling))
                .register(Direction.NORTH, false, true, this.model(unpoweredWall))
                .register(Direction.EAST, false, true, this.rotateForFace(this.model(unpoweredWall), Direction.EAST, false))
                .register(Direction.SOUTH, false, true, this.rotateForFace(this.model(unpoweredWall), Direction.SOUTH, false))
                .register(Direction.WEST, false, true, this.rotateForFace(this.model(unpoweredWall), Direction.WEST, false))
                .register(Direction.UP, false, true, this.model(unpoweredFloor))
                .register(Direction.DOWN, false, true, this.model(unpoweredCeiling))
        );
        generator.blockStateCollector.accept(blockStateSupplier);
    }

    private void registerCargoBox(BlockStateModelGenerator generator, Block block) {
        TextureMap closedTexture = new TextureMap()
                .put(TextureKey.TOP, TextureMap.getSubId(block, "_top"))
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(block, "_bottom"));
        TextureMap openTexture = new TextureMap()
                .put(TextureKey.TOP, TextureMap.getSubId(block, "_top_open"))
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(block, "_bottom"));

        Identifier closedModel = CARGO_BOX.upload(block, closedTexture, generator.modelCollector);
        Identifier openModel = CARGO_BOX_OPEN.upload(block, openTexture, generator.modelCollector);
        Identifier inventoryModel = Models.CUBE_BOTTOM_TOP.upload(block, "_inventory", closedTexture, generator.modelCollector);
        generator.registerParentedItemModel(block, inventoryModel);
        VariantsBlockStateSupplier blockStateSupplier = VariantsBlockStateSupplier.create(block);
        blockStateSupplier.coordinate(
                BlockStateVariantMap.create(CargoBoxBlock.FACING, CargoBoxBlock.OPEN)
                        .register((facing, open) -> this.rotateForFace(this.model(open ? openModel : closedModel), facing, false))
        );
        generator.blockStateCollector.accept(blockStateSupplier);
    }

    private void registerGlassPanel(BlockStateModelGenerator generator, Block block) {
        generator.registerItemModel(block, "_trim");
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.PANE, TextureMap.getId(block))
                .put(TextureKey.EDGE, TextureMap.getSubId(block, "_trim"));
        Identifier model = GLASS_PANEL.upload(block, textureMap, generator.modelCollector);
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, this.model(model))
                        .coordinate(BlockStateVariantMap.create(Properties.FACING)
                                .register(Direction.UP, this.variant(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(Direction.DOWN, this.variant(VariantSettings.X, VariantSettings.Rotation.R270))
                                .register(Direction.SOUTH, this.variant())
                                .register(Direction.NORTH, this.variant(VariantSettings.Y, VariantSettings.Rotation.R180))
                                .register(Direction.EAST, this.variant(VariantSettings.Y, VariantSettings.Rotation.R270))
                                .register(Direction.WEST, this.variant(VariantSettings.Y, VariantSettings.Rotation.R90))));
    }

    private void registerCullingGlass(BlockStateModelGenerator generator) {
        Block block = WatheBlocks.CULLING_GLASS;
        generator.registerItemModel(block);
        Identifier model = ModelIds.getBlockModelId(block);
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, this.model(model))
                        .coordinate(BlockStateVariantMap.create(Properties.FACING)
                                .register(Direction.UP, this.variant(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(Direction.DOWN, this.variant(VariantSettings.X, VariantSettings.Rotation.R270))
                                .register(Direction.SOUTH, this.variant())
                                .register(Direction.NORTH, this.variant(VariantSettings.Y, VariantSettings.Rotation.R180))
                                .register(Direction.EAST, this.variant(VariantSettings.Y, VariantSettings.Rotation.R270))
                                .register(Direction.WEST, this.variant(VariantSettings.Y, VariantSettings.Rotation.R90))));
    }

    private void registerHorizontalAxisBlock(BlockStateModelGenerator generator, Block block, boolean registerInventoryParent) {
        Identifier model = ModelIds.getBlockModelId(block);
        if (registerInventoryParent) generator.registerParentedItemModel(block, model);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(
                BlockStateVariantMap.create(PanelStripesBlock.AXIS)
                        .register(Direction.Axis.X, this.model(model).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                        .register(Direction.Axis.Z, this.model(model))
        ));
    }

    private void registerButton(BlockStateModelGenerator generator, Block block) {
        Identifier model = ModelIds.getBlockModelId(block);
        Identifier pressedModel = ModelIds.getBlockSubModelId(block, "_pressed");
        generator.registerParentedItemModel(block, model);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(
                this.wallMountedVariantMap(ButtonBlock.POWERED, pressedModel, model)
        ));
    }

    private void registerCulledBlock(BlockStateModelGenerator generator, Block block, Block textureBlock) {
        Identifier model = Models.TEMPLATE_SINGLE_FACE.upload(block, TextureMap.texture(textureBlock), generator.modelCollector);
        Identifier modelGlass = Models.TEMPLATE_SINGLE_FACE.upload(block, "_glass", TextureMap.texture(TextureMap.getId(WatheBlocks.HULL_GLASS)), generator.modelCollector);
        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), TextureMap.layer0(textureBlock), generator.modelCollector);
        MultipartBlockStateSupplier blockStateSupplier = MultipartBlockStateSupplier.create(block);
        for (Direction direction : Direction.values()) {
            blockStateSupplier.with(
                    When.create().set(ConnectingBlock.FACING_PROPERTIES.get(direction), true),
                    this.rotateForFace(this.model(model), direction, true)
            );
            blockStateSupplier.with(
                    When.create().set(ConnectingBlock.FACING_PROPERTIES.get(direction), false),
                    this.rotateForFace(this.model(modelGlass), direction, true)
            );
        }
        generator.blockStateCollector.accept(blockStateSupplier);
    }

    private void registerOrnament(BlockStateModelGenerator generator, Block block) {
        TextureMap allTexture = TextureMap.texture(TextureMap.getSubId(block, "_all"));
        TextureMap endTexture = TextureMap.texture(TextureMap.getSubId(block, "_end"));
        TextureMap sideTexture = TextureMap.texture(TextureMap.getSubId(block, "_side"));
        TextureMap cornerTexture = TextureMap.texture(TextureMap.getSubId(block, "_corner"));
        TextureMap sidesTexture = TextureMap.texture(TextureMap.getSubId(block, "_sides"));
        TextureMap centerTexture = TextureMap.texture(TextureMap.getSubId(block, "_center"));
        TextureMap sidesCenterTexture = TextureMap.texture(TextureMap.getSubId(block, "_sides_center"));
        ORNAMENT_R0.upload(block, "_all", allTexture, generator.modelCollector);
        ORNAMENT_R0.upload(block, "_center", centerTexture, generator.modelCollector);
        ORNAMENT_R0.upload(block, "_left_right_center", sidesCenterTexture, generator.modelCollector);
        ORNAMENT_R0.upload(block, "_left", sideTexture, generator.modelCollector);
        ORNAMENT_R90.upload(block, "_top", sideTexture, generator.modelCollector);
        ORNAMENT_R180.upload(block, "_right", sideTexture, generator.modelCollector);
        ORNAMENT_R270.upload(block, "_bottom", sideTexture, generator.modelCollector);
        ORNAMENT_R0.upload(block, "_left_bottom", cornerTexture, generator.modelCollector);
        ORNAMENT_R90.upload(block, "_left_top", cornerTexture, generator.modelCollector);
        ORNAMENT_R180.upload(block, "_right_top", cornerTexture, generator.modelCollector);
        ORNAMENT_R270.upload(block, "_right_bottom", cornerTexture, generator.modelCollector);
        ORNAMENT_R0.upload(block, "_left_right", sidesTexture, generator.modelCollector);
        ORNAMENT_R90.upload(block, "_top_bottom", sidesTexture, generator.modelCollector);
        ORNAMENT_R0.upload(block, "_left_right_top", endTexture, generator.modelCollector);
        ORNAMENT_R90.upload(block, "_right_top_bottom", endTexture, generator.modelCollector);
        ORNAMENT_R180.upload(block, "_left_right_bottom", endTexture, generator.modelCollector);
        ORNAMENT_R270.upload(block, "_left_top_bottom", endTexture, generator.modelCollector);
        generator.registerItemModel(block, "_all");
        BlockStateVariantMap map = BlockStateVariantMap.create(OrnamentBlock.FACING, OrnamentBlock.SHAPE).register((facing, shape) ->
                this.rotateForFace(this.model(ModelIds.getBlockSubModelId(block, "_" + shape.asString())), facing, false)
        );
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(map));
    }

    private void registerSpaceHelmet(BlockStateModelGenerator generator, Block block) {
        generator.registerParentedItemModel(block.asItem(), Identifier.ofVanilla("item/template_skull"));

        Identifier model = Models.PARTICLE.upload(block, TextureMap.particle(WatheBlocks.STAINLESS_STEEL), generator.modelCollector);
        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, model));
    }

    private void registerHullGlass(BlockStateModelGenerator generator, Block block) {
        Identifier model = Models.CUBE_ALL.upload(block, TextureMap.all(block), generator.modelCollector);
        Identifier opaqueModel = Models.CUBE_ALL.upload(block, "_opaque", TextureMap.all(TextureMap.getSubId(block, "_opaque")), generator.modelCollector);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(BlockStateModelGenerator.createBooleanModelMap(PrivacyGlassBlock.OPAQUE, opaqueModel, model)));
        generator.registerParentedItemModel(block, model);
    }

    private void registerPrivacyGlassPanel(BlockStateModelGenerator generator, Block block) {
        generator.registerItemModel(block, "_trim");
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.PANE, TextureMap.getId(block))
                .put(TextureKey.EDGE, TextureMap.getSubId(block, "_trim"));
        TextureMap opaqueTextureMap = new TextureMap()
                .put(TextureKey.PANE, TextureMap.getSubId(block, "_opaque"))
                .put(TextureKey.EDGE, TextureMap.getSubId(block, "_trim"));

        Identifier model = GLASS_PANEL.upload(block, textureMap, generator.modelCollector);
        Identifier opaqueModel = GLASS_PANEL.upload(block, "_opaque", opaqueTextureMap, generator.modelCollector);

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block)
                        .coordinate(BlockStateVariantMap.create(Properties.FACING, PrivacyGlassPanelBlock.OPAQUE)
                                .register(Direction.UP, true, this.variant(opaqueModel, VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(Direction.UP, false, this.variant(model, VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(Direction.DOWN, true, this.variant(opaqueModel, VariantSettings.X, VariantSettings.Rotation.R270))
                                .register(Direction.DOWN, false, this.variant(model, VariantSettings.X, VariantSettings.Rotation.R270))
                                .register(Direction.SOUTH, true, this.model(opaqueModel))
                                .register(Direction.SOUTH, false, this.model(model))
                                .register(Direction.NORTH, true, this.variant(opaqueModel, VariantSettings.Y, VariantSettings.Rotation.R180))
                                .register(Direction.NORTH, false, this.variant(model, VariantSettings.Y, VariantSettings.Rotation.R180))
                                .register(Direction.EAST, true, this.variant(opaqueModel, VariantSettings.Y, VariantSettings.Rotation.R270))
                                .register(Direction.EAST, false, this.variant(model, VariantSettings.Y, VariantSettings.Rotation.R270))
                                .register(Direction.WEST, true, this.variant(opaqueModel, VariantSettings.Y, VariantSettings.Rotation.R90))
                                .register(Direction.WEST, false, this.variant(model, VariantSettings.Y, VariantSettings.Rotation.R90))));
    }

    private void registerRailing(BlockStateModelGenerator generator, Block block, Block post, Block diagonal) {
        Identifier model = ModelIds.getBlockModelId(block);
        Identifier inventoryModel = ModelIds.getBlockSubModelId(block, "_inventory");
        Identifier diagonalLeftModel = ModelIds.getBlockSubModelId(block, "_diagonal_left");
        Identifier diagonalRightModel = ModelIds.getBlockSubModelId(block, "_diagonal_right");
        Identifier diagonalTopLeftModel = ModelIds.getBlockSubModelId(block, "_diagonal_top_left");
        Identifier diagonalTopRightModel = ModelIds.getBlockSubModelId(block, "_diagonal_top_right");
        Identifier diagonalBottomLeftModel = ModelIds.getBlockSubModelId(block, "_diagonal_bottom_left");
        Identifier diagonalBottomRightModel = ModelIds.getBlockSubModelId(block, "_diagonal_bottom_right");
        Identifier postModel = ModelIds.getBlockSubModelId(block, "_post");
        generator.registerParentedItemModel(block, inventoryModel);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, this.model(model))
                .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(post, this.model(postModel))
                .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(diagonal)
                .coordinate(BlockStateVariantMap.create(DiagonalRailingBlock.FACING, DiagonalRailingBlock.LEFT, DiagonalRailingBlock.SHAPE).register(
                        (facing, left, shape) -> {
                            BlockStateVariant variant = this.model(switch (shape) {
                                case TOP -> left ? diagonalTopLeftModel : diagonalTopRightModel;
                                case MIDDLE -> left ? diagonalLeftModel : diagonalRightModel;
                                case BOTTOM -> left ? diagonalBottomLeftModel : diagonalBottomRightModel;
                            });
                            return this.rotateForFace(
                                    variant,
                                    left ? facing : facing.getOpposite(),
                                    false
                            );
                        }
                )));
    }

    private void registerBed(BlockStateModelGenerator generator, Block block) {
        TextureMap textureMap = TextureMap.texture(block);
        Identifier inventoryModel = TRIMMED_BED_INVENTORY.upload(block, textureMap, generator.modelCollector);
        Identifier headModel = TRIMMED_BED_HEAD.upload(block, textureMap, generator.modelCollector);
        Identifier footModel = TRIMMED_BED_FOOT.upload(block, textureMap, generator.modelCollector);
        generator.registerParentedItemModel(block, inventoryModel);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(TrimmedBedBlock.FACING, TrimmedBedBlock.PART).register(
                        (facing, part) -> this.rotateForFace(
                                this.model(part == BedPart.HEAD ? headModel : footModel),
                                facing,
                                false
                        )
                )));
    }

    private void registerPipe(BlockStateModelGenerator generator, Block block) {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TOP, TextureMap.getSubId(block, "_top"))
                .put(TextureKey.BOTTOM, TextureMap.getSubId(block, "_top"))
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"));
        Identifier model = CARGO_BOX.upload(block, textureMap, generator.modelCollector);
        Identifier inventoryModel = Models.CUBE_BOTTOM_TOP.upload(block, "_inventory", textureMap, generator.modelCollector);
        generator.registerParentedItemModel(block, inventoryModel);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, this.model(model)).coordinate(
                BlockStateModelGenerator.createNorthDefaultRotationStates()
        ));
    }

    private void registerPump(BlockStateModelGenerator generator, Block block) {
        Identifier model = ModelIds.getBlockModelId(block);
        generator.registerParentedItemModel(block, model);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, this.model(model)).coordinate(
                BlockStateModelGenerator.createNorthDefaultRotationStates()
        ));
    }

    private void registerVentHatch(BlockStateModelGenerator generator, Block block) {
        TextureMap textureMap = TextureMap.texture(block);
        Identifier model = VENT_HATCH.upload(block, textureMap, generator.modelCollector);
        Identifier openModel = VENT_HATCH_OPEN.upload(block, textureMap, generator.modelCollector);
        generator.registerItemModel(block);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(
                this.wallMountedVariantMap(VentHatchBlock.OPEN, openModel, model))
        );
    }

    private void registerNeonPillar(BlockStateModelGenerator generator, Block block, boolean hasEmergencyVariant) {
        TextureMap litTextureMap = new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_lit"))
                .put(TextureKey.END, TextureMap.getSubId(block, "_top_lit"))
                .put(TextureKey.PARTICLE, TextureMap.getSubId(block, "_lit"));
        TextureMap unlitTextureMap = new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_unlit"))
                .put(TextureKey.END, TextureMap.getSubId(block, "_top_unlit"))
                .put(TextureKey.PARTICLE, TextureMap.getSubId(block, "_unlit"));
        TextureMap unpoweredTextureMap = hasEmergencyVariant ? new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getSubId(block, "_emergency"))
                .put(TextureKey.END, TextureMap.getSubId(block, "_top_emergency"))
                .put(TextureKey.PARTICLE, TextureMap.getSubId(block, "_emergency")) : unlitTextureMap;
        Identifier litModel = Models.CUBE_COLUMN.upload(block, "_lit", litTextureMap, generator.modelCollector);
        Identifier litHorizontalModel = Models.CUBE_COLUMN_HORIZONTAL.upload(block, "_lit", litTextureMap, generator.modelCollector);
        Identifier unlitModel = Models.CUBE_COLUMN.upload(block, "_unlit", unlitTextureMap, generator.modelCollector);
        Identifier unlitHorizontalModel = Models.CUBE_COLUMN_HORIZONTAL.upload(block, "_unlit", unlitTextureMap, generator.modelCollector);
        Identifier unpoweredModel = hasEmergencyVariant ? Models.CUBE_COLUMN.upload(block, "_emergency", unpoweredTextureMap, generator.modelCollector) : unlitModel;
        Identifier unpoweredHorizontalModel = hasEmergencyVariant ? Models.CUBE_COLUMN_HORIZONTAL.upload(block, "_emergency", unpoweredTextureMap, generator.modelCollector) : unlitHorizontalModel;

        generator.registerParentedItemModel(block, unlitModel);

        BlockStateVariantMap map = BlockStateVariantMap.create(NeonPillarBlock.AXIS, NeonPillarBlock.ACTIVE, NeonPillarBlock.LIT).register((axis, powered, lit) -> {
            if (axis == Direction.Axis.Y) {
                return this.model(powered ? (lit ? litModel : unlitModel) : unpoweredModel);
            }
            BlockStateVariant variant = this.model(powered ? (lit ? litHorizontalModel : unlitHorizontalModel) : unpoweredHorizontalModel).put(VariantSettings.X, VariantSettings.Rotation.R90);
            if (axis == Direction.Axis.X) {
                return variant.put(VariantSettings.Y, VariantSettings.Rotation.R90);
            }
            return variant;
        });
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(map));
    }

    private void registerNeonTube(BlockStateModelGenerator generator, Block block, boolean hasEmergencyVariant) {
        TextureMap textureMap = new TextureMap().put(TextureKey.TEXTURE, TextureMap.getSubId(block, "_lit"));
        TextureMap unlitTextureMap = new TextureMap().put(TextureKey.TEXTURE, TextureMap.getSubId(block, "_unlit"));

        Identifier model = THICK_BAR.upload(block, textureMap, generator.modelCollector);
        Identifier unlitModel = THICK_BAR.upload(block, "_unlit", unlitTextureMap, generator.modelCollector);

        TextureMap unpoweredTextureMap = new TextureMap().put(TextureKey.TEXTURE, TextureMap.getSubId(block, "_emergency"));
        Identifier unpoweredModel = hasEmergencyVariant ? THICK_BAR.upload(block, "_emergency", unpoweredTextureMap, generator.modelCollector) : unlitModel;

        Identifier topModel = THICK_BAR_TOP.upload(block, textureMap, generator.modelCollector);
        Identifier bottomModel = THICK_BAR_BOTTOM.upload(block, textureMap, generator.modelCollector);

        generator.registerItemModel(block.asItem());

        MultipartBlockStateSupplier blockStateSupplier = MultipartBlockStateSupplier.create(block);
        for (Direction.Axis axis : Direction.Axis.values()) {
            for (Boolean lit : NeonTubeBlock.LIT.getValues()) {
                for (Boolean powered : NeonTubeBlock.ACTIVE.getValues()) {
                    blockStateSupplier.with(
                            When.create().set(BarBlock.AXIS, axis).set(NeonTubeBlock.ACTIVE, powered).set(NeonTubeBlock.LIT, lit),
                            this.rotateForAxis(this.model(powered ? (lit ? model : unlitModel) : unpoweredModel), axis)
                    ).with(
                            When.create().set(BarBlock.AXIS, axis).set(BarBlock.TOP, true),
                            this.rotateForAxis(this.model(topModel), axis)
                    ).with(
                            When.create().set(BarBlock.AXIS, axis).set(BarBlock.BOTTOM, true),
                            this.rotateForAxis(this.model(bottomModel), axis)
                    );
                }
            }
        }
        generator.blockStateCollector.accept(blockStateSupplier);
    }
}
