package com.flatrock.product.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.common.model.OrderItemDto;
import com.flatrock.product.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductReversalListener {
    private final Logger log = LoggerFactory.getLogger(ProductReversalListener.class);
    private final ObjectMapper objectMapper;

    private final StockService stockService;

    public ProductReversalListener(ObjectMapper objectMapper, StockService stockService) {
        this.objectMapper = objectMapper;
        this.stockService = stockService;
    }


    @KafkaListener(topics = "${application.topic.order-reversal}", groupId = "product")
    public void listen(String json) throws JsonProcessingException {
        log.debug("Kafka received reversal-request :{}", json);
        List<OrderItemDto> reversalProducts = objectMapper.readValue(json, new TypeReference<>() {});
        reversalProducts.forEach(stockService::reversalQuantity);
    }

}
