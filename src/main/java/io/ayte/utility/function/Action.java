package io.ayte.utility.function;

/**
 * Just a function that may throw. So it's not a function actually,
 * since functions don't throw. Ugh.
 *
 * @see Actions
 *
 * @param <T>
 * @param <R>
 * @param <E>
 */
@FunctionalInterface
public interface Action<T, R, E extends Throwable> {
    R apply(T value) throws E;
}
