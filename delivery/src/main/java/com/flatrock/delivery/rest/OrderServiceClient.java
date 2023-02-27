package com.flatrock.delivery.rest;

import com.flatrock.common.model.OrderSellersData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${application.services.order}")
public interface OrderServiceClient {

    @GetMapping("/order/api/order/{id}/seller")
    OrderSellersData getSellerData(@PathVariable Long id);

}
