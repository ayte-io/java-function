package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.function.BiFunction;

public class BiActions {
    private BiActions() {}

    public static <T1, T2, R> BiAction<T1, T2, R, RuntimeException> fromBiFunction(BiFunction<T1, T2, R> source) {
        return new BiFunctionBiAction<>(source);
    }

    public static <T1, T2, R> BiAction<T1, T2, R, RuntimeException> from(BiFunction<T1, T2, R> source) {
        return fromBiFunction(source);
    }

    public static <T1, T2, R, E extends Throwable> Action<T2, R, E> partial(BiAction<T1, T2, R, E> action, T1 alpha) {
        return new PartiallyAppliedBiAction<>(action, alpha);
    }

    @RequiredArgsConstructor
    private static class BiFunctionBiAction<T1, T2, R> implements BiAction<T1, T2, R, RuntimeException> {
        private final BiFunction<T1, T2, R> delegate;

        @Override
        public R apply(T1 alpha, T2 beta) {
            return delegate.apply(alpha, beta);
        }
    }

    @RequiredArgsConstructor
    private static class PartiallyAppliedBiAction<T1, T2, R, E extends Throwable> implements Action<T2, R, E> {
        private final BiAction<T1, T2, R, E> delegate;
        private final T1 alpha;

        @Override
        public R apply(T2 beta) throws E {
            return delegate.apply(alpha, beta);
        }
    }
}
