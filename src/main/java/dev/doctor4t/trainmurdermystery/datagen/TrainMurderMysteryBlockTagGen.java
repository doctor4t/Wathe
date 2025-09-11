package dev.doctor4t.trainmurdermystery.datagen;

import dev.doctor4t.trainmurdermystery.index.TrainMurderMysteryBlocks;
import dev.doctor4t.trainmurdermystery.index.tag.TrainMurderMysteryBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class TrainMurderMysteryBlockTagGen extends FabricTagProvider.BlockTagProvider {

    public TrainMurderMysteryBlockTagGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        this.getOrCreateTagBuilder(TrainMurderMysteryBlockTags.BRANCHES)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_BRANCH)
                .add(TrainMurderMysteryBlocks.DARK_STEEL_BRANCH)
                .add(TrainMurderMysteryBlocks.OAK_BRANCH)
                .add(TrainMurderMysteryBlocks.SPRUCE_BRANCH)
                .add(TrainMurderMysteryBlocks.BIRCH_BRANCH)
                .add(TrainMurderMysteryBlocks.JUNGLE_BRANCH)
                .add(TrainMurderMysteryBlocks.ACACIA_BRANCH)
                .add(TrainMurderMysteryBlocks.DARK_OAK_BRANCH)
                .add(TrainMurderMysteryBlocks.MANGROVE_BRANCH)
                .add(TrainMurderMysteryBlocks.CHERRY_BRANCH)
                .add(TrainMurderMysteryBlocks.BAMBOO_POLE)
                .add(TrainMurderMysteryBlocks.CRIMSON_STIPE)
                .add(TrainMurderMysteryBlocks.WARPED_STIPE)
                .add(TrainMurderMysteryBlocks.STRIPPED_OAK_BRANCH)
                .add(TrainMurderMysteryBlocks.STRIPPED_SPRUCE_BRANCH)
                .add(TrainMurderMysteryBlocks.STRIPPED_BIRCH_BRANCH)
                .add(TrainMurderMysteryBlocks.STRIPPED_JUNGLE_BRANCH)
                .add(TrainMurderMysteryBlocks.STRIPPED_ACACIA_BRANCH)
                .add(TrainMurderMysteryBlocks.STRIPPED_DARK_OAK_BRANCH)
                .add(TrainMurderMysteryBlocks.STRIPPED_MANGROVE_BRANCH)
                .add(TrainMurderMysteryBlocks.STRIPPED_CHERRY_BRANCH)
                .add(TrainMurderMysteryBlocks.STRIPPED_BAMBOO_POLE)
                .add(TrainMurderMysteryBlocks.STRIPPED_CRIMSON_STIPE)
                .add(TrainMurderMysteryBlocks.STRIPPED_WARPED_STIPE);

        this.getOrCreateTagBuilder(TrainMurderMysteryBlockTags.VENT_SHAFTS)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_VENT_SHAFT)
                .add(TrainMurderMysteryBlocks.DARK_STEEL_VENT_SHAFT)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_VENT_SHAFT);

        this.getOrCreateTagBuilder(TrainMurderMysteryBlockTags.VENT_HATCHES)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_VENT_HATCH)
                .add(TrainMurderMysteryBlocks.DARK_STEEL_VENT_HATCH)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_VENT_HATCH);

        this.getOrCreateTagBuilder(TrainMurderMysteryBlockTags.WALKWAYS)
                .add(TrainMurderMysteryBlocks.METAL_SHEET_WALKWAY)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_WALKWAY)
                .add(TrainMurderMysteryBlocks.DARK_STEEL_WALKWAY);

        this.getOrCreateTagBuilder(TrainMurderMysteryBlockTags.SPRINKLERS)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_SPRINKLER)
                .add(TrainMurderMysteryBlocks.GOLD_SPRINKLER);

        this.getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .addTag(TrainMurderMysteryBlockTags.WALKWAYS)
                .add(TrainMurderMysteryBlocks.MAHOGANY_PANEL)
                .add(TrainMurderMysteryBlocks.BUBINGA_PANEL)
                .add(TrainMurderMysteryBlocks.EBONY_PANEL);

        this.getOrCreateTagBuilder(BlockTags.ENCHANTMENT_POWER_PROVIDER)
                .add(TrainMurderMysteryBlocks.MAHOGANY_BOOKSHELF)
                .add(TrainMurderMysteryBlocks.BUBINGA_BOOKSHELF)
                .add(TrainMurderMysteryBlocks.EBONY_BOOKSHELF);

        this.getOrCreateTagBuilder(BlockTags.CLIMBABLE)
                .addTag(TrainMurderMysteryBlockTags.VENT_SHAFTS)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_LADDER);

        this.getOrCreateTagBuilder(BlockTags.GUARDED_BY_PIGLINS)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_STAIRS)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_SLAB)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_WALL)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_PILLAR)
                .add(TrainMurderMysteryBlocks.GOLD)
                .add(TrainMurderMysteryBlocks.GOLD_STAIRS)
                .add(TrainMurderMysteryBlocks.GOLD_SLAB)
                .add(TrainMurderMysteryBlocks.GOLD_WALL)
                .add(TrainMurderMysteryBlocks.GOLD_PILLAR)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD_STAIRS)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD_SLAB)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD_WALL)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD_PILLAR)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_VENT_HATCH)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_VENT_SHAFT)
                .add(TrainMurderMysteryBlocks.GOLD_BAR)
                .add(TrainMurderMysteryBlocks.GOLD_LEDGE)
                .add(TrainMurderMysteryBlocks.TRIMMED_LANTERN)
                .add(TrainMurderMysteryBlocks.WALL_LAMP)
                .add(TrainMurderMysteryBlocks.GOLD_ORNAMENT);

        this.getOrCreateTagBuilder(BlockTags.BEDS)
                .add(TrainMurderMysteryBlocks.WHITE_TRIMMED_BED)
                .add(TrainMurderMysteryBlocks.RED_TRIMMED_BED);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS)
                .add(TrainMurderMysteryBlocks.SMALL_BUTTON)
                .add(TrainMurderMysteryBlocks.ELEVATOR_BUTTON);

        this.getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(TrainMurderMysteryBlocks.MAHOGANY_PLANKS)
                .add(TrainMurderMysteryBlocks.BUBINGA_PLANKS)
                .add(TrainMurderMysteryBlocks.EBONY_PLANKS);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
                .add(TrainMurderMysteryBlocks.MAHOGANY_STAIRS)
                .add(TrainMurderMysteryBlocks.BUBINGA_STAIRS)
                .add(TrainMurderMysteryBlocks.EBONY_STAIRS);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
                .add(TrainMurderMysteryBlocks.MAHOGANY_SLAB)
                .add(TrainMurderMysteryBlocks.BUBINGA_SLAB)
                .add(TrainMurderMysteryBlocks.EBONY_SLAB);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_FENCES);

        this.getOrCreateTagBuilder(BlockTags.FENCE_GATES);

        this.getOrCreateTagBuilder(BlockTags.STAIRS)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_STAIRS)
                .add(TrainMurderMysteryBlocks.GOLD_STAIRS)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD_STAIRS)
                .add(TrainMurderMysteryBlocks.METAL_SHEET_STAIRS)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_STAIRS)
                .add(TrainMurderMysteryBlocks.MARBLE_STAIRS)
                .add(TrainMurderMysteryBlocks.MARBLE_TILE_STAIRS)
                .add(TrainMurderMysteryBlocks.DARK_MARBLE_STAIRS)
                .add(TrainMurderMysteryBlocks.WHITE_HULL_STAIRS)
                .add(TrainMurderMysteryBlocks.BLACK_HULL_STAIRS)
                .add(TrainMurderMysteryBlocks.BLACK_HULL_SHEET_STAIRS)
                .add(TrainMurderMysteryBlocks.MAHOGANY_HERRINGBONE_STAIRS)
                .add(TrainMurderMysteryBlocks.SMOOTH_MAHOGANY_STAIRS)
                .add(TrainMurderMysteryBlocks.BUBINGA_HERRINGBONE_STAIRS)
                .add(TrainMurderMysteryBlocks.SMOOTH_BUBINGA_STAIRS)
                .add(TrainMurderMysteryBlocks.EBONY_HERRINGBONE_STAIRS)
                .add(TrainMurderMysteryBlocks.SMOOTH_EBONY_STAIRS);

        this.getOrCreateTagBuilder(BlockTags.SLABS)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_SLAB)
                .add(TrainMurderMysteryBlocks.GOLD_SLAB)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD_SLAB)
                .add(TrainMurderMysteryBlocks.METAL_SHEET_SLAB)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_SLAB)
                .add(TrainMurderMysteryBlocks.MARBLE_SLAB)
                .add(TrainMurderMysteryBlocks.MARBLE_TILE_SLAB)
                .add(TrainMurderMysteryBlocks.DARK_MARBLE_SLAB)
                .add(TrainMurderMysteryBlocks.WHITE_HULL_SLAB)
                .add(TrainMurderMysteryBlocks.BLACK_HULL_SLAB)
                .add(TrainMurderMysteryBlocks.BLACK_HULL_SHEET_SLAB)
                .add(TrainMurderMysteryBlocks.MAHOGANY_HERRINGBONE_SLAB)
                .add(TrainMurderMysteryBlocks.SMOOTH_MAHOGANY_SLAB)
                .add(TrainMurderMysteryBlocks.BUBINGA_HERRINGBONE_SLAB)
                .add(TrainMurderMysteryBlocks.SMOOTH_BUBINGA_SLAB)
                .add(TrainMurderMysteryBlocks.EBONY_HERRINGBONE_SLAB)
                .add(TrainMurderMysteryBlocks.SMOOTH_EBONY_SLAB);

        this.getOrCreateTagBuilder(BlockTags.WALLS)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_WALL)
                .add(TrainMurderMysteryBlocks.GOLD_WALL)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD_WALL)
                .add(TrainMurderMysteryBlocks.METAL_SHEET_WALL)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_WALL)
                .add(TrainMurderMysteryBlocks.MARBLE_WALL)
                .add(TrainMurderMysteryBlocks.MARBLE_TILE_WALL)
                .add(TrainMurderMysteryBlocks.DARK_MARBLE_WALL)
                .add(TrainMurderMysteryBlocks.WHITE_HULL_WALL)
                .add(TrainMurderMysteryBlocks.BLACK_HULL_WALL)
                .add(TrainMurderMysteryBlocks.BLACK_HULL_SHEET_WALL);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_DOORS);

        this.getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS);

        this.getOrCreateTagBuilder(BlockTags.DOORS)
                .add(TrainMurderMysteryBlocks.METAL_SHEET_DOOR)
                .add(TrainMurderMysteryBlocks.COCKPIT_DOOR);

        this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .addTag(TrainMurderMysteryBlockTags.BRANCHES)
                .add(TrainMurderMysteryBlocks.MAHOGANY_HERRINGBONE)
                .add(TrainMurderMysteryBlocks.MAHOGANY_HERRINGBONE_STAIRS)
                .add(TrainMurderMysteryBlocks.MAHOGANY_HERRINGBONE_SLAB)
                .add(TrainMurderMysteryBlocks.SMOOTH_MAHOGANY)
                .add(TrainMurderMysteryBlocks.SMOOTH_MAHOGANY_STAIRS)
                .add(TrainMurderMysteryBlocks.SMOOTH_MAHOGANY_SLAB)
                .add(TrainMurderMysteryBlocks.MAHOGANY_CABINET)
                .add(TrainMurderMysteryBlocks.MAHOGANY_PANEL)
                .add(TrainMurderMysteryBlocks.BUBINGA_HERRINGBONE)
                .add(TrainMurderMysteryBlocks.BUBINGA_HERRINGBONE_STAIRS)
                .add(TrainMurderMysteryBlocks.BUBINGA_HERRINGBONE_SLAB)
                .add(TrainMurderMysteryBlocks.SMOOTH_BUBINGA)
                .add(TrainMurderMysteryBlocks.SMOOTH_BUBINGA_STAIRS)
                .add(TrainMurderMysteryBlocks.SMOOTH_BUBINGA_SLAB)
                .add(TrainMurderMysteryBlocks.BUBINGA_CABINET)
                .add(TrainMurderMysteryBlocks.BUBINGA_PANEL)
                .add(TrainMurderMysteryBlocks.EBONY_HERRINGBONE)
                .add(TrainMurderMysteryBlocks.EBONY_HERRINGBONE_STAIRS)
                .add(TrainMurderMysteryBlocks.EBONY_HERRINGBONE_SLAB)
                .add(TrainMurderMysteryBlocks.SMOOTH_EBONY)
                .add(TrainMurderMysteryBlocks.SMOOTH_EBONY_STAIRS)
                .add(TrainMurderMysteryBlocks.SMOOTH_EBONY_SLAB)
                .add(TrainMurderMysteryBlocks.EBONY_CABINET)
                .add(TrainMurderMysteryBlocks.EBONY_PANEL)
                .add(TrainMurderMysteryBlocks.PANEL_STRIPES)
                .add(TrainMurderMysteryBlocks.TRIMMED_RAILING)
                .add(TrainMurderMysteryBlocks.TRIMMED_RAILING_POST)
                .add(TrainMurderMysteryBlocks.DIAGONAL_TRIMMED_RAILING)
                .add(TrainMurderMysteryBlocks.TRIMMED_EBONY_STAIRS)
                .add(TrainMurderMysteryBlocks.WHITE_LOUNGE_COUCH)
                .add(TrainMurderMysteryBlocks.WHITE_OTTOMAN)
                .add(TrainMurderMysteryBlocks.WHITE_TRIMMED_BED)
                .add(TrainMurderMysteryBlocks.RED_TRIMMED_BED)
                .add(TrainMurderMysteryBlocks.BLUE_LOUNGE_COUCH)
                .add(TrainMurderMysteryBlocks.GREEN_LOUNGE_COUCH)
                .add(TrainMurderMysteryBlocks.RED_LEATHER_COUCH)
                .add(TrainMurderMysteryBlocks.BROWN_LEATHER_COUCH)
                .add(TrainMurderMysteryBlocks.BEIGE_LEATHER_COUCH)
                .add(TrainMurderMysteryBlocks.COFFEE_TABLE)
                .add(TrainMurderMysteryBlocks.BAR_TABLE)
                .add(TrainMurderMysteryBlocks.BAR_STOOL)
                .add(TrainMurderMysteryBlocks.SMALL_BUTTON)
                .add(TrainMurderMysteryBlocks.ELEVATOR_BUTTON)
                .add(TrainMurderMysteryBlocks.MAHOGANY_BOOKSHELF)
                .add(TrainMurderMysteryBlocks.BUBINGA_BOOKSHELF)
                .add(TrainMurderMysteryBlocks.EBONY_BOOKSHELF);

        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .addTag(TrainMurderMysteryBlockTags.VENT_SHAFTS)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_VENT_HATCH)
                .add(TrainMurderMysteryBlocks.DARK_STEEL_VENT_HATCH)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_VENT_HATCH)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_STAIRS)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_SLAB)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_WALL)
                .add(TrainMurderMysteryBlocks.TARNISHED_GOLD_PILLAR)
                .add(TrainMurderMysteryBlocks.GOLD)
                .add(TrainMurderMysteryBlocks.GOLD_STAIRS)
                .add(TrainMurderMysteryBlocks.GOLD_SLAB)
                .add(TrainMurderMysteryBlocks.GOLD_WALL)
                .add(TrainMurderMysteryBlocks.GOLD_PILLAR)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD_STAIRS)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD_SLAB)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD_WALL)
                .add(TrainMurderMysteryBlocks.PRISTINE_GOLD_PILLAR)
                .add(TrainMurderMysteryBlocks.METAL_SHEET)
                .add(TrainMurderMysteryBlocks.METAL_SHEET_STAIRS)
                .add(TrainMurderMysteryBlocks.METAL_SHEET_SLAB)
                .add(TrainMurderMysteryBlocks.METAL_SHEET_DOOR)
                .add(TrainMurderMysteryBlocks.COCKPIT_DOOR)
                .add(TrainMurderMysteryBlocks.METAL_SHEET_WALKWAY)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_LADDER)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_STAIRS)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_SLAB)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_WALKWAY)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_PILLAR)
                .add(TrainMurderMysteryBlocks.DARK_STEEL)
                .add(TrainMurderMysteryBlocks.DARK_STEEL_STAIRS)
                .add(TrainMurderMysteryBlocks.DARK_STEEL_SLAB)
                .add(TrainMurderMysteryBlocks.DARK_STEEL_WALKWAY)
                .add(TrainMurderMysteryBlocks.DARK_STEEL_PILLAR)
                .add(TrainMurderMysteryBlocks.RHOMBUS_GLASS)
                .add(TrainMurderMysteryBlocks.HULL_GLASS)
                .add(TrainMurderMysteryBlocks.RHOMBUS_HULL_GLASS)
                .add(TrainMurderMysteryBlocks.GOLDEN_GLASS_PANEL)
                .add(TrainMurderMysteryBlocks.CULLING_GLASS)
                .add(TrainMurderMysteryBlocks.MARBLE)
                .add(TrainMurderMysteryBlocks.MARBLE_STAIRS)
                .add(TrainMurderMysteryBlocks.MARBLE_SLAB)
                .add(TrainMurderMysteryBlocks.MARBLE_TILES)
                .add(TrainMurderMysteryBlocks.MARBLE_TILE_STAIRS)
                .add(TrainMurderMysteryBlocks.MARBLE_TILE_SLAB)
                .add(TrainMurderMysteryBlocks.DARK_MARBLE)
                .add(TrainMurderMysteryBlocks.DARK_MARBLE_STAIRS)
                .add(TrainMurderMysteryBlocks.DARK_MARBLE_SLAB)
                .add(TrainMurderMysteryBlocks.MARBLE_MOSAIC)
                .add(TrainMurderMysteryBlocks.WHITE_HULL)
                .add(TrainMurderMysteryBlocks.WHITE_HULL_STAIRS)
                .add(TrainMurderMysteryBlocks.WHITE_HULL_SLAB)
                .add(TrainMurderMysteryBlocks.CULLING_WHITE_HULL)
                .add(TrainMurderMysteryBlocks.BLACK_HULL)
                .add(TrainMurderMysteryBlocks.BLACK_HULL_STAIRS)
                .add(TrainMurderMysteryBlocks.BLACK_HULL_SLAB)
                .add(TrainMurderMysteryBlocks.CULLING_BLACK_HULL)
                .add(TrainMurderMysteryBlocks.BLACK_HULL_SHEETS)
                .add(TrainMurderMysteryBlocks.BLACK_HULL_SHEET_STAIRS)
                .add(TrainMurderMysteryBlocks.BLACK_HULL_SHEET_SLAB)
                .add(TrainMurderMysteryBlocks.CARGO_BOX)
                .add(TrainMurderMysteryBlocks.GOLD_BAR)
                .add(TrainMurderMysteryBlocks.GOLD_LEDGE)
                .add(TrainMurderMysteryBlocks.STAINLESS_STEEL_BAR)
                .add(TrainMurderMysteryBlocks.TRIMMED_LANTERN)
                .add(TrainMurderMysteryBlocks.WALL_LAMP)
                .add(TrainMurderMysteryBlocks.NEON_PILLAR)
                .add(TrainMurderMysteryBlocks.NEON_TUBE)
                .addTag(TrainMurderMysteryBlockTags.SPRINKLERS)
                .add(TrainMurderMysteryBlocks.NAVY_STEEL_PANEL)
                .add(TrainMurderMysteryBlocks.NAVY_STEEL_TILES)
                .add(TrainMurderMysteryBlocks.NAVY_STEEL_TILES_PANEL)
                .add(TrainMurderMysteryBlocks.SMOOTH_NAVY_STEEL)
                .add(TrainMurderMysteryBlocks.SMOOTH_NAVY_STEEL_STAIRS)
                .add(TrainMurderMysteryBlocks.SMOOTH_NAVY_STEEL_SLAB)
                .add(TrainMurderMysteryBlocks.SMOOTH_NAVY_STEEL_PANEL);



        this.getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .addTag(TrainMurderMysteryBlockTags.VENT_SHAFTS);
    }
}
