package dev.doctor4t.wathe.index;

import dev.doctor4t.ratatouille.util.registrar.ItemRegistrar;
import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public @SuppressWarnings("unchecked") interface WatheItems {
    ItemRegistrar registrar = new ItemRegistrar(Wathe.MOD_ID);

    RegistryKey<ItemGroup> BUILDING_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Wathe.id("building"));
    RegistryKey<ItemGroup> DECORATION_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Wathe.id("decoration"));
    RegistryKey<ItemGroup> EQUIPMENT_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Wathe.id("equipment"));

    Item KEY = registrar.create("key", new KeyItem(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item LOCKPICK = registrar.create("lockpick", new LockpickItem(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item KNIFE = registrar.create(KnifeItem.ITEM_ID.getPath(), new KnifeItem(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item BAT = registrar.create("bat", new BatItem(new Item.Settings().maxCount(1).attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.WOOD, 0.0F, -3.0F))), EQUIPMENT_GROUP);
    Item CROWBAR = registrar.create("crowbar", new CrowbarItem(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item GRENADE = registrar.create("grenade", new GrenadeItem(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item THROWN_GRENADE = registrar.create("thrown_grenade", new GrenadeItem(new Item.Settings().maxCount(1)));
    Item FIRECRACKER = registrar.create("firecracker", new FirecrackerItem(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item REVOLVER = registrar.create("revolver", new RevolverItem(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item DERRINGER = registrar.create("derringer", new DerringerItem(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item BODY_BAG = registrar.create("body_bag", new BodyBagItem(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item LETTER = registrar.create("letter", new Item(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item BLACKOUT = registrar.create("blackout", new Item(new Item.Settings().maxCount(1)));
    Item PSYCHO_MODE = registrar.create("psycho_mode", new Item(new Item.Settings().maxCount(1)));
    Item POISON_VIAL = registrar.create("poison_vial", new Item(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item SCORPION = registrar.create("scorpion", new Item(new Item.Settings().maxCount(1)), EQUIPMENT_GROUP);
    Item OLD_FASHIONED = registrar.create("old_fashioned", new CocktailItem(new Item.Settings().maxCount(1).food(FoodComponents.HONEY_BOTTLE)), EQUIPMENT_GROUP);
    Item MOJITO = registrar.create("mojito", new CocktailItem(new Item.Settings().maxCount(1).food(FoodComponents.HONEY_BOTTLE)), EQUIPMENT_GROUP);
    Item MARTINI = registrar.create("martini", new CocktailItem(new Item.Settings().maxCount(1).food(FoodComponents.HONEY_BOTTLE)), EQUIPMENT_GROUP);
    Item COSMOPOLITAN = registrar.create("cosmopolitan", new CocktailItem(new Item.Settings().maxCount(1).food(FoodComponents.HONEY_BOTTLE)), EQUIPMENT_GROUP);
    Item CHAMPAGNE = registrar.create("champagne", new CocktailItem(new Item.Settings().maxCount(1).food(FoodComponents.HONEY_BOTTLE)), EQUIPMENT_GROUP);
    Item NOTE = registrar.create("note", new NoteItem(new Item.Settings().maxCount(4)), EQUIPMENT_GROUP);

    static void initialize() {
        registrar.registerEntries();

        Registry.register(Registries.ITEM_GROUP, BUILDING_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.wathe.building"))
                .icon(() -> new ItemStack(WatheBlocks.TARNISHED_GOLD_PILLAR))
                .build());
        Registry.register(Registries.ITEM_GROUP, DECORATION_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.wathe.decoration"))
                .icon(() -> new ItemStack(WatheBlocks.TARNISHED_GOLD_VENT_SHAFT))
                .build());
        Registry.register(Registries.ITEM_GROUP, EQUIPMENT_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.wathe.equipment"))
                .icon(() -> new ItemStack(KEY))
                .build());

        registrar.getEntriesToRegister().forEach((item, identifier) -> Registries.ITEM.addAlias(Identifier.of("trainmurdermystery", identifier.getPath()), identifier));
    }
}
