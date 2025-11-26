package dev.doctor4t.trainmurdermystery.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.datafixers.util.Either;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerMoodComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerPoisonComponent;
import dev.doctor4t.trainmurdermystery.event.AllowPlayerPunching;
import dev.doctor4t.trainmurdermystery.event.IsPlayerPunchable;
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
import net.minecraft.util.math.BlockPos;
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
    private float sprintingTicks;
    @Unique
    private Scheduler.ScheduledTask poisonSleepTask;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


    @ModifyReturnValue(method = "getMovementSpeed", at = @At("RETURN"))
    public float tmm$overrideMovementSpeed(float original) {
        if (GameFunctions.isPlayerAliveAndSurvival((PlayerEntity) (Object) this)) {
            return this.isSprinting() ? 0.1f : 0.07f;
        } else {
            return original;
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void tmm$limitSprint(CallbackInfo ci) {
        GameWorldComponent gameComponent = GameWorldComponent.KEY.get(this.getWorld());
        if (GameFunctions.isPlayerAliveAndSurvival((PlayerEntity) (Object) this) && gameComponent != null && gameComponent.isRunning()) {
            Role role = gameComponent.getRole((PlayerEntity) (Object) this);
            if (role != null && role.getMaxSprintTime() >= 0) {
                if (this.isSprinting()) {
                    sprintingTicks = Math.max(sprintingTicks - 1, 0);
                } else {
                    sprintingTicks = Math.min(sprintingTicks + 0.25f, role.getMaxSprintTime());
                }

                if (sprintingTicks <= 0) {
                    this.setSprinting(false);
                }
            }
        }
    }

    @WrapMethod(method = "attack")
    public void attack(Entity target, Operation<Void> original) {
        PlayerEntity self = (PlayerEntity) (Object) this;

        if (getMainHandStack().isOf(TMMItems.BAT) && target instanceof PlayerEntity playerTarget && this.getAttackCooldownProgress(0.5F) >= 1f) {
            GameFunctions.killPlayer(playerTarget, true, self, GameConstants.DeathReasons.BAT);
            self.getEntityWorld().playSound(self,
                    playerTarget.getX(), playerTarget.getEyeY(), playerTarget.getZ(),
                    TMMSounds.ITEM_BAT_HIT, SoundCategory.PLAYERS,
                    3f, 1f);
            return;
        }

        if (!GameFunctions.isPlayerAliveAndSurvival(self) || this.getMainHandStack().isOf(TMMItems.KNIFE)
                || IsPlayerPunchable.EVENT.invoker().gotPunchable(target) || AllowPlayerPunching.EVENT.invoker().allowPunching(self)) {
            original.call(target);
        }
    }

    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;eat(Lnet/minecraft/component/type/FoodComponent;)V", shift = At.Shift.AFTER))
    private void tmm$poisonedFoodEffect(@NotNull World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
        if (world.isClient) return;
        String poisoner = stack.getOrDefault(TMMDataComponentTypes.POISONER, null);
        if (poisoner != null) {
            int poisonTicks = PlayerPoisonComponent.KEY.get(this).poisonTicks;
            if (poisonTicks == -1) {
                PlayerPoisonComponent.KEY.get(this).setPoisonTicks(world.getRandom().nextBetween(PlayerPoisonComponent.clampTime.getLeft(), PlayerPoisonComponent.clampTime.getRight()), UUID.fromString(poisoner));
            } else {
                PlayerPoisonComponent.KEY.get(this).setPoisonTicks(MathHelper.clamp(poisonTicks - world.getRandom().nextBetween(100, 300), 0, PlayerPoisonComponent.clampTime.getRight()), UUID.fromString(poisoner));
            }
        }
    }

    @Inject(method = "wakeUp(ZZ)V", at = @At("HEAD"))
    private void tmm$poisonSleep(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci) {
        if (this.poisonSleepTask != null) {
            this.poisonSleepTask.cancel();
            this.poisonSleepTask = null;
        }
    }

    @Inject(method = "trySleep", at = @At("TAIL"))
    private void tmm$poisonSleepMessage(BlockPos pos, CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> cir) {
        PlayerEntity self = (PlayerEntity) (Object) (this);
        if (cir.getReturnValue().right().isPresent() && self instanceof ServerPlayerEntity serverPlayer) {
            if (this.poisonSleepTask != null) this.poisonSleepTask.cancel();

            this.poisonSleepTask = Scheduler.schedule(
                    () -> PoisonUtils.bedPoison(serverPlayer),
                    40
            );
        }
    }

    @Inject(method = "canConsume(Z)Z", at = @At("HEAD"), cancellable = true)
    private void tmm$allowEatingRegardlessOfHunger(boolean ignoreHunger, @NotNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "eatFood", at = @At("HEAD"))
    private void tmm$eat(World world, ItemStack stack, FoodComponent foodComponent, @NotNull CallbackInfoReturnable<ItemStack> cir) {
        if (!(stack.getItem() instanceof CocktailItem)) {
            PlayerMoodComponent.KEY.get(this).eatFood();
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void tmm$saveData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putFloat("sprintingTicks", this.sprintingTicks);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void tmm$readData(NbtCompound nbt, CallbackInfo ci) {
        this.sprintingTicks = nbt.getFloat("sprintingTicks");
    }
}
