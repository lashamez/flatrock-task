package com.flatrock.product.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.common.model.event.OrderCreatedEvent;
import com.flatrock.product.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {
    private final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);


    private final ObjectMapper objectMapper;

    private final StockService stockService;

    public OrderCreatedListener(ObjectMapper objectMapper, StockService stockService) {
        this.objectMapper = objectMapper;
        this.stockService = stockService;
    }


    @KafkaListener(topics = "${application.topic.order-created}", groupId = "order2")
    public void listen(String orderCreated) throws JsonProcessingException {
        log.debug("Kafka received order creation notification :{}", orderCreated);
        log.debug("Updating reserved product quantities");
        OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(orderCreated, new TypeReference<>() {});
        stockService.reduceQuantities(orderCreatedEvent.getItems());
    }

}
