package com.virtual_thread_vs_web_flux.poc.virtualThread.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtual_thread_vs_web_flux.poc.common.model.response.AgifyResponse;
import com.virtual_thread_vs_web_flux.poc.common.model.response.ResponseCommon;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Repository
public class AgifyRepository extends ApiAbstractClass implements ApiRepository {
    protected AgifyRepository(HttpClient client, ObjectMapper objectMapper) {
        super(client, objectMapper);
    }

    @Override
    public ResponseCommon getApi() throws URISyntaxException {
       try {
           HttpRequest request = HttpRequest.newBuilder()
                   .uri(new URI("https://api.agify.io?name=michael"))
                   .GET()
                   .build();

           HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

           AgifyResponse agifyResponse = this.objectMapper.readValue(response.body(), AgifyResponse.class);

           return agifyResponse;
       } catch (Exception exception) {
          throw new RuntimeException();
       }
    }
}
