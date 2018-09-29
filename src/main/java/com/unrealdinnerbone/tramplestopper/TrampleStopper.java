package com.unrealdinnerbone.tramplestopper;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"WeakerAccess", "UnnecessaryEnumModifier"})
@Mod.EventBusSubscriber
@Mod(modid = TrampleStopper.MOD_ID, dependencies = "required:forge@[14.23.4.2725,);")
public class TrampleStopper {

    public static final String MOD_ID = "tramplestopper";

    private static Logger logger;

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MOD_ID)) {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
        }
    }


    @SubscribeEvent
    public static void onFarmlandTrampleEvent(BlockEvent.FarmlandTrampleEvent event) {
        switch (TrampleConfig.type) {
            case FEATHER_FALLING:
                Iterable<ItemStack> armorInventoryList = event.getEntity().getArmorInventoryList();
                for (ItemStack itemStack : armorInventoryList) {
                    if(itemStack.getItem() instanceof ItemArmor) {
                        ItemArmor amourItem = (ItemArmor) itemStack.getItem();
                        if(amourItem.getEquipmentSlot() == EntityEquipmentSlot.FEET) {
                            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, itemStack) >= 1) {
                                event.setCanceled(true);
                                getLogger().debug("Canceled FarmlandTrampleEvent");
                            }
                        }
                    }
                }
                break;
            case NEVER:
                event.setCanceled(true);
                getLogger().debug("Canceled FarmlandTrampleEvent");
                break;
            case ALWAYS:
                event.setCanceled(false);
                break;
            case DEFAULT:
                break;

        }
    }

    @Config(modid = TrampleStopper.MOD_ID)
    public static class TrampleConfig {

        @Config.Comment({
                "When should farmland get trampled",
                "Never: Never trampled farmland",
                "Always: It Always get trampled",
                "Default: Normal behavior",
                "Feather Falling: Does not get trampled with you have feather falling boots"})
        public static Type type = Type.FEATHER_FALLING;


    }

    public static enum Type {
        NEVER,
        ALWAYS,
        FEATHER_FALLING,
        DEFAULT
    }


    public static Logger getLogger() {
        if(logger == null) {
            logger = LogManager.getLogger(MOD_ID);
        }
        return logger;
    }
}



