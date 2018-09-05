package io.ayte.utility.function;

/**
 * @see Action
 *
 * @param <T1>
 * @param <T2>
 * @param <R>
 * @param <E>
 */
@FunctionalInterface
public interface BiAction<T1, T2, R, E extends Throwable> {
    R apply(T1 alpha, T2 beta) throws E;
}
