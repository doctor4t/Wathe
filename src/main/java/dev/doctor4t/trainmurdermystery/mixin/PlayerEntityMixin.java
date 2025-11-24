package dev.doctor4t.trainmurdermystery.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.datafixers.util.Either;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerMoodComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerPoisonComponent;
import dev.doctor4t.trainmurdermystery.api.event.AllowPlayerPunching;
import dev.doctor4t.trainmurdermystery.api.event.IsPlayerPunchable;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.doctor4t.trainmurdermystery.index.TMMDataComponentTypes;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import dev.doctor4t.trainmurdermystery.item.CocktailItem;
import dev.doctor4t.trainmurdermystery.util.PoisonUtils;
import dev.doctor4t.trainmurdermystery.util.Scheduler;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Unit;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    public abstract float getAttackCooldownProgress(float baseTime);

    @Unique
    private float trainmurdermystery$sprintingTicks;
    @Unique
    private Scheduler.ScheduledTask trainmurdermystery$poisonSleepTask;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "getMovementSpeed", at = @At("RETURN"))
    public float overrideMovementSpeed(float original) {
        if (GameFunctions.isPlayerAliveAndSurvival((PlayerEntity) (Object) this)) {
            return this.isSprinting() ? 0.1f : 0.07f;
        } else {
            return original;
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void limitSprint(CallbackInfo ci) {
        if (!GameFunctions.isPlayerAliveAndSurvival((PlayerEntity) (Object) this)) {
            return;
        }

        GameWorldComponent gameComponent = GameWorldComponent.KEY.get(this.getWorld());
        if (gameComponent != null) {
            if (!gameComponent.isRunning()) {
                return;
            }
            if (gameComponent.canUseKillerFeatures((PlayerEntity)(Object) this)) {
                return;
            }
            if (gameComponent.getGameMode() == GameWorldComponent.GameMode.LOOSE_ENDS) {
                return;
            }
        }

        if (this.isSprinting()) {
            trainmurdermystery$sprintingTicks = Math.max(trainmurdermystery$sprintingTicks - 1, 0);
        } else {
            trainmurdermystery$sprintingTicks = Math.min(trainmurdermystery$sprintingTicks + 0.25f, GameConstants.MAX_SPRINTING_TICKS);
        }

        if (trainmurdermystery$sprintingTicks <= 0) {
            this.setSprinting(false);
        }
    }

    @WrapMethod(method = "attack")
    public void attack(Entity target, Operation<Void> original) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (!GameFunctions.isPlayerAliveAndSurvival(self) || this.getMainHandStack().isOf(TMMItems.KNIFE)
                || IsPlayerPunchable.EVENT.invoker().gotPunchable(target) || AllowPlayerPunching.EVENT.invoker().allowPunching(self)) {
            original.call(target);
        }

        if (GameFunctions.isPlayerAliveAndSurvival(self) && getMainHandStack().isOf(TMMItems.BAT) && target instanceof PlayerEntity playerTarget && this.getAttackCooldownProgress(0.5F) >= 1f) {
            GameFunctions.killPlayer(playerTarget, true, self, GameConstants.DeathReasons.BAT);
            self.getEntityWorld().playSound(self,
                    playerTarget.getX(), playerTarget.getEyeY(), playerTarget.getZ(),
                    TMMSounds.ITEM_BAT_HIT, SoundCategory.PLAYERS,
                    3f, 1f);
        }
    }

    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;eat(Lnet/minecraft/component/type/FoodComponent;)V", shift = At.Shift.AFTER))
    private void poisonedFoodEffect(@NotNull World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
        if (world.isClient) {
            return;
        }

        String poisoner = stack.getOrDefault(TMMDataComponentTypes.POISONER, null);
        if (poisoner == null) {
            return;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(poisoner);
        } catch (IllegalArgumentException e) {
            TMM.LOGGER.error("Unable to read UUID from String {} when attempting to eat poisoned food!", poisoner, e);
            uuid = null;
        }

        PoisonUtils.updatePoisonTicks((PlayerEntity) (Object) this, uuid, world.getRandom());
    }

    @Inject(method = "wakeUp(ZZ)V", at = @At("HEAD"))
    private void poisonSleep(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci) {
        if (this.trainmurdermystery$poisonSleepTask == null) {
            return;
        }

        this.trainmurdermystery$poisonSleepTask.cancel();
        this.trainmurdermystery$poisonSleepTask = null;
    }

    @ModifyReturnValue(method = "trySleep", at = @At("TAIL"))
    private Either<PlayerEntity.SleepFailureReason, Unit> poisonSleepMessage(Either<PlayerEntity.SleepFailureReason, Unit> original) {
        PlayerEntity self = (PlayerEntity) (Object) (this);
        if (original.right().isEmpty() || !(self instanceof ServerPlayerEntity serverPlayer)) {
            return original;
        }

        if (this.trainmurdermystery$poisonSleepTask != null) {
            this.trainmurdermystery$poisonSleepTask.cancel();
        }

        this.trainmurdermystery$poisonSleepTask = Scheduler.schedule(
                () -> PoisonUtils.bedPoison(serverPlayer),
                40
        );
        return original;
    }

    @Inject(method = "canConsume(Z)Z", at = @At("HEAD"), cancellable = true)
    private void allowEatingRegardlessOfHunger(boolean ignoreHunger, @NotNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "eatFood", at = @At("HEAD"))
    private void eat(World world, ItemStack stack, FoodComponent foodComponent, @NotNull CallbackInfoReturnable<ItemStack> cir) {
        if (stack.getItem() instanceof CocktailItem) {
            return;
        }
        PlayerMoodComponent.KEY.get(this).eatFood();
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void saveData(NbtCompound nbt, CallbackInfo ci) {
        // I would change the key to something more specific so other mods/datapacks don't interfere with it
        nbt.putFloat("sprintingTicks", this.trainmurdermystery$sprintingTicks);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readData(NbtCompound nbt, CallbackInfo ci) {
        this.trainmurdermystery$sprintingTicks = nbt.getFloat("sprintingTicks");
    }
}
