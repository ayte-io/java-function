package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class AsyncTasks {
    private AsyncTasks() {}
    public static AsyncTask synchronous(Task<? extends Exception> task) {
        return new SynchronousTaskWrapper(task);
    }

    public static AsyncTask fromTaskAndExecutor(Task<? extends Exception> task, Executor executor) {
        return new ExecutorDelegatedTask(task, executor);
    }

    public static AsyncTask from(Task<? extends Exception> task, Executor executor) {
        return fromTaskAndExecutor(task, executor);
    }

    public static Task<Exception> toTask(AsyncTask task) {
        return new SynchronousExecution(task);
    }

    /**
     * Not using {@link Supplier} interface since it is not implied
     * that there could be any amount of calls made. {@link Callable}
     * is somewhat that is usually considered for probably being called
     * only once, something that {@link Task} implies as well.
     */
    @RequiredArgsConstructor
    private static class SyncTaskExecution implements Runnable, Callable<CompletableFuture<Void>> {
        private final Task<? extends Exception> task;
        private final CompletableFuture<Void> result;

        @Override
        public void run() {
            call();
        }

        @Override
        public CompletableFuture<Void> call() {
            try {
                task.execute();
                result.complete(null);
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
            return result;
        }
    }

    @RequiredArgsConstructor
    private static class SynchronousTaskWrapper implements AsyncTask {
        private final Task<? extends Exception> task;

        @Override
        public CompletableFuture<Void> execute() {
            return new SyncTaskExecution(task, new CompletableFuture<>()).call();
        }
    }

    @RequiredArgsConstructor
    private static class SynchronousExecution implements Task<Exception> {
        private final AsyncTask task;

        @Override
        public void execute() throws InterruptedException, ExecutionException {
            task.execute().get();
        }
    }

    @RequiredArgsConstructor
    private static class ExecutorDelegatedTask implements AsyncTask {
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
