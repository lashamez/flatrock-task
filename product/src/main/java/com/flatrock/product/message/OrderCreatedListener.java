package com.flatrock.product.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.common.model.event.OrderCreatedEvent;
import com.flatrock.product.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {
    private final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);


    private final ObjectMapper objectMapper;

    private final StockService stockService;

    private final KafkaTemplate<Object, Long> kafkaTemplate;

    @Value("${application.topic.reserve-failed}")
    private String reserveFailedTopic;

    public OrderCreatedListener(ObjectMapper objectMapper, StockService stockService, KafkaTemplate<Object, Long> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.stockService = stockService;
        this.kafkaTemplate = kafkaTemplate;
    }


    @KafkaListener(topics = "${application.topic.order-created}", groupId = "order2")
    public void listen(String orderCreated) throws JsonProcessingException {
        log.debug("Kafka received order creation notification :{}", orderCreated);
        log.debug("Updating reserved product quantities");
        OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(orderCreated, new TypeReference<>() {});

        try {
            stockService.reduceQuantities(orderCreatedEvent.getItems());
        }catch (Exception e) {
            kafkaTemplate.send(reserveFailedTopic, orderCreatedEvent.getOrderId());
        }
    }

}
