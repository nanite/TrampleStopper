package tramplestopper;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tramplestopper.test.GameTestHandler;

@Mod(TrampleStopper.MOD_ID)
@Mod.EventBusSubscriber
public class TrampleStopper
{

    public static final String MOD_ID = "tramplestopper";
    public static ForgeConfigSpec.EnumValue<TrampleType> type;
    public static ForgeConfigSpec.IntValue intValue;
    public static ForgeConfigSpec.DoubleValue doubleValue;
    public static RegistryObject<ResourceLocation> FARMLAND_TRAMPLED;
    public static RegistryObject<ResourceLocation> FARMLAND_NOT_TRAMPLED;
    private static TrampleStopper THIS;

    private static final DeferredRegister<ResourceLocation> STATS = DeferredRegister.create(Registries.CUSTOM_STAT, MOD_ID);

    private static final Logger LOGGER = LogManager.getLogger();

    public TrampleStopper() {
        THIS = this;
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("general");
        type = builder.comment(
                "When should farmland get trampled",
                "Never: Never trampled farmland",
                "Always: It Always get trampled",
                "Default: Normal behavior",
                "Feather Falling: Does not get trampled with you have feather falling boots")
                .defineEnum("type", TrampleType.NEVER);
        intValue = builder.comment("Level of Feather Falling needed")
                .defineInRange("level", 1,1,  3);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
        doubleValue = builder.comment("At how many blocks should trampling stop start")
                .defineInRange("blocks",  0.0, 0, Short.MAX_VALUE);
        FARMLAND_TRAMPLED = STATS.register("farmland_trampled", () -> new ResourceLocation(MOD_ID, "farmland_trampled"));
        FARMLAND_NOT_TRAMPLED = STATS.register("farmland_not_trampled", () -> new ResourceLocation(MOD_ID, "farmland_not_trampled"));
        STATS.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(TrampleStopper::onCropTrample);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerGameTest);
    }


    public void registerGameTest(RegisterGameTestsEvent event) {
        event.register(GameTestHandler.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Hello");
    }

    @SubscribeEvent
    public static void onCropTrample(BlockEvent.FarmlandTrampleEvent event) {
        if(type.get().getFunction().apply(THIS, new Pair<>(event.getEntity(), event.getFallDistance()))) {
            event.setCanceled(true);
        }
        if(event.getEntity() instanceof Player player) {
            player.awardStat(event.isCanceled() ? FARMLAND_NOT_TRAMPLED.get() : FARMLAND_TRAMPLED.get());
        }
    }
}
