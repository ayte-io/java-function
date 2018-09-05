package io.ayte.utility.function;

import java.util.function.Function;

/**
 * @see Function
 *
 * @param <T1>
 * @param <T2>
 * @param <T3>
 * @param <R>
 */
@FunctionalInterface
public interface TriFunction<T1, T2, T3, R> {
    R apply(T1 alpha, T2 beta, T3 gamma);
}
