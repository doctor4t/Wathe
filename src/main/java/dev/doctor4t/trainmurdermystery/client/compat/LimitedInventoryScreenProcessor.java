package dev.doctor4t.trainmurdermystery.client.compat;

import dev.doctor4t.trainmurdermystery.client.gui.screen.ingame.LimitedInventoryScreen;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;

public class LimitedInventoryScreenProcessor extends ScreenProcessor<LimitedInventoryScreen> {
    public LimitedInventoryScreenProcessor(LimitedInventoryScreen screen) {
        super(screen);
    }

    @Override
    public VirtualMouseBehaviour virtualMouseBehaviour() {
        return VirtualMouseBehaviour.ENABLED;
    }
}
