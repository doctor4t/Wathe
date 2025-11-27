package dev.doctor4t.trainmurdermystery.cca;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class PlayerShopComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    public static final ComponentKey<PlayerShopComponent> KEY = ComponentRegistry.getOrCreate(TMM.id("shop"), PlayerShopComponent.class);
    private final PlayerEntity player;
    public int balance = 0;

    public PlayerShopComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public void reset() {
        this.balance = 0;
        this.sync();
    }

    public void addToBalance(int amount) {
        this.setBalance(this.balance + amount);
    }

    public void setBalance(int amount) {
        this.balance = amount;
        this.sync();
    }

    public void tryBuy(int index) {
        if (index < 0 || index >= GameConstants.SHOP_ENTRIES.size()) return;
        ShopEntry entry = GameConstants.SHOP_ENTRIES.get(index);
        if (FabricLoader.getInstance().isDevelopmentEnvironment() && this.balance < entry.price())
            this.balance = entry.price() * 10;
        if (this.balance >= entry.price() && !this.player.getItemCooldownManager().isCoolingDown(entry.stack().getItem()) && entry.onBuy(this.player)) {
            this.balance -= entry.price();
            if (this.player instanceof ServerPlayerEntity player) {
                player.networkHandler.sendPacket(new PlaySoundS2CPacket(Registries.SOUND_EVENT.getEntry(TMMSounds.UI_SHOP_BUY), SoundCategory.PLAYERS, player.getX(), player.getY(), player.getZ(), 1.0f, 0.9f + this.player.getRandom().nextFloat() * 0.2f, player.getRandom().nextLong()));
            }
        } else {
            this.player.sendMessage(Text.literal("Purchase Failed").formatted(Formatting.DARK_RED), true);
            if (this.player instanceof ServerPlayerEntity player) {
                player.networkHandler.sendPacket(new PlaySoundS2CPacket(Registries.SOUND_EVENT.getEntry(TMMSounds.UI_SHOP_BUY_FAIL), SoundCategory.PLAYERS, player.getX(), player.getY(), player.getZ(), 1.0f, 0.9f + this.player.getRandom().nextFloat() * 0.2f, player.getRandom().nextLong()));
            }
        }
        this.sync();
    }

    @Override
    public void clientTick() {

    }

    @Override
    public void serverTick() {

    }

    public static boolean useBlackout(@NotNull PlayerEntity player) {
        player.getItemCooldownManager().set(TMMItems.BLACKOUT, GameConstants.ITEM_COOLDOWNS.getOrDefault(TMMItems.BLACKOUT, 0));
        return WorldBlackoutComponent.KEY.get(player.getWorld()).triggerBlackout();
    }

    public static boolean usePsychoMode(@NotNull PlayerEntity player) {
        player.getItemCooldownManager().set(TMMItems.PSYCHO_MODE, GameConstants.ITEM_COOLDOWNS.getOrDefault(TMMItems.PSYCHO_MODE, 0));
        return PlayerPsychoComponent.KEY.get(player).startPsycho();
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("Balance", this.balance);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.balance = tag.getInt("Balance");
    }
}