package tramplestopper;

import com.mojang.datafixers.util.Pair;
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
        if (pair.getFirst() instanceof PlayerEntity) {
            PlayerEntity entityPlayerEntity = (PlayerEntity) pair.getFirst();
            if(pair.getSecond() >= TrampleStopper.doubleValue.get()) {
                for (ItemStack itemStack : entityPlayerEntity.getArmorSlots()) {
                    if (itemStack.getItem() instanceof ArmorItem) {
                        ArmorItem armorItem = (ArmorItem) itemStack.getItem();
                        if(armorItem.getSlot() == EquipmentSlotType.FEET) {
                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FALL_PROTECTION, itemStack) >= TrampleStopper.intValue.get()) {
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

    private final BiFunction<TrampleStopper, Pair<Entity, Float>, Boolean> function;

    TrampleType(BiFunction<TrampleStopper, Pair<Entity, Float>, Boolean> function) {
        this.function = function;
    }

    public BiFunction<TrampleStopper, Pair<Entity, Float>, Boolean> getFunction() {
        return function;
    }
}
