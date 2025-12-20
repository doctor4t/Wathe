package dev.doctor4t.wathe.datagen;

import dev.doctor4t.wathe.index.WatheBlocks;
import dev.doctor4t.wathe.index.tag.WatheBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class WatheBlockTagGen extends FabricTagProvider.BlockTagProvider {

    public WatheBlockTagGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        this.getOrCreateTagBuilder(WatheBlockTags.BRANCHES)
                .add(WatheBlocks.STAINLESS_STEEL_BRANCH)
                .add(WatheBlocks.DARK_STEEL_BRANCH)
                .add(WatheBlocks.OAK_BRANCH)
                .add(WatheBlocks.SPRUCE_BRANCH)
                .add(WatheBlocks.BIRCH_BRANCH)
                .add(WatheBlocks.JUNGLE_BRANCH)
                .add(WatheBlocks.ACACIA_BRANCH)
                .add(WatheBlocks.DARK_OAK_BRANCH)
                .add(WatheBlocks.MANGROVE_BRANCH)
                .add(WatheBlocks.CHERRY_BRANCH)
                .add(WatheBlocks.BAMBOO_POLE)
                .add(WatheBlocks.CRIMSON_STIPE)
                .add(WatheBlocks.WARPED_STIPE)
                .add(WatheBlocks.STRIPPED_OAK_BRANCH)
                .add(WatheBlocks.STRIPPED_SPRUCE_BRANCH)
                .add(WatheBlocks.STRIPPED_BIRCH_BRANCH)
                .add(WatheBlocks.STRIPPED_JUNGLE_BRANCH)
                .add(WatheBlocks.STRIPPED_ACACIA_BRANCH)
                .add(WatheBlocks.STRIPPED_DARK_OAK_BRANCH)
                .add(WatheBlocks.STRIPPED_MANGROVE_BRANCH)
                .add(WatheBlocks.STRIPPED_CHERRY_BRANCH)
                .add(WatheBlocks.STRIPPED_BAMBOO_POLE)
                .add(WatheBlocks.STRIPPED_CRIMSON_STIPE)
                .add(WatheBlocks.STRIPPED_WARPED_STIPE);

        this.getOrCreateTagBuilder(WatheBlockTags.VENT_SHAFTS)
                .add(WatheBlocks.STAINLESS_STEEL_VENT_SHAFT)
                .add(WatheBlocks.DARK_STEEL_VENT_SHAFT)
                .add(WatheBlocks.TARNISHED_GOLD_VENT_SHAFT);

        this.getOrCreateTagBuilder(WatheBlockTags.VENT_HATCHES)
                .add(WatheBlocks.STAINLESS_STEEL_VENT_HATCH)
                .add(WatheBlocks.DARK_STEEL_VENT_HATCH)
                .add(WatheBlocks.TARNISHED_GOLD_VENT_HATCH);

        this.getOrCreateTagBuilder(WatheBlockTags.WALKWAYS)
                .add(WatheBlocks.METAL_SHEET_WALKWAY)
                .add(WatheBlocks.STAINLESS_STEEL_WALKWAY)
                .add(WatheBlocks.DARK_STEEL_WALKWAY);

        this.getOrCreateTagBuilder(WatheBlockTags.SPRINKLERS)
                .add(WatheBlocks.STAINLESS_STEEL_SPRINKLER)
                .add(WatheBlocks.GOLD_SPRINKLER);

        this.getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .addTag(WatheBlockTags.WALKWAYS)
                .add(WatheBlocks.MAHOGANY_PANEL)
                .add(WatheBlocks.BUBINGA_PANEL)
                .add(WatheBlocks.EBONY_PANEL);

        this.getOrCreateTagBuilder(BlockTags.ENCHANTMENT_POWER_PROVIDER)
                .add(WatheBlocks.MAHOGANY_BOOKSHELF)
                .add(WatheBlocks.BUBINGA_BOOKSHELF)
                .add(WatheBlocks.EBONY_BOOKSHELF);

        this.getOrCreateTagBuilder(BlockTags.CLIMBABLE)
                .addTag(WatheBlockTags.VENT_SHAFTS)
                .add(WatheBlocks.STAINLESS_STEEL_LADDER);

        this.getOrCreateTagBuilder(BlockTags.GUARDED_BY_PIGLINS)
                .add(WatheBlocks.TARNISHED_GOLD)
                .add(WatheBlocks.TARNISHED_GOLD_STAIRS)
                .add(WatheBlocks.TARNISHED_GOLD_SLAB)
                .add(WatheBlocks.TARNISHED_GOLD_WALL)
                .add(WatheBlocks.TARNISHED_GOLD_PILLAR)
                .add(WatheBlocks.GOLD)
                .add(WatheBlocks.GOLD_STAIRS)
                .add(WatheBlocks.GOLD_SLAB)
                .add(WatheBlocks.GOLD_WALL)
                .add(WatheBlocks.GOLD_PILLAR)
                .add(WatheBlocks.PRISTINE_GOLD)
                .add(WatheBlocks.PRISTINE_GOLD_STAIRS)
                .add(WatheBlocks.PRISTINE_GOLD_SLAB)
                .add(WatheBlocks.PRISTINE_GOLD_WALL)
                .add(WatheBlocks.PRISTINE_GOLD_PILLAR)
                .add(WatheBlocks.TARNISHED_GOLD_VENT_HATCH)
                .add(WatheBlocks.TARNISHED_GOLD_VENT_SHAFT)
                .add(WatheBlocks.GOLD_BAR)
                .add(WatheBlocks.GOLD_LEDGE)
                .add(WatheBlocks.TRIMMED_LANTERN)
                .add(WatheBlocks.WALL_LAMP)
                .add(WatheBlocks.GOLD_ORNAMENT);

        this.getOrCreateTagBuilder(BlockTags.BEDS)
                .add(WatheBlocks.WHITE_TRIMMED_BED)
                .add(WatheBlocks.RED_TRIMMED_BED);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS)
                .add(WatheBlocks.SMALL_BUTTON)
                .add(WatheBlocks.ELEVATOR_BUTTON);

        this.getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(WatheBlocks.MAHOGANY_PLANKS)
                .add(WatheBlocks.BUBINGA_PLANKS)
                .add(WatheBlocks.EBONY_PLANKS);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
                .add(WatheBlocks.MAHOGANY_STAIRS)
                .add(WatheBlocks.BUBINGA_STAIRS)
                .add(WatheBlocks.EBONY_STAIRS);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
                .add(WatheBlocks.MAHOGANY_SLAB)
                .add(WatheBlocks.BUBINGA_SLAB)
                .add(WatheBlocks.EBONY_SLAB);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_FENCES);

        this.getOrCreateTagBuilder(BlockTags.FENCE_GATES);

        this.getOrCreateTagBuilder(BlockTags.STAIRS)
                .add(WatheBlocks.TARNISHED_GOLD_STAIRS)
                .add(WatheBlocks.GOLD_STAIRS)
                .add(WatheBlocks.PRISTINE_GOLD_STAIRS)
                .add(WatheBlocks.METAL_SHEET_STAIRS)
                .add(WatheBlocks.STAINLESS_STEEL_STAIRS)
                .add(WatheBlocks.MARBLE_STAIRS)
                .add(WatheBlocks.MARBLE_TILE_STAIRS)
                .add(WatheBlocks.DARK_MARBLE_STAIRS)
                .add(WatheBlocks.WHITE_HULL_STAIRS)
                .add(WatheBlocks.BLACK_HULL_STAIRS)
                .add(WatheBlocks.BLACK_HULL_SHEET_STAIRS)
                .add(WatheBlocks.MAHOGANY_HERRINGBONE_STAIRS)
                .add(WatheBlocks.SMOOTH_MAHOGANY_STAIRS)
                .add(WatheBlocks.BUBINGA_HERRINGBONE_STAIRS)
                .add(WatheBlocks.SMOOTH_BUBINGA_STAIRS)
                .add(WatheBlocks.EBONY_HERRINGBONE_STAIRS)
                .add(WatheBlocks.SMOOTH_EBONY_STAIRS);

        this.getOrCreateTagBuilder(BlockTags.SLABS)
                .add(WatheBlocks.TARNISHED_GOLD_SLAB)
                .add(WatheBlocks.GOLD_SLAB)
                .add(WatheBlocks.PRISTINE_GOLD_SLAB)
                .add(WatheBlocks.METAL_SHEET_SLAB)
                .add(WatheBlocks.STAINLESS_STEEL_SLAB)
                .add(WatheBlocks.MARBLE_SLAB)
                .add(WatheBlocks.MARBLE_TILE_SLAB)
                .add(WatheBlocks.DARK_MARBLE_SLAB)
                .add(WatheBlocks.WHITE_HULL_SLAB)
                .add(WatheBlocks.BLACK_HULL_SLAB)
                .add(WatheBlocks.BLACK_HULL_SHEET_SLAB)
                .add(WatheBlocks.MAHOGANY_HERRINGBONE_SLAB)
                .add(WatheBlocks.SMOOTH_MAHOGANY_SLAB)
                .add(WatheBlocks.BUBINGA_HERRINGBONE_SLAB)
                .add(WatheBlocks.SMOOTH_BUBINGA_SLAB)
                .add(WatheBlocks.EBONY_HERRINGBONE_SLAB)
                .add(WatheBlocks.SMOOTH_EBONY_SLAB);

        this.getOrCreateTagBuilder(BlockTags.WALLS)
                .add(WatheBlocks.TARNISHED_GOLD_WALL)
                .add(WatheBlocks.GOLD_WALL)
                .add(WatheBlocks.PRISTINE_GOLD_WALL)
                .add(WatheBlocks.METAL_SHEET_WALL)
                .add(WatheBlocks.STAINLESS_STEEL_WALL)
                .add(WatheBlocks.MARBLE_WALL)
                .add(WatheBlocks.MARBLE_TILE_WALL)
                .add(WatheBlocks.DARK_MARBLE_WALL)
                .add(WatheBlocks.WHITE_HULL_WALL)
                .add(WatheBlocks.BLACK_HULL_WALL)
                .add(WatheBlocks.BLACK_HULL_SHEET_WALL);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_DOORS);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS);

        this.getOrCreateTagBuilder(BlockTags.DOORS)
                .add(WatheBlocks.METAL_SHEET_DOOR)
                .add(WatheBlocks.COCKPIT_DOOR);

        this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .addTag(WatheBlockTags.BRANCHES)
                .add(WatheBlocks.MAHOGANY_HERRINGBONE)
                .add(WatheBlocks.MAHOGANY_HERRINGBONE_STAIRS)
                .add(WatheBlocks.MAHOGANY_HERRINGBONE_SLAB)
                .add(WatheBlocks.SMOOTH_MAHOGANY)
                .add(WatheBlocks.SMOOTH_MAHOGANY_STAIRS)
                .add(WatheBlocks.SMOOTH_MAHOGANY_SLAB)
                .add(WatheBlocks.MAHOGANY_CABINET)
                .add(WatheBlocks.MAHOGANY_PANEL)
                .add(WatheBlocks.BUBINGA_HERRINGBONE)
                .add(WatheBlocks.BUBINGA_HERRINGBONE_STAIRS)
                .add(WatheBlocks.BUBINGA_HERRINGBONE_SLAB)
                .add(WatheBlocks.SMOOTH_BUBINGA)
                .add(WatheBlocks.SMOOTH_BUBINGA_STAIRS)
                .add(WatheBlocks.SMOOTH_BUBINGA_SLAB)
                .add(WatheBlocks.BUBINGA_CABINET)
                .add(WatheBlocks.BUBINGA_PANEL)
                .add(WatheBlocks.EBONY_HERRINGBONE)
                .add(WatheBlocks.EBONY_HERRINGBONE_STAIRS)
                .add(WatheBlocks.EBONY_HERRINGBONE_SLAB)
                .add(WatheBlocks.SMOOTH_EBONY)
                .add(WatheBlocks.SMOOTH_EBONY_STAIRS)
                .add(WatheBlocks.SMOOTH_EBONY_SLAB)
                .add(WatheBlocks.EBONY_CABINET)
                .add(WatheBlocks.EBONY_PANEL)
                .add(WatheBlocks.PANEL_STRIPES)
                .add(WatheBlocks.TRIMMED_RAILING)
                .add(WatheBlocks.TRIMMED_RAILING_POST)
                .add(WatheBlocks.DIAGONAL_TRIMMED_RAILING)
                .add(WatheBlocks.TRIMMED_EBONY_STAIRS)
                .add(WatheBlocks.WHITE_LOUNGE_COUCH)
                .add(WatheBlocks.WHITE_OTTOMAN)
                .add(WatheBlocks.WHITE_TRIMMED_BED)
                .add(WatheBlocks.RED_TRIMMED_BED)
                .add(WatheBlocks.BLUE_LOUNGE_COUCH)
                .add(WatheBlocks.GREEN_LOUNGE_COUCH)
                .add(WatheBlocks.RED_LEATHER_COUCH)
                .add(WatheBlocks.BROWN_LEATHER_COUCH)
                .add(WatheBlocks.BEIGE_LEATHER_COUCH)
                .add(WatheBlocks.COFFEE_TABLE)
                .add(WatheBlocks.BAR_TABLE)
                .add(WatheBlocks.BAR_STOOL)
                .add(WatheBlocks.SMALL_BUTTON)
                .add(WatheBlocks.ELEVATOR_BUTTON)
                .add(WatheBlocks.MAHOGANY_BOOKSHELF)
                .add(WatheBlocks.BUBINGA_BOOKSHELF)
                .add(WatheBlocks.EBONY_BOOKSHELF);

        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .addTag(WatheBlockTags.VENT_SHAFTS)
                .add(WatheBlocks.STAINLESS_STEEL_VENT_HATCH)
                .add(WatheBlocks.DARK_STEEL_VENT_HATCH)
                .add(WatheBlocks.TARNISHED_GOLD_VENT_HATCH)
                .add(WatheBlocks.TARNISHED_GOLD)
                .add(WatheBlocks.TARNISHED_GOLD_STAIRS)
                .add(WatheBlocks.TARNISHED_GOLD_SLAB)
                .add(WatheBlocks.TARNISHED_GOLD_WALL)
                .add(WatheBlocks.TARNISHED_GOLD_PILLAR)
                .add(WatheBlocks.GOLD)
                .add(WatheBlocks.GOLD_STAIRS)
                .add(WatheBlocks.GOLD_SLAB)
                .add(WatheBlocks.GOLD_WALL)
                .add(WatheBlocks.GOLD_PILLAR)
                .add(WatheBlocks.PRISTINE_GOLD)
                .add(WatheBlocks.PRISTINE_GOLD_STAIRS)
                .add(WatheBlocks.PRISTINE_GOLD_SLAB)
                .add(WatheBlocks.PRISTINE_GOLD_WALL)
                .add(WatheBlocks.PRISTINE_GOLD_PILLAR)
                .add(WatheBlocks.METAL_SHEET)
                .add(WatheBlocks.METAL_SHEET_STAIRS)
                .add(WatheBlocks.METAL_SHEET_SLAB)
                .add(WatheBlocks.METAL_SHEET_DOOR)
                .add(WatheBlocks.COCKPIT_DOOR)
                .add(WatheBlocks.METAL_SHEET_WALKWAY)
                .add(WatheBlocks.STAINLESS_STEEL_LADDER)
                .add(WatheBlocks.STAINLESS_STEEL)
                .add(WatheBlocks.STAINLESS_STEEL_STAIRS)
                .add(WatheBlocks.STAINLESS_STEEL_SLAB)
                .add(WatheBlocks.STAINLESS_STEEL_WALKWAY)
                .add(WatheBlocks.STAINLESS_STEEL_PILLAR)
                .add(WatheBlocks.DARK_STEEL)
                .add(WatheBlocks.DARK_STEEL_STAIRS)
                .add(WatheBlocks.DARK_STEEL_SLAB)
                .add(WatheBlocks.DARK_STEEL_WALKWAY)
                .add(WatheBlocks.DARK_STEEL_PILLAR)
                .add(WatheBlocks.RHOMBUS_GLASS)
                .add(WatheBlocks.HULL_GLASS)
                .add(WatheBlocks.RHOMBUS_HULL_GLASS)
                .add(WatheBlocks.GOLDEN_GLASS_PANEL)
                .add(WatheBlocks.CULLING_GLASS)
                .add(WatheBlocks.MARBLE)
                .add(WatheBlocks.MARBLE_STAIRS)
                .add(WatheBlocks.MARBLE_SLAB)
                .add(WatheBlocks.MARBLE_TILES)
                .add(WatheBlocks.MARBLE_TILE_STAIRS)
                .add(WatheBlocks.MARBLE_TILE_SLAB)
                .add(WatheBlocks.DARK_MARBLE)
                .add(WatheBlocks.DARK_MARBLE_STAIRS)
                .add(WatheBlocks.DARK_MARBLE_SLAB)
                .add(WatheBlocks.MARBLE_MOSAIC)
                .add(WatheBlocks.WHITE_HULL)
                .add(WatheBlocks.WHITE_HULL_STAIRS)
                .add(WatheBlocks.WHITE_HULL_SLAB)
                .add(WatheBlocks.CULLING_WHITE_HULL)
                .add(WatheBlocks.BLACK_HULL)
                .add(WatheBlocks.BLACK_HULL_STAIRS)
                .add(WatheBlocks.BLACK_HULL_SLAB)
                .add(WatheBlocks.CULLING_BLACK_HULL)
                .add(WatheBlocks.BLACK_HULL_SHEETS)
                .add(WatheBlocks.BLACK_HULL_SHEET_STAIRS)
                .add(WatheBlocks.BLACK_HULL_SHEET_SLAB)
                .add(WatheBlocks.CARGO_BOX)
                .add(WatheBlocks.GOLD_BAR)
                .add(WatheBlocks.GOLD_LEDGE)
                .add(WatheBlocks.STAINLESS_STEEL_BAR)
                .add(WatheBlocks.TRIMMED_LANTERN)
                .add(WatheBlocks.WALL_LAMP)
                .add(WatheBlocks.NEON_PILLAR)
                .add(WatheBlocks.NEON_TUBE)
                .addTag(WatheBlockTags.SPRINKLERS)
                .add(WatheBlocks.ANTHRACITE_STEEL_PANEL)
                .add(WatheBlocks.ANTHRACITE_STEEL_TILES)
                .add(WatheBlocks.ANTHRACITE_STEEL_TILES_PANEL)
                .add(WatheBlocks.SMOOTH_ANTHRACITE_STEEL)
                .add(WatheBlocks.SMOOTH_ANTHRACITE_STEEL_STAIRS)
                .add(WatheBlocks.SMOOTH_ANTHRACITE_STEEL_SLAB)
                .add(WatheBlocks.SMOOTH_ANTHRACITE_STEEL_PANEL)
                .add(WatheBlocks.KHAKI_STEEL_PANEL)
                .add(WatheBlocks.KHAKI_STEEL_TILES)
                .add(WatheBlocks.KHAKI_STEEL_TILES_PANEL)
                .add(WatheBlocks.SMOOTH_KHAKI_STEEL)
                .add(WatheBlocks.SMOOTH_KHAKI_STEEL_STAIRS)
                .add(WatheBlocks.SMOOTH_KHAKI_STEEL_SLAB)
                .add(WatheBlocks.SMOOTH_KHAKI_STEEL_PANEL)
                .add(WatheBlocks.MAROON_STEEL_PANEL)
                .add(WatheBlocks.MAROON_STEEL_TILES)
                .add(WatheBlocks.MAROON_STEEL_TILES_PANEL)
                .add(WatheBlocks.SMOOTH_MAROON_STEEL)
                .add(WatheBlocks.SMOOTH_MAROON_STEEL_STAIRS)
                .add(WatheBlocks.SMOOTH_MAROON_STEEL_SLAB)
                .add(WatheBlocks.SMOOTH_MAROON_STEEL_PANEL)
                .add(WatheBlocks.MUNTZ_STEEL_PANEL)
                .add(WatheBlocks.MUNTZ_STEEL_TILES)
                .add(WatheBlocks.MUNTZ_STEEL_TILES_PANEL)
                .add(WatheBlocks.SMOOTH_MUNTZ_STEEL)
                .add(WatheBlocks.SMOOTH_MUNTZ_STEEL_STAIRS)
                .add(WatheBlocks.SMOOTH_MUNTZ_STEEL_SLAB)
                .add(WatheBlocks.SMOOTH_MUNTZ_STEEL_PANEL)
                .add(WatheBlocks.NAVY_STEEL_PANEL)
                .add(WatheBlocks.NAVY_STEEL_TILES)
                .add(WatheBlocks.NAVY_STEEL_TILES_PANEL)
                .add(WatheBlocks.SMOOTH_NAVY_STEEL)
                .add(WatheBlocks.SMOOTH_NAVY_STEEL_STAIRS)
                .add(WatheBlocks.SMOOTH_NAVY_STEEL_SLAB)
                .add(WatheBlocks.SMOOTH_NAVY_STEEL_PANEL)
                .add(WatheBlocks.RAIL_BEAM);


        this.getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .addTag(WatheBlockTags.VENT_SHAFTS);
    }
}
