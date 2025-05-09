package com.virtual_thread_vs_web_flux.poc.virtualThread.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtual_thread_vs_web_flux.poc.common.model.response.AgifyResponse;
import com.virtual_thread_vs_web_flux.poc.common.repository.ApiAbstract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.virtual_thread_vs_web_flux.poc.virtualThread.util.VirtualThreadConstants.AGIFY_HOLDER_ENDPOINT;

@Repository
public class AgifyRepository extends ApiAbstract {
    private static final String agifyApiUrl = "https://api.agify.io";

    protected AgifyRepository(HttpClient client, ObjectMapper objectMapper) {
        super(client, objectMapper);
    }

    public AgifyResponse getApi() throws URISyntaxException {
       try {
           HttpRequest request = HttpRequest.newBuilder()
                   .uri(new URI(agifyApiUrl + AGIFY_HOLDER_ENDPOINT))
                   .GET()
                   .build();

           HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

           return this.objectMapper.readValue(response.body(), AgifyResponse.class);
       } catch (Exception exception) {

          throw new RuntimeException();
       }
    }
}
