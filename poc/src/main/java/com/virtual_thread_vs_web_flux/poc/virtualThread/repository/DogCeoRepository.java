package com.virtual_thread_vs_web_flux.poc.virtualThread.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtual_thread_vs_web_flux.poc.common.model.response.DogCeoResponse;
import com.virtual_thread_vs_web_flux.poc.common.repository.ApiAbstract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.virtual_thread_vs_web_flux.poc.virtualThread.util.VirtualThreadConstants.DOG_CEO_HOLDER_ENDPOINT;

@Repository
public class DogCeoRepository extends ApiAbstract {
    @Value("${api.url.DOG_CEO_API_URL}")
    private String dogCeoApiUri;

    protected DogCeoRepository(HttpClient client, ObjectMapper objectMapper) {
        super(client, objectMapper);
    }

    public DogCeoResponse getApi() throws URISyntaxException {
       try {
           HttpRequest request = HttpRequest.newBuilder()
                   .uri(new URI(dogCeoApiUri + DOG_CEO_HOLDER_ENDPOINT))
                   .GET()
                   .build();

           HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

           return this.objectMapper.readValue(response.body(), DogCeoResponse.class);
       } catch (Exception exception) {

          throw new RuntimeException();
       }
    }
}
