package com.unrealdinnerbone.tramplestopper;


import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

@Mod(TrampleStopper.MOD_ID)
public class TrampleStopperNeo {
    
    public TrampleStopperNeo() {
        TrampleStopper.init();
        NeoForge.EVENT_BUS.addListener(TrampleStopperNeo::onData);
    }

    private static void onData(BlockEvent.FarmlandTrampleEvent event) {
        if(TrampleConfig.CONFIG.get().type().getFunction().apply(event.getEntity())) {
            event.setCanceled(true);
        }
    }
}