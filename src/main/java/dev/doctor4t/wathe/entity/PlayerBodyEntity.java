package dev.doctor4t.wathe.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.util.Arm;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class PlayerBodyEntity extends LivingEntity {
    private static final TrackedData<Optional<UUID>> PLAYER = DataTracker.registerData(PlayerBodyEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public PlayerBodyEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(PLAYER, Optional.empty());
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return null;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.dataTracker.set(PLAYER, Optional.of(playerUuid));
    }

    public UUID getPlayerUuid() {
        Optional<UUID> optional = this.dataTracker.get(PLAYER);
        return optional.orElseGet(() -> UUID.fromString("25adae11-cd98-48f4-990b-9fe1b2ee0886")); // Folly default because that's lowkey funny
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return !damageSource.isOf(DamageTypes.GENERIC_KILL) && !damageSource.isOf(DamageTypes.OUT_OF_WORLD);
    }

    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    public void pushAwayFrom(Entity entity) {
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 999999.0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.getPlayerUuid() != null) {
            nbt.putUuid("Player", this.getPlayerUuid());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        UUID uUID;
        if (nbt.containsUuid("Player")) {
            uUID = nbt.getUuid("Player");
        } else {
            String string = nbt.getString("Player");
            uUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), string);
        }

        if (uUID != null) {
            this.setPlayerUuid(uUID);
        }
    }

}
