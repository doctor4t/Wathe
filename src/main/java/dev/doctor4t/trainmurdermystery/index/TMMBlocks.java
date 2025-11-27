package dev.doctor4t.trainmurdermystery.index;

import dev.doctor4t.ratatouille.util.registrar.BlockRegistrar;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.block.*;
import dev.doctor4t.trainmurdermystery.util.BlockSettingsAdditions;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Rarity;

import java.util.function.ToIntFunction;

@SuppressWarnings("unchecked")
public interface TMMBlocks {
    BlockRegistrar registrar = new BlockRegistrar(TMM.MOD_ID);

    // Metallic blocks
    Block TARNISHED_GOLD = registrar.createWithItem("tarnished_gold", new Block(AbstractBlock.Settings.create().strength(-1.0f, 3600000.0f).sounds(BlockSoundGroup.NETHERITE)), TMMItems.BUILDING_GROUP);
    Block TARNISHED_GOLD_STAIRS = registrar.createWithItem("tarnished_gold_stairs", new StairsBlock(TARNISHED_GOLD.getDefaultState(), AbstractBlock.Settings.copy(TARNISHED_GOLD)), TMMItems.BUILDING_GROUP);
    Block TARNISHED_GOLD_SLAB = registrar.createWithItem("tarnished_gold_slab", new SlabBlock(AbstractBlock.Settings.copy(TARNISHED_GOLD)), TMMItems.BUILDING_GROUP);
    Block TARNISHED_GOLD_WALL = registrar.createWithItem("tarnished_gold_wall", new WallBlock(AbstractBlock.Settings.copy(TARNISHED_GOLD).solid()), TMMItems.BUILDING_GROUP);
    Block TARNISHED_GOLD_PILLAR = registrar.createWithItem("tarnished_gold_pillar", new PillarBlock(AbstractBlock.Settings.copy(TARNISHED_GOLD)), TMMItems.BUILDING_GROUP);
    Block GOLD = registrar.createWithItem("gold", new Block(AbstractBlock.Settings.copy(TARNISHED_GOLD)), TMMItems.BUILDING_GROUP);
    Block GOLD_STAIRS = registrar.createWithItem("gold_stairs", new StairsBlock(GOLD.getDefaultState(), AbstractBlock.Settings.copy(GOLD)), TMMItems.BUILDING_GROUP);
    Block GOLD_SLAB = registrar.createWithItem("gold_slab", new SlabBlock(AbstractBlock.Settings.copy(GOLD)), TMMItems.BUILDING_GROUP);
    Block GOLD_WALL = registrar.createWithItem("gold_wall", new WallBlock(AbstractBlock.Settings.copy(GOLD).solid()), TMMItems.BUILDING_GROUP);
    Block GOLD_PILLAR = registrar.createWithItem("gold_pillar", new PillarBlock(AbstractBlock.Settings.copy(GOLD)), TMMItems.BUILDING_GROUP);
    Block PRISTINE_GOLD = registrar.createWithItem("pristine_gold", new Block(AbstractBlock.Settings.copy(TARNISHED_GOLD)), TMMItems.BUILDING_GROUP);
    Block PRISTINE_GOLD_STAIRS = registrar.createWithItem("pristine_gold_stairs", new StairsBlock(PRISTINE_GOLD.getDefaultState(), AbstractBlock.Settings.copy(PRISTINE_GOLD)), TMMItems.BUILDING_GROUP);
    Block PRISTINE_GOLD_SLAB = registrar.createWithItem("pristine_gold_slab", new SlabBlock(AbstractBlock.Settings.copy(PRISTINE_GOLD)), TMMItems.BUILDING_GROUP);
    Block PRISTINE_GOLD_WALL = registrar.createWithItem("pristine_gold_wall", new WallBlock(AbstractBlock.Settings.copy(PRISTINE_GOLD).solid()), TMMItems.BUILDING_GROUP);
    Block PRISTINE_GOLD_PILLAR = registrar.createWithItem("pristine_gold_pillar", new PillarBlock(AbstractBlock.Settings.copy(PRISTINE_GOLD)), TMMItems.BUILDING_GROUP);
    Block WHITE_HULL = registrar.createWithItem("white_hull", new Block(AbstractBlock.Settings.copy(TARNISHED_GOLD).mapColor(MapColor.WHITE)), TMMItems.BUILDING_GROUP);
    Block WHITE_HULL_STAIRS = registrar.createWithItem("white_hull_stairs", new StairsBlock(WHITE_HULL.getDefaultState(), AbstractBlock.Settings.copy(WHITE_HULL)), TMMItems.BUILDING_GROUP);
    Block WHITE_HULL_SLAB = registrar.createWithItem("white_hull_slab", new SlabBlock(AbstractBlock.Settings.copy(WHITE_HULL)), TMMItems.BUILDING_GROUP);
    Block WHITE_HULL_WALL = registrar.createWithItem("white_hull_wall", new WallBlock(AbstractBlock.Settings.copy(WHITE_HULL).solid()), TMMItems.BUILDING_GROUP);
    Block CULLING_WHITE_HULL = registrar.createWithItem("culling_white_hull", new CullingBlock(AbstractBlock.Settings.copy(WHITE_HULL).nonOpaque()), TMMItems.BUILDING_GROUP);
    Block BLACK_HULL = registrar.createWithItem("black_hull", new Block(AbstractBlock.Settings.copy(WHITE_HULL).mapColor(MapColor.BLACK)), TMMItems.BUILDING_GROUP);
    Block BLACK_HULL_STAIRS = registrar.createWithItem("black_hull_stairs", new StairsBlock(BLACK_HULL.getDefaultState(), AbstractBlock.Settings.copy(BLACK_HULL)), TMMItems.BUILDING_GROUP);
    Block BLACK_HULL_SLAB = registrar.createWithItem("black_hull_slab", new SlabBlock(AbstractBlock.Settings.copy(BLACK_HULL)), TMMItems.BUILDING_GROUP);
    Block BLACK_HULL_WALL = registrar.createWithItem("black_hull_wall", new WallBlock(AbstractBlock.Settings.copy(BLACK_HULL).solid()), TMMItems.BUILDING_GROUP);
    Block CULLING_BLACK_HULL = registrar.createWithItem("culling_black_hull", new CullingBlock(AbstractBlock.Settings.copy(BLACK_HULL).nonOpaque()), TMMItems.BUILDING_GROUP);
    Block BLACK_HULL_SHEETS = registrar.createWithItem("black_hull_sheets", new Block(AbstractBlock.Settings.copy(BLACK_HULL)), TMMItems.BUILDING_GROUP);
    Block BLACK_HULL_SHEET_STAIRS = registrar.createWithItem("black_hull_sheet_stairs", new StairsBlock(BLACK_HULL_SHEETS.getDefaultState(), AbstractBlock.Settings.copy(BLACK_HULL_SHEETS)), TMMItems.BUILDING_GROUP);
    Block BLACK_HULL_SHEET_SLAB = registrar.createWithItem("black_hull_sheet_slab", new SlabBlock(AbstractBlock.Settings.copy(BLACK_HULL_SHEETS)), TMMItems.BUILDING_GROUP);
    Block BLACK_HULL_SHEET_WALL = registrar.createWithItem("black_hull_sheet_wall", new WallBlock(AbstractBlock.Settings.copy(BLACK_HULL_SHEETS).solid()), TMMItems.BUILDING_GROUP);
    Block GOLD_BAR = registrar.createWithItem("gold_bar", new BarBlock(AbstractBlock.Settings.copy(TARNISHED_GOLD).nonOpaque().strength(0.5f)), TMMItems.DECORATION_GROUP);
    Block GOLD_LEDGE = registrar.createWithItem("gold_ledge", new LedgeBlock(AbstractBlock.Settings.copy(TARNISHED_GOLD).nonOpaque().strength(0.5f).dynamicBounds()), TMMItems.DECORATION_GROUP);
    Block METAL_SHEET = registrar.createWithItem("metal_sheet", new Block(AbstractBlock.Settings.create().strength(2f).sounds(BlockSoundGroup.COPPER)), TMMItems.BUILDING_GROUP);
    Block METAL_SHEET_STAIRS = registrar.createWithItem("metal_sheet_stairs", new StairsBlock(METAL_SHEET.getDefaultState(), AbstractBlock.Settings.copy(METAL_SHEET)), TMMItems.BUILDING_GROUP);
    Block METAL_SHEET_SLAB = registrar.createWithItem("metal_sheet_slab", new SlabBlock(AbstractBlock.Settings.copy(METAL_SHEET)), TMMItems.BUILDING_GROUP);
    Block METAL_SHEET_WALL = registrar.createWithItem("metal_sheet_wall", new WallBlock(AbstractBlock.Settings.copy(METAL_SHEET).solid()), TMMItems.BUILDING_GROUP);
    Block METAL_SHEET_WALKWAY = registrar.createWithItem("metal_sheet_walkway", new WalkwayBlock(AbstractBlock.Settings.copy(METAL_SHEET).sounds(BlockSoundGroup.COPPER_GRATE).nonOpaque()), TMMItems.BUILDING_GROUP);
    Block METAL_SHEET_DOOR = registrar.createWithItem("metal_sheet_door", new DoorBlock(SetType.METAL_SHEET, AbstractBlock.Settings.create().requiresTool().strength(5.0F).nonOpaque().sounds(BlockSoundGroup.COPPER).pistonBehavior(PistonBehavior.DESTROY)), TMMItems.BUILDING_GROUP);
    Block COCKPIT_DOOR = registrar.createWithItem("cockpit_door", new DoorBlock(SetType.METAL_SHEET, AbstractBlock.Settings.copy(METAL_SHEET_DOOR)), TMMItems.BUILDING_GROUP);
    Block STAINLESS_STEEL = registrar.createWithItem("stainless_steel", new Block(AbstractBlock.Settings.create().strength(-1.0f, 3600000.0f).sounds(BlockSoundGroup.COPPER).requiresTool()), TMMItems.BUILDING_GROUP);
    Block STAINLESS_STEEL_STAIRS = registrar.createWithItem("stainless_steel_stairs", new StairsBlock(STAINLESS_STEEL.getDefaultState(), AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block STAINLESS_STEEL_SLAB = registrar.createWithItem("stainless_steel_slab", new SlabBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block STAINLESS_STEEL_WALL = registrar.createWithItem("stainless_steel_wall", new WallBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL).solid()), TMMItems.BUILDING_GROUP);
    Block STAINLESS_STEEL_WALKWAY = registrar.createWithItem("stainless_steel_walkway", new WalkwayBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL).sounds(BlockSoundGroup.COPPER_GRATE).nonOpaque()), TMMItems.BUILDING_GROUP);
    Block STAINLESS_STEEL_BRANCH = createBranch("stainless_steel_branch", TMMBlocks.STAINLESS_STEEL, registrar);
    Block STAINLESS_STEEL_PILLAR = registrar.createWithItem("stainless_steel_pillar", new PillarBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block DARK_STEEL = registrar.createWithItem("dark_steel", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block DARK_STEEL_STAIRS = registrar.createWithItem("dark_steel_stairs", new StairsBlock(DARK_STEEL.getDefaultState(), AbstractBlock.Settings.copy(DARK_STEEL)), TMMItems.BUILDING_GROUP);
    Block DARK_STEEL_SLAB = registrar.createWithItem("dark_steel_slab", new SlabBlock(AbstractBlock.Settings.copy(DARK_STEEL)), TMMItems.BUILDING_GROUP);
    Block DARK_STEEL_WALL = registrar.createWithItem("dark_steel_wall", new WallBlock(AbstractBlock.Settings.copy(DARK_STEEL).solid()), TMMItems.BUILDING_GROUP);
    Block DARK_STEEL_WALKWAY = registrar.createWithItem("dark_steel_walkway", new WalkwayBlock(AbstractBlock.Settings.copy(DARK_STEEL).sounds(BlockSoundGroup.COPPER_GRATE).nonOpaque()), TMMItems.BUILDING_GROUP);
    Block DARK_STEEL_BRANCH = createBranch("dark_steel_branch", TMMBlocks.DARK_STEEL, registrar);
    Block DARK_STEEL_PILLAR = registrar.createWithItem("dark_steel_pillar", new PillarBlock(AbstractBlock.Settings.copy(DARK_STEEL)), TMMItems.BUILDING_GROUP);
    Block STAINLESS_STEEL_BAR = registrar.createWithItem("stainless_steel_bar", new BarBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL).nonOpaque().strength(0.5f)), TMMItems.DECORATION_GROUP);
    Block RAIL_BEAM = registrar.createWithItem("rail_beam", new RailBeamBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL).solid()), TMMItems.DECORATION_GROUP);

    // Doors
    Block SMALL_GLASS_DOOR = registrar.createWithItem("small_glass_door", new SmallDoorBlock(() -> TMMBlockEntities.SMALL_GLASS_DOOR, AbstractBlock.Settings.create().dynamicBounds().strength(-1, 3600000).mapColor(MapColor.CLEAR).dropsNothing().nonOpaque().allowsSpawning(Blocks::never).pistonBehavior(PistonBehavior.BLOCK).sounds(BlockSoundGroup.COPPER_BULB)), TMMItems.DECORATION_GROUP);
    Block SMALL_WOOD_DOOR = registrar.createWithItem("small_wood_door", new SmallDoorBlock(() -> TMMBlockEntities.SMALL_WOOD_DOOR, AbstractBlock.Settings.copy(SMALL_GLASS_DOOR).sounds(BlockSoundGroup.COPPER)), TMMItems.DECORATION_GROUP);

    // Fancy steel
    Block ANTHRACITE_STEEL = registrar.createWithItem("anthracite_steel", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block ANTHRACITE_STEEL_PANEL = registrar.createWithItem("anthracite_steel_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block ANTHRACITE_STEEL_TILES = registrar.createWithItem("anthracite_steel_tiles", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block ANTHRACITE_STEEL_TILES_PANEL = registrar.createWithItem("anthracite_steel_tiles_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_ANTHRACITE_STEEL = registrar.createWithItem("smooth_anthracite_steel", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_ANTHRACITE_STEEL_STAIRS = registrar.createWithItem("smooth_anthracite_steel_stairs", new StairsBlock(ANTHRACITE_STEEL.getDefaultState(), AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_ANTHRACITE_STEEL_SLAB = registrar.createWithItem("smooth_anthracite_steel_slab", new SlabBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_ANTHRACITE_STEEL_PANEL = registrar.createWithItem("smooth_anthracite_steel_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_ANTHRACITE_STEEL_WALL = registrar.createWithItem("smooth_anthracite_steel_wall", new WallBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL).solid()), TMMItems.BUILDING_GROUP);
    Block ANTHRACITE_STEEL_DOOR = registrar.createWithItem("anthracite_steel_door", new TrainDoorBlock(() -> TMMBlockEntities.ANTHRACITE_STEEL_DOOR, AbstractBlock.Settings.copy(SMALL_GLASS_DOOR).sounds(BlockSoundGroup.COPPER)), TMMItems.DECORATION_GROUP);
    Block KHAKI_STEEL = registrar.createWithItem("khaki_steel", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block KHAKI_STEEL_PANEL = registrar.createWithItem("khaki_steel_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block KHAKI_STEEL_TILES = registrar.createWithItem("khaki_steel_tiles", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block KHAKI_STEEL_TILES_PANEL = registrar.createWithItem("khaki_steel_tiles_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_KHAKI_STEEL = registrar.createWithItem("smooth_khaki_steel", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_KHAKI_STEEL_STAIRS = registrar.createWithItem("smooth_khaki_steel_stairs", new StairsBlock(KHAKI_STEEL.getDefaultState(), AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_KHAKI_STEEL_SLAB = registrar.createWithItem("smooth_khaki_steel_slab", new SlabBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_KHAKI_STEEL_PANEL = registrar.createWithItem("smooth_khaki_steel_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_KHAKI_STEEL_WALL = registrar.createWithItem("smooth_khaki_steel_wall", new WallBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL).solid()), TMMItems.BUILDING_GROUP);
    Block KHAKI_STEEL_DOOR = registrar.createWithItem("khaki_steel_door", new TrainDoorBlock(() -> TMMBlockEntities.KHAKI_STEEL_DOOR, AbstractBlock.Settings.copy(SMALL_GLASS_DOOR).sounds(BlockSoundGroup.COPPER)), TMMItems.DECORATION_GROUP);
    Block MAROON_STEEL = registrar.createWithItem("maroon_steel", new Block(AbstractBlock.Settings.copy(DARK_STEEL)), TMMItems.BUILDING_GROUP);
    Block MAROON_STEEL_PANEL = registrar.createWithItem("maroon_steel_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block MAROON_STEEL_TILES = registrar.createWithItem("maroon_steel_tiles", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block MAROON_STEEL_TILES_PANEL = registrar.createWithItem("maroon_steel_tiles_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MAROON_STEEL = registrar.createWithItem("smooth_maroon_steel", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MAROON_STEEL_STAIRS = registrar.createWithItem("smooth_maroon_steel_stairs", new StairsBlock(MAROON_STEEL.getDefaultState(), AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MAROON_STEEL_SLAB = registrar.createWithItem("smooth_maroon_steel_slab", new SlabBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MAROON_STEEL_PANEL = registrar.createWithItem("smooth_maroon_steel_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MAROON_STEEL_WALL = registrar.createWithItem("smooth_maroon_steel_wall", new WallBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL).solid()), TMMItems.BUILDING_GROUP);
    Block MAROON_STEEL_DOOR = registrar.createWithItem("maroon_steel_door", new TrainDoorBlock(() -> TMMBlockEntities.MAROON_STEEL_DOOR, AbstractBlock.Settings.copy(SMALL_GLASS_DOOR).sounds(BlockSoundGroup.COPPER)), TMMItems.DECORATION_GROUP);
    Block MUNTZ_STEEL = registrar.createWithItem("muntz_steel", new Block(AbstractBlock.Settings.copy(DARK_STEEL)), TMMItems.BUILDING_GROUP);
    Block MUNTZ_STEEL_PANEL = registrar.createWithItem("muntz_steel_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block MUNTZ_STEEL_TILES = registrar.createWithItem("muntz_steel_tiles", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block MUNTZ_STEEL_TILES_PANEL = registrar.createWithItem("muntz_steel_tiles_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MUNTZ_STEEL = registrar.createWithItem("smooth_muntz_steel", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MUNTZ_STEEL_STAIRS = registrar.createWithItem("smooth_muntz_steel_stairs", new StairsBlock(MUNTZ_STEEL.getDefaultState(), AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MUNTZ_STEEL_SLAB = registrar.createWithItem("smooth_muntz_steel_slab", new SlabBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MUNTZ_STEEL_PANEL = registrar.createWithItem("smooth_muntz_steel_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MUNTZ_STEEL_WALL = registrar.createWithItem("smooth_muntz_steel_wall", new WallBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL).solid()), TMMItems.BUILDING_GROUP);
    Block MUNTZ_STEEL_DOOR = registrar.createWithItem("muntz_steel_door", new TrainDoorBlock(() -> TMMBlockEntities.MUNTZ_STEEL_DOOR, AbstractBlock.Settings.copy(SMALL_GLASS_DOOR).sounds(BlockSoundGroup.COPPER)), TMMItems.DECORATION_GROUP);
    Block NAVY_STEEL = registrar.createWithItem("navy_steel", new Block(AbstractBlock.Settings.copy(DARK_STEEL)), TMMItems.BUILDING_GROUP);
    Block NAVY_STEEL_PANEL = registrar.createWithItem("navy_steel_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block NAVY_STEEL_TILES = registrar.createWithItem("navy_steel_tiles", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block NAVY_STEEL_TILES_PANEL = registrar.createWithItem("navy_steel_tiles_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_NAVY_STEEL = registrar.createWithItem("smooth_navy_steel", new Block(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_NAVY_STEEL_STAIRS = registrar.createWithItem("smooth_navy_steel_stairs", new StairsBlock(NAVY_STEEL.getDefaultState(), AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_NAVY_STEEL_SLAB = registrar.createWithItem("smooth_navy_steel_slab", new SlabBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_NAVY_STEEL_PANEL = registrar.createWithItem("smooth_navy_steel_panel", new PanelBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_NAVY_STEEL_WALL = registrar.createWithItem("smooth_navy_steel_wall", new WallBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL).solid()), TMMItems.BUILDING_GROUP);
    Block NAVY_STEEL_DOOR = registrar.createWithItem("navy_steel_door", new TrainDoorBlock(() -> TMMBlockEntities.NAVY_STEEL_DOOR, AbstractBlock.Settings.copy(SMALL_GLASS_DOOR).sounds(BlockSoundGroup.COPPER)), TMMItems.DECORATION_GROUP);

    // Glass
    Block HULL_GLASS = registrar.createWithItem("hull_glass", new PrivacyGlassBlock(AbstractBlock.Settings.copy(Blocks.BLACK_STAINED_GLASS).strength(-1.0f, 3600000.0f)), TMMItems.BUILDING_GROUP);
    Block RHOMBUS_HULL_GLASS = registrar.createWithItem("rhombus_hull_glass", new StainedGlassBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_STAINED_GLASS).strength(-1.0f, 3600000.0f)), TMMItems.BUILDING_GROUP);
    Block RHOMBUS_GLASS = registrar.createWithItem("rhombus_glass", new StainedGlassBlock(DyeColor.BLACK, AbstractBlock.Settings.copy(Blocks.BLACK_STAINED_GLASS)), TMMItems.BUILDING_GROUP);
    Block GOLDEN_GLASS_PANEL = registrar.createWithItem("golden_glass_panel", new GlassPanelBlock(AbstractBlock.Settings.create().strength(0.3f).sounds(BlockSoundGroup.GLASS).allowsSpawning(Blocks::never)), TMMItems.DECORATION_GROUP);
    Block PRIVACY_GLASS_PANEL = registrar.createWithItem("privacy_glass_panel", new PrivacyGlassPanelBlock(AbstractBlock.Settings.create().strength(0.3f).sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(Blocks::never)), TMMItems.DECORATION_GROUP);
    Block CULLING_GLASS = registrar.createWithItem("culling_glass", new CullingGlassBlock(AbstractBlock.Settings.create().solid().strength(-1.0f, 3600000.0f).allowsSpawning(Blocks::never).sounds(BlockSoundGroup.GLASS)), TMMItems.DECORATION_GROUP);

    // Stones
    Block MARBLE = registrar.createWithItem("marble", new Block(AbstractBlock.Settings.create().strength(2f).sounds(BlockSoundGroup.CALCITE)), TMMItems.BUILDING_GROUP);
    Block MARBLE_STAIRS = registrar.createWithItem("marble_stairs", new StairsBlock(MARBLE.getDefaultState(), AbstractBlock.Settings.copy(MARBLE)), TMMItems.BUILDING_GROUP);
    Block MARBLE_SLAB = registrar.createWithItem("marble_slab", new SlabBlock(AbstractBlock.Settings.copy(MARBLE)), TMMItems.BUILDING_GROUP);
    Block MARBLE_WALL = registrar.createWithItem("marble_wall", new WallBlock(AbstractBlock.Settings.copy(MARBLE).solid()), TMMItems.BUILDING_GROUP);
    Block MARBLE_MOSAIC = registrar.createWithItem("marble_mosaic", new GlazedTerracottaBlock(AbstractBlock.Settings.copy(MARBLE)), TMMItems.BUILDING_GROUP);
    Block DARK_MARBLE = registrar.createWithItem("dark_marble", new Block(AbstractBlock.Settings.copy(MARBLE)), TMMItems.BUILDING_GROUP);
    Block DARK_MARBLE_STAIRS = registrar.createWithItem("dark_marble_stairs", new StairsBlock(DARK_MARBLE.getDefaultState(), AbstractBlock.Settings.copy(DARK_MARBLE)), TMMItems.BUILDING_GROUP);
    Block DARK_MARBLE_SLAB = registrar.createWithItem("dark_marble_slab", new SlabBlock(AbstractBlock.Settings.copy(DARK_MARBLE)), TMMItems.BUILDING_GROUP);
    Block DARK_MARBLE_WALL = registrar.createWithItem("dark_marble_wall", new WallBlock(AbstractBlock.Settings.copy(DARK_MARBLE).solid()), TMMItems.BUILDING_GROUP);
    Block MARBLE_TILES = registrar.createWithItem("marble_tiles", new Block(AbstractBlock.Settings.create().strength(2f).sounds(BlockSoundGroup.CALCITE)), TMMItems.BUILDING_GROUP);
    Block MARBLE_TILE_STAIRS = registrar.createWithItem("marble_tile_stairs", new StairsBlock(MARBLE_TILES.getDefaultState(), AbstractBlock.Settings.copy(MARBLE_TILES)), TMMItems.BUILDING_GROUP);
    Block MARBLE_TILE_SLAB = registrar.createWithItem("marble_tile_slab", new SlabBlock(AbstractBlock.Settings.copy(MARBLE_TILES)), TMMItems.BUILDING_GROUP);
    Block MARBLE_TILE_WALL = registrar.createWithItem("marble_tile_wall", new WallBlock(AbstractBlock.Settings.copy(MARBLE_TILES).solid()), TMMItems.BUILDING_GROUP);

    // Carpets
    Block RED_MOQUETTE = registrar.createWithItem("red_moquette", new Block(AbstractBlock.Settings.copy(Blocks.RED_WOOL).strength(-1.0f, 3600000.0f)), TMMItems.BUILDING_GROUP);
    Block BROWN_MOQUETTE = registrar.createWithItem("brown_moquette", new Block(AbstractBlock.Settings.copy(Blocks.BROWN_WOOL).strength(-1.0f, 3600000.0f)), TMMItems.BUILDING_GROUP);
    Block BLUE_MOQUETTE = registrar.createWithItem("blue_moquette", new Block(AbstractBlock.Settings.copy(Blocks.BLUE_WOOL).strength(-1.0f, 3600000.0f)), TMMItems.BUILDING_GROUP);

    // Woods
    Block MAHOGANY_PLANKS = registrar.createWithItem("mahogany_planks", new Block(AbstractBlock.Settings.copy(Blocks.MANGROVE_PLANKS).strength(-1.0f, 3600000.0f).sounds(BlockSoundGroup.CHERRY_WOOD)), TMMItems.BUILDING_GROUP);
    Block MAHOGANY_STAIRS = registrar.createWithItem("mahogany_stairs", new StairsBlock(MAHOGANY_PLANKS.getDefaultState(), AbstractBlock.Settings.copy(MAHOGANY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block MAHOGANY_SLAB = registrar.createWithItem("mahogany_slab", new SlabBlock(AbstractBlock.Settings.copy(MAHOGANY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block MAHOGANY_HERRINGBONE = registrar.createWithItem("mahogany_herringbone", new Block(AbstractBlock.Settings.copy(MAHOGANY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block MAHOGANY_HERRINGBONE_STAIRS = registrar.createWithItem("mahogany_herringbone_stairs", new StairsBlock(MAHOGANY_HERRINGBONE.getDefaultState(), AbstractBlock.Settings.copy(MAHOGANY_HERRINGBONE)), TMMItems.BUILDING_GROUP);
    Block MAHOGANY_HERRINGBONE_SLAB = registrar.createWithItem("mahogany_herringbone_slab", new SlabBlock(AbstractBlock.Settings.copy(MAHOGANY_HERRINGBONE)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MAHOGANY = registrar.createWithItem("smooth_mahogany", new Block(AbstractBlock.Settings.copy(MAHOGANY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MAHOGANY_STAIRS = registrar.createWithItem("smooth_mahogany_stairs", new StairsBlock(SMOOTH_MAHOGANY.getDefaultState(), AbstractBlock.Settings.copy(SMOOTH_MAHOGANY)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_MAHOGANY_SLAB = registrar.createWithItem("smooth_mahogany_slab", new SlabBlock(AbstractBlock.Settings.copy(SMOOTH_MAHOGANY)), TMMItems.BUILDING_GROUP);
    Block MAHOGANY_PANEL = registrar.createWithItem("mahogany_panel", new PanelBlock(AbstractBlock.Settings.copy(MAHOGANY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block MAHOGANY_CABINET = registrar.createWithItem("mahogany_cabinet", new CabinetBlock(AbstractBlock.Settings.copy(MAHOGANY_PLANKS).nonOpaque()), TMMItems.BUILDING_GROUP);
    Block MAHOGANY_BOOKSHELF = registrar.createWithItem("mahogany_bookshelf", new Block(AbstractBlock.Settings.copy(MAHOGANY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block BUBINGA_PLANKS = registrar.createWithItem("bubinga_planks", new Block(AbstractBlock.Settings.copy(Blocks.ACACIA_PLANKS).strength(-1.0f, 3600000.0f).sounds(BlockSoundGroup.CHERRY_WOOD)), TMMItems.BUILDING_GROUP);
    Block BUBINGA_STAIRS = registrar.createWithItem("bubinga_stairs", new StairsBlock(BUBINGA_PLANKS.getDefaultState(), AbstractBlock.Settings.copy(BUBINGA_PLANKS)), TMMItems.BUILDING_GROUP);
    Block BUBINGA_SLAB = registrar.createWithItem("bubinga_slab", new SlabBlock(AbstractBlock.Settings.copy(BUBINGA_PLANKS)), TMMItems.BUILDING_GROUP);
    Block BUBINGA_HERRINGBONE = registrar.createWithItem("bubinga_herringbone", new Block(AbstractBlock.Settings.copy(BUBINGA_PLANKS)), TMMItems.BUILDING_GROUP);
    Block BUBINGA_HERRINGBONE_STAIRS = registrar.createWithItem("bubinga_herringbone_stairs", new StairsBlock(BUBINGA_HERRINGBONE.getDefaultState(), AbstractBlock.Settings.copy(MAHOGANY_HERRINGBONE)), TMMItems.BUILDING_GROUP);
    Block BUBINGA_HERRINGBONE_SLAB = registrar.createWithItem("bubinga_herringbone_slab", new SlabBlock(AbstractBlock.Settings.copy(BUBINGA_HERRINGBONE)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_BUBINGA = registrar.createWithItem("smooth_bubinga", new Block(AbstractBlock.Settings.copy(BUBINGA_PLANKS)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_BUBINGA_STAIRS = registrar.createWithItem("smooth_bubinga_stairs", new StairsBlock(SMOOTH_BUBINGA.getDefaultState(), AbstractBlock.Settings.copy(SMOOTH_BUBINGA)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_BUBINGA_SLAB = registrar.createWithItem("smooth_bubinga_slab", new SlabBlock(AbstractBlock.Settings.copy(SMOOTH_BUBINGA)), TMMItems.BUILDING_GROUP);
    Block BUBINGA_PANEL = registrar.createWithItem("bubinga_panel", new PanelBlock(AbstractBlock.Settings.copy(BUBINGA_PLANKS)), TMMItems.BUILDING_GROUP);
    Block BUBINGA_CABINET = registrar.createWithItem("bubinga_cabinet", new CabinetBlock(AbstractBlock.Settings.copy(BUBINGA_PLANKS).nonOpaque()), TMMItems.BUILDING_GROUP);
    Block BUBINGA_BOOKSHELF = registrar.createWithItem("bubinga_bookshelf", new Block(AbstractBlock.Settings.copy(BUBINGA_PLANKS)), TMMItems.BUILDING_GROUP);
    Block EBONY_PLANKS = registrar.createWithItem("ebony_planks", new Block(AbstractBlock.Settings.copy(Blocks.DARK_OAK_PLANKS).strength(-1.0f, 3600000.0f).sounds(BlockSoundGroup.CHERRY_WOOD)), TMMItems.BUILDING_GROUP);
    Block EBONY_STAIRS = registrar.createWithItem("ebony_stairs", new StairsBlock(EBONY_PLANKS.getDefaultState(), AbstractBlock.Settings.copy(EBONY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block EBONY_SLAB = registrar.createWithItem("ebony_slab", new SlabBlock(AbstractBlock.Settings.copy(EBONY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block EBONY_HERRINGBONE = registrar.createWithItem("ebony_herringbone", new Block(AbstractBlock.Settings.copy(EBONY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block EBONY_HERRINGBONE_STAIRS = registrar.createWithItem("ebony_herringbone_stairs", new StairsBlock(EBONY_HERRINGBONE.getDefaultState(), AbstractBlock.Settings.copy(MAHOGANY_HERRINGBONE)), TMMItems.BUILDING_GROUP);
    Block EBONY_HERRINGBONE_SLAB = registrar.createWithItem("ebony_herringbone_slab", new SlabBlock(AbstractBlock.Settings.copy(EBONY_HERRINGBONE)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_EBONY = registrar.createWithItem("smooth_ebony", new Block(AbstractBlock.Settings.copy(EBONY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_EBONY_STAIRS = registrar.createWithItem("smooth_ebony_stairs", new StairsBlock(SMOOTH_EBONY.getDefaultState(), AbstractBlock.Settings.copy(SMOOTH_EBONY)), TMMItems.BUILDING_GROUP);
    Block SMOOTH_EBONY_SLAB = registrar.createWithItem("smooth_ebony_slab", new SlabBlock(AbstractBlock.Settings.copy(SMOOTH_EBONY)), TMMItems.BUILDING_GROUP);
    Block EBONY_PANEL = registrar.createWithItem("ebony_panel", new PanelBlock(AbstractBlock.Settings.copy(EBONY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block EBONY_CABINET = registrar.createWithItem("ebony_cabinet", new CabinetBlock(AbstractBlock.Settings.copy(EBONY_PLANKS).nonOpaque()), TMMItems.BUILDING_GROUP);
    Block TRIMMED_EBONY_STAIRS = registrar.createWithItem("trimmed_ebony_stairs", new TrimmedStairsBlock(AbstractBlock.Settings.copy(EBONY_PLANKS)), TMMItems.BUILDING_GROUP);
    Block EBONY_BOOKSHELF = registrar.createWithItem("ebony_bookshelf", new Block(AbstractBlock.Settings.copy(EBONY_PLANKS)), TMMItems.BUILDING_GROUP);

    // Vents
    Block STAINLESS_STEEL_VENT_SHAFT = registrar.createWithItem("stainless_steel_vent_shaft", new VentShaftBlock(AbstractBlock.Settings.create().strength(-1.0f, 3600000.0f).sounds(TMMSounds.VENT_SHAFT).mapColor(MapColor.GRAY)), TMMItems.DECORATION_GROUP);
    Block STAINLESS_STEEL_VENT_HATCH = registrar.createWithItem("stainless_steel_vent_hatch", new VentHatchBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL_VENT_SHAFT).strength(0.3f).sounds(BlockSoundGroup.COPPER).nonOpaque()), TMMItems.DECORATION_GROUP);
    Block DARK_STEEL_VENT_HATCH = registrar.createWithItem("dark_steel_vent_hatch", new VentHatchBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL_VENT_HATCH)), TMMItems.DECORATION_GROUP);
    Block TARNISHED_GOLD_VENT_HATCH = registrar.createWithItem("tarnished_gold_vent_hatch", new VentHatchBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL_VENT_HATCH)), TMMItems.DECORATION_GROUP);
    Block DARK_STEEL_VENT_SHAFT = registrar.createWithItem("dark_steel_vent_shaft", new VentShaftBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL_VENT_SHAFT)), TMMItems.DECORATION_GROUP);
    Block TARNISHED_GOLD_VENT_SHAFT = registrar.createWithItem("tarnished_gold_vent_shaft", new VentShaftBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL_VENT_SHAFT)), TMMItems.DECORATION_GROUP);

    // Furniture / Decor
    Block STAINLESS_STEEL_LADDER = registrar.createWithItem("stainless_steel_ladder", new TrainLadderBlock(AbstractBlock.Settings.create().nonOpaque().strength(0.5f).sounds(BlockSoundGroup.LANTERN)), TMMItems.DECORATION_GROUP);
    Block OAK_BRANCH = createBranch("oak_branch", Blocks.OAK_WOOD, registrar);
    Block SPRUCE_BRANCH = createBranch("spruce_branch", Blocks.SPRUCE_WOOD, registrar);
    Block BIRCH_BRANCH = createBranch("birch_branch", Blocks.BIRCH_WOOD, registrar);
    Block JUNGLE_BRANCH = createBranch("jungle_branch", Blocks.JUNGLE_WOOD, registrar);
    Block ACACIA_BRANCH = createBranch("acacia_branch", Blocks.ACACIA_WOOD, registrar);
    Block DARK_OAK_BRANCH = createBranch("dark_oak_branch", Blocks.DARK_OAK_WOOD, registrar);
    Block MANGROVE_BRANCH = createBranch("mangrove_branch", Blocks.MANGROVE_WOOD, registrar);
    Block CHERRY_BRANCH = createBranch("cherry_branch", Blocks.CHERRY_WOOD, registrar);
    Block BAMBOO_POLE = createBranch("bamboo_pole", Blocks.BAMBOO_BLOCK, registrar);
    Block CRIMSON_STIPE = createBranch("crimson_stipe", Blocks.CRIMSON_HYPHAE, registrar);
    Block WARPED_STIPE = createBranch("warped_stipe", Blocks.WARPED_HYPHAE, registrar);
    Block STRIPPED_OAK_BRANCH = createBranch("stripped_oak_branch", Blocks.STRIPPED_OAK_WOOD, registrar);
    Block STRIPPED_SPRUCE_BRANCH = createBranch("stripped_spruce_branch", Blocks.STRIPPED_SPRUCE_WOOD, registrar);
    Block STRIPPED_BIRCH_BRANCH = createBranch("stripped_birch_branch", Blocks.STRIPPED_BIRCH_WOOD, registrar);
    Block STRIPPED_JUNGLE_BRANCH = createBranch("stripped_jungle_branch", Blocks.STRIPPED_JUNGLE_WOOD, registrar);
    Block STRIPPED_ACACIA_BRANCH = createBranch("stripped_acacia_branch", Blocks.STRIPPED_ACACIA_WOOD, registrar);
    Block STRIPPED_DARK_OAK_BRANCH = createBranch("stripped_dark_oak_branch", Blocks.STRIPPED_DARK_OAK_WOOD, registrar);
    Block STRIPPED_MANGROVE_BRANCH = createBranch("stripped_mangrove_branch", Blocks.STRIPPED_MANGROVE_WOOD, registrar);
    Block STRIPPED_CHERRY_BRANCH = createBranch("stripped_cherry_branch", Blocks.STRIPPED_CHERRY_WOOD, registrar);
    Block STRIPPED_BAMBOO_POLE = createBranch("stripped_bamboo_pole", Blocks.STRIPPED_BAMBOO_BLOCK, registrar);
    Block STRIPPED_CRIMSON_STIPE = createBranch("stripped_crimson_stipe", Blocks.STRIPPED_CRIMSON_HYPHAE, registrar);
    Block STRIPPED_WARPED_STIPE = createBranch("stripped_warped_stipe", Blocks.STRIPPED_WARPED_HYPHAE, registrar);
    Block TRIMMED_RAILING_POST = registrar.create("trimmed_railing_post", new RailingPostBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.CHERRY_WOOD_HANGING_SIGN).strength(1f).nonOpaque()));
    Block DIAGONAL_TRIMMED_RAILING = registrar.create("diagonal_trimmed_railing", new DiagonalRailingBlock(AbstractBlock.Settings.copy(TRIMMED_RAILING_POST)));
    Block TRIMMED_RAILING = registrar.createWithItem("trimmed_railing", new RailingBlock(DIAGONAL_TRIMMED_RAILING, TRIMMED_RAILING_POST, AbstractBlock.Settings.copy(TRIMMED_RAILING_POST)), TMMItems.DECORATION_GROUP);
    Block PANEL_STRIPES = registrar.createWithItem("panel_stripes", new PanelStripesBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.CHISELED_BOOKSHELF).strength(0.5f).nonOpaque()), TMMItems.DECORATION_GROUP);
    Block CARGO_BOX = registrar.createWithItem("cargo_box", new CargoBoxBlock(AbstractBlock.Settings.create().strength(1).sounds(BlockSoundGroup.COPPER).mapColor(MapColor.GRAY).nonOpaque()), TMMItems.DECORATION_GROUP);
    Block WHITE_LOUNGE_COUCH = registrar.createWithItem("white_lounge_couch", new LoungeCouch(AbstractBlock.Settings.create().nonOpaque().strength(0.5f).sounds(BlockSoundGroup.CHISELED_BOOKSHELF)), TMMItems.DECORATION_GROUP);
    Block WHITE_OTTOMAN = registrar.createWithItem("white_ottoman", new OttomanBlock(AbstractBlock.Settings.copy(WHITE_LOUNGE_COUCH)), TMMItems.DECORATION_GROUP);
    Block BLUE_LOUNGE_COUCH = registrar.createWithItem("blue_lounge_couch", new LoungeCouch(AbstractBlock.Settings.copy(WHITE_LOUNGE_COUCH)), TMMItems.DECORATION_GROUP);
    Block GREEN_LOUNGE_COUCH = registrar.createWithItem("green_lounge_couch", new LoungeCouch(AbstractBlock.Settings.copy(WHITE_LOUNGE_COUCH)), TMMItems.DECORATION_GROUP);
    Block RED_LEATHER_COUCH = registrar.createWithItem("red_leather_couch", new LeatherCouch(AbstractBlock.Settings.copy(WHITE_LOUNGE_COUCH)), TMMItems.DECORATION_GROUP);
    Block BROWN_LEATHER_COUCH = registrar.createWithItem("brown_leather_couch", new LeatherCouch(AbstractBlock.Settings.copy(WHITE_LOUNGE_COUCH)), TMMItems.DECORATION_GROUP);
    Block BEIGE_LEATHER_COUCH = registrar.createWithItem("beige_leather_couch", new LeatherCouch(AbstractBlock.Settings.copy(WHITE_LOUNGE_COUCH)), TMMItems.DECORATION_GROUP);
    Block COFFEE_TABLE = registrar.createWithItem("coffee_table", new CoffeeTableBlock(AbstractBlock.Settings.copy(WHITE_LOUNGE_COUCH)), TMMItems.DECORATION_GROUP);
    Block BAR_TABLE = registrar.createWithItem("bar_table", new BarTableBlock(AbstractBlock.Settings.copy(WHITE_LOUNGE_COUCH)), TMMItems.DECORATION_GROUP);
    Block BAR_STOOL = registrar.createWithItem("bar_stool", new BarStoolBlock(AbstractBlock.Settings.copy(WHITE_LOUNGE_COUCH)), TMMItems.DECORATION_GROUP);
    Block WHITE_TRIMMED_BED = registrar.createWithItem("white_trimmed_bed", new TrimmedBedBlock(AbstractBlock.Settings.copy(WHITE_LOUNGE_COUCH)), TMMItems.DECORATION_GROUP);
    Block RED_TRIMMED_BED = registrar.createWithItem("red_trimmed_bed", new TrimmedBedBlock(AbstractBlock.Settings.copy(WHITE_LOUNGE_COUCH)), TMMItems.DECORATION_GROUP);
    Block HORN = registrar.createWithItem("horn", new HornBlock(AbstractBlock.Settings.copy(Blocks.CHAIN).nonOpaque().noCollision()), TMMItems.DECORATION_GROUP);

    // Lamps
    Block TRIMMED_LANTERN = registrar.createWithItem("trimmed_lantern", new TrimmedLanternBlock(AbstractBlock.Settings.create().strength(0.5f).nonOpaque().luminance(createLightLevelFromLitPoweredBlockState(15)).sounds(BlockSoundGroup.LANTERN)), TMMItems.DECORATION_GROUP);
    Block WALL_LAMP = registrar.createWithItem("wall_lamp", new WallLampBlock(AbstractBlock.Settings.copy(TRIMMED_LANTERN).luminance(createLightLevelFromLitPoweredBlockState(15))), TMMItems.DECORATION_GROUP);
    Block NEON_PILLAR = registrar.createWithItem("neon_pillar", new NeonPillarBlock(AbstractBlock.Settings.create().strength(1.5f).sounds(BlockSoundGroup.COPPER_BULB).luminance(createLightLevelFromLitPoweredBlockState(15))), TMMItems.DECORATION_GROUP);
    Block NEON_TUBE = registrar.createWithItem("neon_tube", new NeonTubeBlock(AbstractBlock.Settings.create().strength(1.5f).sounds(BlockSoundGroup.COPPER_BULB).luminance(createLightLevelFromLitPoweredBlockState(15))), TMMItems.DECORATION_GROUP);

    Block SMALL_BUTTON = registrar.createWithItem("small_button", new SmallButtonBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.CHERRY_WOOD).nonOpaque().noCollision().strength(-1.0f, 3600000.0f)), TMMItems.DECORATION_GROUP);
    Block ELEVATOR_BUTTON = registrar.createWithItem("elevator_button", new ElevatorButtonBlock(AbstractBlock.Settings.copy(SMALL_BUTTON)), TMMItems.DECORATION_GROUP);
    Block STAINLESS_STEEL_SPRINKLER = registrar.createWithItem("stainless_steel_sprinkler", new SprinklerBlock(AbstractBlock.Settings.create().strength(0.5f).nonOpaque().sounds(BlockSoundGroup.LANTERN)), TMMItems.DECORATION_GROUP);
    Block GOLD_SPRINKLER = registrar.createWithItem("gold_sprinkler", new SprinklerBlock(AbstractBlock.Settings.copy(STAINLESS_STEEL_SPRINKLER)), TMMItems.DECORATION_GROUP);
    Block GOLD_ORNAMENT = registrar.createWithItem("gold_ornament", new OrnamentBlock(AbstractBlock.Settings.create().nonOpaque().noCollision().strength(0.25f).sounds(BlockSoundGroup.COPPER)), TMMItems.DECORATION_GROUP);

    // Wheels
    Block WHEEL = registrar.createWithItem("wheel", new WheelBlock(AbstractBlock.Settings.copy(DARK_STEEL).nonOpaque().sounds(BlockSoundGroup.COPPER)), TMMItems.DECORATION_GROUP);
    Block RUSTED_WHEEL = registrar.createWithItem("rusted_wheel", new WheelBlock(AbstractBlock.Settings.copy(DARK_STEEL).nonOpaque().sounds(BlockSoundGroup.COPPER)), TMMItems.DECORATION_GROUP);

    // Platters
    Block FOOD_PLATTER = registrar.createWithItem("food_platter", new FoodPlatterBlock(
            AbstractBlock.Settings.copy(Blocks.WHITE_GLAZED_TERRACOTTA)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.COPPER)
                    .breakInstantly()
                    .noCollision()
    ), TMMItems.DECORATION_GROUP);
    Block DRINK_TRAY = registrar.createWithItem("drink_tray", new DrinkTrayBlock(
            AbstractBlock.Settings.copy(Blocks.WHITE_GLAZED_TERRACOTTA)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.CHERRY_WOOD)
                    .breakInstantly()
    ), TMMItems.DECORATION_GROUP);
    Block CHIMNEY = registrar.createWithItem("chimney", new ChimneyBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK).noCollision()), TMMItems.DECORATION_GROUP);

    // Op
    Block BARRIER_PANEL = registrar.createWithItem("barrier_panel", new BarrierPanelBlock(AbstractBlock.Settings.copy(ANTHRACITE_STEEL_PANEL).strength(-1.0F, 3600000.8F).nonOpaque().sounds(BlockSoundGroup.STONE)), new Item.Settings().rarity(Rarity.EPIC), ItemGroups.OPERATOR);
    Block LIGHT_BARRIER = registrar.createWithItem("light_barrier", new LightBarrierBlock(((BlockSettingsAdditions) AbstractBlock.Settings.copy(Blocks.BARRIER)).tmm$setCollidable(false)), new Item.Settings().rarity(Rarity.EPIC), ItemGroups.OPERATOR);

    private static Block createBranch(String name, Block wood, BlockRegistrar registrar) {
        return registrar.createWithItem(name, new BranchBlock(AbstractBlock.Settings.copy(wood).mapColor(wood.getDefaultMapColor())), TMMItems.DECORATION_GROUP);
    }

    private static ToIntFunction<BlockState> createLightLevelFromLitPoweredBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) && state.get(TMMProperties.ACTIVE) ? litLevel : 0;
    }

    interface Family {
        BlockFamily TARNISHED_GOLD = new BlockFamily.Builder(TMMBlocks.TARNISHED_GOLD)
                .stairs(TARNISHED_GOLD_STAIRS)
                .slab(TARNISHED_GOLD_SLAB)
                .wall(TARNISHED_GOLD_WALL)
                .build();
        BlockFamily GOLD = new BlockFamily.Builder(TMMBlocks.GOLD)
                .stairs(GOLD_STAIRS)
                .slab(GOLD_SLAB)
                .wall(GOLD_WALL)
                .build();
        BlockFamily PRISTINE_GOLD = new BlockFamily.Builder(TMMBlocks.PRISTINE_GOLD)
                .stairs(PRISTINE_GOLD_STAIRS)
                .slab(PRISTINE_GOLD_SLAB)
                .wall(PRISTINE_GOLD_WALL)
                .build();

        BlockFamily METAL_SHEET = new BlockFamily.Builder(TMMBlocks.METAL_SHEET)
                .stairs(METAL_SHEET_STAIRS)
                .slab(METAL_SHEET_SLAB)
                .wall(METAL_SHEET_WALL)
                .door(METAL_SHEET_DOOR)
                .build();

        BlockFamily STAINLESS_STEEL = new BlockFamily.Builder(TMMBlocks.STAINLESS_STEEL)
                .stairs(STAINLESS_STEEL_STAIRS)
                .slab(STAINLESS_STEEL_SLAB)
                .wall(STAINLESS_STEEL_WALL)
                .build();

        BlockFamily DARK_STEEL = new BlockFamily.Builder(TMMBlocks.DARK_STEEL)
                .stairs(DARK_STEEL_STAIRS)
                .slab(DARK_STEEL_SLAB)
                .wall(DARK_STEEL_WALL)
                .build();

        BlockFamily SMOOTH_ANTHRACITE_STEEL = new BlockFamily.Builder(TMMBlocks.SMOOTH_ANTHRACITE_STEEL)
                .stairs(SMOOTH_ANTHRACITE_STEEL_STAIRS)
                .slab(SMOOTH_ANTHRACITE_STEEL_SLAB)
                .wall(SMOOTH_ANTHRACITE_STEEL_WALL)
                .build();

        BlockFamily SMOOTH_KHAKI_STEEL = new BlockFamily.Builder(TMMBlocks.SMOOTH_KHAKI_STEEL)
                .stairs(SMOOTH_KHAKI_STEEL_STAIRS)
                .slab(SMOOTH_KHAKI_STEEL_SLAB)
                .wall(SMOOTH_KHAKI_STEEL_WALL)
                .build();

        BlockFamily SMOOTH_MAROON_STEEL = new BlockFamily.Builder(TMMBlocks.SMOOTH_MAROON_STEEL)
                .stairs(SMOOTH_MAROON_STEEL_STAIRS)
                .slab(SMOOTH_MAROON_STEEL_SLAB)
                .wall(SMOOTH_MAROON_STEEL_WALL)
                .build();

        BlockFamily SMOOTH_MUNTZ_STEEL = new BlockFamily.Builder(TMMBlocks.SMOOTH_MUNTZ_STEEL)
                .stairs(SMOOTH_MUNTZ_STEEL_STAIRS)
                .slab(SMOOTH_MUNTZ_STEEL_SLAB)
                .wall(SMOOTH_MUNTZ_STEEL_WALL)
                .build();

        BlockFamily SMOOTH_NAVY_STEEL = new BlockFamily.Builder(TMMBlocks.SMOOTH_NAVY_STEEL)
                .stairs(SMOOTH_NAVY_STEEL_STAIRS)
                .slab(SMOOTH_NAVY_STEEL_SLAB)
                .wall(SMOOTH_NAVY_STEEL_WALL)
                .build();

        BlockFamily MARBLE = new BlockFamily.Builder(TMMBlocks.MARBLE)
                .stairs(MARBLE_STAIRS)
                .slab(MARBLE_SLAB)
                .wall(MARBLE_WALL)
                .build();

        BlockFamily MARBLE_TILE = new BlockFamily.Builder(TMMBlocks.MARBLE_TILES)
                .stairs(MARBLE_TILE_STAIRS)
                .slab(MARBLE_TILE_SLAB)
                .wall(MARBLE_TILE_WALL)
                .build();

        BlockFamily DARK_MARBLE = new BlockFamily.Builder(TMMBlocks.DARK_MARBLE)
                .stairs(DARK_MARBLE_STAIRS)
                .slab(DARK_MARBLE_SLAB)
                .wall(DARK_MARBLE_WALL)
                .build();

        BlockFamily WHITE_HULL = new BlockFamily.Builder(TMMBlocks.WHITE_HULL)
                .stairs(WHITE_HULL_STAIRS)
                .slab(WHITE_HULL_SLAB)
                .wall(WHITE_HULL_WALL)
                .build();

        BlockFamily BLACK_HULL = new BlockFamily.Builder(TMMBlocks.BLACK_HULL)
                .stairs(BLACK_HULL_STAIRS)
                .slab(BLACK_HULL_SLAB)
                .wall(BLACK_HULL_WALL)
                .build();

        BlockFamily BLACK_HULL_SHEET = new BlockFamily.Builder(TMMBlocks.BLACK_HULL_SHEETS)
                .stairs(BLACK_HULL_SHEET_STAIRS)
                .slab(BLACK_HULL_SHEET_SLAB)
                .wall(BLACK_HULL_SHEET_WALL)
                .build();

        BlockFamily MAHOGANY = new BlockFamily.Builder(TMMBlocks.MAHOGANY_PLANKS)
                .stairs(MAHOGANY_STAIRS)
                .slab(MAHOGANY_SLAB)
                .build();

        BlockFamily MAHOGANY_HERRINGBONE = new BlockFamily.Builder(TMMBlocks.MAHOGANY_HERRINGBONE)
                .stairs(MAHOGANY_HERRINGBONE_STAIRS)
                .slab(MAHOGANY_HERRINGBONE_SLAB)
                .build();

        BlockFamily SMOOTH_MAHOGANY = new BlockFamily.Builder(TMMBlocks.SMOOTH_MAHOGANY)
                .stairs(SMOOTH_MAHOGANY_STAIRS)
                .slab(SMOOTH_MAHOGANY_SLAB)
                .build();

        BlockFamily BUBINGA = new BlockFamily.Builder(TMMBlocks.BUBINGA_PLANKS)
                .stairs(BUBINGA_STAIRS)
                .slab(BUBINGA_SLAB)
                .build();

        BlockFamily BUBINGA_HERRINGBONE = new BlockFamily.Builder(TMMBlocks.BUBINGA_HERRINGBONE)
                .stairs(BUBINGA_HERRINGBONE_STAIRS)
                .slab(BUBINGA_HERRINGBONE_SLAB)
                .build();

        BlockFamily SMOOTH_BUBINGA = new BlockFamily.Builder(TMMBlocks.SMOOTH_BUBINGA)
                .stairs(SMOOTH_BUBINGA_STAIRS)
                .slab(SMOOTH_BUBINGA_SLAB)
                .build();

        BlockFamily EBONY = new BlockFamily.Builder(TMMBlocks.EBONY_PLANKS)
                .stairs(EBONY_STAIRS)
                .slab(EBONY_SLAB)
                .build();

        BlockFamily EBONY_HERRINGBONE = new BlockFamily.Builder(TMMBlocks.EBONY_HERRINGBONE)
                .stairs(EBONY_HERRINGBONE_STAIRS)
                .slab(EBONY_HERRINGBONE_SLAB)
                .build();

        BlockFamily SMOOTH_EBONY = new BlockFamily.Builder(TMMBlocks.SMOOTH_EBONY)
                .stairs(SMOOTH_EBONY_STAIRS)
                .slab(SMOOTH_EBONY_SLAB)
                .build();
    }

    interface SetType {
        BlockSetType METAL_SHEET = BlockSetTypeBuilder.copyOf(BlockSetType.COPPER)
                .openableByHand(true)
                .openableByWindCharge(true)
                .buttonActivatedByArrows(true)
                .build(TMM.id("metal_sheet"));

    }

    static void initialize() {
        BranchBlock.STRIPPED_BRANCHES.put(STAINLESS_STEEL_BRANCH, STAINLESS_STEEL);
        BranchBlock.STRIPPED_BRANCHES.put(OAK_BRANCH, STRIPPED_OAK_BRANCH);
        BranchBlock.STRIPPED_BRANCHES.put(SPRUCE_BRANCH, STRIPPED_SPRUCE_BRANCH);
        BranchBlock.STRIPPED_BRANCHES.put(BIRCH_BRANCH, STRIPPED_BIRCH_BRANCH);
        BranchBlock.STRIPPED_BRANCHES.put(JUNGLE_BRANCH, STRIPPED_JUNGLE_BRANCH);
        BranchBlock.STRIPPED_BRANCHES.put(ACACIA_BRANCH, STRIPPED_ACACIA_BRANCH);
        BranchBlock.STRIPPED_BRANCHES.put(DARK_OAK_BRANCH, STRIPPED_DARK_OAK_BRANCH);
        BranchBlock.STRIPPED_BRANCHES.put(MANGROVE_BRANCH, STRIPPED_MANGROVE_BRANCH);
        BranchBlock.STRIPPED_BRANCHES.put(CHERRY_BRANCH, STRIPPED_CHERRY_BRANCH);
        BranchBlock.STRIPPED_BRANCHES.put(BAMBOO_POLE, STRIPPED_BAMBOO_POLE);
        BranchBlock.STRIPPED_BRANCHES.put(CRIMSON_STIPE, STRIPPED_CRIMSON_STIPE);
        BranchBlock.STRIPPED_BRANCHES.put(WARPED_STIPE, STRIPPED_WARPED_STIPE);

        FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();
        flammableBlockRegistry.add(OAK_BRANCH, 5, 20);
        flammableBlockRegistry.add(STRIPPED_OAK_BRANCH, 5, 20);
        flammableBlockRegistry.add(SPRUCE_BRANCH, 5, 20);
        flammableBlockRegistry.add(STRIPPED_SPRUCE_BRANCH, 5, 20);
        flammableBlockRegistry.add(BIRCH_BRANCH, 5, 20);
        flammableBlockRegistry.add(STRIPPED_BIRCH_BRANCH, 5, 20);
        flammableBlockRegistry.add(JUNGLE_BRANCH, 5, 20);
        flammableBlockRegistry.add(STRIPPED_JUNGLE_BRANCH, 5, 20);
        flammableBlockRegistry.add(ACACIA_BRANCH, 5, 20);
        flammableBlockRegistry.add(STRIPPED_ACACIA_BRANCH, 5, 20);
        flammableBlockRegistry.add(DARK_OAK_BRANCH, 5, 20);
        flammableBlockRegistry.add(STRIPPED_DARK_OAK_BRANCH, 5, 20);
        flammableBlockRegistry.add(MANGROVE_BRANCH, 5, 20);
        flammableBlockRegistry.add(STRIPPED_MANGROVE_BRANCH, 5, 20);
        flammableBlockRegistry.add(CHERRY_BRANCH, 5, 20);
        flammableBlockRegistry.add(STRIPPED_CHERRY_BRANCH, 5, 20);
        flammableBlockRegistry.add(BAMBOO_POLE, 5, 20);
        flammableBlockRegistry.add(STRIPPED_BAMBOO_POLE, 5, 20);

        registrar.registerEntries();

        Registries.BLOCK.addAlias(TMM.id("small_train_door"), TMM.id("navy_steel_door"));
        Registries.ITEM.addAlias(TMM.id("small_train_door"), TMM.id("navy_steel_door"));
    }
}
