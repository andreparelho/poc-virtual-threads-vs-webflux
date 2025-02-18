package com.virtual_thread_vs_web_flux.poc.common.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;

import static com.virtual_thread_vs_web_flux.poc.common.util.LogConstants.*;


public abstract class ApiAbstract {
    protected final static Logger logger = LoggerFactory.getLogger(ApiAbstract.class);
    protected final HttpClient client;
    protected final ObjectMapper objectMapper;

    protected ApiAbstract(HttpClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    protected void logInfo(String className, String method, String message) {
        logger.atInfo()
                .addKeyValue(CLASS, className)
                .addKeyValue(METHOD, method)
                .addKeyValue(MESSAGE, message)
                .log();
    }

    protected void logError(String className, String method, String message, Exception exception) {
        logger.atError()
                .addKeyValue(CLASS, className)
                .addKeyValue(METHOD, method)
                .addKeyValue(MESSAGE, message)
                .addKeyValue(STACK_TRACE, exception.getStackTrace())
                .setMessage(exception.getMessage())
                .log();
    }
}
