package io.ayte.utility.function;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface AsyncTask {
    CompletableFuture<Void> execute();
}
