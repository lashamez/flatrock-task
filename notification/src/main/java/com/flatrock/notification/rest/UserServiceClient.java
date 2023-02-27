package com.flatrock.notification.rest;

import com.flatrock.common.model.ContactDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "http://USER-SERVICE/")
public interface UserServiceClient {
    @GetMapping("/user/api/admin/user/{id}/contact")
    ContactDto getUserContactById(@PathVariable Long id);

}
