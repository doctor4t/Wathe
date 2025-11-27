package dev.doctor4t.trainmurdermystery.mixin.compat.controlify;

import dev.doctor4t.trainmurdermystery.client.gui.screen.ingame.LimitedInventoryScreen;
import dev.doctor4t.trainmurdermystery.compat.controlify.LimitedInventoryScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/*
* https://docs.isxander.dev/controlify/developers/screen-operation-api
* Apparently this is how you're supposed to do it - curious as to why you can't
* just implement this with the class on its own...
* */

@Mixin(LimitedInventoryScreen.class)
public class LimitedInventoryScreenMixin implements ScreenProcessorProvider {
    @Unique private final ScreenProcessor<?> processor =
            new LimitedInventoryScreenProcessor((LimitedInventoryScreen)(Object)this);

    @Override
    public ScreenProcessor<?> screenProcessor() {
        return this.processor;
    }
}
