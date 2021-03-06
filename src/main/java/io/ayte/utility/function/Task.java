package io.ayte.utility.function;

/**
 * Pretty much {@link Runnable} but with throw support. Acts same as
 * runnable in raw form.
 *
 * @param <E>
 */
@FunctionalInterface
public interface Task<E extends Throwable> {
    @SuppressWarnings("squid:S00112")
    void execute() throws E;

    @SuppressWarnings("unused")
    static Task<Throwable> from(Runnable runnable) {
        return runnable::run;
    }
}
