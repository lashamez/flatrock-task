package com.flatrock.order.rest;

import com.flatrock.common.model.OrderItemDto;
import com.flatrock.common.model.OrderSellersData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class MicroserviceRequest {
    @Value("${application.services.product}")
    private String productServiceBaseUrl;

    private final RestTemplate restTemplate;

    public MicroserviceRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<OrderSellersData> getSellerData(List<OrderItemDto> orderItemDtos){
        HttpEntity<List<OrderItemDto>> entity = new HttpEntity<>(orderItemDtos);
        ResponseEntity<OrderSellersData> response = restTemplate.exchange(productServiceBaseUrl + "api/stocks/seller", HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});
        return response;
    }
}
