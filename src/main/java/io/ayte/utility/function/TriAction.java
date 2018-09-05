package io.ayte.utility.function;

/**
 * @see Action
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
}
