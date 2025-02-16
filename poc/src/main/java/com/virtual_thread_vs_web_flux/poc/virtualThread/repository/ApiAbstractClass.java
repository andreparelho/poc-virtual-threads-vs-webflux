package com.virtual_thread_vs_web_flux.poc.virtualThread.repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;

public abstract class ApiAbstractClass {
    protected final HttpClient client;
    protected final ObjectMapper objectMapper;

    protected ApiAbstractClass(HttpClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }
}
