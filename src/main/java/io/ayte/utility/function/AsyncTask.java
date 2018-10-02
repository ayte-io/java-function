package io.ayte.utility.function;

import java.util.concurrent.CompletableFuture;

/**
 * @see AsyncTasks
 */
@FunctionalInterface
public interface AsyncTask {
    CompletableFuture<Void> execute();
}
