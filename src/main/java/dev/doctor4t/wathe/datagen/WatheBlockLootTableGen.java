package dev.doctor4t.wathe.datagen;

import dev.doctor4t.wathe.block.OrnamentBlock;
import dev.doctor4t.wathe.block.PanelBlock;
import dev.doctor4t.wathe.block.property.OrnamentShape;
import dev.doctor4t.wathe.index.WatheBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BedPart;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Direction;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class WatheBlockLootTableGen extends FabricBlockLootTableProvider {

    public WatheBlockLootTableGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        // Vents
        this.addSelfDrop(WatheBlocks.STAINLESS_STEEL_VENT_SHAFT);
        this.addSelfDrop(WatheBlocks.STAINLESS_STEEL_VENT_HATCH);
        this.addSelfDrop(WatheBlocks.DARK_STEEL_VENT_SHAFT);
        this.addSelfDrop(WatheBlocks.DARK_STEEL_VENT_HATCH);
        this.addSelfDrop(WatheBlocks.TARNISHED_GOLD_VENT_SHAFT);
        this.addSelfDrop(WatheBlocks.TARNISHED_GOLD_VENT_HATCH);
        // Metallic Blocks
        this.addFamily(WatheBlocks.Family.TARNISHED_GOLD);
        this.addSelfDrop(WatheBlocks.TARNISHED_GOLD_PILLAR);
        this.addFamily(WatheBlocks.Family.GOLD);
        this.addSelfDrop(WatheBlocks.GOLD_PILLAR);
        this.addFamily(WatheBlocks.Family.PRISTINE_GOLD);
        this.addSelfDrop(WatheBlocks.PRISTINE_GOLD_PILLAR);
        this.addFamily(WatheBlocks.Family.WHITE_HULL);
        this.addSelfDrop(WatheBlocks.CULLING_WHITE_HULL);
        this.addFamily(WatheBlocks.Family.BLACK_HULL);
        this.addSelfDrop(WatheBlocks.CULLING_BLACK_HULL);
        this.addFamily(WatheBlocks.Family.BLACK_HULL_SHEET);
        this.addSelfDrop(WatheBlocks.METAL_SHEET);
        this.addSelfDrop(WatheBlocks.METAL_SHEET_STAIRS);
        this.addSelfDrop(WatheBlocks.METAL_SHEET_SLAB);
        this.addSelfDrop(WatheBlocks.METAL_SHEET_WALL);
        this.addSelfDrop(WatheBlocks.METAL_SHEET_DOOR, this::doorDrops);
        this.addSelfDrop(WatheBlocks.METAL_SHEET_WALKWAY);
        this.addSelfDrop(WatheBlocks.STAINLESS_STEEL_LADDER);
        this.addFamily(WatheBlocks.Family.STAINLESS_STEEL);
        this.addSelfDrop(WatheBlocks.STAINLESS_STEEL_WALKWAY);
        this.addSelfDrop(WatheBlocks.STAINLESS_STEEL_BRANCH);
        this.addSelfDrop(WatheBlocks.STAINLESS_STEEL_PILLAR);
        // Glass
        this.addSelfDrop(WatheBlocks.GOLDEN_GLASS_PANEL);
        this.addSelfDrop(WatheBlocks.CULLING_GLASS);
        this.addSelfDrop(WatheBlocks.RHOMBUS_GLASS);
        this.addSelfDrop(WatheBlocks.HULL_GLASS);
        this.addSelfDrop(WatheBlocks.PRIVACY_GLASS_PANEL);
        // Stones
        this.addFamily(WatheBlocks.Family.MARBLE);
        this.addSelfDrop(WatheBlocks.MARBLE_MOSAIC);
        this.addFamily(WatheBlocks.Family.MARBLE_TILE);
        this.addFamily(WatheBlocks.Family.DARK_MARBLE);
        // Woods
        this.addFamily(WatheBlocks.Family.MAHOGANY);
        this.addFamily(WatheBlocks.Family.MAHOGANY_HERRINGBONE);
        this.addFamily(WatheBlocks.Family.SMOOTH_MAHOGANY);
        this.addSelfDrop(WatheBlocks.MAHOGANY_PANEL, this::panelDrops);
        this.addSelfDrop(WatheBlocks.MAHOGANY_CABINET, this::nameableContainerDrops);
        this.addSelfDrop(WatheBlocks.MAHOGANY_BOOKSHELF);
        this.addFamily(WatheBlocks.Family.BUBINGA);
        this.addFamily(WatheBlocks.Family.BUBINGA_HERRINGBONE);
        this.addFamily(WatheBlocks.Family.SMOOTH_BUBINGA);
        this.addSelfDrop(WatheBlocks.BUBINGA_PANEL, this::panelDrops);
        this.addSelfDrop(WatheBlocks.BUBINGA_CABINET, this::nameableContainerDrops);
        this.addSelfDrop(WatheBlocks.BUBINGA_BOOKSHELF);
        this.addFamily(WatheBlocks.Family.EBONY);
        this.addFamily(WatheBlocks.Family.EBONY_HERRINGBONE);
        this.addFamily(WatheBlocks.Family.SMOOTH_EBONY);
        this.addSelfDrop(WatheBlocks.EBONY_PANEL, this::panelDrops);
        this.addSelfDrop(WatheBlocks.EBONY_CABINET, this::nameableContainerDrops);
        this.addSelfDrop(WatheBlocks.TRIMMED_EBONY_STAIRS);
        this.addSelfDrop(WatheBlocks.EBONY_BOOKSHELF);
        this.addSelfDrop(WatheBlocks.OAK_BRANCH);
        this.addSelfDrop(WatheBlocks.SPRUCE_BRANCH);
        this.addSelfDrop(WatheBlocks.BIRCH_BRANCH);
        this.addSelfDrop(WatheBlocks.JUNGLE_BRANCH);
        this.addSelfDrop(WatheBlocks.ACACIA_BRANCH);
        this.addSelfDrop(WatheBlocks.DARK_OAK_BRANCH);
        this.addSelfDrop(WatheBlocks.MANGROVE_BRANCH);
        this.addSelfDrop(WatheBlocks.CHERRY_BRANCH);
        this.addSelfDrop(WatheBlocks.BAMBOO_POLE);
        this.addSelfDrop(WatheBlocks.CRIMSON_STIPE);
        this.addSelfDrop(WatheBlocks.WARPED_STIPE);
        this.addSelfDrop(WatheBlocks.STRIPPED_OAK_BRANCH);
        this.addSelfDrop(WatheBlocks.STRIPPED_SPRUCE_BRANCH);
        this.addSelfDrop(WatheBlocks.STRIPPED_BIRCH_BRANCH);
        this.addSelfDrop(WatheBlocks.STRIPPED_JUNGLE_BRANCH);
        this.addSelfDrop(WatheBlocks.STRIPPED_ACACIA_BRANCH);
        this.addSelfDrop(WatheBlocks.STRIPPED_DARK_OAK_BRANCH);
        this.addSelfDrop(WatheBlocks.STRIPPED_MANGROVE_BRANCH);
        this.addSelfDrop(WatheBlocks.STRIPPED_CHERRY_BRANCH);
        this.addSelfDrop(WatheBlocks.STRIPPED_BAMBOO_POLE);
        this.addSelfDrop(WatheBlocks.STRIPPED_CRIMSON_STIPE);
        this.addSelfDrop(WatheBlocks.STRIPPED_WARPED_STIPE);
        this.addSelfDrop(WatheBlocks.PANEL_STRIPES);
        this.addSelfDrop(WatheBlocks.RAIL_BEAM);
        this.addSelfDrop(WatheBlocks.TRIMMED_RAILING_POST, block -> this.drops(WatheBlocks.TRIMMED_RAILING));
        this.addSelfDrop(WatheBlocks.DIAGONAL_TRIMMED_RAILING, block -> this.drops(WatheBlocks.TRIMMED_RAILING));
        this.addSelfDrop(WatheBlocks.TRIMMED_RAILING);
        // Furniture / Decor
        this.addSelfDrop(WatheBlocks.CARGO_BOX, this::nameableContainerDrops);
        this.addSelfDrop(WatheBlocks.WHITE_LOUNGE_COUCH);
        this.addSelfDrop(WatheBlocks.WHITE_OTTOMAN);
        this.addSelfDrop(WatheBlocks.WHITE_TRIMMED_BED, block -> this.dropsWithProperty(block, BedBlock.PART, BedPart.HEAD));
        this.addSelfDrop(WatheBlocks.RED_TRIMMED_BED, block -> this.dropsWithProperty(block, BedBlock.PART, BedPart.HEAD));
        this.addSelfDrop(WatheBlocks.BLUE_LOUNGE_COUCH);
        this.addSelfDrop(WatheBlocks.GREEN_LOUNGE_COUCH);
        this.addSelfDrop(WatheBlocks.RED_LEATHER_COUCH);
        this.addSelfDrop(WatheBlocks.BROWN_LEATHER_COUCH);
        this.addSelfDrop(WatheBlocks.BEIGE_LEATHER_COUCH);
        this.addSelfDrop(WatheBlocks.COFFEE_TABLE);
        this.addSelfDrop(WatheBlocks.BAR_TABLE);
        this.addSelfDrop(WatheBlocks.BAR_STOOL);
        this.addSelfDrop(WatheBlocks.GOLD_BAR);
        this.addSelfDrop(WatheBlocks.GOLD_LEDGE);
        this.addSelfDrop(WatheBlocks.STAINLESS_STEEL_BAR);
        this.addSelfDrop(WatheBlocks.TRIMMED_LANTERN);
        this.addSelfDrop(WatheBlocks.WALL_LAMP);
        this.addSelfDrop(WatheBlocks.NEON_PILLAR);
        this.addSelfDrop(WatheBlocks.NEON_TUBE);
        this.addSelfDrop(WatheBlocks.STAINLESS_STEEL_SPRINKLER);
        this.addSelfDrop(WatheBlocks.GOLD_SPRINKLER);
        this.addSelfDrop(WatheBlocks.SMALL_BUTTON);
        this.addSelfDrop(WatheBlocks.ELEVATOR_BUTTON);
        this.addSelfDrop(WatheBlocks.GOLD_ORNAMENT, this::ornamentDrops);
        this.addNothingDrop(WatheBlocks.SMALL_GLASS_DOOR);
        this.addNothingDrop(WatheBlocks.SMALL_WOOD_DOOR);
        this.addNothingDrop(WatheBlocks.ANTHRACITE_STEEL_DOOR);
        this.addNothingDrop(WatheBlocks.KHAKI_STEEL_DOOR);
        this.addNothingDrop(WatheBlocks.MAROON_STEEL_DOOR);
        this.addNothingDrop(WatheBlocks.MUNTZ_STEEL_DOOR);
        this.addNothingDrop(WatheBlocks.NAVY_STEEL_DOOR);
        this.addNothingDrop(WatheBlocks.WHEEL);
        this.addNothingDrop(WatheBlocks.RUSTED_WHEEL);
    }

    private void addFamily(BlockFamily family) {
        this.addFamily(family, this::addSelfDrop);
    }

    private void addFamily(BlockFamily family, Consumer<Block> consumer) {
        family.getVariants().values().forEach(consumer);
        consumer.accept(family.getBaseBlock());
    }

    private void addSelfDrop(Block block) {
        this.addSelfDrop(block, this::drops);
    }

    private void addSelfDrop(Block block, Function<Block, LootTable.Builder> function) {
        if (block.getHardness() == -1.0f) {
            // Register drops as nothing if block is unbreakable
            this.addDrop(block, dropsNothing());
        } else {
            this.addDrop(block, function);
        }
    }

    private void addNothingDrop(Block block) {
        this.addDrop(block, dropsNothing());
    }

    private ConstantLootNumberProvider count(float value) {
        return ConstantLootNumberProvider.create(value);
    }

    private LootTable.Builder panelDrops(Block block) {
        return LootTable.builder().pool(LootPool.builder().with(
                this.addSurvivesExplosionCondition(block, ItemEntry.builder(block))
                        .apply(
                                Direction.values(),
                                direction -> SetCountLootFunction.builder(this.count(1), true)
                                        .conditionally(BlockStatePropertyLootCondition.builder(block).properties(
                                                StatePredicate.Builder.create().exactMatch(PanelBlock.getProperty(direction), true)
                                        ))
                        ).apply(SetCountLootFunction.builder(this.count(-1f), true))
        ));
    }

    private LootTable.Builder ornamentDrops(Block block) {
        return LootTable.builder().pool(LootPool.builder().with(
                this.addSurvivesExplosionCondition(block, ItemEntry.builder(block))
                        .apply(
                                OrnamentShape.values(),
                                shape -> SetCountLootFunction.builder(this.count(shape.getCount()), false)
                                        .conditionally(BlockStatePropertyLootCondition.builder(block).properties(
                                                StatePredicate.Builder.create().exactMatch(OrnamentBlock.SHAPE, shape)
                                        ))
                        )
        ));
    }
}
