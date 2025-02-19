package com.virtual_thread_vs_web_flux.poc.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServiceLoggerAbstract {
    protected final static Logger logger = LoggerFactory.getLogger(ServiceLoggerAbstract.class);

    protected void logInfo(String className, String method, String message) {
        logger.atInfo()
                .addKeyValue("CLASS", className)
                .addKeyValue("METHOD", method)
                .addKeyValue("MESSAGE", message)
                .log();
    }

    protected void logError(String className, String method, String message, Exception exception) {
        logger.atError()
                .addKeyValue("CLASS", className)
                .addKeyValue("METHOD", method)
                .addKeyValue("MESSAGE", message)
                .addKeyValue("STACK_TRACE", exception.getStackTrace())
                .setMessage(exception.getMessage())
                .log();
    }

    protected void logError(String className, String method, String message, Throwable throwable) {
        logger.atError()
                .addKeyValue("CLASS", className)
                .addKeyValue("METHOD", method)
                .addKeyValue("MESSAGE", message)
                .addKeyValue("STACK_TRACE", throwable.getStackTrace())
                .setMessage(throwable.getMessage())
                .log();
    }
}
