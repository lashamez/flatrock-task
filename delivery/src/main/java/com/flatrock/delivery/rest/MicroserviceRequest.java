package com.flatrock.delivery.rest;

import com.flatrock.common.model.OrderSellersData;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MicroserviceRequest {
    private final String orderServiceName = "ORDER-SERVICE";

    private final RestTemplate restTemplate;

    private final EurekaClient eurekaClient;

    private InstanceInfo instanceInfo;

    public MicroserviceRequest(RestTemplate restTemplate, EurekaClient eurekaClient) {
        this.restTemplate = restTemplate;
        this.eurekaClient = eurekaClient;
    }

    public OrderSellersData getSellerData(Long id){
        return restTemplate.getForObject(getOrderServiceUrl() + "order/api/order/{id}/seller", OrderSellersData.class, id);
    }

    public String getOrderServiceUrl() {
        if (instanceInfo == null) {
            synchronized (this) {
                instanceInfo = eurekaClient.getNextServerFromEureka(orderServiceName, false);
            }
        }
        return instanceInfo.getHomePageUrl();
    }
}
