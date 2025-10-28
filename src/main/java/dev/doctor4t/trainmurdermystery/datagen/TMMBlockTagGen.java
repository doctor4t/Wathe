package dev.doctor4t.trainmurdermystery.datagen;

import dev.doctor4t.trainmurdermystery.index.TMMBlocks;
import dev.doctor4t.trainmurdermystery.index.tag.TMMBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class TMMBlockTagGen extends FabricTagProvider.BlockTagProvider {

    public TMMBlockTagGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        this.getOrCreateTagBuilder(TMMBlockTags.BRANCHES)
                .add(TMMBlocks.STAINLESS_STEEL_BRANCH)
                .add(TMMBlocks.DARK_STEEL_BRANCH)
                .add(TMMBlocks.OAK_BRANCH)
                .add(TMMBlocks.SPRUCE_BRANCH)
                .add(TMMBlocks.BIRCH_BRANCH)
                .add(TMMBlocks.JUNGLE_BRANCH)
                .add(TMMBlocks.ACACIA_BRANCH)
                .add(TMMBlocks.DARK_OAK_BRANCH)
                .add(TMMBlocks.MANGROVE_BRANCH)
                .add(TMMBlocks.CHERRY_BRANCH)
                .add(TMMBlocks.BAMBOO_POLE)
                .add(TMMBlocks.CRIMSON_STIPE)
                .add(TMMBlocks.WARPED_STIPE)
                .add(TMMBlocks.STRIPPED_OAK_BRANCH)
                .add(TMMBlocks.STRIPPED_SPRUCE_BRANCH)
                .add(TMMBlocks.STRIPPED_BIRCH_BRANCH)
                .add(TMMBlocks.STRIPPED_JUNGLE_BRANCH)
                .add(TMMBlocks.STRIPPED_ACACIA_BRANCH)
                .add(TMMBlocks.STRIPPED_DARK_OAK_BRANCH)
                .add(TMMBlocks.STRIPPED_MANGROVE_BRANCH)
                .add(TMMBlocks.STRIPPED_CHERRY_BRANCH)
                .add(TMMBlocks.STRIPPED_BAMBOO_POLE)
                .add(TMMBlocks.STRIPPED_CRIMSON_STIPE)
                .add(TMMBlocks.STRIPPED_WARPED_STIPE);

        this.getOrCreateTagBuilder(TMMBlockTags.VENT_SHAFTS)
                .add(TMMBlocks.STAINLESS_STEEL_VENT_SHAFT)
                .add(TMMBlocks.DARK_STEEL_VENT_SHAFT)
                .add(TMMBlocks.TARNISHED_GOLD_VENT_SHAFT);

        this.getOrCreateTagBuilder(TMMBlockTags.VENT_HATCHES)
                .add(TMMBlocks.STAINLESS_STEEL_VENT_HATCH)
                .add(TMMBlocks.DARK_STEEL_VENT_HATCH)
                .add(TMMBlocks.TARNISHED_GOLD_VENT_HATCH);

        this.getOrCreateTagBuilder(TMMBlockTags.WALKWAYS)
                .add(TMMBlocks.METAL_SHEET_WALKWAY)
                .add(TMMBlocks.STAINLESS_STEEL_WALKWAY)
                .add(TMMBlocks.DARK_STEEL_WALKWAY);

        this.getOrCreateTagBuilder(TMMBlockTags.SPRINKLERS)
                .add(TMMBlocks.STAINLESS_STEEL_SPRINKLER)
                .add(TMMBlocks.GOLD_SPRINKLER);

        this.getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .addTag(TMMBlockTags.WALKWAYS)
                .add(TMMBlocks.MAHOGANY_PANEL)
                .add(TMMBlocks.BUBINGA_PANEL)
                .add(TMMBlocks.EBONY_PANEL);

        this.getOrCreateTagBuilder(BlockTags.ENCHANTMENT_POWER_PROVIDER)
                .add(TMMBlocks.MAHOGANY_BOOKSHELF)
                .add(TMMBlocks.BUBINGA_BOOKSHELF)
                .add(TMMBlocks.EBONY_BOOKSHELF);

        this.getOrCreateTagBuilder(BlockTags.CLIMBABLE)
                .addTag(TMMBlockTags.VENT_SHAFTS)
                .add(TMMBlocks.STAINLESS_STEEL_LADDER);

        this.getOrCreateTagBuilder(BlockTags.GUARDED_BY_PIGLINS)
                .add(TMMBlocks.TARNISHED_GOLD)
                .add(TMMBlocks.TARNISHED_GOLD_STAIRS)
                .add(TMMBlocks.TARNISHED_GOLD_SLAB)
                .add(TMMBlocks.TARNISHED_GOLD_WALL)
                .add(TMMBlocks.TARNISHED_GOLD_PILLAR)
                .add(TMMBlocks.GOLD)
                .add(TMMBlocks.GOLD_STAIRS)
                .add(TMMBlocks.GOLD_SLAB)
                .add(TMMBlocks.GOLD_WALL)
                .add(TMMBlocks.GOLD_PILLAR)
                .add(TMMBlocks.PRISTINE_GOLD)
                .add(TMMBlocks.PRISTINE_GOLD_STAIRS)
                .add(TMMBlocks.PRISTINE_GOLD_SLAB)
                .add(TMMBlocks.PRISTINE_GOLD_WALL)
                .add(TMMBlocks.PRISTINE_GOLD_PILLAR)
                .add(TMMBlocks.TARNISHED_GOLD_VENT_HATCH)
                .add(TMMBlocks.TARNISHED_GOLD_VENT_SHAFT)
                .add(TMMBlocks.GOLD_BAR)
                .add(TMMBlocks.GOLD_LEDGE)
                .add(TMMBlocks.TRIMMED_LANTERN)
                .add(TMMBlocks.WALL_LAMP)
                .add(TMMBlocks.GOLD_ORNAMENT);

        this.getOrCreateTagBuilder(BlockTags.BEDS)
                .add(TMMBlocks.WHITE_TRIMMED_BED)
                .add(TMMBlocks.RED_TRIMMED_BED);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS)
                .add(TMMBlocks.SMALL_BUTTON)
                .add(TMMBlocks.ELEVATOR_BUTTON);

        this.getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(TMMBlocks.MAHOGANY_PLANKS)
                .add(TMMBlocks.BUBINGA_PLANKS)
                .add(TMMBlocks.EBONY_PLANKS);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
                .add(TMMBlocks.MAHOGANY_STAIRS)
                .add(TMMBlocks.BUBINGA_STAIRS)
                .add(TMMBlocks.EBONY_STAIRS);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
                .add(TMMBlocks.MAHOGANY_SLAB)
                .add(TMMBlocks.BUBINGA_SLAB)
                .add(TMMBlocks.EBONY_SLAB);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_FENCES);

        this.getOrCreateTagBuilder(BlockTags.FENCE_GATES);

        this.getOrCreateTagBuilder(BlockTags.STAIRS)
                .add(TMMBlocks.TARNISHED_GOLD_STAIRS)
                .add(TMMBlocks.GOLD_STAIRS)
                .add(TMMBlocks.PRISTINE_GOLD_STAIRS)
                .add(TMMBlocks.METAL_SHEET_STAIRS)
                .add(TMMBlocks.STAINLESS_STEEL_STAIRS)
                .add(TMMBlocks.MARBLE_STAIRS)
                .add(TMMBlocks.MARBLE_TILE_STAIRS)
                .add(TMMBlocks.DARK_MARBLE_STAIRS)
                .add(TMMBlocks.WHITE_HULL_STAIRS)
                .add(TMMBlocks.BLACK_HULL_STAIRS)
                .add(TMMBlocks.BLACK_HULL_SHEET_STAIRS)
                .add(TMMBlocks.MAHOGANY_HERRINGBONE_STAIRS)
                .add(TMMBlocks.SMOOTH_MAHOGANY_STAIRS)
                .add(TMMBlocks.BUBINGA_HERRINGBONE_STAIRS)
                .add(TMMBlocks.SMOOTH_BUBINGA_STAIRS)
                .add(TMMBlocks.EBONY_HERRINGBONE_STAIRS)
                .add(TMMBlocks.SMOOTH_EBONY_STAIRS);

        this.getOrCreateTagBuilder(BlockTags.SLABS)
                .add(TMMBlocks.TARNISHED_GOLD_SLAB)
                .add(TMMBlocks.GOLD_SLAB)
                .add(TMMBlocks.PRISTINE_GOLD_SLAB)
                .add(TMMBlocks.METAL_SHEET_SLAB)
                .add(TMMBlocks.STAINLESS_STEEL_SLAB)
                .add(TMMBlocks.MARBLE_SLAB)
                .add(TMMBlocks.MARBLE_TILE_SLAB)
                .add(TMMBlocks.DARK_MARBLE_SLAB)
                .add(TMMBlocks.WHITE_HULL_SLAB)
                .add(TMMBlocks.BLACK_HULL_SLAB)
                .add(TMMBlocks.BLACK_HULL_SHEET_SLAB)
                .add(TMMBlocks.MAHOGANY_HERRINGBONE_SLAB)
                .add(TMMBlocks.SMOOTH_MAHOGANY_SLAB)
                .add(TMMBlocks.BUBINGA_HERRINGBONE_SLAB)
                .add(TMMBlocks.SMOOTH_BUBINGA_SLAB)
                .add(TMMBlocks.EBONY_HERRINGBONE_SLAB)
                .add(TMMBlocks.SMOOTH_EBONY_SLAB);

        this.getOrCreateTagBuilder(BlockTags.WALLS)
                .add(TMMBlocks.TARNISHED_GOLD_WALL)
                .add(TMMBlocks.GOLD_WALL)
                .add(TMMBlocks.PRISTINE_GOLD_WALL)
                .add(TMMBlocks.METAL_SHEET_WALL)
                .add(TMMBlocks.STAINLESS_STEEL_WALL)
                .add(TMMBlocks.MARBLE_WALL)
                .add(TMMBlocks.MARBLE_TILE_WALL)
                .add(TMMBlocks.DARK_MARBLE_WALL)
                .add(TMMBlocks.WHITE_HULL_WALL)
                .add(TMMBlocks.BLACK_HULL_WALL)
                .add(TMMBlocks.BLACK_HULL_SHEET_WALL);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_DOORS);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS);

        this.getOrCreateTagBuilder(BlockTags.DOORS)
                .add(TMMBlocks.METAL_SHEET_DOOR)
                .add(TMMBlocks.COCKPIT_DOOR);

        this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .addTag(TMMBlockTags.BRANCHES)
                .add(TMMBlocks.MAHOGANY_HERRINGBONE)
                .add(TMMBlocks.MAHOGANY_HERRINGBONE_STAIRS)
                .add(TMMBlocks.MAHOGANY_HERRINGBONE_SLAB)
                .add(TMMBlocks.SMOOTH_MAHOGANY)
                .add(TMMBlocks.SMOOTH_MAHOGANY_STAIRS)
                .add(TMMBlocks.SMOOTH_MAHOGANY_SLAB)
                .add(TMMBlocks.MAHOGANY_CABINET)
                .add(TMMBlocks.MAHOGANY_PANEL)
                .add(TMMBlocks.BUBINGA_HERRINGBONE)
                .add(TMMBlocks.BUBINGA_HERRINGBONE_STAIRS)
                .add(TMMBlocks.BUBINGA_HERRINGBONE_SLAB)
                .add(TMMBlocks.SMOOTH_BUBINGA)
                .add(TMMBlocks.SMOOTH_BUBINGA_STAIRS)
                .add(TMMBlocks.SMOOTH_BUBINGA_SLAB)
                .add(TMMBlocks.BUBINGA_CABINET)
                .add(TMMBlocks.BUBINGA_PANEL)
                .add(TMMBlocks.EBONY_HERRINGBONE)
                .add(TMMBlocks.EBONY_HERRINGBONE_STAIRS)
                .add(TMMBlocks.EBONY_HERRINGBONE_SLAB)
                .add(TMMBlocks.SMOOTH_EBONY)
                .add(TMMBlocks.SMOOTH_EBONY_STAIRS)
                .add(TMMBlocks.SMOOTH_EBONY_SLAB)
                .add(TMMBlocks.EBONY_CABINET)
                .add(TMMBlocks.EBONY_PANEL)
                .add(TMMBlocks.PANEL_STRIPES)
                .add(TMMBlocks.TRIMMED_RAILING)
                .add(TMMBlocks.TRIMMED_RAILING_POST)
                .add(TMMBlocks.DIAGONAL_TRIMMED_RAILING)
                .add(TMMBlocks.TRIMMED_EBONY_STAIRS)
                .add(TMMBlocks.WHITE_LOUNGE_COUCH)
                .add(TMMBlocks.WHITE_OTTOMAN)
                .add(TMMBlocks.WHITE_TRIMMED_BED)
                .add(TMMBlocks.RED_TRIMMED_BED)
                .add(TMMBlocks.BLUE_LOUNGE_COUCH)
                .add(TMMBlocks.GREEN_LOUNGE_COUCH)
                .add(TMMBlocks.RED_LEATHER_COUCH)
                .add(TMMBlocks.BROWN_LEATHER_COUCH)
                .add(TMMBlocks.BEIGE_LEATHER_COUCH)
                .add(TMMBlocks.COFFEE_TABLE)
                .add(TMMBlocks.BAR_TABLE)
                .add(TMMBlocks.BAR_STOOL)
                .add(TMMBlocks.SMALL_BUTTON)
                .add(TMMBlocks.ELEVATOR_BUTTON)
                .add(TMMBlocks.MAHOGANY_BOOKSHELF)
                .add(TMMBlocks.BUBINGA_BOOKSHELF)
                .add(TMMBlocks.EBONY_BOOKSHELF);

        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .addTag(TMMBlockTags.VENT_SHAFTS)
                .add(TMMBlocks.STAINLESS_STEEL_VENT_HATCH)
                .add(TMMBlocks.DARK_STEEL_VENT_HATCH)
                .add(TMMBlocks.TARNISHED_GOLD_VENT_HATCH)
                .add(TMMBlocks.TARNISHED_GOLD)
                .add(TMMBlocks.TARNISHED_GOLD_STAIRS)
                .add(TMMBlocks.TARNISHED_GOLD_SLAB)
                .add(TMMBlocks.TARNISHED_GOLD_WALL)
                .add(TMMBlocks.TARNISHED_GOLD_PILLAR)
                .add(TMMBlocks.GOLD)
                .add(TMMBlocks.GOLD_STAIRS)
                .add(TMMBlocks.GOLD_SLAB)
                .add(TMMBlocks.GOLD_WALL)
                .add(TMMBlocks.GOLD_PILLAR)
                .add(TMMBlocks.PRISTINE_GOLD)
                .add(TMMBlocks.PRISTINE_GOLD_STAIRS)
                .add(TMMBlocks.PRISTINE_GOLD_SLAB)
                .add(TMMBlocks.PRISTINE_GOLD_WALL)
                .add(TMMBlocks.PRISTINE_GOLD_PILLAR)
                .add(TMMBlocks.METAL_SHEET)
                .add(TMMBlocks.METAL_SHEET_STAIRS)
                .add(TMMBlocks.METAL_SHEET_SLAB)
                .add(TMMBlocks.METAL_SHEET_DOOR)
                .add(TMMBlocks.COCKPIT_DOOR)
                .add(TMMBlocks.METAL_SHEET_WALKWAY)
                .add(TMMBlocks.STAINLESS_STEEL_LADDER)
                .add(TMMBlocks.STAINLESS_STEEL)
                .add(TMMBlocks.STAINLESS_STEEL_STAIRS)
                .add(TMMBlocks.STAINLESS_STEEL_SLAB)
                .add(TMMBlocks.STAINLESS_STEEL_WALKWAY)
                .add(TMMBlocks.STAINLESS_STEEL_PILLAR)
                .add(TMMBlocks.DARK_STEEL)
                .add(TMMBlocks.DARK_STEEL_STAIRS)
                .add(TMMBlocks.DARK_STEEL_SLAB)
                .add(TMMBlocks.DARK_STEEL_WALKWAY)
                .add(TMMBlocks.DARK_STEEL_PILLAR)
                .add(TMMBlocks.RHOMBUS_GLASS)
                .add(TMMBlocks.HULL_GLASS)
                .add(TMMBlocks.RHOMBUS_HULL_GLASS)
                .add(TMMBlocks.GOLDEN_GLASS_PANEL)
                .add(TMMBlocks.CULLING_GLASS)
                .add(TMMBlocks.MARBLE)
                .add(TMMBlocks.MARBLE_STAIRS)
                .add(TMMBlocks.MARBLE_SLAB)
                .add(TMMBlocks.MARBLE_TILES)
                .add(TMMBlocks.MARBLE_TILE_STAIRS)
                .add(TMMBlocks.MARBLE_TILE_SLAB)
                .add(TMMBlocks.DARK_MARBLE)
                .add(TMMBlocks.DARK_MARBLE_STAIRS)
                .add(TMMBlocks.DARK_MARBLE_SLAB)
                .add(TMMBlocks.MARBLE_MOSAIC)
                .add(TMMBlocks.WHITE_HULL)
                .add(TMMBlocks.WHITE_HULL_STAIRS)
                .add(TMMBlocks.WHITE_HULL_SLAB)
                .add(TMMBlocks.CULLING_WHITE_HULL)
                .add(TMMBlocks.BLACK_HULL)
                .add(TMMBlocks.BLACK_HULL_STAIRS)
                .add(TMMBlocks.BLACK_HULL_SLAB)
                .add(TMMBlocks.CULLING_BLACK_HULL)
                .add(TMMBlocks.BLACK_HULL_SHEETS)
                .add(TMMBlocks.BLACK_HULL_SHEET_STAIRS)
                .add(TMMBlocks.BLACK_HULL_SHEET_SLAB)
                .add(TMMBlocks.CARGO_BOX)
                .add(TMMBlocks.GOLD_BAR)
                .add(TMMBlocks.GOLD_LEDGE)
                .add(TMMBlocks.STAINLESS_STEEL_BAR)
                .add(TMMBlocks.TRIMMED_LANTERN)
                .add(TMMBlocks.WALL_LAMP)
                .add(TMMBlocks.NEON_PILLAR)
                .add(TMMBlocks.NEON_TUBE)
                .addTag(TMMBlockTags.SPRINKLERS)
                .add(TMMBlocks.ANTHRACITE_STEEL_PANEL)
                .add(TMMBlocks.ANTHRACITE_STEEL_TILES)
                .add(TMMBlocks.ANTHRACITE_STEEL_TILES_PANEL)
                .add(TMMBlocks.SMOOTH_ANTHRACITE_STEEL)
                .add(TMMBlocks.SMOOTH_ANTHRACITE_STEEL_STAIRS)
                .add(TMMBlocks.SMOOTH_ANTHRACITE_STEEL_SLAB)
                .add(TMMBlocks.SMOOTH_ANTHRACITE_STEEL_PANEL)
                .add(TMMBlocks.KHAKI_STEEL_PANEL)
                .add(TMMBlocks.KHAKI_STEEL_TILES)
                .add(TMMBlocks.KHAKI_STEEL_TILES_PANEL)
                .add(TMMBlocks.SMOOTH_KHAKI_STEEL)
                .add(TMMBlocks.SMOOTH_KHAKI_STEEL_STAIRS)
                .add(TMMBlocks.SMOOTH_KHAKI_STEEL_SLAB)
                .add(TMMBlocks.SMOOTH_KHAKI_STEEL_PANEL)
                .add(TMMBlocks.MAROON_STEEL_PANEL)
                .add(TMMBlocks.MAROON_STEEL_TILES)
                .add(TMMBlocks.MAROON_STEEL_TILES_PANEL)
                .add(TMMBlocks.SMOOTH_MAROON_STEEL)
                .add(TMMBlocks.SMOOTH_MAROON_STEEL_STAIRS)
                .add(TMMBlocks.SMOOTH_MAROON_STEEL_SLAB)
                .add(TMMBlocks.SMOOTH_MAROON_STEEL_PANEL)
                .add(TMMBlocks.MUNTZ_STEEL_PANEL)
                .add(TMMBlocks.MUNTZ_STEEL_TILES)
                .add(TMMBlocks.MUNTZ_STEEL_TILES_PANEL)
                .add(TMMBlocks.SMOOTH_MUNTZ_STEEL)
                .add(TMMBlocks.SMOOTH_MUNTZ_STEEL_STAIRS)
                .add(TMMBlocks.SMOOTH_MUNTZ_STEEL_SLAB)
                .add(TMMBlocks.SMOOTH_MUNTZ_STEEL_PANEL)
                .add(TMMBlocks.NAVY_STEEL_PANEL)
                .add(TMMBlocks.NAVY_STEEL_TILES)
                .add(TMMBlocks.NAVY_STEEL_TILES_PANEL)
                .add(TMMBlocks.SMOOTH_NAVY_STEEL)
                .add(TMMBlocks.SMOOTH_NAVY_STEEL_STAIRS)
                .add(TMMBlocks.SMOOTH_NAVY_STEEL_SLAB)
                .add(TMMBlocks.SMOOTH_NAVY_STEEL_PANEL)
                .add(TMMBlocks.RAIL_BEAM);


        this.getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .addTag(TMMBlockTags.VENT_SHAFTS);
    }
}
