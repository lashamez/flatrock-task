package com.flatrock.order.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.common.errors.BadRequestAlertException;
import com.flatrock.common.model.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedProducer {
    private final Logger log = LoggerFactory.getLogger(OrderCreatedProducer.class);

    private final KafkaTemplate<Object, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Value("${application.topic.order-created}")
    private String topic;

    public OrderCreatedProducer(KafkaTemplate<Object, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(OrderCreatedEvent orderCreatedEvent) {
        log.debug("Sending Kafka message to order-created: {}", orderCreatedEvent);
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(orderCreatedEvent));
        } catch (JsonProcessingException e) {
            throw new BadRequestAlertException(e.getMessage(), "order", "jsonerror");
        }
    }
}
