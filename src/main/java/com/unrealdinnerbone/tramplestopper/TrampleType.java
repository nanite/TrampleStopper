package com.unrealdinnerbone.tramplestopper;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import java.util.function.BiFunction;

public enum TrampleType {

    FEATHER_FALLING((trampleConfig, pair) -> {
        if (pair.getA() instanceof PlayerEntity) {
            PlayerEntity entityPlayer = (PlayerEntity) pair.getA();
            if(pair.getB() >= trampleConfig.doubleValue.get()) {
                for (ItemStack itemStack : entityPlayer.getArmorInventoryList()) {
                    if (itemStack.getItem() instanceof ArmorItem) {
                        ArmorItem armorItem = (ArmorItem) itemStack.getItem();
                        if(armorItem.getEquipmentSlot() == EquipmentSlotType.FEET) {
                            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, itemStack) >= trampleConfig.intValue.get()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }),
    NEVER((trampleConfig, entity) -> true),
    ALWAYS((trampleConfig, entity) -> false);

    private final BiFunction<TrampleStopper, TrampleStopper.Pair<Entity, Float>, Boolean> function;

    TrampleType(BiFunction<TrampleStopper, TrampleStopper.Pair<Entity, Float>, Boolean> function) {
        this.function = function;
    }

    public BiFunction<TrampleStopper, TrampleStopper.Pair<Entity, Float>, Boolean> getFunction() {
        return function;
    }
}
