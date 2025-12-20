package dev.doctor4t.wathe.mixin;

import dev.doctor4t.wathe.util.BlockSettingsAdditions;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractBlock.Settings.class)
public class AbstractBlockSettingsMixin implements BlockSettingsAdditions {
    @Shadow
    boolean collidable;

    @Override
    public AbstractBlock.Settings wathe$setCollidable(boolean collidable) {
        this.collidable = collidable;
        return (AbstractBlock.Settings) (Object) this;
    }
}