package com.flatrock.notification.rest;

import com.flatrock.common.model.ContactDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${application.services.user}")
public interface UserServiceClient {
    @GetMapping("/user/api/admin/user/{id}/contact")
    ContactDto getUserContactById(@PathVariable Long id);

}
