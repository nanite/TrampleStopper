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

@Config(modid = "tramplestopper")
@Mod.EventBusSubscriber
@Mod(modid = "tramplestopper", dependencies = "required:forge@[14.23.4.2725,);")
public class TrampleStopper {

    @Mod.EventHandler
    public static void onPreInit(FMLPreInitializationEvent event) {
        ConfigManager.sync("tramplestopper", Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals("tramplestopper")) {
            ConfigManager.sync("tramplestopper", Config.Type.INSTANCE);
        }
    }


    @SubscribeEvent
    public static void onFarmlandTrampleEvent(BlockEvent.FarmlandTrampleEvent event) {
        switch (type) {
            case FEATHER_FALLING:
                Iterable<ItemStack> armorInventoryList = event.getEntity().getArmorInventoryList();
                for (ItemStack itemStack : armorInventoryList) {
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, itemStack) >= 1) {
                        if (itemStack.getItem() instanceof ItemArmor) {
                            ItemArmor itemArmor = (ItemArmor) itemStack.getItem();
                            if (itemArmor.getEquipmentSlot() == EntityEquipmentSlot.FEET) {
                                event.setCanceled(true);
                            }
                        }
                    }
                }
                break;
            case NEVER:
                event.setCanceled(true);
                break;
            case ALWAYS:
                event.setCanceled(false);
                break;
            case DEFAULT:
                break;

        }
    }

    @Config.Comment({
            "When should farmland get trampled",
            "Never: Never trampled farmland",
            "Always: It Always get trampled",
            "Default: Normal behavior",
            "Feather Falling: Does not get trampled with you have feather falling boots"})
    public static Type type = Type.FEATHER_FALLING;

    public static enum Type {
        NEVER,
        ALWAYS,
        FEATHER_FALLING,
        DEFAULT;
    }
}

