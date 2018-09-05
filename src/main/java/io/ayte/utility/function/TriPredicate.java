package io.ayte.utility.function;

import java.util.Objects;
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
}
