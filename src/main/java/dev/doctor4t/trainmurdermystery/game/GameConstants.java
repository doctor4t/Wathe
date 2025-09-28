package dev.doctor4t.trainmurdermystery.game;

import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

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
        ITEM_COOLDOWNS.put(TMMItems.GRENADE, getInTicks(5, 0));
        ITEM_COOLDOWNS.put(TMMItems.LOCKPICK, getInTicks(3, 0));
        ITEM_COOLDOWNS.put(TMMItems.CROWBAR, getInTicks(3, 0));
        ITEM_COOLDOWNS.put(TMMItems.BODY_BAG, getInTicks(5, 0));
        ITEM_COOLDOWNS.put(TMMItems.PSYCHO_MODE, getInTicks(5, 0));
        ITEM_COOLDOWNS.put(TMMItems.BLACKOUT, getInTicks(3, 0));
    }

    int JAMMED_DOOR_TIME = getInTicks(1, 0);

    // Sprint
    int MAX_SPRINTING_TICKS = getInTicks(0, 10);

    // Kill count
    float KILL_COUNT_PERCENTAGE = .7f;

    // Corpses
    int TIME_TO_DECOMPOSITION = getInTicks(1, 0);
    int DECOMPOSING_TIME = getInTicks(4, 0);

    // Game areas
    Vec3d SPAWN_POS = new Vec3d(-872.5, 0, -323);
    Box READY_AREA = new Box(-1017, -1, -364, -813, 3, -358);
    Vec3d PLAY_OFFSET = new Vec3d(963, 121, -175);
    Consumer<ServerPlayerEntity> SPECTATOR_TP = serverPlayerEntity -> serverPlayerEntity.teleport(serverPlayerEntity.getServerWorld(), -68, 133, -535.5, -90, 15);
    Box PLAY_AREA = new Box(-140, 118, -535.5f - 15, 230, 200, -535.5f + 15);
    Box BACKUP_TRAIN_LOCATION = new Box(-57, 64, -531, 177, 74, -541);
    Box TRAIN_LOCATION = BACKUP_TRAIN_LOCATION.offset(0, 55, 0);

    // Task Variables
    float MOOD_GAIN = 0.5f;
    float MOOD_DRAIN = 1f / getInTicks(5, 0);
    int TIME_TO_FIRST_TASK = getInTicks(0, 30);
    int MIN_TASK_COOLDOWN = getInTicks(0, 30);
    int MAX_TASK_COOLDOWN = getInTicks(1, 0);
    int SLEEP_TASK_DURATION = getInTicks(0, 8);
    int OUTSIDE_TASK_DURATION = getInTicks(0, 8);
    float MID_MOOD_THRESHOLD = 0.55f;
    float DEPRESSIVE_MOOD_THRESHOLD = 0.2f;
    float ITEM_PSYCHOSIS_CHANCE = .5f; // in percent
    float ITEM_PSYCHOSIS_REROLL_TIME = getInTicks(1, 0);

    // Shop Variables
    List<ShopEntry> SHOP_ENTRIES = List.of(
            new ShopEntry(TMMItems.KNIFE.getDefaultStack(), 10, ShopEntry.Type.WEAPON),
            new ShopEntry(TMMItems.REVOLVER.getDefaultStack(), 300, ShopEntry.Type.WEAPON),
            new ShopEntry(TMMItems.GRENADE.getDefaultStack(), 400, ShopEntry.Type.WEAPON),
            new ShopEntry(TMMItems.PSYCHO_MODE.getDefaultStack(), 400, ShopEntry.Type.WEAPON) {
                @Override
                public boolean onBuy(@NotNull PlayerEntity player) {
                    return PlayerShopComponent.usePsychoMode(player);
                }
            },
            new ShopEntry(TMMItems.POISON_VIAL.getDefaultStack(), 100, ShopEntry.Type.POISON),
            new ShopEntry(TMMItems.SCORPION.getDefaultStack(), 50, ShopEntry.Type.POISON),
            new ShopEntry(TMMItems.FIRECRACKER.getDefaultStack(), 20, ShopEntry.Type.TOOL),
            new ShopEntry(TMMItems.LOCKPICK.getDefaultStack(), 75, ShopEntry.Type.TOOL),
            new ShopEntry(TMMItems.CROWBAR.getDefaultStack(), 50, ShopEntry.Type.TOOL),
            new ShopEntry(TMMItems.BODY_BAG.getDefaultStack(), 500, ShopEntry.Type.TOOL),
            new ShopEntry(TMMItems.BLACKOUT.getDefaultStack(), 200, ShopEntry.Type.TOOL) {
                @Override
                public boolean onBuy(@NotNull PlayerEntity player) {
                    return PlayerShopComponent.useBlackout(player);
                }
            },
            new ShopEntry(new ItemStack(TMMItems.NOTE, 4), 5, ShopEntry.Type.TOOL)
    );
    int MONEY_START = 100;
    Function<Long, Integer> PASSIVE_MONEY_TICKER = time -> {
        if (time % getInTicks(0, 10) == 0) {
            return 5;
        }
        return 0;
    };
    int MONEY_PER_KILL = 100;

    // Timers
    int PSYCHO_TIMER = getInTicks(0, 30);
    int FIRECRACKER_TIMER = getInTicks(0, 30);
    int BLACKOUT_MIN_DURATION = getInTicks(0, 10);
    int BLACKOUT_MAX_DURATION = getInTicks(0, 12);

    static int getInTicks(int minutes, int seconds) {
        return (minutes * 60 + seconds) * 20;
    }
}