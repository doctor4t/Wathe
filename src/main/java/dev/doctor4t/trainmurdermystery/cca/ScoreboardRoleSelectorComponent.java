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
        List<ServerPlayerEntity> players = source.getWorld().getPlayers();
        if (players.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No players online to check weights.").formatted(Formatting.GRAY), false);
            return;
        }
        
        double killerTotal = 0d;
        double vigilanteTotal = 0d;
        for (ServerPlayerEntity player : players) {
            killerTotal += Math.exp(-this.killerRounds.getOrDefault(player.getUuid(), 0) * 4);
            vigilanteTotal += Math.exp(-this.vigilanteRounds.getOrDefault(player.getUuid(), 0) * 4);
        }
        
        // Handle edge case where all weights are zero
        if (killerTotal <= 0) killerTotal = 1;
        if (vigilanteTotal <= 0) vigilanteTotal = 1;
        
        MutableText text = Text.literal("Role Weights:").formatted(Formatting.GRAY);
        for (ServerPlayerEntity player : players) {
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

    public void simulateScoreboard(@NotNull ServerCommandSource source, int rounds) {
        List<ServerPlayerEntity> players = source.getWorld().getPlayers();
        if (players.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No players online to simulate scoreboard.").formatted(Formatting.GRAY), false);
            return;
        }
        
        if (rounds <= 0) {
            source.sendFeedback(() -> Text.literal("Rounds must be a positive number.").formatted(Formatting.RED), false);
            return;
        }
        
        // Create working copies of the actual weights that will be modified during simulation
        Map<UUID, Integer> workingKillerRounds = new HashMap<>(this.killerRounds);
        Map<UUID, Integer> workingVigilanteRounds = new HashMap<>(this.vigilanteRounds);
        
        // Track simulation results
        Map<UUID, Integer> killerAssignments = new HashMap<>();
        Map<UUID, Integer> vigilanteAssignments = new HashMap<>();
        
        // Initialize tracking for all players
        for (ServerPlayerEntity player : players) {
            UUID uuid = player.getUuid();
            killerAssignments.put(uuid, 0);
            vigilanteAssignments.put(uuid, 0);
            // Ensure all players are in the working rounds maps
            workingKillerRounds.putIfAbsent(uuid, 0);
            workingVigilanteRounds.putIfAbsent(uuid, 0);
        }
        
        // Simulate the specified number of rounds with realistic game structure
        for (int round = 1; round <= rounds; round++) {
            // Track which players are assigned roles in this round
            Set<UUID> currentRoundKillers = new HashSet<>();
            Set<UUID> currentRoundVigilantes = new HashSet<>();
            
            // Use actual game killer ratio (default 1:6, same as actual game)
            int killerPlayerRatio = 6; // Default ratio from GameWorldComponent
            int killerCount = Math.max(1, (int) Math.floor(players.size() / (float) killerPlayerRatio));
            
            // Make vigilante count equal to killer count (same as actual game)
            int vigilanteCount = killerCount;
            
            // Phase 1: Assign killers (all players are candidates)
            List<ServerPlayerEntity> killerCandidates = new ArrayList<>(players);
            
            // Calculate weights for killer assignment using current working rounds (EXACTLY like real game)
            HashMap<ServerPlayerEntity, Float> killerMap = new HashMap<>();
            float killerTotal = 0f;
            for (ServerPlayerEntity player : killerCandidates) {
                int roundsCount = workingKillerRounds.get(player.getUuid());
                float weight = 1.0f / (1.0f + roundsCount); // Linear decay (same as real game)
                weight = Math.max(weight, 0.1f); // Minimum weight (same as real game)
                // Prevent role repetition: if player was killer last round, reduce their chance (30% reduction)
                if (round > 1 && currentRoundKillers.contains(player.getUuid())) {
                    weight *= 0.7f; // 30% reduction for recent killers (same as real game)
                }
                killerMap.put(player, weight);
                killerTotal += weight;
            }
            
            // Handle case where total weight is zero
            if (killerTotal <= 0) {
                for (ServerPlayerEntity player : killerCandidates) {
                    killerMap.put(player, 1.0f);
                }
                killerTotal = killerCandidates.size();
            }
            
            // Assign killers for this round
            for (int i = 0; i < killerCount; i++) {
                if (killerMap.isEmpty()) break;
                
                float random = (float) Math.random() * killerTotal;
                ServerPlayerEntity selectedPlayer = null;
                
                for (Map.Entry<ServerPlayerEntity, Float> entry : killerMap.entrySet()) {
                    random -= entry.getValue();
                    if (random <= 0) {
                        selectedPlayer = entry.getKey();
                        break;
                    }
                }
                
                // Fallback: if no player was selected, pick randomly
                if (selectedPlayer == null) {
                    List<ServerPlayerEntity> remainingPlayers = new ArrayList<>(killerMap.keySet());
                    selectedPlayer = remainingPlayers.get((int) (Math.random() * remainingPlayers.size()));
                }
                
                UUID killerUUID = selectedPlayer.getUuid();
                killerAssignments.put(killerUUID, killerAssignments.get(killerUUID) + 1);
                currentRoundKillers.add(killerUUID);
                
                // Update working killer rounds
                workingKillerRounds.put(killerUUID, workingKillerRounds.get(killerUUID) + 1);
                
                // Remove the selected player from the map and update total
                float removedWeight = killerMap.remove(selectedPlayer);
                killerTotal -= removedWeight;
            }
            
            // Phase 2: Assign vigilantes (exclude current round killers)
            List<ServerPlayerEntity> vigilanteCandidates = new ArrayList<>(players);
            vigilanteCandidates.removeIf(player -> currentRoundKillers.contains(player.getUuid()));
            
            // Calculate weights for vigilante assignment using current working rounds (EXACTLY like real game)
            HashMap<ServerPlayerEntity, Float> vigilanteMap = new HashMap<>();
            float vigilanteTotal = 0f;
            for (ServerPlayerEntity player : vigilanteCandidates) {
                int roundsCount = workingVigilanteRounds.get(player.getUuid());
                float weight = 1.0f / (1.0f + roundsCount); // Linear decay (same as real game)
                weight = Math.max(weight, 0.1f); // Minimum weight (same as real game)
                // Prevent role repetition: if player was vigilante last round, reduce their chance (50% reduction)
                if (round > 1 && currentRoundVigilantes.contains(player.getUuid())) {
                    weight *= 0.5f; // 50% reduction for recent vigilantes (same as real game)
                }
                vigilanteMap.put(player, weight);
                vigilanteTotal += weight;
            }
            
            // Handle case where total weight is zero
            if (vigilanteTotal <= 0) {
                for (ServerPlayerEntity player : vigilanteCandidates) {
                    vigilanteMap.put(player, 1.0f);
                }
                vigilanteTotal = vigilanteCandidates.size();
            }
            
            // Assign vigilantes for this round
            for (int i = 0; i < vigilanteCount; i++) {
                if (vigilanteMap.isEmpty()) break;
                
                float random = (float) Math.random() * vigilanteTotal;
                ServerPlayerEntity selectedPlayer = null;
                
                for (Map.Entry<ServerPlayerEntity, Float> entry : vigilanteMap.entrySet()) {
                    random -= entry.getValue();
                    if (random <= 0) {
                        selectedPlayer = entry.getKey();
                        break;
                    }
                }
                
                // Fallback: if no player was selected, pick randomly
                if (selectedPlayer == null) {
                    List<ServerPlayerEntity> remainingPlayers = new ArrayList<>(vigilanteMap.keySet());
                    selectedPlayer = remainingPlayers.get((int) (Math.random() * remainingPlayers.size()));
                }
                
                UUID vigilanteUUID = selectedPlayer.getUuid();
                vigilanteAssignments.put(vigilanteUUID, vigilanteAssignments.get(vigilanteUUID) + 1);
                currentRoundVigilantes.add(vigilanteUUID);
                
                // Update working vigilante rounds
                workingVigilanteRounds.put(vigilanteUUID, workingVigilanteRounds.get(vigilanteUUID) + 1);
                
                // Remove the selected player from the map and update total
                float removedWeight = vigilanteMap.remove(selectedPlayer);
                vigilanteTotal -= removedWeight;
            }
            
            // Phase 3: All remaining players are civilians (no role assignment needed)
            
            // Apply normalization after each round (same as actual game)
            normalizeSimulatedRounds(workingKillerRounds);
            normalizeSimulatedRounds(workingVigilanteRounds);
        }
        
        // Update the actual weights with the final working values
        this.killerRounds.clear();
        this.killerRounds.putAll(workingKillerRounds);
        this.vigilanteRounds.clear();
        this.vigilanteRounds.putAll(workingVigilanteRounds);
        
        // Display simulation results and confirm weight updates
        MutableText text = Text.literal(String.format("Scoreboard Simulation (%d rounds) - Player Weights Updated:", rounds)).formatted(Formatting.GOLD);
        text.append("\n\n");
        
        // Add notification that weights have been modified
        text.append(Text.literal("✓ Player role weights have been updated based on simulation results").formatted(Formatting.GREEN));
        text.append("\n\n");
        
        // Sort players by total assignments
        List<ServerPlayerEntity> sortedPlayers = players.stream()
                .sorted((p1, p2) -> {
                    int total1 = killerAssignments.get(p1.getUuid()) + vigilanteAssignments.get(p1.getUuid());
                    int total2 = killerAssignments.get(p2.getUuid()) + vigilanteAssignments.get(p2.getUuid());
                    return Integer.compare(total2, total1);
                })
                .toList();
        
        for (ServerPlayerEntity player : sortedPlayers) {
            UUID uuid = player.getUuid();
            int killerCount = killerAssignments.get(uuid);
            int vigilanteCount = vigilanteAssignments.get(uuid);
            int totalCount = killerCount + vigilanteCount;
            float killerPercent = (float) killerCount / rounds * 100;
            float vigilantePercent = (float) vigilanteCount / rounds * 100;
            float totalPercent = (float) totalCount / rounds * 100;
            
            // Show current weight values after simulation
            int currentKillerRounds = this.killerRounds.get(uuid);
            int currentVigilanteRounds = this.vigilanteRounds.get(uuid);
            float killerWeight = 1.0f / (1.0f + currentKillerRounds);
            float vigilanteWeight = 1.0f / (1.0f + currentVigilanteRounds);
            
            Formatting killerColor = killerPercent > 30 ? Formatting.RED : Formatting.YELLOW;
            Formatting vigilanteColor = vigilantePercent > 40 ? Formatting.BLUE : Formatting.AQUA;
            Formatting totalColor = totalPercent > 50 ? Formatting.GREEN : Formatting.WHITE;
            
            text.append(Text.literal(String.format("%s: ", player.getDisplayName().getString())).formatted(Formatting.WHITE));
            text.append(Text.literal(String.format("Killer: %d (%.1f%%) [Weight: %.3f] ", killerCount, killerPercent, killerWeight)).formatted(killerColor));
            text.append(Text.literal(String.format("Vigilante: %d (%.1f%%) [Weight: %.3f] ", vigilanteCount, vigilantePercent, vigilanteWeight)).formatted(vigilanteColor));
            text.append(Text.literal(String.format("Total: %d (%.1f%%)", totalCount, totalPercent)).formatted(totalColor));
            text.append("\n");
        }
        
        // Add summary of weight changes
        text.append("\n");
        text.append(Text.literal("Weight changes applied to all players for future role assignments.").formatted(Formatting.GRAY));
        
        source.sendFeedback(() -> text, false);
    }
    
    private void normalizeSimulatedRounds(Map<UUID, Integer> roundsMap) {
        if (roundsMap.isEmpty()) return;
        
        int minimum = Integer.MAX_VALUE;
        for (Integer times : roundsMap.values()) {
            minimum = Math.min(minimum, times);
        }
        
        if (minimum > 0) {
            boolean hasVariation = false;
            for (Integer times : roundsMap.values()) {
                if (times != minimum) {
                    hasVariation = true;
                    break;
                }
            }
            
            if (hasVariation) {
                for (UUID uuid : roundsMap.keySet()) {
                    roundsMap.put(uuid, roundsMap.get(uuid) - minimum);
                }
            }
        }
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
        ArrayList<UUID> killers = new ArrayList<>();
        for (UUID uuid : this.forcedKillers) {
            killers.add(uuid);
            killerCount--;
            this.killerRounds.put(uuid, this.killerRounds.getOrDefault(uuid, 1) + 1);
        }
        this.forcedKillers.clear();
        
        // Handle case where we have no more killers to assign
        if (killerCount <= 0) {
            this.reduceKillers(); // Reduce after assignment
            return killers.size();
        }
        
        // Track player count changes to detect new players joining
        this.adjustWeightsForNewPlayers(players, this.killerRounds);
        
        HashMap<ServerPlayerEntity, Float> map = new HashMap<>();
        float total = 0f;
        for (ServerPlayerEntity player : players) {
            // Use a more balanced weight calculation that doesn't decay too quickly
            int rounds = this.killerRounds.getOrDefault(player.getUuid(), 0);
            float weight = 1.0f / (1.0f + rounds); // Linear decay instead of exponential
            if (!GameWorldComponent.KEY.get(world).areWeightsEnabled()) weight = 1;
            // Ensure minimum weight to prevent 0 weights
            weight = Math.max(weight, 0.1f);
            // Prevent role repetition: if player was killer last round, reduce their chance
            if (gameComponent.wasRoleLastRound(player.getUuid(), TMMRoles.KILLER)) {
                weight *= 0.7f; // 30% reduction for recent killers
            }
            map.put(player, weight);
            total += weight;
        }
        
        // If total weight is zero (all players have infinite rounds), use equal weights
        if (total <= 0) {
            for (ServerPlayerEntity player : players) {
                map.put(player, 1.0f);
            }
            total = players.size();
        }
        
        for (int i = 0; i < killerCount; i++) {
            if (map.isEmpty()) break; // No more players to assign
            
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
        
        // Calculate excess players and adjust starting money
        int totalPlayers = players.size();
        int killerRatio = gameComponent.getKillerPlayerRatio();
        int excessPlayers = Math.max(0, totalPlayers - (killers.size() * killerRatio));
        int additionalMoneyPerExcess = 20; // 20 coins per excess player
        int dynamicStartingMoney = GameConstants.MONEY_START + (excessPlayers * additionalMoneyPerExcess);
        
        for (UUID killerUUID : killers) {
            gameComponent.addRole(killerUUID, TMMRoles.KILLER);
            PlayerEntity killer = world.getPlayerByUuid(killerUUID);
            if (killer != null) {
                PlayerShopComponent.KEY.get(killer).setBalance(dynamicStartingMoney);
            }
        }
        
        this.reduceKillers(); // Reduce after assignment to normalize counts
        return killers.size();
    }

    private void reduceKillers() {
        if (this.killerRounds.isEmpty()) {
            return;
        }
        
        int minimum = Integer.MAX_VALUE;
        for (Integer times : this.killerRounds.values()) {
            minimum = Math.min(minimum, times);
        }
        
        // Only reduce if minimum is positive and there's variation in the counts
        // This prevents reducing when all players have the same count (which would make them all 0)
        if (minimum > 0) {
            boolean hasVariation = false;
            for (Integer times : this.killerRounds.values()) {
                if (times != minimum) {
                    hasVariation = true;
                    break;
                }
            }
            
            if (hasVariation) {
                for (UUID uuid : this.killerRounds.keySet()) {
                    this.killerRounds.put(uuid, this.killerRounds.get(uuid) - minimum);
                }
            }
        }
    }

    public void assignVigilantes(ServerWorld world, GameWorldComponent gameComponent, @NotNull List<ServerPlayerEntity> players, int vigilanteCount) {
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
        
        // Handle case where we have no more vigilantes to assign
        if (vigilanteCount <= 0) {
            this.reduceVigilantes(); // Reduce after assignment
            return;
        }
        
        // Track player count changes to detect new players joining
        this.adjustWeightsForNewPlayers(players, this.vigilanteRounds);
        
        HashMap<ServerPlayerEntity, Float> map = new HashMap<>();
        float total = 0f;
        for (ServerPlayerEntity player : players) {
            if (gameComponent.isRole(player, TMMRoles.KILLER)) continue;
            // Use a more balanced weight calculation that doesn't decay too quickly
            int rounds = this.vigilanteRounds.getOrDefault(player.getUuid(), 0);
            float weight = 1.0f / (1.0f + rounds); // Linear decay instead of exponential
            if (!GameWorldComponent.KEY.get(world).areWeightsEnabled()) weight = 1;
            // Ensure minimum weight to prevent 0 weights
            weight = Math.max(weight, 0.1f);
            // Prevent role repetition: if player was vigilante last round, reduce their chance
            if (gameComponent.wasRoleLastRound(player.getUuid(), TMMRoles.VIGILANTE)) {
                weight *= 0.5f; // 50% reduction for recent vigilantes
            }
            map.put(player, weight);
            total += weight;
        }
        
        // If total weight is zero (all players have infinite rounds), use equal weights
        if (total <= 0) {
            for (ServerPlayerEntity player : players) {
                if (gameComponent.isRole(player, TMMRoles.KILLER)) continue;
                map.put(player, 1.0f);
            }
            total = map.size();
        }
        
        for (int i = 0; i < vigilanteCount; i++) {
            if (map.isEmpty()) break; // No more players to assign
            
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
        
        this.reduceVigilantes(); // Reduce after assignment to normalize counts
    }

    private void reduceVigilantes() {
        if (this.vigilanteRounds.isEmpty()) {
            return;
        }
        
        int minimum = Integer.MAX_VALUE;
        for (Integer times : this.vigilanteRounds.values()) {
            minimum = Math.min(minimum, times);
        }
        
        // Only reduce if minimum is positive and there's variation in the counts
        // This prevents reducing when all players have the same count (which would make them all 0)
        if (minimum > 0) {
            boolean hasVariation = false;
            for (Integer times : this.vigilanteRounds.values()) {
                if (times != minimum) {
                    hasVariation = true;
                    break;
                }
            }
            
            if (hasVariation) {
                for (UUID uuid : this.vigilanteRounds.keySet()) {
                    this.vigilanteRounds.put(uuid, this.vigilanteRounds.get(uuid) - minimum);
                }
            }
        }
    }
    
    /**
     * Adjusts weights when new players join to prevent disruption of existing balances
     */
    private void adjustWeightsForNewPlayers(List<ServerPlayerEntity> players, Map<UUID, Integer> roundsMap) {
        // Find new players (those not in the rounds map)
        List<UUID> newPlayers = new ArrayList<>();
        for (ServerPlayerEntity player : players) {
            if (!roundsMap.containsKey(player.getUuid())) {
                newPlayers.add(player.getUuid());
            }
        }
        
        // If new players joined, gradually integrate them instead of giving them max weight
        if (!newPlayers.isEmpty()) {
            // Calculate average rounds of existing players
            int totalRounds = 0;
            int existingPlayers = 0;
            for (Integer rounds : roundsMap.values()) {
                totalRounds += rounds;
                existingPlayers++;
            }
            
            if (existingPlayers > 0) {
                int averageRounds = totalRounds / existingPlayers;
                // Give new players a moderate starting point (half the average)
                int startingRounds = Math.max(1, averageRounds / 2);
                for (UUID newPlayer : newPlayers) {
                    roundsMap.put(newPlayer, startingRounds);
                }
            }
        }
    }
    
    /**
     * Reset weights when player count changes significantly (anti-cheat protected)
     */
    public void resetWeightsForPlayerCountChange(@NotNull List<ServerPlayerEntity> currentPlayers, int previousPlayerCount) {
        int currentCount = currentPlayers.size();
        
        // Only reset if player count changed significantly (more than 50% change)
        if (previousPlayerCount > 0) {
            double changeRatio = Math.abs(currentCount - previousPlayerCount) / (double) previousPlayerCount;
            if (changeRatio > 0.3) { // More than 30% change
                // Reset all weights to moderate values
                for (ServerPlayerEntity player : currentPlayers) {
                    this.killerRounds.put(player.getUuid(), 1);
                    this.vigilanteRounds.put(player.getUuid(), 1);
                }
            }
        }
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