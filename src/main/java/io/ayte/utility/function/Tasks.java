package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class Tasks {
    private Tasks() {}

    public static Task<RuntimeException> fromRunnable(Runnable source) {
        return new RunnableTask(source);
    }

    public static Task<RuntimeException> from(Runnable source) {
        return fromRunnable(source);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> Task<E> empty() {
        return (Task<E>) EmptyTask.INSTANCE;
    }

    /**
     * Wraps task in a protective barrier that allows task to be
     * invoked only once.
     *
     * Task class itself doesn't restrict number of invocations, but
     * implies that task should be invoked only once. This method
     * allows to ensure that task would only be called once.
     *
     * Passing such a wrapper <b>does not</b> add additional wrapper.
     */
    public static <E extends Throwable> Task<E> singleInvocation(Task<E> task) {
        return task instanceof SingleRun ? task : new SingleRun<>(task);
    }

    @RequiredArgsConstructor
    private static class RunnableTask implements Task<RuntimeException> {
        private final Runnable runnable;

        @Override
        public void execute() {
            runnable.run();
        }
    }

    public static Callable<Void> toCallable(Task<? extends Exception> task) {
        return new TaskCallable(task);
    }

    @RequiredArgsConstructor
    private static class TaskCallable implements Callable<Void> {
        private final Task<? extends Exception> task;

        @Override
        public Void call() throws Exception {
            task.execute();
            return null;
        }
    }

    private static class EmptyTask implements Task<RuntimeException> {
        public static final EmptyTask INSTANCE = new EmptyTask();

        @SuppressWarnings("squid:S1186")
        @Override
        public void execute() {}

        @Override
        public String toString() {
            return EmptyTask.class.getSimpleName();
        }
    }

    @RequiredArgsConstructor
    private static class SingleRun<E extends Throwable> implements Task<E> {
        private final AtomicBoolean launched = new AtomicBoolean(false);

        private final Task<E> delegate;

        @Override
        public void execute() throws E {
            if (!launched.compareAndSet(false, true)) {
                throw new IllegalStateException("This task has already been run");
            }
            delegate.execute();
        }
    }
}
