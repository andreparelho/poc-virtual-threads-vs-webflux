package com.virtual_thread_vs_web_flux.poc.virtualThread.service;

import com.virtual_thread_vs_web_flux.poc.common.model.response.*;
import com.virtual_thread_vs_web_flux.poc.common.service.ServiceLoggerAbstract;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.AgifyRepository;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.DogCeoRepository;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.JsonPlaceHolderRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.virtual_thread_vs_web_flux.poc.virtualThread.util.VirtualThreadConstants.*;

@Service
public class VirtualThreadFutureService extends ServiceLoggerAbstract {
    private final static String CLASS_NAME = VirtualThreadFutureService.class.getName();

    private final JsonPlaceHolderRepository jsonPlaceHolderRepository;
    private final DogCeoRepository dogCeoRepository;
    private final AgifyRepository agifyRepository;

    public VirtualThreadFutureService(JsonPlaceHolderRepository jsonPlaceHolderRepository, DogCeoRepository dogCeoRepository, AgifyRepository agifyRepository) {
        this.jsonPlaceHolderRepository = jsonPlaceHolderRepository;
        this.dogCeoRepository = dogCeoRepository;
        this.agifyRepository = agifyRepository;
    }

    public VirtualThreadResponse get() {
        this.logInfo(CLASS_NAME, GET_METHOD, FUTURE_MESSAGE);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<AgifyResponse> agifyResponseFuture = executor.submit(this.agifyRepository::getApi);
            Future<JsonPlaceHolderResponse> jsonPlaceHolderResponseFuture = executor.submit(this.jsonPlaceHolderRepository::getApi);
            Future<DogCeoResponse> dogCeoResponseFuture = executor.submit(this.dogCeoRepository::getApi);

            AgifyResponse agifyResponse = agifyResponseFuture.get();
            JsonPlaceHolderResponse jsonPlaceHolderResponse = jsonPlaceHolderResponseFuture.get();
            DogCeoResponse dogCeoResponse = dogCeoResponseFuture.get();

            return new VirtualThreadResponse(agifyResponse, jsonPlaceHolderResponse, dogCeoResponse);
        } catch (ExecutionException exception) {
            this.logError(CLASS_NAME, GET_METHOD, ERROR_MESSAGE_FUTURE, exception);

            throw new RuntimeException(exception);
        } catch (InterruptedException exception) {
            this.logError(CLASS_NAME, GET_METHOD, ERROR_MESSAGE_FUTURE, exception);

            throw new RuntimeException(exception);
        }
    }
}
