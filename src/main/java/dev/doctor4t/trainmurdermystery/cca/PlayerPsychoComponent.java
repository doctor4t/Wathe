package dev.doctor4t.trainmurdermystery.cca;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.client.animation.Interpolations;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class PlayerPsychoComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    public static final ComponentKey<PlayerPsychoComponent> KEY = ComponentRegistry.getOrCreate(TMM.id("psycho"), PlayerPsychoComponent.class);

    private final PlayerEntity player;
    public int psychoTicks = 0;
    public void setPsychoTicks(int ticks) {
        this.psychoTicks = ticks;
        this.sync();
    }
    public int getPsychoTicks() {
        return this.psychoTicks;
    }

    public PlayerPsychoComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public void reset() {
        this.stopPsycho();
        this.sync();
    }

    @Override
    public void clientTick() {
        this.psychoTicks--;
    }

    @Override
    public void serverTick() {
        if (this.psychoTicks > 0) {
            if (--this.psychoTicks == 0) {
                stopPsycho();
            }
        }
        this.sync();
    }

    public void stopPsycho() {
        this.psychoTicks = 0;
        this.player.getInventory().remove(itemStack -> itemStack.isOf(TMMItems.BAT), Integer.MAX_VALUE, this.player.playerScreenHandler.getCraftingInput());
    }

    public boolean startPsycho() {
        boolean ret = ShopEntry.insertStackInFreeSlot(player, new ItemStack(TMMItems.BAT));
        if (ret) {
            this.setPsychoTicks(GameConstants.PSYCHO_TIMER);
        }
        return ret;
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("psychoTicks", this.psychoTicks);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.psychoTicks = tag.contains("psychoTicks") ? tag.getInt("psychoTicks") : 0;
    }
}