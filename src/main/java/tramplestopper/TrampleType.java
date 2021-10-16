package tramplestopper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.function.BiFunction;

public enum TrampleType {

    FEATHER_FALLING((trampleConfig, pair) -> {
        if (pair.getFirst() instanceof Player) {
            Player entityPlayerEntity = (Player) pair.getFirst();
            if(pair.getSecond() >= TrampleStopper.doubleValue.get()) {
                for (ItemStack itemStack : entityPlayerEntity.getArmorSlots()) {
                    if (itemStack.getItem() instanceof ArmorItem) {
                        ArmorItem armorItem = (ArmorItem) itemStack.getItem();
                        if(armorItem.getSlot() == EquipmentSlot.FEET) {
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
