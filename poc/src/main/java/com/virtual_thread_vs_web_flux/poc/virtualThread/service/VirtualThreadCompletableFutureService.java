package com.virtual_thread_vs_web_flux.poc.virtualThread.service;

import com.virtual_thread_vs_web_flux.poc.common.model.response.*;
import com.virtual_thread_vs_web_flux.poc.common.service.ServiceLoggerAbstract;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.AgifyRepository;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.DogCeoRepository;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.JsonPlaceHolderRepository;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static com.virtual_thread_vs_web_flux.poc.virtualThread.util.VirtualThreadConstants.*;

@Service
public class VirtualThreadCompletableFutureService extends ServiceLoggerAbstract {
    private final static String CLASS_NAME = VirtualThreadCompletableFutureService.class.getName();

    private final JsonPlaceHolderRepository jsonPlaceHolderRepository;
    private final DogCeoRepository dogCeoRepository;
    private final AgifyRepository agifyRepository;

    public VirtualThreadCompletableFutureService(JsonPlaceHolderRepository jsonPlaceHolderRepository, DogCeoRepository dogCeoRepository, AgifyRepository agifyRepository) {
        this.jsonPlaceHolderRepository = jsonPlaceHolderRepository;
        this.dogCeoRepository = dogCeoRepository;
        this.agifyRepository = agifyRepository;
    }

    public VirtualThreadResponse get() {
        this.logInfo(CLASS_NAME, GET_METHOD, COMPLETABLE_FUTURE_MESSAGE);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CompletableFuture<AgifyResponse> completableFutureAgify = CompletableFuture.supplyAsync(() -> {
                try {
                    return this.agifyRepository.getApi();
                } catch (URISyntaxException exception) {
                    this.logError(CLASS_NAME, COMPLETABLE_FUTURE_AGIFY, ERROR_MESSAGE_COMPLETABLE_FUTURE, exception);

                    throw new RuntimeException(exception);
                }
            }, executor);

            CompletableFuture<JsonPlaceHolderResponse> completableFutureJsonPlaceHolder = CompletableFuture.supplyAsync(() -> {
                try {
                    return this.jsonPlaceHolderRepository.getApi();
                } catch (URISyntaxException exception) {
                    this.logError(CLASS_NAME, COMPLETABLE_FUTURE_JSON_PLACE_HOLDER, ERROR_MESSAGE_COMPLETABLE_FUTURE, exception);

                    throw new RuntimeException(exception);
                }
            }, executor);

            CompletableFuture<DogCeoResponse> completableFutureDogCeo = CompletableFuture.supplyAsync(() -> {
                try {
                    return this.dogCeoRepository.getApi();
                } catch (URISyntaxException exception) {
                    this.logError(CLASS_NAME, COMPLETABLE_FUTURE_DOG_CEO, ERROR_MESSAGE_COMPLETABLE_FUTURE, exception);

                    throw new RuntimeException(exception);
                }
            }, executor);

            CompletableFuture.allOf(completableFutureAgify, completableFutureJsonPlaceHolder, completableFutureDogCeo).join();

            AgifyResponse agifyResponse = completableFutureAgify.get();
            JsonPlaceHolderResponse jsonPlaceHolderResponse = completableFutureJsonPlaceHolder.get();
            DogCeoResponse dogCeoResponse = completableFutureDogCeo.get();

            return new VirtualThreadResponse(agifyResponse, jsonPlaceHolderResponse, dogCeoResponse);
        } catch (ExecutionException exception) {
            this.logError(CLASS_NAME, GET_METHOD, ERROR_MESSAGE_COMPLETABLE_FUTURE, exception);

            throw new RuntimeException(exception);
        } catch (InterruptedException exception){
            this.logError(CLASS_NAME, GET_METHOD, ERROR_MESSAGE_COMPLETABLE_FUTURE, exception);

            throw new RuntimeException(exception);
        }
    }
}
