package com.unrealdinnerbone.tramplestopper;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.unrealdinnerbone.trenzalore.api.config.ConfigManger;

public record TrampleConfig(TrampleType type, int featherFallingLevel) {

    public static Supplier<TrampleConfig> CONFIG = Suppliers.memoize(() -> ConfigManger.getOrCreateConfig(TrampleStopper.MOD_ID, TrampleConfig.class,
            () -> new TrampleConfig(TrampleType.FEATHER_FALLING, 1)));
}
