package dev.doctor4t.wathe.index;

import dev.doctor4t.ratatouille.util.registrar.BlockEntityTypeRegistrar;
import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.block.entity.HornBlockEntity;
import dev.doctor4t.wathe.block_entity.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public interface WatheBlockEntities {
    BlockEntityTypeRegistrar registrar = new BlockEntityTypeRegistrar(Wathe.MOD_ID);

    BlockEntityType<SprinklerBlockEntity> SPRINKLER = registrar.create("sprinkler", BlockEntityType.Builder.create(SprinklerBlockEntity::new, WatheBlocks.GOLD_SPRINKLER, WatheBlocks.STAINLESS_STEEL_SPRINKLER));
    BlockEntityType<SmallDoorBlockEntity> SMALL_GLASS_DOOR = registrar.create("small_glass_door", BlockEntityType.Builder.create(SmallDoorBlockEntity::createGlass, WatheBlocks.SMALL_GLASS_DOOR));
    BlockEntityType<SmallDoorBlockEntity> SMALL_WOOD_DOOR = registrar.create("small_wood_door", BlockEntityType.Builder.create(SmallDoorBlockEntity::createWood, WatheBlocks.SMALL_WOOD_DOOR));
    BlockEntityType<SmallDoorBlockEntity> ANTHRACITE_STEEL_DOOR = registrar.create("anthracite_steel_door", BlockEntityType.Builder.create((pos, state) -> new SmallDoorBlockEntity(WatheBlockEntities.ANTHRACITE_STEEL_DOOR, pos, state), WatheBlocks.ANTHRACITE_STEEL_DOOR));
    BlockEntityType<SmallDoorBlockEntity> KHAKI_STEEL_DOOR = registrar.create("khaki_steel_door", BlockEntityType.Builder.create((pos, state) -> new SmallDoorBlockEntity(WatheBlockEntities.KHAKI_STEEL_DOOR, pos, state), WatheBlocks.KHAKI_STEEL_DOOR));
    BlockEntityType<SmallDoorBlockEntity> MAROON_STEEL_DOOR = registrar.create("maroon_steel_door", BlockEntityType.Builder.create((pos, state) -> new SmallDoorBlockEntity(WatheBlockEntities.MAROON_STEEL_DOOR, pos, state), WatheBlocks.MAROON_STEEL_DOOR));
    BlockEntityType<SmallDoorBlockEntity> MUNTZ_STEEL_DOOR = registrar.create("muntz_steel_door", BlockEntityType.Builder.create((pos, state) -> new SmallDoorBlockEntity(WatheBlockEntities.MUNTZ_STEEL_DOOR, pos, state), WatheBlocks.MUNTZ_STEEL_DOOR));
    BlockEntityType<SmallDoorBlockEntity> NAVY_STEEL_DOOR = registrar.create("navy_steel_door", BlockEntityType.Builder.create((pos, state) -> new SmallDoorBlockEntity(WatheBlockEntities.NAVY_STEEL_DOOR, pos, state), WatheBlocks.NAVY_STEEL_DOOR));
    BlockEntityType<WheelBlockEntity> WHEEL = registrar.create("wheel", BlockEntityType.Builder.create((pos, state) -> new WheelBlockEntity(WatheBlockEntities.WHEEL, pos, state), WatheBlocks.WHEEL));
    BlockEntityType<WheelBlockEntity> RUSTED_WHEEL = registrar.create("rusted_wheel", BlockEntityType.Builder.create((pos, state) -> new WheelBlockEntity(WatheBlockEntities.RUSTED_WHEEL, pos, state), WatheBlocks.RUSTED_WHEEL));
    BlockEntityType<BeveragePlateBlockEntity> BEVERAGE_PLATE = registrar.create("beverage_plate", BlockEntityType.Builder.create(BeveragePlateBlockEntity::new, WatheBlocks.FOOD_PLATTER, WatheBlocks.DRINK_TRAY));
    BlockEntityType<TrimmedBedBlockEntity> TRIMMED_BED = registrar.create("trimmed_bed", BlockEntityType.Builder.create(TrimmedBedBlockEntity::create, WatheBlocks.RED_TRIMMED_BED, WatheBlocks.WHITE_TRIMMED_BED));
    BlockEntityType<HornBlockEntity> HORN = registrar.create("horn", BlockEntityType.Builder.create(HornBlockEntity::new, WatheBlocks.HORN));
    BlockEntityType<ChimneyBlockEntity> CHIMNEY = registrar.create("chimney", BlockEntityType.Builder.create(ChimneyBlockEntity::new, WatheBlocks.CHIMNEY));

    static void initialize() {
        registrar.registerEntries();

        registrar.getEntriesToRegister().forEach((item, identifier) -> Registries.BLOCK_ENTITY_TYPE.addAlias(Identifier.of("trainmurdermystery", identifier.getPath()), identifier));
    }
}
