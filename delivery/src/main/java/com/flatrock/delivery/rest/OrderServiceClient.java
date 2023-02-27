package com.flatrock.delivery.rest;

import com.flatrock.common.model.OrderSellersData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "http://ORDER-SERVICE/order/")
public interface OrderServiceClient {

    @GetMapping("/api/order/{id}/seller")
    OrderSellersData getSellerData(@PathVariable Long id);

}
