package com.flatrock.delivery.rest;

import com.flatrock.common.model.OrderSellersData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MicroserviceRequest {
    @Value("${application.services.order}")
    private String orderServiceBaseUrl;

    private final RestTemplate restTemplate;

    public MicroserviceRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OrderSellersData getSellerData(Long id){
        return restTemplate.getForObject(orderServiceBaseUrl + "/api/order/{id}/seller", OrderSellersData.class, id);
    }
}
