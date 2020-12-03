package com.unrealdinnerbone.tramplestopper;

import java.util.function.Supplier;

public class CacheStore<T>
{
    private Supplier<T> tSupplier;
    private T t;

    public CacheStore(Supplier<T> tSupplier) {
        this.tSupplier = tSupplier;
        t = null;
    }

    public T getT() {
        if(t == null) {
            t = tSupplier.get();
        }
        return t;
    }

    public void clear() {
        this.t = null;
    }
}
