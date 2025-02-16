package com.virtual_thread_vs_web_flux.poc.virtualThread.repository;


import com.virtual_thread_vs_web_flux.poc.common.model.response.ResponseCommon;

import java.net.URISyntaxException;

public interface ApiRepository {
    ResponseCommon getApi() throws URISyntaxException;
}
