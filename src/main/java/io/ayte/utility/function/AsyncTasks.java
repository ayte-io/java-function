package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

public class AsyncTasks {
    private AsyncTasks() {}

    public static AsyncTask fromSync(Task<? extends Exception> task) {
        return new SyncTaskWrapper(task);
    }

    public static AsyncTask from(Task<? extends Exception> task) {
        return fromSync(task);
    }

    @RequiredArgsConstructor
    private static class SyncTaskWrapper implements AsyncTask {
        private final Task<? extends Exception> task;

        @Override
        public CompletableFuture<Void> execute() {
            try {
                task.execute();
                return CompletableFuture.completedFuture(null);
            } catch (Exception e) {
                CompletableFuture<Void> future = new CompletableFuture<>();
                future.completeExceptionally(e);
                return future;
            }
        }
    }
}
