package dev.doctor4t.trainmurdermystery.cca;

import com.mojang.authlib.GameProfile;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameRoundEndComponent implements AutoSyncedComponent {
    public static final ComponentKey<GameRoundEndComponent> KEY = ComponentRegistry.getOrCreate(TMM.id("roundend"), GameRoundEndComponent.class);
    private final World world;
    private final List<RoundEndData> players = new ArrayList<>();
    private GameFunctions.WinStatus winStatus = GameFunctions.WinStatus.NONE;

    public GameRoundEndComponent(World world) {
        this.world = world;
    }

    public void sync() {
        KEY.sync(this.world);
    }

    public void setRoundEndData(@NotNull List<ServerPlayerEntity> players, GameFunctions.WinStatus winStatus) {
        this.players.clear();
        for (var player : players) {
            var role = RoleAnnouncementTexts.BLANK;
            var game = GameWorldComponent.KEY.get(this.world);
            if (game.canUseKillerFeatures(player)) {
                role = RoleAnnouncementTexts.KILLER;
            } else if (game.isRole(player, TMMRoles.VIGILANTE)) {
                role = RoleAnnouncementTexts.VIGILANTE;
            } else {
                role = RoleAnnouncementTexts.CIVILIAN;
            }
            this.players.add(new RoundEndData(player.getGameProfile(), role, !GameFunctions.isPlayerAliveAndSurvival(player)));
        }
        this.winStatus = winStatus;
        this.sync();
    }

    public boolean didWin(UUID uuid) {
        if (GameFunctions.WinStatus.NONE == this.winStatus) return false;
        for (var detail : this.players) {
            if (!detail.player.getId().equals(uuid)) continue;
            return switch (this.winStatus) {
                case KILLERS -> detail.role == RoleAnnouncementTexts.KILLER;
                case PASSENGERS, TIME -> detail.role != RoleAnnouncementTexts.KILLER;
                default -> false;
            };
        }
        return false;
    }

    public List<RoundEndData> getPlayers() {
        return this.players;
    }

    public GameFunctions.WinStatus getWinStatus() {
        return this.winStatus;
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        var list = new NbtList();
        for (var detail : this.players) list.add(detail.writeToNbt());
        tag.put("players", list);
        tag.putInt("winstatus", this.winStatus.ordinal());
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.players.clear();
        for (var element : tag.getList("players", 10)) this.players.add(new RoundEndData((NbtCompound) element));
        this.winStatus = GameFunctions.WinStatus.values()[tag.getInt("winstatus")];
    }

    public record RoundEndData(GameProfile player, RoleAnnouncementTexts.RoleAnnouncementText role, boolean wasDead) {
        public RoundEndData(@NotNull NbtCompound tag) {
            this(new GameProfile(tag.getUuid("uuid"), tag.getString("name")), RoleAnnouncementTexts.ROLE_ANNOUNCEMENT_TEXTS.get(tag.getInt("role")), tag.getBoolean("wasDead"));
        }

        public @NotNull NbtCompound writeToNbt() {
            var tag = new NbtCompound();
            tag.putUuid("uuid", this.player.getId());
            tag.putString("name", this.player.getName());
            tag.putInt("role", RoleAnnouncementTexts.ROLE_ANNOUNCEMENT_TEXTS.indexOf(this.role));
            tag.putBoolean("wasDead", this.wasDead);
            return tag;
        }
    }
}