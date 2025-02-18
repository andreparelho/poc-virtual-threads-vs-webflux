package com.virtual_thread_vs_web_flux.poc.virtualThread.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtual_thread_vs_web_flux.poc.common.model.response.JsonPlaceHolderResponse;
import com.virtual_thread_vs_web_flux.poc.common.repository.ApiAbstract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.virtual_thread_vs_web_flux.poc.virtualThread.util.VirtualThreadConstants.*;

@Repository
public class JsonPlaceHolderRepository extends ApiAbstract {
    @Value("${api.url.JSON_PLACE_HOLDER_API_URL}")
    private String jsonApiUrl;

    protected JsonPlaceHolderRepository(HttpClient client, ObjectMapper objectMapper) {
        super(client, objectMapper);
    }

    public JsonPlaceHolderResponse getApi() throws URISyntaxException {
       try {

           HttpRequest request = HttpRequest.newBuilder()
                   .uri(new URI(jsonApiUrl + JSON_PLACE_HOLDER_ENDPOINT))
                   .GET()
                   .build();

           HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

           return this.objectMapper.readValue(response.body(), JsonPlaceHolderResponse.class);
       } catch (Exception exception) {

          throw new RuntimeException();
       }
    }
}
