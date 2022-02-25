package tramplestopper.test;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import tramplestopper.TrampleStopper;

import java.util.List;

public class GameTestHandler {

    @GameTestGenerator
    public static List<TestFunction> generateTests()
    {
        TestFunction testStone = new TestFunction("defaultBatch", "farmland_test", new ResourceLocation(TrampleStopper.MOD_ID, "farmland_test").toString(), Rotation.NONE, 100, 0, true, GameTestHandler::farmlandTest);
        return List.of(testStone);
    }


    public static void farmlandTest(GameTestHelper helper) {

        BlockPos blockPos = new BlockPos(0, 1, 0);
        Zombie zombie = EntityType.ZOMBIE.create(helper.getLevel());
        ItemStack itemStack = new ItemStack(Items.DIAMOND_BOOTS);
        itemStack.enchant(Enchantments.FALL_PROTECTION, 1);
        zombie.setItemSlot(EquipmentSlot.FEET, itemStack);
        BlockState block = helper.getBlockState(blockPos);
        block.getBlock().fallOn(helper.getLevel(), block, helper.absolutePos(blockPos), zombie,5.0f);
        helper.assertBlockState(blockPos, b -> b.is(Blocks.FARMLAND), () -> "Block is not farmland");
        helper.succeed();
    }

}
