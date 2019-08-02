package com.unrealdinnerbone.tramplestopper;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TrampleStopper.MOD_ID)
@Mod.EventBusSubscriber
public class TrampleStopper
{

    public static final String MOD_ID = "tramplestopper";
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec.EnumValue<TrampleType> type;
    public ForgeConfigSpec.IntValue intValue;
    public static ResourceLocation FARMLAND_TRAMPLED;
    private static TrampleStopper THIS;


    private static final Logger LOGGER = LogManager.getLogger();

    public TrampleStopper() {
        THIS = this;
        builder.push("general");
        type = builder.comment(
                "When should farmland get trampled",
                "Never: Never trampled farmland",
                "Always: It Always get trampled",
                "Default: Normal behavior",
                "Feather Falling: Does not get trampled with you have feather falling boots")
                .defineEnum("type", TrampleType.FEATHER_FALLING);
        intValue = builder.comment("Level of Feather Falling needed").defineInRange("level", 1,1,  3);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HI");
        FARMLAND_TRAMPLED = Registry.register(Registry.CUSTOM_STAT, new ResourceLocation(MOD_ID, "farmland_trampled"), new ResourceLocation(MOD_ID, "farmland_trampled"));

    }

    @SubscribeEvent
    public static void onCropTrample(BlockEvent.FarmlandTrampleEvent event) {
        if(type.get().getFunction().apply(THIS, event.getEntity())) {
            LOGGER.debug("Canceled FarmlandTrampleEvent");
            event.setCanceled(true);
        }else {
            if(event.getEntity() instanceof PlayerEntity) {
                ((PlayerEntity) event.getEntity()).addStat(FARMLAND_TRAMPLED);
            }
        }
    }



}
