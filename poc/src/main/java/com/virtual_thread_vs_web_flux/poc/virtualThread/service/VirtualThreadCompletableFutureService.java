package com.virtual_thread_vs_web_flux.poc.virtualThread.service;

import com.virtual_thread_vs_web_flux.poc.common.model.response.*;
import com.virtual_thread_vs_web_flux.poc.common.service.ServiceLoggerAbstract;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.AgifyRepository;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.DogCeoRepository;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.JsonPlaceHolderRepository;
import io.github.resilience4j.retry.Retry;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static com.virtual_thread_vs_web_flux.poc.virtualThread.util.VirtualThreadConstants.*;

@Service
public class VirtualThreadCompletableFutureService extends ServiceLoggerAbstract {
    private final static String CLASS_NAME = VirtualThreadCompletableFutureService.class.getName();

    private final JsonPlaceHolderRepository jsonPlaceHolderRepository;
    private final DogCeoRepository dogCeoRepository;
    private final AgifyRepository agifyRepository;
    private final Retry executorRetry;

    public VirtualThreadCompletableFutureService(JsonPlaceHolderRepository jsonPlaceHolderRepository, DogCeoRepository dogCeoRepository, AgifyRepository agifyRepository, Retry executorRetry) {
        this.jsonPlaceHolderRepository = jsonPlaceHolderRepository;
        this.dogCeoRepository = dogCeoRepository;
        this.agifyRepository = agifyRepository;
        this.executorRetry = executorRetry;
    }

    public VirtualThreadResponse executor() {
        return get();
    }

    public VirtualThreadResponse get() {
        this.logInfo(CLASS_NAME, GET_METHOD, COMPLETABLE_FUTURE_MESSAGE);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CompletableFuture<AgifyResponse> completableFutureAgify = CompletableFuture.supplyAsync(() -> {
                try {
                    return Retry.decorateCheckedSupplier(executorRetry, this.agifyRepository::getApi).get();
                } catch (URISyntaxException exception) {
                    this.logError(CLASS_NAME, COMPLETABLE_FUTURE_AGIFY, ERROR_MESSAGE_COMPLETABLE_FUTURE, exception);

                    throw new RuntimeException(exception);
                } catch (Throwable e) {
                    if (e instanceof URISyntaxException uriSyntaxException) {
                        logError(CLASS_NAME, COMPLETABLE_FUTURE_AGIFY, ERROR_MESSAGE_COMPLETABLE_FUTURE, uriSyntaxException);
                    }
                    throw new RuntimeException(e);
                }
            }, executor);

            CompletableFuture<JsonPlaceHolderResponse> completableFutureJsonPlaceHolder = CompletableFuture.supplyAsync(() -> {
                try {
                    return Retry.decorateCheckedSupplier(executorRetry, this.jsonPlaceHolderRepository::getApi).get();
                } catch (URISyntaxException exception) {
                    this.logError(CLASS_NAME, COMPLETABLE_FUTURE_JSON_PLACE_HOLDER, ERROR_MESSAGE_COMPLETABLE_FUTURE, exception);

                    throw new RuntimeException(exception);
                } catch (Throwable e) {
                    if (e instanceof URISyntaxException uriSyntaxException) {
                        logError(CLASS_NAME, COMPLETABLE_FUTURE_AGIFY, ERROR_MESSAGE_COMPLETABLE_FUTURE, uriSyntaxException);
                    }
                    throw new RuntimeException(e);
                }
            }, executor);

            return completableFutureAgify
                    .thenCombine(completableFutureJsonPlaceHolder, (agify, jsonPlaceHolder) -> new VirtualThreadResponse(agify, jsonPlaceHolder, null))
                    .join();
        }
    }
}
