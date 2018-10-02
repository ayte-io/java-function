package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

public class TriActions {
    private TriActions() {}

    public static <T1, T2, T3, R> TriAction<T1, T2, T3, R, RuntimeException> fromTriFunction(TriFunction<T1, T2, T3, R> source) {
        return new TriFunctionTriAction<>(source);
    }

    public static <T1, T2, T3, R> TriAction<T1, T2, T3, R, RuntimeException> from(TriFunction<T1, T2, T3, R> source) {
        return fromTriFunction(source);
    }

    @RequiredArgsConstructor
    private static class TriFunctionTriAction<T1, T2, T3, R> implements TriAction<T1, T2, T3, R, RuntimeException> {
        private final TriFunction<T1, T2, T3, R> delegate;

        @Override
        public R apply(T1 alpha, T2 beta, T3 gamma) {
            return delegate.apply(alpha, beta, gamma);
        }
    }
}
