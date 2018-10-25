package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @see Function
 * @see BiFunction
 *
 * @param <T1>
 * @param <T2>
 * @param <T3>
 * @param <R>
 */
@FunctionalInterface
public interface TriFunction<T1, T2, T3, R> {
    R apply(T1 alpha, T2 beta, T3 gamma);

    default BiFunction<T2, T3, R> partial(T1 alpha) {
        return new PartiallyAppliedBiFunction<>(this, alpha);
    }

    default Function<T3, R> partial(T1 alpha, T2 beta) {
        return new PartiallyAppliedFunction<>(this, alpha, beta);
    }

    @RequiredArgsConstructor
    class PartiallyAppliedBiFunction<T1, T2, T3, R> implements BiFunction<T2, T3, R> {
        private final TriFunction<T1, T2, T3, R> delegate;
        private final T1 alpha;

        @Override
        public R apply(T2 beta, T3 gamma) {
            return delegate.apply(alpha, beta, gamma);
        }
    }

    @RequiredArgsConstructor
    class PartiallyAppliedFunction<T1, T2, T3, R> implements Function<T3, R> {
        private final TriFunction<T1, T2, T3, R> delegate;
        private final T1 alpha;
        private final T2 beta;

        @Override
        public R apply(T3 gamma) {
            return delegate.apply(alpha, beta, gamma);
        }
    }
}
