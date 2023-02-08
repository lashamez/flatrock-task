package com.flatrock.notification.rest;

import com.flatrock.common.model.ContactDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MicroserviceRequest {
    @Value("${application.services.user}")
    private String userServiceBaseUrl;

    private final RestTemplate restTemplate;

    public MicroserviceRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ContactDto getUserContactById(Long id){
        return restTemplate.getForObject(userServiceBaseUrl + "/api/admin/user/{id}/contact", ContactDto.class, id);
    }
}
