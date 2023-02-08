package com.flatrock.order.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.common.errors.BadRequestAlertException;
import com.flatrock.common.model.OrderItemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderReversalProducer {
    private final Logger log = LoggerFactory.getLogger(OrderReversalProducer.class);

    private final KafkaTemplate<Object, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Value("${application.topic.order-reversal}")
    private String topic;

    public OrderReversalProducer(KafkaTemplate<Object, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(List<OrderItemDto> orderCreatedEvent) {
        log.debug("Sending Kafka message to order-reversal: {}", orderCreatedEvent);
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(orderCreatedEvent));
        } catch (JsonProcessingException e) {
            throw new BadRequestAlertException(e.getMessage(), "order", "jsonerror");
        }
    }
}
