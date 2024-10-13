package com.finalcoder.core;

import java.util.function.Supplier;

// Copied from https://github.com/selenide/selenide/blob/main/src/main/java/com/codeborne/selenide/impl/Lazy.java
public class Lazy<T> {
    private volatile T value;
    private final Supplier<T> supplier;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (value == null) {
            synchronized (supplier) {
                if (value == null) {
                    value = supplier.get();
                }
            }
        }
        return value;
    }

    public static <T> Lazy<T> lazyEvaluated(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }
}
