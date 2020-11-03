package com.unrealdinnerbone.tramplestopper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TrampleStopper.MOD_ID)
@Mod.EventBusSubscriber
public class TrampleStopper
{

    public static final String MOD_ID = "tramplestopper";
    public static ForgeConfigSpec.EnumValue<TrampleType> type;
    public ForgeConfigSpec.IntValue intValue;
    public ForgeConfigSpec.DoubleValue doubleValue;
    public static ResourceLocation FARMLAND_TRAMPLED;
    public static ResourceLocation FARMLAND_NOT_TRAMPLED;
    private static TrampleStopper THIS;


    private static final Logger LOGGER = LogManager.getLogger();

    public TrampleStopper() {
        THIS = this;
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("general");
        type = builder.comment(
                "When should farmland get trampled",
                "Never: Never trampled farmland",
                "Always: It Always get trampled",
                "Default: Normal behavior",
                "Feather Falling: Does not get trampled with you have feather falling boots")
                .defineEnum("type", TrampleType.NEVER);
        intValue = builder.comment("Level of Feather Falling needed")
                .defineInRange("level", 1,1,  3);
        doubleValue = builder.comment("At how many blocks should trampling stop start")
                .defineInRange("blocks",  0.0, 0.0, 256.0);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(TrampleStopper::onCropTrample);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Hello");
        FARMLAND_TRAMPLED = Registry.register(Registry.CUSTOM_STAT, new ResourceLocation(MOD_ID, "farmland_trampled"), new ResourceLocation(MOD_ID, "farmland_trampled"));
        FARMLAND_NOT_TRAMPLED = Registry.register(Registry.CUSTOM_STAT, new ResourceLocation(MOD_ID, "farmland_saved"), new ResourceLocation(MOD_ID, "farmland_saved"));

    }

    @SubscribeEvent
    public static void onCropTrample(BlockEvent.FarmlandTrampleEvent event) {
        if(type.get().getFunction().apply(THIS, new Pair<>(event.getEntity(), event.getFallDistance()))) {
            event.setCanceled(true);
            if(event.getEntity() instanceof PlayerEntity) {
                ((PlayerEntity) event.getEntity()).addStat(FARMLAND_NOT_TRAMPLED);
            }
        } else {
            if(event.getEntity() instanceof PlayerEntity) {
                ((PlayerEntity) event.getEntity()).addStat(FARMLAND_TRAMPLED);
            }
        }
    }
}
