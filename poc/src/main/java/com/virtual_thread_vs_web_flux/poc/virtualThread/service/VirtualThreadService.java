package com.virtual_thread_vs_web_flux.poc.virtualThread.service;

import com.virtual_thread_vs_web_flux.poc.common.model.response.*;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.AgifyRepository;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.DogCeoRepository;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.JsonPlaceHolderRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class VirtualThreadService {
    private final JsonPlaceHolderRepository jsonPlaceHolderRepository;
    private final DogCeoRepository dogCeoRepository;
    private final AgifyRepository agifyRepository;

    public VirtualThreadService(JsonPlaceHolderRepository jsonPlaceHolderRepository, DogCeoRepository dogCeoRepository, AgifyRepository agifyRepository) {
        this.jsonPlaceHolderRepository = jsonPlaceHolderRepository;
        this.dogCeoRepository = dogCeoRepository;
        this.agifyRepository = agifyRepository;
    }

    public VirtualThreadResponse get() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<ResponseCommon> agifyResponseFuture = executor.submit(this.agifyRepository::getApi);
            Future<ResponseCommon> jsonPlaceHolderResponseFuture = executor.submit(this.jsonPlaceHolderRepository::getApi);
            Future<ResponseCommon> dogCeoResponseFuture = executor.submit(this.dogCeoRepository::getApi);

            AgifyResponse agifyResponse = (AgifyResponse) agifyResponseFuture.get();
            JsonPlaceHolderResponse jsonPlaceHolderResponse = (JsonPlaceHolderResponse) jsonPlaceHolderResponseFuture.get();
            DogCeoResponse dogCeoResponse = (DogCeoResponse) dogCeoResponseFuture.get();

            return new VirtualThreadResponse(agifyResponse, jsonPlaceHolderResponse, dogCeoResponse);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
