package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * @see Predicate
 *
 * @param <T1>
 * @param <T2>
 * @param <T3>
 */
@FunctionalInterface
public interface TriPredicate<T1, T2, T3> {
    boolean test(T1 alpha, T2 beta, T3 gamma);

    default TriPredicate<T1, T2, T3> negate() {
        return (alpha, beta, gamma) -> !test(alpha, beta, gamma);
    }

    default TriPredicate<T1, T2, T3> and(TriPredicate<? super T1, ? super T2, ? super T3> other) {
        Objects.requireNonNull(other);
        return (alpha, beta, gamma) -> test(alpha, beta, gamma) && other.test(alpha, beta, gamma);
    }

    default TriPredicate<T1, T2, T3> or(TriPredicate<? super T1, ? super T2, ? super T3> other) {
        Objects.requireNonNull(other);
        return (alpha, beta, gamma) -> test(alpha, beta, gamma) || other.test(alpha, beta, gamma);
    }

    default BiPredicate<T2, T3> partial(T1 alpha) {
        return new PartiallyAppliedBiPredicate<>(this, alpha);
    }

    default Predicate<T3> partial(T1 alpha, T2 beta) {
        return new PartiallyAppliedPredicate<>(this, alpha, beta);
    }

    @RequiredArgsConstructor
    class PartiallyAppliedBiPredicate<T1, T2, T3> implements BiPredicate<T2, T3> {
        private final TriPredicate<T1, T2, T3> delegate;
        private final T1 alpha;

        @Override
        public boolean test(T2 beta, T3 gamma) {
            return delegate.test(alpha, beta, gamma);
        }
    }

    @RequiredArgsConstructor
    class PartiallyAppliedPredicate<T1, T2, T3> implements Predicate<T3> {
        private final TriPredicate<T1, T2, T3> delegate;
        private final T1 alpha;
        private final T2 beta;

        @Override
        public boolean test(T3 gamma) {
            return delegate.test(alpha, beta, gamma);
        }
    }
}
