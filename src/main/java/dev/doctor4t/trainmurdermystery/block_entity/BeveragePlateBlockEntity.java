package dev.doctor4t.trainmurdermystery.block_entity;

import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.event.CanSeePoison;
import dev.doctor4t.trainmurdermystery.index.TMMBlockEntities;
import dev.doctor4t.trainmurdermystery.index.TMMParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BeveragePlateBlockEntity extends BlockEntity {
    private final List<ItemStack> storedItems = new ArrayList<>();
    private String poisoner = null;
    private PlateType plate = PlateType.DRINK;

    public BeveragePlateBlockEntity(BlockPos pos, BlockState state) {
        super(TMMBlockEntities.BEVERAGE_PLATE, pos, state);
    }

    private void sync() {
        if (this.world != null && !this.world.isClient) {
            this.markDirty();
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
        }
    }

    public static <T extends BlockEntity> void clientTick(World world, BlockPos pos, BlockState state, T t) {
        if (!(t instanceof BeveragePlateBlockEntity tray)) return;
        if ((!TMMClient.isKiller() && !CanSeePoison.EVENT.invoker().visible(MinecraftClient.getInstance().player)) || tray.poisoner == null) return;
        if (world.getRandom().nextBetween(0, 20) < 17) return;
        world.addParticle(
                TMMParticles.POISON,
                pos.getX() + 0.5f,
                pos.getY(),
                pos.getZ() + 0.5f,
                0f, 0.05f, 0f
        );
    }

    public List<ItemStack> getStoredItems() {
        return this.storedItems;
    }

    public void addItem(@NotNull ItemStack stack) {
        if (stack.isEmpty()) return;
        this.storedItems.add(stack.copy());
        this.sync();
    }

    public String getPoisoner() {
        return this.poisoner;
    }

    public void setPoisoner(String poisoner) {
        this.poisoner = poisoner;
        this.sync();
    }

    public boolean isDrink() {
        return this.plate == PlateType.DRINK;
    }

    public void setDrink(boolean drink) {
        this.plate = drink ? PlateType.DRINK : PlateType.FOOD;
        this.sync();
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        var itemsNbt = new NbtCompound();
        for (var i = 0; i < this.storedItems.size(); i++) {
            if (!this.storedItems.get(i).isEmpty()) itemsNbt.put("Item" + i, this.storedItems.get(i).encode(registryLookup));
        }
        nbt.put("Items", itemsNbt);
        if (this.poisoner != null) nbt.putString("poisoner", this.poisoner);
        nbt.putBoolean("Drink", this.plate == PlateType.DRINK);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.storedItems.clear();
        if (nbt.contains("Items")) {
            var itemsNbt = nbt.getCompound("Items");
            for (var key : itemsNbt.getKeys()) {
                var itemStack = ItemStack.fromNbt(registryLookup, itemsNbt.get(key));
                itemStack.ifPresent(this.storedItems::add);
            }
        }
        this.poisoner = nbt.contains("poisoner") ? nbt.getString("poisoner") : null;
        this.plate = nbt.getBoolean("Drink") ? PlateType.DRINK : PlateType.FOOD;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return this.createNbt(registryLookup);
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public enum PlateType {
        DRINK,
        FOOD
    }
}