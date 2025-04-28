package com.virtual_thread_vs_web_flux.poc.virtualThread.controller;

import com.virtual_thread_vs_web_flux.poc.common.model.response.VirtualThreadResponse;
import com.virtual_thread_vs_web_flux.poc.virtualThread.service.VirtualThreadCompletableFutureService;
import com.virtual_thread_vs_web_flux.poc.virtualThread.service.VirtualThreadFutureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/virtual-thread-future")
public class VirtualThreadFutureController {
    private final VirtualThreadFutureService service;

    public VirtualThreadFutureController(VirtualThreadFutureService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<VirtualThreadResponse> get() throws URISyntaxException, IOException, InterruptedException {
        VirtualThreadResponse response = this.service.get();
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
