package com.flatrock.order.rest;

import com.flatrock.common.model.OrderItemDto;
import com.flatrock.common.model.OrderSellersData;
import com.flatrock.common.model.ProductAvailabilityRequest;
import com.flatrock.common.model.ProductAvailabilityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

@FeignClient(name = "http://PRODUCT-SERVICE/product/")
public interface ProductServiceClient {
    @PostMapping("/api/stocks/check")
    ResponseEntity<List<ProductAvailabilityResponse>> validateProduct(@RequestBody List<ProductAvailabilityRequest> requests);

    @PostMapping("/api/stocks/seller")
    ResponseEntity<OrderSellersData> getSellerData(@RequestBody List<OrderItemDto> orderItemDtos);
}
