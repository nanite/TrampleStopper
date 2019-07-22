package com.unrealdinnerbone.tramplestopper;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
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

@Mod("tramplestopper")
@Mod.EventBusSubscriber
public class TrampleStopper
{

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec.EnumValue<Type> type;
    public static ForgeConfigSpec.IntValue intValue;


    private static final Logger LOGGER = LogManager.getLogger();

    public TrampleStopper() {
        builder.push("general");
        type = builder.comment(
                "When should farmland get trampled",
                "Never: Never trampled farmland",
                "Always: It Always get trampled",
                "Default: Normal behavior",
                "Feather Falling: Does not get trampled with you have feather falling boots")
                .defineEnum("type", Type.FEATHER_FALLING);
        intValue = builder.comment("Level of Feather Falling needed").defineInRange("level", 1,1,  3);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HI");
    }


    @SubscribeEvent
    public static void onCropTrample(BlockEvent.FarmlandTrampleEvent event) {
        switch (type.get()) {
            case FEATHER_FALLING:
                Iterable<ItemStack> armorInventoryList = event.getEntity().getArmorInventoryList();
                for (ItemStack itemStack : armorInventoryList) {
                    if(itemStack.getItem() instanceof ArmorItem) {
                        ArmorItem amourItem = (ArmorItem) itemStack.getItem();
                        if(amourItem.getEquipmentSlot() == EquipmentSlotType.FEET) {
                            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, itemStack) >= intValue.get()) {
                                event.setCanceled(true);
                                LOGGER.debug("Canceled FarmlandTrampleEvent");
                            }
                        }
                    }
                }
                break;
            case NEVER:
                event.setCanceled(true);
                LOGGER.debug("Canceled FarmlandTrampleEvent");
                break;
            case ALWAYS:
                event.setCanceled(false);
                break;
            case DEFAULT:
                break;

        }
    }


    public static enum Type {
        NEVER,
        ALWAYS,
        FEATHER_FALLING,
        DEFAULT
    }
}
