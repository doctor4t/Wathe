package dev.doctor4t.wathe.game;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.index.WatheItems;
import dev.doctor4t.wathe.util.ShopEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface GameConstants {
    // Logistics
    int FADE_TIME = 40;
    int FADE_PAUSE = 20;

    // Blocks
    int DOOR_AUTOCLOSE_TIME = getInTicks(0, 5);
    int SPRINKLER_POWERED_TIMER = getInTicks(0, 12); // +- the length of the sound
    int SPRINKLER_GIVE_WET = getInTicks(0, 10);

    // Items
    Map<Item, Integer> ITEM_COOLDOWNS = new HashMap<>();

    static void init() {
        ITEM_COOLDOWNS.put(WatheItems.KNIFE, getInTicks(1, 0));
        ITEM_COOLDOWNS.put(WatheItems.REVOLVER, getInTicks(0, 10));
        ITEM_COOLDOWNS.put(WatheItems.DERRINGER, getInTicks(0, 1));
        ITEM_COOLDOWNS.put(WatheItems.GRENADE, getInTicks(5, 0));
        ITEM_COOLDOWNS.put(WatheItems.LOCKPICK, getInTicks(3, 0));
        ITEM_COOLDOWNS.put(WatheItems.CROWBAR, getInTicks(0, 5));
        ITEM_COOLDOWNS.put(WatheItems.BODY_BAG, getInTicks(2, 0));
        ITEM_COOLDOWNS.put(WatheItems.PSYCHO_MODE, getInTicks(5, 0));
        ITEM_COOLDOWNS.put(WatheItems.BLACKOUT, FabricLoader.getInstance().isDevelopmentEnvironment() ? 20 : getInTicks(2, 0));
    }

    int JAMMED_DOOR_TIME = getInTicks(1, 0);

    // Corpses
    int TIME_TO_DECOMPOSITION = getInTicks(1, 0);
    int DECOMPOSING_TIME = getInTicks(4, 0);

    // Task Variables
    float MOOD_GAIN = 0.2f;
    float MOOD_DRAIN = 1f / getInTicks(5, 0);
    int TIME_TO_FIRST_TASK = getInTicks(0, 30);
    int MIN_TASK_COOLDOWN = getInTicks(0, 30);
    int MAX_TASK_COOLDOWN = getInTicks(1, 0);
    int SLEEP_TASK_DURATION = getInTicks(0, 8);
    int OUTSIDE_TASK_DURATION = getInTicks(0, 8);
    int SHOWER_TASK_DURATION = getInTicks(0, 8);
    int PLANT_TASK_DURATION = getInTicks(0, 12);
    float MID_MOOD_THRESHOLD = 0.65f;
    float DEPRESSIVE_MOOD_THRESHOLD = 0.35f;
    float ITEM_PSYCHOSIS_CHANCE = .5f; // in percent
    int ITEM_PSYCHOSIS_REROLL_TIME = 200;

    // Shop Variables
    List<ShopEntry> SHOP_ENTRIES = Util.make(new ArrayList<>(), entries -> {
        entries.add(new ShopEntry(WatheItems.KNIFE.getDefaultStack(), 100, ShopEntry.Type.WEAPON));
        entries.add(new ShopEntry(WatheItems.REVOLVER.getDefaultStack(), 300, ShopEntry.Type.WEAPON));
        entries.add(new ShopEntry(WatheItems.GRENADE.getDefaultStack(), 350, ShopEntry.Type.WEAPON));
        entries.add(new ShopEntry(WatheItems.PSYCHO_MODE.getDefaultStack(), 400, ShopEntry.Type.WEAPON) {
            @Override
            public boolean onBuy(@NotNull PlayerEntity player) {
                return PlayerShopComponent.usePsychoMode(player);
            }
        });
        entries.add(new ShopEntry(WatheItems.POISON_VIAL.getDefaultStack(), 75, ShopEntry.Type.POISON));
        entries.add(new ShopEntry(WatheItems.SCORPION.getDefaultStack(), 50, ShopEntry.Type.POISON));
        entries.add(new ShopEntry(WatheItems.FIRECRACKER.getDefaultStack(), 10, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(WatheItems.LOCKPICK.getDefaultStack(), 250, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(WatheItems.CROWBAR.getDefaultStack(), 50, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(WatheItems.BODY_BAG.getDefaultStack(), 125, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(WatheItems.BLACKOUT.getDefaultStack(), 200, ShopEntry.Type.TOOL) {
            @Override
            public boolean onBuy(@NotNull PlayerEntity player) {
                return PlayerShopComponent.useBlackout(player);
            }
        });
        entries.add(new ShopEntry(new ItemStack(WatheItems.NOTE, 4), 10, ShopEntry.Type.TOOL));
    });
    int MONEY_START = 100;
    Function<Long, Integer> PASSIVE_MONEY_TICKER = time -> {
        if (time % getInTicks(0, 10) == 0) {
            return 5;
        }
        return 0;
    };
    int MONEY_PER_KILL = 100;
    int PSYCHO_MODE_ARMOUR = 1;

    // Timers
    int PSYCHO_TIMER = getInTicks(0, 30);
    int FIRECRACKER_TIMER = getInTicks(0, 15);
    int BLACKOUT_MIN_DURATION = getInTicks(0, 15);
    int BLACKOUT_MAX_DURATION = getInTicks(0, 20);
    int TIME_ON_CIVILIAN_KILL = getInTicks(1, 0);

    static int getInTicks(int minutes, int seconds) {
        return (minutes * 60 + seconds) * 20;
    }

    static int getRandomBlackoutDuration(Random random) {
        int v = Math.round(Math.min(Math.min(random.nextFloat(), random.nextFloat()), random.nextFloat()) * (GameConstants.BLACKOUT_MAX_DURATION - GameConstants.BLACKOUT_MIN_DURATION));
        return GameConstants.BLACKOUT_MIN_DURATION + v;
    }

    interface DeathReasons {
        Identifier GENERIC = Wathe.id("generic");
        Identifier KNIFE = Wathe.id("knife_stab");
        Identifier GUN = Wathe.id("gun_shot");
        Identifier BAT = Wathe.id("bat_hit");
        Identifier GRENADE = Wathe.id("grenade");
        Identifier POISON = Wathe.id("poison");
        Identifier FELL_OUT_OF_TRAIN = Wathe.id("fell_out_of_train");
    }
}