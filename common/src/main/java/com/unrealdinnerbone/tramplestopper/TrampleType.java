package com.unrealdinnerbone.tramplestopper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum TrampleType {

    FEATHER_FALLING((entity) -> {
        if (entity instanceof LivingEntity livingEntity) {
                for (ItemStack itemStack : livingEntity.getArmorSlots()) {
                    if (itemStack.getItem() instanceof ArmorItem armorItem) {
                        if(armorItem.getEquipmentSlot() == EquipmentSlot.FEET) {
                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FALL_PROTECTION, itemStack) >= TrampleConfig.CONFIG.get().featherFallingLevel()) {
                                return true;
                            }
                        }
                    }
                }
        }
        return false;
    }),
    NEVER((entity) -> true),
    ALWAYS((entity) -> false);

    private final Function<Entity, Boolean> function;

    TrampleType(Function<Entity, Boolean> function) {
        this.function = function;
    }

    public Function<Entity, Boolean> getFunction() {
        return function;
    }
}
