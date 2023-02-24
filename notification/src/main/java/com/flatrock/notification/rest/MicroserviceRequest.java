package com.flatrock.notification.rest;

import com.flatrock.common.model.ContactDto;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MicroserviceRequest {
//    @Value("${application.services.user}")
    private final String userServiceName = "USER-SERVICE";

    private final RestTemplate restTemplate;

    private final EurekaClient eurekaClient;
    private InstanceInfo instanceInfo;
    public MicroserviceRequest(RestTemplate restTemplate, EurekaClient eurekaClient) {
        this.restTemplate = restTemplate;
        this.eurekaClient = eurekaClient;
    }

    public ContactDto getUserContactById(Long id){
        return restTemplate.getForObject(getUserServiceUrl() + "/api/admin/user/{id}/contact", ContactDto.class, id);
    }

    public String getUserServiceUrl() {
        if (instanceInfo == null) {
            synchronized (this) {
                instanceInfo = eurekaClient.getNextServerFromEureka(userServiceName, false);
            }
        }
        return instanceInfo.getHomePageUrl();
    }

}
