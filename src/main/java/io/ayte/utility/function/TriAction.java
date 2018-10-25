package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

/**
 * @see Action
 * @see TriActions
 *
 * @param <T1>
 * @param <T2>
 * @param <T3>
 * @param <R>
 * @param <E>
 */
@FunctionalInterface
public interface TriAction<T1, T2, T3, R, E extends Throwable> {
    R apply(T1 alpha, T2 beta, T3 gamma) throws E;

    default BiAction<T2, T3, R, E> partial(T1 alpha) {
        return new PartiallyAppliedBiAction<>(this, alpha);
    }

    default Action<T3, R, E> partial(T1 alpha, T2 beta) {
        return new PartiallyAppliedAction<>(this, alpha, beta);
    }

    @RequiredArgsConstructor
    class PartiallyAppliedBiAction<T1, T2, T3, R, E extends Throwable> implements BiAction<T2, T3, R, E> {
        private final TriAction<T1, T2, T3, R, E> delegate;
        private final T1 alpha;

        @Override
        public R apply(T2 beta, T3 gamma) throws E {
            return delegate.apply(alpha, beta, gamma);
        }
    }

    @RequiredArgsConstructor
    class PartiallyAppliedAction<T1, T2, T3, R, E extends Throwable> implements Action<T3, R, E> {
        private final TriAction<T1, T2, T3, R, E> delegate;
        private final T1 alpha;
        private final T2 beta;

        @Override
        public R apply(T3 gamma) throws E {
            return delegate.apply(alpha, beta, gamma);
        }
    }
}
