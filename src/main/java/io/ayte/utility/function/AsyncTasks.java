package io.ayte.utility.function;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;

public class AsyncTasks {
    public static final AsyncTask EMPTY = EmptyTask.INSTANCE;

    private AsyncTasks() {}

    // not implementing as from() to emphasize that task will be
    // executed synchronously
    public static AsyncTask synchronous(Task<? extends Exception> task) {
        return new SynchronousTaskWrapper(task);
    }

    // not implementing as from() to emphasize that common JVM pool
    // will be used for execution and there's no magic
    public static AsyncTask fromTaskAndCommonPool(Task<? extends Exception> task) {
        return fromTaskAndExecutor(task, ForkJoinPool.commonPool());
    }

    public static AsyncTask fromTaskAndExecutor(Task<? extends Exception> task, Executor executor) {
        return new ExecutorDelegatedTask(task, executor);
    }

    public static AsyncTask from(Task<? extends Exception> task, Executor executor) {
        return fromTaskAndExecutor(task, executor);
    }

    public static Task<Exception> toTask(AsyncTask task) {
        return new UniSynchronousExecution(task);
    }

    public static Task<Exception> toTask(AsyncTask... tasks) {
        return toTask(Arrays.asList(tasks));
    }

    public static Task<Exception> toTask(Collection<AsyncTask> tasks) {
        // not using single fits-for-anything class since not sure it
        // would be optimized by compiler as good as single classes,
        // and incorporating those if-conditions in this method should
        // not get such performance penalty
        // TODO: statement above is subject for verification using JMH
        switch (tasks.size()) {
            case 0:
                return Tasks.empty();
            case 1:
                return new UniSynchronousExecution(tasks.iterator().next());
            default:
                return new MultiSynchronousExecution(tasks);
        }
    }

    @RequiredArgsConstructor
    private static class UniSynchronousExecution implements Task<Exception> {
        private final AsyncTask delegate;

        @Override
        public void execute() throws InterruptedException, ExecutionException {
            delegate.execute().get();
        }
    }

    @RequiredArgsConstructor
    private static class MultiSynchronousExecution implements Task<Exception> {
        private final Collection<AsyncTask> tasks;

        @Override
        public void execute() throws InterruptedException, ExecutionException {
            val futures = tasks.stream().map(AsyncTask::execute).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).get();
        }
    }

    /**
     * Not using {@link Supplier} interface since it is not implied
     * that there could be any amount of calls made. {@link Callable}
     * is somewhat that is usually considered for probably being called
     * only once, something that {@link Task} implies as well.
     */
    @RequiredArgsConstructor
    private static class SynchronousTaskExecution implements Runnable, Callable<CompletableFuture<Void>> {
        private final Task<? extends Exception> task;
        private final CompletableFuture<Void> result;

        @Override
        public void run() {
            if (result.isDone()) {
                throw new IllegalStateException("This task has already been run");
            }
            try {
                task.execute();
                result.complete(null);
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
        }

        @Override
        public CompletableFuture<Void> call() {
            run();
            return result;
        }
    }

    @RequiredArgsConstructor
    private static class SynchronousTaskWrapper implements AsyncTask {
        private final Task<? extends Exception> task;

        @Override
        public CompletableFuture<Void> execute() {
            return new SynchronousTaskExecution(task, new CompletableFuture<>()).call();
        }
    }

    @RequiredArgsConstructor
    private static class ExecutorDelegatedTask implements AsyncTask {
        private final Task<? extends Exception> task;
        private final Executor executor;

        @Override
        public CompletableFuture<Void> execute() {
            val synchronizer = new CompletableFuture<Void>();
            executor.execute(new SynchronousTaskExecution(task, synchronizer));
            return synchronizer;
        }
    }

    private static class EmptyTask implements AsyncTask {
        public static final EmptyTask INSTANCE = new EmptyTask();

        @Override
        public CompletableFuture<Void> execute() {
            return CompletableFuture.completedFuture(null);
        }
    }
}
