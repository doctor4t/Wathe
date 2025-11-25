package dev.doctor4t.trainmurdermystery.game;

import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public interface GameConstants {
    // Logistics
    int FADE_TIME = 40;
    int FADE_PAUSE = 20;

    // Blocks
    int DOOR_AUTOCLOSE_TIME = getInTicks(0, 5);

    // Items
    Map<Item, Integer> ITEM_COOLDOWNS = new HashMap<>();

    static void init() {
        ITEM_COOLDOWNS.put(TMMItems.KNIFE, getInTicks(1, 0));
        ITEM_COOLDOWNS.put(TMMItems.REVOLVER, getInTicks(0, 10));
        ITEM_COOLDOWNS.put(TMMItems.DERRINGER, getInTicks(0, 1));
        ITEM_COOLDOWNS.put(TMMItems.GRENADE, getInTicks(5, 0));
        ITEM_COOLDOWNS.put(TMMItems.LOCKPICK, getInTicks(3, 0));
        ITEM_COOLDOWNS.put(TMMItems.CROWBAR, getInTicks(0, 10));
        ITEM_COOLDOWNS.put(TMMItems.BODY_BAG, getInTicks(5, 0));
        ITEM_COOLDOWNS.put(TMMItems.PSYCHO_MODE, getInTicks(5, 0));
        ITEM_COOLDOWNS.put(TMMItems.BLACKOUT, getInTicks(3, 0));
    }

    int JAMMED_DOOR_TIME = getInTicks(1, 0);

    // Corpses
    int TIME_TO_DECOMPOSITION = getInTicks(1, 0);
    int DECOMPOSING_TIME = getInTicks(4, 0);

    // Task Variables
    float MOOD_GAIN = 0.5f;
    float MOOD_DRAIN = 1f / getInTicks(4, 0);
    int TIME_TO_FIRST_TASK = getInTicks(0, 30);
    int MIN_TASK_COOLDOWN = getInTicks(0, 30);
    int MAX_TASK_COOLDOWN = getInTicks(1, 0);
    int SLEEP_TASK_DURATION = getInTicks(0, 8);
    int OUTSIDE_TASK_DURATION = getInTicks(0, 8);
    float MID_MOOD_THRESHOLD = 0.55f;
    float DEPRESSIVE_MOOD_THRESHOLD = 0.2f;
    float ITEM_PSYCHOSIS_CHANCE = .5f; // in percent
    int ITEM_PSYCHOSIS_REROLL_TIME = 200;

    // Shop Variables
    List<ShopEntry> SHOP_ENTRIES = Util.make(new ArrayList<>(), entries -> {
        entries.add(new ShopEntry(TMMItems.KNIFE.getDefaultStack(), 100, ShopEntry.Type.WEAPON));
        entries.add(new ShopEntry(TMMItems.REVOLVER.getDefaultStack(), 300, ShopEntry.Type.WEAPON));
        entries.add(new ShopEntry(TMMItems.GRENADE.getDefaultStack(), 350, ShopEntry.Type.WEAPON));
        entries.add(new ShopEntry(TMMItems.PSYCHO_MODE.getDefaultStack(), 300, ShopEntry.Type.WEAPON) {
            @Override
            public boolean onBuy(@NotNull PlayerEntity player) {
                return PlayerShopComponent.usePsychoMode(player);
            }
        });
        entries.add(new ShopEntry(TMMItems.POISON_VIAL.getDefaultStack(), 100, ShopEntry.Type.POISON));
        entries.add(new ShopEntry(TMMItems.SCORPION.getDefaultStack(), 50, ShopEntry.Type.POISON));
        entries.add(new ShopEntry(TMMItems.FIRECRACKER.getDefaultStack(), 10, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(TMMItems.LOCKPICK.getDefaultStack(), 50, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(TMMItems.CROWBAR.getDefaultStack(), 25, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(TMMItems.BODY_BAG.getDefaultStack(), 200, ShopEntry.Type.TOOL));
        entries.add(new ShopEntry(TMMItems.BLACKOUT.getDefaultStack(), 200, ShopEntry.Type.TOOL) {
            @Override
            public boolean onBuy(@NotNull PlayerEntity player) {
                return PlayerShopComponent.useBlackout(player);
            }
        });
        entries.add(new ShopEntry(new ItemStack(TMMItems.NOTE, 4), 10, ShopEntry.Type.TOOL));
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
}