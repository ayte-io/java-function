package io.ayte.utility.function;

@FunctionalInterface
public interface Action<T, R, E extends Throwable> {
    R apply(T value) throws E;
}
