package dev.doctor4t.trainmurdermystery.cca;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.*;

public class ScoreboardRoleSelectorComponent implements AutoSyncedComponent {
    public static final ComponentKey<ScoreboardRoleSelectorComponent> KEY = ComponentRegistry.getOrCreate(TMM.id("rolecounter"), ScoreboardRoleSelectorComponent.class);
    public final Scoreboard scoreboard;
    public final MinecraftServer server;
    public final Map<UUID, Integer> killerRounds = new HashMap<>();
    public final Map<UUID, Integer> vigilanteRounds = new HashMap<>();
    public final List<UUID> forcedKillers = new ArrayList<>();
    public final List<UUID> forcedVigilantes = new ArrayList<>();

    public ScoreboardRoleSelectorComponent(Scoreboard scoreboard, @Nullable MinecraftServer server) {
        this.scoreboard = scoreboard;
        this.server = server;
    }

    public int reset() {
        this.killerRounds.clear();
        this.vigilanteRounds.clear();
        return 1;
    }

    public void checkWeights(@NotNull ServerCommandSource source) {
        double killerTotal = 0d;
        double vigilanteTotal = 0d;
        for (ServerPlayerEntity player : source.getWorld().getPlayers()) {
            killerTotal += Math.exp(-this.killerRounds.getOrDefault(player.getUuid(), 0) * 4);
            vigilanteTotal += Math.exp(-this.vigilanteRounds.getOrDefault(player.getUuid(), 0) * 4);
        }
        MutableText text = Text.literal("Role Weights:").formatted(Formatting.GRAY);
        for (ServerPlayerEntity player : source.getWorld().getPlayers()) {
            text = text.append("\n").append(player.getDisplayName());
            Integer killerRounds = this.killerRounds.getOrDefault(player.getUuid(), 0);
            double killerWeight = Math.exp(-killerRounds * 4);
            double killerPercent = killerWeight / killerTotal * 100;
            Integer vigilanteRounds = this.vigilanteRounds.getOrDefault(player.getUuid(), 0);
            double vigilanteWeight = Math.exp(-vigilanteRounds * 4);
            double vigilantePercent = vigilanteWeight / vigilanteTotal * 100;
            text.append(
                    Text.literal("\n  Killer (").withColor(RoleAnnouncementTexts.KILLER.colour)
                            .append(Text.literal("%d".formatted(killerRounds)).withColor(0x808080))
                            .append(Text.literal("): ").withColor(RoleAnnouncementTexts.KILLER.colour))
                            .append(Text.literal("%.2f%%".formatted(killerPercent)).withColor(0x808080))
            );
            text.append(
                    Text.literal("\n  Vigilante (").withColor(RoleAnnouncementTexts.VIGILANTE.colour)
                            .append(Text.literal("%d".formatted(vigilanteRounds)).withColor(0x808080))
                            .append(Text.literal("): ").withColor(RoleAnnouncementTexts.VIGILANTE.colour))
                            .append(Text.literal("%.2f%%".formatted(vigilantePercent)).withColor(0x808080))
            );
        }
        MutableText finalText = text;
        source.sendFeedback(() -> finalText, false);
    }

    public void setKillerRounds(@NotNull ServerCommandSource source, @NotNull ServerPlayerEntity player, int times) {
        if (times < 0) times = 0;
        if (times == 0) this.killerRounds.remove(player.getUuid());
        else this.killerRounds.put(player.getUuid(), times);
        int finalTimes = times;
        source.sendFeedback(() -> Text.literal("Set ").formatted(Formatting.GRAY)
                .append(player.getDisplayName().copy().formatted(Formatting.YELLOW))
                .append(Text.literal("'s Killer rounds to ").formatted(Formatting.GRAY))
                .append(Text.literal("%d".formatted(finalTimes)).withColor(0x808080))
                .append(Text.literal(".").formatted(Formatting.GRAY)), false);
    }

    public void setVigilanteRounds(@NotNull ServerCommandSource source, @NotNull ServerPlayerEntity player, int times) {
        if (times < 0) times = 0;
        if (times == 0) this.vigilanteRounds.remove(player.getUuid());
        else this.vigilanteRounds.put(player.getUuid(), times);
        int finalTimes = times;
        source.sendFeedback(() -> Text.literal("Set ").formatted(Formatting.GRAY)
                .append(player.getDisplayName().copy().formatted(Formatting.YELLOW))
                .append(Text.literal("'s Vigilante rounds to ").formatted(Formatting.GRAY))
                .append(Text.literal("%d".formatted(finalTimes)).withColor(0x808080))
                .append(Text.literal(".").formatted(Formatting.GRAY)), false);
    }

    public int assignKillers(ServerWorld world, GameWorldComponent gameComponent, @NotNull List<ServerPlayerEntity> players, int killerCount) {
        this.reduceKillers();
        ArrayList<UUID> killers = new ArrayList<>();
        for (UUID uuid : this.forcedKillers) {
            killers.add(uuid);
            killerCount--;
            this.killerRounds.put(uuid, this.killerRounds.getOrDefault(uuid, 1) + 1);
        }
        this.forcedKillers.clear();
        HashMap<ServerPlayerEntity, Float> map = new HashMap<>();
        float total = 0f;
        for (ServerPlayerEntity player : players) {
            float weight = (float) Math.exp(-this.killerRounds.getOrDefault(player.getUuid(), 0) * 4);
            if (!GameWorldComponent.KEY.get(world).areWeightsEnabled()) weight = 1;
            map.put(player, weight);
            total += weight;
        }
        for (int i = 0; i < killerCount; i++) {
            float random = world.getRandom().nextFloat() * total;
            for (Map.Entry<ServerPlayerEntity, Float> entry : map.entrySet()) {
                random -= entry.getValue();
                if (random <= 0) {
                    killers.add(entry.getKey().getUuid());
                    total -= entry.getValue();
                    map.remove(entry.getKey());
                    this.killerRounds.put(entry.getKey().getUuid(), this.killerRounds.getOrDefault(entry.getKey().getUuid(), 1) + 1);
                    break;
                }
            }
        }
        for (UUID killerUUID : killers) {
            gameComponent.addRole(killerUUID, TMMRoles.KILLER);
            PlayerEntity killer = world.getPlayerByUuid(killerUUID);
            if (killer != null) {
                PlayerShopComponent.KEY.get(killer).setBalance(GameConstants.MONEY_START);
            }
        }
        return killers.size();
    }

    private void reduceKillers() {
        int minimum = Integer.MAX_VALUE;
        for (Integer times : this.killerRounds.values()) minimum = Math.min(minimum, times);
        for (UUID times : this.killerRounds.keySet())
            this.killerRounds.put(times, this.killerRounds.get(times) - minimum);
    }

    public void assignVigilantes(ServerWorld world, GameWorldComponent gameComponent, @NotNull List<ServerPlayerEntity> players, int vigilanteCount) {
        this.reduceVigilantes();
        ArrayList<ServerPlayerEntity> vigilantes = new ArrayList<>();
        for (UUID uuid : this.forcedVigilantes) {
            PlayerEntity player = world.getPlayerByUuid(uuid);
            if (player instanceof ServerPlayerEntity serverPlayer && players.contains(serverPlayer) && !gameComponent.canUseKillerFeatures(player)) {
                player.giveItemStack(new ItemStack(TMMItems.REVOLVER));
                gameComponent.addRole(player, TMMRoles.VIGILANTE);
                vigilanteCount--;
                this.vigilanteRounds.put(player.getUuid(), this.vigilanteRounds.getOrDefault(player.getUuid(), 1) + 1);
            }
        }
        this.forcedVigilantes.clear();
        HashMap<ServerPlayerEntity, Float> map = new HashMap<>();
        float total = 0f;
        for (ServerPlayerEntity player : players) {
            if (gameComponent.isRole(player, TMMRoles.KILLER)) continue;
            float weight = (float) Math.exp(-this.vigilanteRounds.getOrDefault(player.getUuid(), 0) * 4);
            if (!GameWorldComponent.KEY.get(world).areWeightsEnabled()) weight = 1;
            map.put(player, weight);
            total += weight;
        }
        for (int i = 0; i < vigilanteCount; i++) {
            float random = world.getRandom().nextFloat() * total;
            for (Map.Entry<ServerPlayerEntity, Float> entry : map.entrySet()) {
                random -= entry.getValue();
                if (random <= 0) {
                    vigilantes.add(entry.getKey());
                    total -= entry.getValue();
                    map.remove(entry.getKey());
                    this.vigilanteRounds.put(entry.getKey().getUuid(), this.vigilanteRounds.getOrDefault(entry.getKey().getUuid(), 1) + 1);
                    break;
                }
            }
        }
        for (ServerPlayerEntity player : vigilantes) {
            player.giveItemStack(new ItemStack(TMMItems.REVOLVER));
            gameComponent.addRole(player, TMMRoles.VIGILANTE);
        }
    }

    private void reduceVigilantes() {
        int minimum = Integer.MAX_VALUE;
        for (Integer times : this.vigilanteRounds.values()) minimum = Math.min(minimum, times);
        for (UUID times : this.vigilanteRounds.keySet())
            this.vigilanteRounds.put(times, this.vigilanteRounds.get(times) - minimum);
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList killerRounds = new NbtList();
        for (Map.Entry<UUID, Integer> detail : this.killerRounds.entrySet()) {
            NbtCompound compound = new NbtCompound();
            compound.putUuid("uuid", detail.getKey());
            compound.putInt("times", detail.getValue());
            killerRounds.add(compound);
        }
        tag.put("killerRounds", killerRounds);
        NbtList vigilanteRounds = new NbtList();
        for (Map.Entry<UUID, Integer> detail : this.vigilanteRounds.entrySet()) {
            NbtCompound compound = new NbtCompound();
            compound.putUuid("uuid", detail.getKey());
            compound.putInt("times", detail.getValue());
            vigilanteRounds.add(compound);
        }
        tag.put("vigilanteRounds", vigilanteRounds);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.killerRounds.clear();
        for (NbtElement element : tag.getList("killerRounds", 10)) {
            NbtCompound compound = (NbtCompound) element;
            if (!compound.contains("uuid") || !compound.contains("times")) continue;
            this.killerRounds.put(compound.getUuid("uuid"), compound.getInt("times"));
        }
        this.vigilanteRounds.clear();
        for (NbtElement element : tag.getList("vigilanteRounds", 10)) {
            NbtCompound compound = (NbtCompound) element;
            if (!compound.contains("uuid") || !compound.contains("times")) continue;
            this.vigilanteRounds.put(compound.getUuid("uuid"), compound.getInt("times"));
        }
    }
}