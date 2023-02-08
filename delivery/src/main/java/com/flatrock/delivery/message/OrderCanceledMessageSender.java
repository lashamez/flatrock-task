package com.flatrock.delivery.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.common.errors.BadRequestAlertException;
import com.flatrock.common.model.event.CanceledOrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderCanceledMessageSender {
    private final Logger log = LoggerFactory.getLogger(OrderCanceledMessageSender.class);
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${application.topic.order-canceled}")
    private String canceledOrderTopic;

    public OrderCanceledMessageSender(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendCancelOrderMessage(CanceledOrderEvent canceledOrderEvent) {
        try {
            log.debug("Sending Kafka message to canceled-order topic: {}", canceledOrderEvent);
            kafkaTemplate.send(canceledOrderTopic, objectMapper.writeValueAsString(canceledOrderEvent));
        } catch (JsonProcessingException e) {
            throw new BadRequestAlertException(e.getMessage(), "delivery", "jsonerror");
        }
    }

}
