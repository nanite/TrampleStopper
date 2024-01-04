package com.unrealdinnerbone.tramplestopper;

import net.fabricmc.api.ModInitializer;

public class TrampleStopperFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        TrampleStopper.init();
    }
}
