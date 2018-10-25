package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

/**
 * @see Action
 * @see BiActions
 *
 * @param <T1>
 * @param <T2>
 * @param <R>
 * @param <E>
 */
@FunctionalInterface
public interface BiAction<T1, T2, R, E extends Throwable> {
    R apply(T1 alpha, T2 beta) throws E;

    default Action<T2, R, E> partial(T1 alpha) {
        return new PartiallyAppliedAction<>(this, alpha);
    }

    @RequiredArgsConstructor
    class PartiallyAppliedAction<T1, T2, R, E extends Throwable> implements Action<T2, R, E> {
        private final BiAction<T1, T2, R, E> delegate;
        private final T1 alpha;

        @Override
        public R apply(T2 beta) throws E {
            return delegate.apply(alpha, beta);
        }
    }
}
