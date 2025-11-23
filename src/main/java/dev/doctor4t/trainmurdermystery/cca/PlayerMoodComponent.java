package dev.doctor4t.trainmurdermystery.cca;

import com.mojang.datafixers.types.templates.Named;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.doctor4t.trainmurdermystery.index.tag.TMMItemTags;
import dev.doctor4t.trainmurdermystery.util.TaskCompletePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dev.doctor4t.trainmurdermystery.TMM.isSkyVisibleAdjacent;

public class PlayerMoodComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    /*public static final Item[] PSYCHOSIS_ITEM_POOL = {
            TMMItems.LETTER, TMMItems.FIRECRACKER, TMMItems.KNIFE, TMMItems.REVOLVER, TMMItems.GRENADE, TMMItems.POISON_VIAL, TMMItems.SCORPION, TMMItems.LOCKPICK, TMMItems.CROWBAR, TMMItems.BODY_BAG
    };*/

    public static final ComponentKey<PlayerMoodComponent> KEY = ComponentRegistry.getOrCreate(TMM.id("mood"), PlayerMoodComponent.class);
    private final PlayerEntity player;
    public final Map<Task, TrainTask> tasks = new HashMap<>();
    public final Map<Task, Integer> timesGotten = new HashMap<>();
    private int nextTaskTimer = 0;
    private float mood = 1f;
    private final HashMap<UUID, ItemStack> psychosisItems = new HashMap<>();
    private static List<Item> cachedPsychosisItems = null;

    public PlayerMoodComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public void reset() {
        this.tasks.clear();
        this.timesGotten.clear();
        this.nextTaskTimer = GameConstants.TIME_TO_FIRST_TASK;
        this.psychosisItems.clear();
        this.setMood(1f);
        this.sync();
    }

    private List<Item> getPsychosisItemPool() {
        if (cachedPsychosisItems == null) {
            cachedPsychosisItems = this.player.getRegistryManager()
                    .createRegistryLookup()
                    .getOrThrow(RegistryKeys.ITEM)
                    .getOptional(TMMItemTags.PSYCHOSIS_ITEMS)
                    .map(RegistryEntryList.ListBacked::stream)
                    .map(stream -> stream.map(RegistryEntry::value).toList())
                    .orElseGet(() -> {
                        TMM.LOGGER.error("Server provided empty tag {}", TMMItemTags.PSYCHOSIS_ITEMS.id());
                        return List.of();
                    });
        }
        return cachedPsychosisItems;
    }

    @Override
    public void clientTick() {
        if (!GameWorldComponent.KEY.get(this.player.getWorld()).isRunning() || !TMMClient.isPlayerAliveAndInSurvival())
            return;
        if (!this.tasks.isEmpty()) this.setMood(this.mood - this.tasks.size() * GameConstants.MOOD_DRAIN);

        if (this.isLowerThanMid()) {
            // imagine random items for players
            for (var playerEntity : this.player.getWorld().getPlayers()) {
                if (!playerEntity.equals(this.player) && this.player.getWorld().getRandom().nextInt(GameConstants.ITEM_PSYCHOSIS_REROLL_TIME) == 0) {
                    ItemStack psychosisStack;
                    List<Item> taggedItems = getPsychosisItemPool();

                    if (!taggedItems.isEmpty() && this.player.getRandom().nextFloat() < GameConstants.ITEM_PSYCHOSIS_CHANCE) {
                        Item item = Util.getRandom(taggedItems, this.player.getRandom());
                        psychosisStack = new ItemStack(item);
                    } else {
                        psychosisStack = playerEntity.getMainHandStack();
                    }

                    //this.psychosisItems.put(playerEntity.getUuid(), playerEntity.getRandom().nextFloat() < GameConstants.ITEM_PSYCHOSIS_CHANCE ? PSYCHOSIS_ITEM_POOL[playerEntity.getRandom().nextInt(PSYCHOSIS_ITEM_POOL.length)].getDefaultStack() : playerEntity.getMainHandStack());
                    this.psychosisItems.put(playerEntity.getUuid(), psychosisStack);
                }
            }
        } else {
            if (!this.psychosisItems.isEmpty()) this.psychosisItems.clear();
        }
    }

    @Override
    public void serverTick() {
        var gameWorldComponent = GameWorldComponent.KEY.get(this.player.getWorld());
        if (!gameWorldComponent.isRunning() || !GameFunctions.isPlayerAliveAndSurvival(this.player)) return;
        if (!this.tasks.isEmpty()) this.setMood(this.mood - this.tasks.size() * GameConstants.MOOD_DRAIN);
        var shouldSync = false;
        this.nextTaskTimer--;
        if (this.nextTaskTimer <= 0) {
            var task = this.generateTask();
            if (task != null) {
                this.tasks.put(task.getType(), task);
                this.timesGotten.putIfAbsent(task.getType(), 1);
                this.timesGotten.put(task.getType(), this.timesGotten.get(task.getType()) + 1);
            }
            this.nextTaskTimer = (int) (this.player.getRandom().nextFloat() * (GameConstants.MAX_TASK_COOLDOWN - GameConstants.MIN_TASK_COOLDOWN) + GameConstants.MIN_TASK_COOLDOWN);
            this.nextTaskTimer = Math.max(this.nextTaskTimer, 2);
            shouldSync = true;
        }
        var removals = new ArrayList<Task>();
        for (var task : this.tasks.values()) {
            task.tick(this.player);
            if (task.isFulfilled(this.player)) {
                removals.add(task.getType());
                this.setMood(this.mood + GameConstants.MOOD_GAIN);
                if (this.player instanceof ServerPlayerEntity tempPlayer)
                    ServerPlayNetworking.send(tempPlayer, new TaskCompletePayload());
                shouldSync = true;
            }
        }
        for (var task : removals) this.tasks.remove(task);
        if (shouldSync) this.sync();
    }

    private @Nullable TrainTask generateTask() {
        if (!this.tasks.isEmpty()) return null;
        var map = new HashMap<Task, Float>();
        var total = 0f;
        for (var task : Task.values()) {
            if (this.tasks.containsKey(task)) continue;
            var weight = 1f / this.timesGotten.getOrDefault(task, 1);
            map.put(task, weight);
            total += weight;
        }
        var random = this.player.getRandom().nextFloat() * total;
        for (var entry : map.entrySet()) {
            random -= entry.getValue();
            if (random <= 0) {
                return switch (entry.getKey()) {
                    case SLEEP -> new SleepTask(GameConstants.SLEEP_TASK_DURATION);
                    case OUTSIDE -> new OutsideTask(GameConstants.OUTSIDE_TASK_DURATION);
                    case EAT -> new EatTask();
                    case DRINK -> new DrinkTask();
                };
            }
        }
        return null;
    }

    public float getMood() {
        GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(this.player.getWorld());
        return gameWorldComponent.canUseKillerFeatures(player) || gameWorldComponent.getGameMode() != GameWorldComponent.GameMode.MURDER ? 1 : this.mood;
    }

    public void setMood(float mood) {
        GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(this.player.getWorld());
        this.mood = gameWorldComponent.canUseKillerFeatures(player) || gameWorldComponent.getGameMode() != GameWorldComponent.GameMode.MURDER ? 1 : Math.clamp(mood, 0, 1);
        this.sync();
    }

    public void eatFood() {
        if (this.tasks.get(Task.EAT) instanceof EatTask eatTask) eatTask.fulfilled = true;
    }

    public void drinkCocktail() {
        if (this.tasks.get(Task.DRINK) instanceof DrinkTask drinkTask) drinkTask.fulfilled = true;
    }

    public boolean isLowerThanMid() {
        return this.getMood() < GameConstants.MID_MOOD_THRESHOLD;
    }

    public boolean isLowerThanDepressed() {
        return this.getMood() < GameConstants.DEPRESSIVE_MOOD_THRESHOLD;
    }

    public HashMap<UUID, ItemStack> getPsychosisItems() {
        return this.psychosisItems;
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        tag.putFloat("mood", this.mood);
        var tasks = new NbtList();
        for (var task : this.tasks.values()) tasks.add(task.toNbt());
        tag.put("tasks", tasks);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        this.mood = tag.contains("mood", NbtElement.FLOAT_TYPE) ? tag.getFloat("mood") : 1f;
        this.tasks.clear();
        if (tag.contains("tasks", NbtElement.LIST_TYPE)) {
            for (var element : tag.getList("tasks", NbtElement.COMPOUND_TYPE)) {
                if (element instanceof NbtCompound compound && compound.contains("type")) {
                    var type = compound.getInt("type");
                    if (type < 0 || type >= Task.values().length) continue;
                    var typeEnum = Task.values()[type];
                    this.tasks.put(typeEnum, typeEnum.setFunction.apply(compound));
                }
            }
        }
    }

    public enum Task {
        SLEEP(nbt -> new SleepTask(nbt.getInt("timer"))),
        OUTSIDE(nbt -> new OutsideTask(nbt.getInt("timer"))),
        EAT(nbt -> new EatTask()),
        DRINK(nbt -> new DrinkTask());

        public final @NotNull Function<NbtCompound, TrainTask> setFunction;

        Task(@NotNull Function<NbtCompound, TrainTask> function) {
            this.setFunction = function;
        }
    }

    public static class SleepTask implements TrainTask {
        private int timer;

        public SleepTask(int time) {
            this.timer = time;
        }

        @Override
        public void tick(@NotNull PlayerEntity player) {
            if (player.isSleeping() && this.timer > 0) this.timer--;
        }

        @Override
        public boolean isFulfilled(@NotNull PlayerEntity player) {
            return this.timer <= 0;
        }

        @Override
        public String getName() {
            return "sleep";
        }

        @Override
        public Task getType() {
            return Task.SLEEP;
        }

        @Override
        public NbtCompound toNbt() {
            var nbt = new NbtCompound();
            nbt.putInt("type", Task.SLEEP.ordinal());
            nbt.putInt("timer", this.timer);
            return nbt;
        }
    }

    public static class OutsideTask implements TrainTask {
        private int timer;

        public OutsideTask(int time) {
            this.timer = time;
        }

        @Override
        public void tick(@NotNull PlayerEntity player) {
            if (isSkyVisibleAdjacent(player) && this.timer > 0) this.timer--;
        }

        @Override
        public boolean isFulfilled(@NotNull PlayerEntity player) {
            return this.timer <= 0;
        }

        @Override
        public String getName() {
            return "outside";
        }

        @Override
        public Task getType() {
            return Task.OUTSIDE;
        }

        @Override
        public NbtCompound toNbt() {
            var nbt = new NbtCompound();
            nbt.putInt("type", Task.OUTSIDE.ordinal());
            nbt.putInt("timer", this.timer);
            return nbt;
        }
    }

    public static class EatTask implements TrainTask {
        public boolean fulfilled = false;

        @Override
        public boolean isFulfilled(@NotNull PlayerEntity player) {
            return this.fulfilled;
        }

        @Override
        public String getName() {
            return "eat";
        }

        @Override
        public Task getType() {
            return Task.EAT;
        }

        @Override
        public NbtCompound toNbt() {
            var nbt = new NbtCompound();
            nbt.putInt("type", Task.EAT.ordinal());
            return nbt;
        }
    }

    public static class DrinkTask implements TrainTask {
        public boolean fulfilled = false;

        @Override
        public boolean isFulfilled(@NotNull PlayerEntity player) {
            return this.fulfilled;
        }

        @Override
        public String getName() {
            return "drink";
        }

        @Override
        public Task getType() {
            return Task.DRINK;
        }

        @Override
        public NbtCompound toNbt() {
            var nbt = new NbtCompound();
            nbt.putInt("type", Task.DRINK.ordinal());
            return nbt;
        }
    }

    public interface TrainTask {
        default void tick(@NotNull PlayerEntity player) {
        }

        boolean isFulfilled(PlayerEntity player);

        String getName();

        Task getType();

        NbtCompound toNbt();
    }
}
