package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;

public class Tasks {
    private Tasks() {}

    public static Task<RuntimeException> fromRunnable(Runnable source) {
        return new RunnableTask(source);
    }

    public static Task<RuntimeException> from(Runnable source) {
        return fromRunnable(source);
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
}
