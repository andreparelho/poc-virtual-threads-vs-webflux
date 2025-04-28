package com.virtual_thread_vs_web_flux.poc.virtualThread.config;

import java.util.concurrent.*;
import java.util.function.Supplier;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class PersonalizedCompletableFuture<T> extends CompletableFuture<T> {

    private final RequestAttributes actualContext;

    private static final ExecutorService VIRTUAL_THREAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    public PersonalizedCompletableFuture() {
        this.actualContext = RequestContextHolder.getRequestAttributes();
    }

    public static <U> CompletableFuture<U> runAsync(Supplier<U> supplier) {
        PersonalizedCompletableFuture<U> future = new PersonalizedCompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(future.actualContext);
            try {
                future.complete(supplier.get());
            } catch (Throwable ex) {
                future.completeExceptionally(ex);
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }, VIRTUAL_THREAD_EXECUTOR);

        return future;
    }
}
