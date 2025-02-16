package com.virtual_thread_vs_web_flux.poc.virtualThread.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtual_thread_vs_web_flux.poc.common.model.response.DogCeoResponse;
import com.virtual_thread_vs_web_flux.poc.common.model.response.JsonPlaceHolderResponse;
import com.virtual_thread_vs_web_flux.poc.common.model.response.ResponseCommon;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Repository
public class DogCeoRepository extends ApiAbstractClass implements ApiRepository {
    protected DogCeoRepository(HttpClient client, ObjectMapper objectMapper) {
        super(client, objectMapper);
    }

    @Override
    public ResponseCommon getApi() throws URISyntaxException {
       try {
           HttpRequest.Builder builder = HttpRequest.newBuilder();
           builder.uri(new URI("https://dog.ceo/api/breeds/image/random"));
           builder.GET();
           HttpRequest request = builder
                   .build();

           HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

           DogCeoResponse dogCeoResponse = this.objectMapper.readValue(response.body(), DogCeoResponse.class);
           return dogCeoResponse;
       } catch (Exception exception) {
          throw new RuntimeException();
       }
    }
}
