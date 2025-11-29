package dev.doctor4t.trainmurdermystery.compat.controlify;

import dev.doctor4t.trainmurdermystery.client.compat.LimitedInventoryScreenProcessor;
import dev.doctor4t.trainmurdermystery.client.gui.screen.ingame.LimitedInventoryScreen;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.api.entrypoint.InitContext;
import dev.isxander.controlify.api.entrypoint.PreInitContext;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;

public class ControlifyCompat implements ControlifyEntrypoint {
    @Override
    public void onControllersDiscovered(ControlifyApi controlifyApi) {

    }

    @Override
    public void onControlifyInit(InitContext initContext) {

    }

    @Override
    public void onControlifyPreInit(PreInitContext preInitContext) {
        ScreenProcessorProvider.registerProvider(
                LimitedInventoryScreen.class,
                LimitedInventoryScreenProcessor::new
        );
    }
}
