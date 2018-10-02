package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

public class Tasks {
    private Tasks() {}

    public static Task<RuntimeException> fromRunnable(Runnable source) {
        return new RunnableTask(source);
    }

    public static Task<RuntimeException> from(Runnable source) {
        return fromRunnable(source);
    }

    @RequiredArgsConstructor
    private static class RunnableTask implements Task<RuntimeException> {
        private final Runnable runnable;

        @Override
        public void execute() {
            runnable.run();
        }
    }
}
