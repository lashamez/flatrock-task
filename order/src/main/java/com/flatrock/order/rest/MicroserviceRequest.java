package com.flatrock.order.rest;

import com.flatrock.common.model.OrderItemDto;
import com.flatrock.common.model.OrderSellersData;
import com.flatrock.common.model.ProductAvailabilityRequest;
import com.flatrock.common.model.ProductAvailabilityResponse;
import com.netflix.appinfo.InstanceInfo;
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
    private final RestTemplate restTemplate;
    private final EurekaClient discoveryClient;

    public MicroserviceRequest(RestTemplate restTemplate, EurekaClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    public ResponseEntity<OrderSellersData> getSellerData(List<OrderItemDto> orderItemDtos) {
        HttpEntity<List<OrderItemDto>> entity = new HttpEntity<>(orderItemDtos);
        return restTemplate.exchange(getProductServiceUrl() + "api/stocks/seller", HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
        });
    }

    public ResponseEntity<List<ProductAvailabilityResponse>> validateProduct(List<ProductAvailabilityRequest> requests) {
        HttpEntity<List<ProductAvailabilityRequest>> entity = new HttpEntity<>(requests);
        return restTemplate.exchange(getProductServiceUrl() + "api/stocks/check", HttpMethod.POST, entity, new ParameterizedTypeReference<>() {
        });
    }

    public String getProductServiceUrl() {
        InstanceInfo instanceInfo = discoveryClient.getNextServerFromEureka("PRODUCT_SERVICE", false);
        return instanceInfo.getHomePageUrl();
    }

}
