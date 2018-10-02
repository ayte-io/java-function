package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

public class Actions {
    private Actions() {}

    public static <T, R> Action<T, R, RuntimeException> fromFunction(Function<T, R> source) {
        return new FunctionAction<>(source);
    }

    @RequiredArgsConstructor
    private static class FunctionAction<T, R> implements Action<T, R, RuntimeException> {
        private final Function<T, R> delegate;

        @Override
        public R apply(T value) {
            return delegate.apply(value);
        }
    }
}
