package com.flatrock.common.application;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;


public class RestTemplateConfiguration implements ClientHttpRequestInterceptor {
    private final String origin;

    public RestTemplateConfiguration(String origin) {
        this.origin = origin;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (!request.getHeaders().containsKey(HttpHeaders.ORIGIN)) {
            HttpHeaders headers = request.getHeaders();
            headers.add(HttpHeaders.ORIGIN, origin);
        }
        return execution.execute(request, body);
    }
}
