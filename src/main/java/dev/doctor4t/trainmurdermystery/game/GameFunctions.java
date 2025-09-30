package dev.doctor4t.trainmurdermystery.game;

import com.google.common.collect.Lists;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.*;
import dev.doctor4t.trainmurdermystery.entity.PlayerBodyEntity;
import dev.doctor4t.trainmurdermystery.index.TMMEntities;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.TeleportTarget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.UnaryOperator;

public class GameFunctions {
    public static void limitPlayerToBox(ServerPlayerEntity player, Box box) {
        Vec3d playerPos = player.getPos();

        if (!box.contains(playerPos)) {
            double x = playerPos.getX();
            double y = playerPos.getY();
            double z = playerPos.getZ();

            if (z < box.minZ) {
                z = box.minZ;
            }
            if (z > box.maxZ) {
                z = box.maxZ;
            }

            if (y < box.minY) {
                y = box.minY;
            }
            if (y > box.maxY) {
                y = box.maxY;
            }

            if (x < box.minX) {
                x = box.minX;
            }
            if (x > box.maxX) {
                x = box.maxX;
            }

            player.requestTeleport(x, y, z);
        }
    }

    public static void startGame(ServerWorld world) {
        GameWorldComponent component = TMMComponents.GAME.get(world);
        component.setGameStatus(GameWorldComponent.GameStatus.STARTING);
    }

    public static void stopGame(ServerWorld world) {
        GameWorldComponent component = TMMComponents.GAME.get(world);
        component.setGameStatus(GameWorldComponent.GameStatus.STOPPING);
    }

    public static void testStart(ServerWorld world) {
        initializeGame(world);

        GameWorldComponent gameWorldComponent = TMMComponents.GAME.get(world);
        List<UUID> killers = gameWorldComponent.getKillers();
        killers.add(UUID.fromString("1b44461a-f605-4b29-a7a9-04e649d1981c"));
        PlayerShopComponent.KEY.get(world.getPlayerByUuid(UUID.fromString("1b44461a-f605-4b29-a7a9-04e649d1981c"))).addToBalance(9999);
        gameWorldComponent.setKillers(killers);
        gameWorldComponent.sync();
    }

    public static void initializeGame(ServerWorld world) {
        TrainWorldComponent trainComponent = TMMComponents.TRAIN.get(world);
        trainComponent.setTrainSpeed(130);
        WorldBlackoutComponent.KEY.get(world).reset();
        GameWorldComponent gameComponent = TMMComponents.GAME.get(world);

        world.getGameRules().get(GameRules.KEEP_INVENTORY).set(true, world.getServer());
        world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, world.getServer());
        world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, world.getServer());
        world.getGameRules().get(GameRules.DO_MOB_GRIEFING).set(false, world.getServer());
        world.getGameRules().get(GameRules.DO_MOB_SPAWNING).set(false, world.getServer());
        world.getGameRules().get(GameRules.ANNOUNCE_ADVANCEMENTS).set(false, world.getServer());
        world.getGameRules().get(GameRules.DO_TRADER_SPAWNING).set(false, world.getServer());
        world.getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE).set(9999, world.getServer());
        world.getServer().setDifficulty(Difficulty.PEACEFUL, true);
        world.setTimeOfDay(18000);

        // dismount all players as it can cause issues
        for (ServerPlayerEntity player : world.getPlayers()) {
            player.dismountVehicle();
        }

        // teleport players to play area
        List<ServerPlayerEntity> playerPool = world.getPlayers(serverPlayerEntity -> !serverPlayerEntity.isInCreativeMode() && !serverPlayerEntity.isSpectator() && (GameConstants.READY_AREA.contains(serverPlayerEntity.getPos())));
        for (ServerPlayerEntity player : playerPool) {
            Vec3d pos = player.getPos().add(GameConstants.PLAY_OFFSET);
            player.requestTeleport(pos.getX(), pos.getY(), pos.getZ());
        }

        // teleport non playing players
        for (ServerPlayerEntity player : world.getPlayers(serverPlayerEntity -> !playerPool.contains(serverPlayerEntity))) {
            player.changeGameMode(GameMode.SPECTATOR);
            GameConstants.SPECTATOR_TP.accept(player);
        }

        // clear items, clear previous game data
        for (var serverPlayerEntity : playerPool) {
            serverPlayerEntity.getInventory().clear();
            PlayerMoodComponent.KEY.get(serverPlayerEntity).reset();
            PlayerShopComponent.KEY.get(serverPlayerEntity).reset();
            PlayerPoisonComponent.KEY.get(serverPlayerEntity).reset();
            PlayerPsychoComponent.KEY.get(serverPlayerEntity).reset();
            PlayerNoteComponent.KEY.get(serverPlayerEntity).reset();
            PlayerShopComponent.KEY.get(serverPlayerEntity).reset();

            // remove item cooldowns
            var copy = new HashSet<>(serverPlayerEntity.getItemCooldownManager().entries.keySet());
            for (var item : copy) serverPlayerEntity.getItemCooldownManager().remove(item);
        }
        gameComponent.resetKillerList();
        GameTimeComponent.KEY.get(world).reset();

        var roleSelector = ScoreboardRoleSelectorComponent.KEY.get(world.getScoreboard());
        var killerCount = (int) Math.floor(playerPool.size() * .2f);
        roleSelector.assignKillers(world, gameComponent, playerPool, killerCount);
        roleSelector.assignVigilantes(world, gameComponent, playerPool, killerCount);

        // set the kill left count as the percentage of players that are not killers that need to be killed in order to achieve a win
        gameComponent.setKillsLeft((int) ((playerPool.size() - killerCount) * GameConstants.KILL_COUNT_PERCENTAGE));

        // select rooms
        Collections.shuffle(playerPool);
        int roomNumber = 0;
        for (ServerPlayerEntity serverPlayerEntity : playerPool) {
            ItemStack itemStack = new ItemStack(TMMItems.KEY);
            roomNumber = roomNumber % 7 + 1;
            int finalRoomNumber = roomNumber;
            itemStack.apply(DataComponentTypes.LORE, LoreComponent.DEFAULT, component -> new LoreComponent(Text.literal("Room " + finalRoomNumber).getWithStyle(Style.EMPTY.withItalic(false).withColor(0xFF8C00))));
            serverPlayerEntity.giveItemStack(itemStack);

            // give letter
            ItemStack letter = new ItemStack(TMMItems.LETTER);

            letter.set(DataComponentTypes.ITEM_NAME, Text.translatable(letter.getTranslationKey()));
            int letterColor = 0xC5AE8B;
            String tipString = "tip.letter.";
            letter.apply(DataComponentTypes.LORE, LoreComponent.DEFAULT, component -> {
                        List<Text> text = new ArrayList<>();
                        UnaryOperator<Style> stylizer = style -> style.withItalic(false).withColor(letterColor);
                        text.add(Text.translatable(tipString + "name", serverPlayerEntity.getName().getString()).styled(style -> style.withItalic(false).withColor(0xFFFFFF)));
                        text.add(Text.translatable(tipString + "room").styled(stylizer));
                        text.add(Text.translatable(tipString + "tooltip1",
                                Text.translatable(tipString + "room." + switch (finalRoomNumber) {
                                    case 1 -> "grand_suite";
                                    case 2, 3 -> "cabin_suite";
                                    default -> "twin_cabin";
                                }).getString()
                        ).styled(stylizer));
                        text.add(Text.translatable(tipString + "tooltip2").styled(stylizer));

                        return new LoreComponent(text);
                    }
            );
            serverPlayerEntity.giveItemStack(letter);
        }

        // reset train
        tryResetTrain(world);

        gameComponent.setGameStatus(GameWorldComponent.GameStatus.ACTIVE);
        trainComponent.setTime(0);
        gameComponent.sync();
    }

    public static void finalizeGame(ServerWorld world) {
        WorldBlackoutComponent.KEY.get(world).reset();
        TrainWorldComponent trainComponent = TMMComponents.TRAIN.get(world);
        trainComponent.setTrainSpeed(0);
        world.setTimeOfDay(6000);

        // reset train
        tryResetTrain(world);

        // discard all player bodies
        for (var body : world.getEntitiesByType(TMMEntities.PLAYER_BODY, playerBodyEntity -> true)) body.discard();
        for (var entity : world.getEntitiesByType(TMMEntities.FIRECRACKER, entity -> true)) entity.discard();
        for (var entity : world.getEntitiesByType(TMMEntities.NOTE, entity -> true)) entity.discard();

        // reset all players to adventure mode, clear inventory and teleport to spawn
        var teleportTarget = new TeleportTarget(world, GameConstants.SPAWN_POS, Vec3d.ZERO, 90, 0, TeleportTarget.NO_OP);
        for (var player : world.getPlayers()) {
            player.changeGameMode(GameMode.ADVENTURE);
            // dismount all players as it can cause issues
            player.dismountVehicle();
            player.getInventory().clear();
            player.teleportTo(teleportTarget);
            PlayerMoodComponent.KEY.get(player).reset();
            PlayerShopComponent.KEY.get(player).reset();
            PlayerPoisonComponent.KEY.get(player).reset();
            PlayerPsychoComponent.KEY.get(player).reset();
            PlayerNoteComponent.KEY.get(player).reset();
        }
        GameTimeComponent.KEY.get(world).reset();

        // reset game component
        var gameComponent = TMMComponents.GAME.get(world);
        gameComponent.resetKillerList();
        gameComponent.setGameStatus(GameWorldComponent.GameStatus.INACTIVE);
        trainComponent.setTime(0);
        gameComponent.sync();
    }

    public static boolean isPlayerEliminated(PlayerEntity player) {
        return player == null || !player.isAlive() || player.isCreative() || player.isSpectator();
    }

    public static void killPlayer(PlayerEntity victim, boolean spawnBody, @Nullable PlayerEntity killer) {
        var component = PlayerPsychoComponent.KEY.get(victim);
        if (component.getPsychoTicks() > 0) {
            if (component.getArmour() > 0) {
                component.setArmour(component.getArmour() - 1);
                component.sync();
                victim.playSoundToPlayer(TMMSounds.ITEM_PSYCHO_ARMOUR, SoundCategory.MASTER, 5F, 1F);
                return;
            } else {
                component.stopPsycho();
            }
        }

        if (victim instanceof ServerPlayerEntity serverPlayerEntity && isPlayerAliveAndSurvival(serverPlayerEntity)) {
            serverPlayerEntity.changeGameMode(GameMode.SPECTATOR);
        } else {
            return;
        }

        if (killer != null) {
            PlayerShopComponent.KEY.get(killer).addToBalance(GameConstants.MONEY_PER_KILL);
        }

        PlayerMoodComponent.KEY.get(victim).reset();

        if (spawnBody) {
            var body = TMMEntities.PLAYER_BODY.create(victim.getWorld());
            if (body != null) {
                body.setPlayerUuid(victim.getUuid());
                var spawnPos = victim.getPos().add(victim.getRotationVector().normalize().multiply(1));
                body.refreshPositionAndAngles(spawnPos.getX(), victim.getY(), spawnPos.getZ(), victim.getHeadYaw(), 0f);
                body.setYaw(victim.getHeadYaw());
                body.setHeadYaw(victim.getHeadYaw());
                victim.getWorld().spawnEntity(body);
            }
        }

        for (List<ItemStack> list : victim.getInventory().combinedInventory) {
            for (var i = 0; i < list.size(); i++) {
                var stack = list.get(i);
                if (shouldDropOnDeath(stack)) {
                    victim.dropItem(stack, true, false);
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }

        var gameWorldComponent = TMMComponents.GAME.get(victim.getWorld());
        if (gameWorldComponent.isCivilian(victim)) {
            gameWorldComponent.decrementKillsLeft();
            GameTimeComponent.KEY.get(victim.getWorld()).addTime(GameConstants.TIME_ON_CIVILIAN_KILL);
        }
    }

    public static boolean shouldDropOnDeath(@NotNull ItemStack stack) {
        return !stack.isEmpty() && stack.isOf(TMMItems.REVOLVER);
    }

    public static boolean isPlayerAliveAndSurvival(PlayerEntity player) {
        return player != null && !player.isSpectator() && !player.isCreative();
    }

    public static boolean isPlayerSpectatingOrCreative(PlayerEntity player) {
        return player != null && (player.isSpectator() || player.isCreative());
    }

    record BlockEntityInfo(NbtCompound nbt, ComponentMap components) {
    }

    record BlockInfo(BlockPos pos, BlockState state, @Nullable BlockEntityInfo blockEntityInfo) {
    }

    enum Mode {
        FORCE(true),
        MOVE(true),
        NORMAL(false);

        private final boolean allowsOverlap;

        Mode(final boolean allowsOverlap) {
            this.allowsOverlap = allowsOverlap;
        }

        public boolean allowsOverlap() {
            return this.allowsOverlap;
        }
    }

    // returns whether another reset should be attempted
    public static boolean tryResetTrain(ServerWorld serverWorld) {
        if (serverWorld.getServer().getOverworld().equals(serverWorld)) {
            BlockPos backupMinPos = BlockPos.ofFloored(GameConstants.BACKUP_TRAIN_LOCATION.getMinPos());
            BlockPos backupMaxPos = BlockPos.ofFloored(GameConstants.BACKUP_TRAIN_LOCATION.getMaxPos());
            BlockBox backupTrainBox = BlockBox.create(backupMinPos, backupMaxPos);
            BlockPos trainMinPos = BlockPos.ofFloored(GameConstants.TRAIN_LOCATION.getMinPos());
            BlockPos trainMaxPos = trainMinPos.add(backupTrainBox.getDimensions());
            BlockBox trainBox = BlockBox.create(trainMinPos, trainMaxPos);

            Mode mode = Mode.FORCE;

            if (serverWorld.isRegionLoaded(backupMinPos, backupMaxPos) && serverWorld.isRegionLoaded(trainMinPos, trainMaxPos)) {
                List<BlockInfo> list = Lists.newArrayList();
                List<BlockInfo> list2 = Lists.newArrayList();
                List<BlockInfo> list3 = Lists.newArrayList();
                Deque<BlockPos> deque = Lists.newLinkedList();
                BlockPos blockPos5 = new BlockPos(
                        trainBox.getMinX() - backupTrainBox.getMinX(), trainBox.getMinY() - backupTrainBox.getMinY(), trainBox.getMinZ() - backupTrainBox.getMinZ()
                );

                for (int k = backupTrainBox.getMinZ(); k <= backupTrainBox.getMaxZ(); k++) {
                    for (int l = backupTrainBox.getMinY(); l <= backupTrainBox.getMaxY(); l++) {
                        for (int m = backupTrainBox.getMinX(); m <= backupTrainBox.getMaxX(); m++) {
                            BlockPos blockPos6 = new BlockPos(m, l, k);
                            BlockPos blockPos7 = blockPos6.add(blockPos5);
                            CachedBlockPosition cachedBlockPosition = new CachedBlockPosition(serverWorld, blockPos6, false);
                            BlockState blockState = cachedBlockPosition.getBlockState();

                            BlockEntity blockEntity = serverWorld.getBlockEntity(blockPos6);
                            if (blockEntity != null) {
                                BlockEntityInfo blockEntityInfo = new BlockEntityInfo(
                                        blockEntity.createComponentlessNbt(serverWorld.getRegistryManager()), blockEntity.getComponents()
                                );
                                list2.add(new BlockInfo(blockPos7, blockState, blockEntityInfo));
                                deque.addLast(blockPos6);
                            } else if (!blockState.isOpaqueFullCube(serverWorld, blockPos6) && !blockState.isFullCube(serverWorld, blockPos6)) {
                                list3.add(new BlockInfo(blockPos7, blockState, null));
                                deque.addFirst(blockPos6);
                            } else {
                                list.add(new BlockInfo(blockPos7, blockState, null));
                                deque.addLast(blockPos6);
                            }
                        }
                    }
                }

                if (mode == Mode.MOVE) {
                    for (BlockPos blockPos8 : deque) {
                        BlockEntity blockEntity2 = serverWorld.getBlockEntity(blockPos8);
                        Clearable.clear(blockEntity2);
                        serverWorld.setBlockState(blockPos8, Blocks.BARRIER.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }

                    for (BlockPos blockPos8 : deque) {
                        serverWorld.setBlockState(blockPos8, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                    }
                }

                List<BlockInfo> list4 = Lists.newArrayList();
                list4.addAll(list);
                list4.addAll(list2);
                list4.addAll(list3);
                List<BlockInfo> list5 = Lists.reverse(list4);

                for (BlockInfo blockInfo : list5) {
                    BlockEntity blockEntity3 = serverWorld.getBlockEntity(blockInfo.pos);
                    Clearable.clear(blockEntity3);
                    serverWorld.setBlockState(blockInfo.pos, Blocks.BARRIER.getDefaultState(), Block.NOTIFY_LISTENERS);
                }

                int mx = 0;

                for (BlockInfo blockInfo2 : list4) {
                    if (serverWorld.setBlockState(blockInfo2.pos, blockInfo2.state, Block.NOTIFY_LISTENERS)) {
                        mx++;
                    }
                }

                for (BlockInfo blockInfo2x : list2) {
                    BlockEntity blockEntity4 = serverWorld.getBlockEntity(blockInfo2x.pos);
                    if (blockInfo2x.blockEntityInfo != null && blockEntity4 != null) {
                        blockEntity4.readComponentlessNbt(blockInfo2x.blockEntityInfo.nbt, serverWorld.getRegistryManager());
                        blockEntity4.setComponents(blockInfo2x.blockEntityInfo.components);
                        blockEntity4.markDirty();
                    }

                    serverWorld.setBlockState(blockInfo2x.pos, blockInfo2x.state, Block.NOTIFY_LISTENERS);
                }

                for (BlockInfo blockInfo2x : list5) {
                    serverWorld.updateNeighbors(blockInfo2x.pos, blockInfo2x.state.getBlock());
                }

                serverWorld.getBlockTickScheduler().scheduleTicks(serverWorld.getBlockTickScheduler(), backupTrainBox, blockPos5);
                if (mx == 0) {
                    TMM.LOGGER.info("Train reset failed: No blocks copied. Queueing another attempt.");
                    return true;
                }
            } else {
                TMM.LOGGER.info("Train reset failed: Clone positions not loaded. Queueing another attempt.");
                return true;
            }

            // discard all player bodies and items
            for (PlayerBodyEntity body : serverWorld.getEntitiesByType(TMMEntities.PLAYER_BODY, playerBodyEntity -> true)) {
                body.discard();
            }
            for (ItemEntity item : serverWorld.getEntitiesByType(EntityType.ITEM, playerBodyEntity -> true)) {
                item.discard();
            }
            for (var entity : serverWorld.getEntitiesByType(TMMEntities.FIRECRACKER, entity -> true)) entity.discard();
            for (var entity : serverWorld.getEntitiesByType(TMMEntities.NOTE, entity -> true)) entity.discard();


            TMM.LOGGER.info("Train reset successful.");
            return false;
        }
        return false;
    }

    public enum WinStatus {
        NONE, KILLERS, PASSENGERS, TIME
    }
}
