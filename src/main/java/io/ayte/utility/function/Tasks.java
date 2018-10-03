package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;

public class Tasks {
    public static final Task<RuntimeException> EMPTY_TASK = new EmptyTask();

    private Tasks() {}

    public static Task<RuntimeException> fromRunnable(Runnable source) {
        return new RunnableTask(source);
    }

    public static Task<RuntimeException> from(Runnable source) {
        return fromRunnable(source);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> Task<E> empty() {
        return (Task<E>) EMPTY_TASK;
    }

    public static Callable<Void> toCallable(Task<? extends Exception> task) {
        return new TaskCallable(task);
    }

    @RequiredArgsConstructor
    private static class RunnableTask implements Task<RuntimeException> {
        private final Runnable runnable;

        @Override
        public void execute() {
            runnable.run();
        }
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
        @Override
        @SuppressWarnings("squid:S1186")
        public void execute() {}
    }
}
