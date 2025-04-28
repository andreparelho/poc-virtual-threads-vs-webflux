package com.virtual_thread_vs_web_flux.poc.virtualThread.service;

import com.virtual_thread_vs_web_flux.poc.common.model.response.AgifyResponse;
import com.virtual_thread_vs_web_flux.poc.common.model.response.JsonPlaceHolderResponse;
import com.virtual_thread_vs_web_flux.poc.common.model.response.VirtualThreadResponse;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.AgifyRepository;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.DogCeoRepository;
import com.virtual_thread_vs_web_flux.poc.virtualThread.repository.JsonPlaceHolderRepository;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VirtualThreadCompletableFutureServiceTest {

    @InjectMocks
    private VirtualThreadCompletableFutureService service;

    @Mock
    private AgifyRepository agifyRepository;

    @Mock
    private JsonPlaceHolderRepository jsonPlaceHolderRepository;

    @Mock
    private Retry retry;

    @BeforeEach
    public void t() {
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(ofMillis(100))
                .build();

        retry = Retry.of("testRetry", retryConfig);

        service = new VirtualThreadCompletableFutureService(jsonPlaceHolderRepository, null, agifyRepository, retry);
    }

    @Test
    void testExecutor_Success() throws URISyntaxException {
        AgifyResponse agifyResponse = mock(AgifyResponse.class);
        JsonPlaceHolderResponse jsonPlaceHolderResponse = mock(JsonPlaceHolderResponse.class);
        when(agifyRepository.getApi()).thenReturn(agifyResponse);
        when(jsonPlaceHolderRepository.getApi()).thenReturn(jsonPlaceHolderResponse);

        VirtualThreadResponse response = service.executor();

        assertNotNull(response);
        assertEquals(agifyResponse, response.agifyResponse());
        assertEquals(jsonPlaceHolderResponse, response.jsonPlaceHolderResponse());
    }


    @Test
    void testRetryMechanism() throws URISyntaxException {
        AgifyResponse agifyResponse = mock(AgifyResponse.class);
        JsonPlaceHolderResponse jsonPlaceHolderResponse = mock(JsonPlaceHolderResponse.class);

        when(agifyRepository.getApi())
                .thenThrow(new URISyntaxException("Invalid URL", "url"))
                .thenThrow(new URISyntaxException("Invalid URL", "url"))
                .thenReturn(agifyResponse); // Sucesso após falha
        when(jsonPlaceHolderRepository.getApi()).thenReturn(jsonPlaceHolderResponse);

        VirtualThreadResponse response = service.executor();

        assertNotNull(response);
        assertEquals(agifyResponse, response.agifyResponse());
        assertEquals(jsonPlaceHolderResponse, response.jsonPlaceHolderResponse());
        verify(agifyRepository, times(3)).getApi();
    }

}
