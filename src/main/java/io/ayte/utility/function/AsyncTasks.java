package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AsyncTasks {
    private AsyncTasks() {}
    public static AsyncTask sync(Task<? extends Exception> task) {
        return new SyncTaskWrapper(task);
    }

    public static AsyncTask fromTaskAndExecutor(Task<? extends Exception> task, Executor executor) {
        return new ExecutedSyncTask(task, executor);
    }

    public static AsyncTask from(Task<? extends Exception> task, Executor executor) {
        return fromTaskAndExecutor(task, executor);
    }

    @RequiredArgsConstructor
    private static class SyncTaskExecution implements Runnable {
        private final Task<? extends Exception> task;
        private final CompletableFuture<Void> result;

        @Override
        public void run() {
            try {
                task.execute();
                result.complete(null);
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
        }
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

    @RequiredArgsConstructor
    private static class ExecutedSyncTask implements AsyncTask {
        private final Task<? extends Exception> task;
        private final Executor executor;

        @Override
        public CompletableFuture<Void> execute() {
            CompletableFuture<Void> synchronizer = new CompletableFuture<>();
            executor.execute(new SyncTaskExecution(task, synchronizer));
            return synchronizer;
        }
    }
}
