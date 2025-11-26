package dev.doctor4t.trainmurdermystery.cca;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
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
    public int armour = 1;

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
        if (this.psychoTicks <= 0) return;
        this.psychoTicks--;
        if (this.player.getMainHandStack().isOf(TMMItems.BAT)) return;
        if (GameFunctions.isPlayerAliveAndSurvival(player)) {
            for (int i = 0; i < 9; i++) {
                if (!this.player.getInventory().getStack(i).isOf(TMMItems.BAT)) continue;
                this.player.getInventory().selectedSlot = i;
                break;
            }
        }
    }

    @Override
    public void serverTick() {
        if (this.psychoTicks <= 0) return;
//        if (this.psychoTicks % 20 == 0) this.player.sendMessage(Text.translatable("game.psycho_mode.time", this.psychoTicks / 20).withColor(Colors.RED), true);
        if (--this.psychoTicks == 0) {
//            this.player.sendMessage(Text.translatable("game.psycho_mode.over").withColor(Colors.RED), true);
            this.stopPsycho();
        }

        this.sync();
    }

    public boolean startPsycho() {
        if (ShopEntry.insertStackInFreeSlot(this.player, new ItemStack(TMMItems.BAT))) {
            this.setPsychoTicks(GameConstants.PSYCHO_TIMER);
            this.setArmour(GameConstants.PSYCHO_MODE_ARMOUR);
            GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(this.player.getWorld());
            gameWorldComponent.setPsychosActive(gameWorldComponent.getPsychosActive() + 1);
            return true;
        }
        return false;
    }

    public void stopPsycho() {
        GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(this.player.getWorld());
        gameWorldComponent.setPsychosActive(gameWorldComponent.getPsychosActive() - 1);
        this.psychoTicks = 0;
        this.player.getInventory().remove(itemStack -> itemStack.isOf(TMMItems.BAT), Integer.MAX_VALUE, this.player.playerScreenHandler.getCraftingInput());
    }

    public int getArmour() {
        return this.armour;
    }

    public void setArmour(int armour) {
        this.armour = armour;
        this.sync();
    }

    public int getPsychoTicks() {
        return this.psychoTicks;
    }

    public void setPsychoTicks(int ticks) {
        this.psychoTicks = ticks;
        this.sync();
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("psychoTicks", this.psychoTicks);
        tag.putInt("armour", this.armour);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.psychoTicks = tag.contains("psychoTicks") ? tag.getInt("psychoTicks") : 0;
        this.armour = tag.contains("armour") ? tag.getInt("armour") : 1;
    }
}