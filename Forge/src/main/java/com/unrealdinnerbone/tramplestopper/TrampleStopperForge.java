package com.unrealdinnerbone.tramplestopper;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TrampleStopper.MOD_ID)
public class TrampleStopperForge {
    
    public TrampleStopperForge() {
        TrampleStopper.init();
        MinecraftForge.EVENT_BUS.addListener(TrampleStopperForge::onData);
    }

    private static void onData(BlockEvent.FarmlandTrampleEvent event) {
        if(TrampleConfig.CONFIG.get().type().getFunction().apply(event.getEntity())) {
            event.setCanceled(true);
        }
    }
}