package com.unrealdinnerbone.tramplestopper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.function.Function;

public enum TrampleType {

    FEATHER_FALLING((entity) -> {
        if (entity instanceof LivingEntity livingEntity) {
            RegistryAccess frozen = livingEntity.registryAccess();
            HolderLookup.RegistryLookup<Enchantment> lookup = frozen.lookupOrThrow(Registries.ENCHANTMENT);
            Holder.Reference<Enchantment> featherFalling = lookup.getOrThrow(Enchantments.FEATHER_FALLING);
            ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
            if (stack.has(DataComponents.ENCHANTMENTS)) {
                ItemEnchantments itemEnchantments = stack.get(DataComponents.ENCHANTMENTS);
                if (itemEnchantments == null) {
                    return false;
                }
                int level = itemEnchantments.getLevel(featherFalling);
                return level >= TrampleConfig.CONFIG.get().featherFallingLevel();
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
