package dev.doctor4t.wathe.index;

import dev.doctor4t.ratatouille.util.registrar.SoundEventRegistrar;
import dev.doctor4t.wathe.Wathe;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;

public interface WatheSounds {
    SoundEventRegistrar registrar = new SoundEventRegistrar(Wathe.MOD_ID);

    BlockSoundGroup VENT_SHAFT = registrar.createBlockSoundGroup("block.vent_shaft", 1f, 1f);

    // Block special sounds
    SoundEvent BLOCK_CARGO_BOX_OPEN = registrar.create("block.cargo_box.open");
    SoundEvent BLOCK_CARGO_BOX_CLOSE = registrar.create("block.cargo_box.close");
    SoundEvent BLOCK_LIGHT_TOGGLE = registrar.create("block.light.toggle");
    SoundEvent BLOCK_PRIVACY_PANEL_TOGGLE = registrar.create("block.privacy_panel.toggle");
    SoundEvent BLOCK_SPACE_BUTTON_TOGGLE = registrar.create("block.space_button.toggle");
    SoundEvent BLOCK_BUTTON_TOGGLE_NO_POWER = registrar.create("block.button.toggle_no_power");
    SoundEvent BLOCK_DOOR_TOGGLE = registrar.create("block.door.toggle");
    SoundEvent BLOCK_SPRINKLER_RUN = registrar.create("block.sprinkler.run");
    SoundEvent BLOCK_DOOR_LOCKED = registrar.create("block.door.locked");

    // Items
    SoundEvent ITEM_KEY_DOOR = registrar.create("item.key.door");
    SoundEvent ITEM_LOCKPICK_DOOR = registrar.create("item.lockpick.door");
    SoundEvent ITEM_KNIFE_PREPARE = registrar.create("item.knife.prepare");
    SoundEvent ITEM_KNIFE_STAB = registrar.create("item.knife.stab");
    SoundEvent ITEM_REVOLVER_CLICK = registrar.create("item.revolver.click");
    SoundEvent ITEM_REVOLVER_SHOOT = registrar.create("item.revolver.shoot");
    SoundEvent ITEM_DERRINGER_RELOAD = registrar.create("item.derringer.reload");
    SoundEvent ITEM_BAT_HIT = registrar.create("item.bat.hit");
    SoundEvent ITEM_CROWBAR_PRY = registrar.create("item.crowbar.pry");
    SoundEvent ITEM_GRENADE_THROW = registrar.create("item.grenade.throw");
    SoundEvent ITEM_GRENADE_EXPLODE = registrar.create("item.grenade.explode");
    SoundEvent ITEM_PSYCHO_ARMOUR = registrar.create("item.psycho.armour");

    // Ambience
    SoundEvent AMBIENT_TRAIN_INSIDE = registrar.create("ambient.train.inside");
    SoundEvent AMBIENT_TRAIN_OUTSIDE = registrar.create("ambient.train.outside");
    SoundEvent AMBIENT_PSYCHO_DRONE = registrar.create("ambient.psycho_drone");
    SoundEvent AMBIENT_BLACKOUT = registrar.create("ambient.blackout");
    SoundEvent AMBIENT_TRAIN_HORN = registrar.create("ambient.train.horn");

    // UI
    SoundEvent UI_SHOP_BUY = registrar.create("ui.shop.buy");
    SoundEvent UI_SHOP_BUY_FAIL = registrar.create("ui.shop.buy_fail");
    SoundEvent UI_PIANO = registrar.create("ui.piano");
    SoundEvent UI_PIANO_WIN = registrar.create("ui.piano_win");
    SoundEvent UI_PIANO_LOSE = registrar.create("ui.piano_lose");
    SoundEvent UI_PIANO_STINGER = registrar.create("ui.piano_stinger");
    SoundEvent UI_RISER = registrar.create("ui.riser");

    static void initialize() {
        registrar.registerEntries();
    }
}
