package com.flatrock.order.rest;

import com.flatrock.common.model.OrderItemDto;
import com.flatrock.common.model.OrderSellersData;
import com.flatrock.common.model.ProductAvailabilityRequest;
import com.flatrock.common.model.ProductAvailabilityResponse;
import com.netflix.discovery.EurekaClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class MicroserviceRequest {
    //    @Value("${application.services.product}")
    private String productServiceBaseUrl;

    private final RestTemplate restTemplate;

    public MicroserviceRequest(RestTemplate restTemplate, EurekaClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.productServiceBaseUrl = discoveryClient.getApplication("PRODUCT_SERVICE").getInstances().get(0).getHomePageUrl();
    }

    public ResponseEntity<OrderSellersData> getSellerData(List<OrderItemDto> orderItemDtos) {
        HttpEntity<List<OrderItemDto>> entity = new HttpEntity<>(orderItemDtos);
        return restTemplate.exchange(productServiceBaseUrl + "api/stocks/seller", HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
        });
    }

    public ResponseEntity<List<ProductAvailabilityResponse>> validateProduct(List<ProductAvailabilityRequest> requests) {
        HttpEntity<List<ProductAvailabilityRequest>> entity = new HttpEntity<>(requests);
        return restTemplate.exchange(productServiceBaseUrl + "api/stocks/check", HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
        });

    }


}
