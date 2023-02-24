package com.flatrock.order.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductReserveFailedListener {
    private final Logger log = LoggerFactory.getLogger(ProductReserveFailedListener.class);
    private final OrderService orderService;

    @Value("${application.topic.reserve-failed}")
    private String topic;

    public ProductReserveFailedListener(OrderService orderService) {
        this.orderService = orderService;
    }


    @KafkaListener(topics = "${application.topic.reserve-failed}", groupId = "notification")
    public void listen(Long orderId) {
        log.debug("Kafka received reserve-failed message :{}", orderId);
        orderService.deleteById(orderId);
    }
}
