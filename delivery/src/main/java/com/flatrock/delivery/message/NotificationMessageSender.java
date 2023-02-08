package com.flatrock.delivery.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.common.errors.BadRequestAlertException;
import com.flatrock.common.model.SellerItemData;
import com.flatrock.common.model.event.OrderStatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageSender {
    private final Logger log = LoggerFactory.getLogger(NotificationMessageSender.class);
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${application.topic.customer-notification}")
    private String customerTopic;

    @Value("${application.topic.seller-notification}")
    private String sellerTopic;

    public NotificationMessageSender(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendCustomerNotification(OrderStatusEvent orderStatusEvent) {
        try {
            log.debug("Sending Kafka message to customer-notification: {}", orderStatusEvent);
            kafkaTemplate.send(customerTopic, objectMapper.writeValueAsString(orderStatusEvent));
        } catch (JsonProcessingException e) {
            throw new BadRequestAlertException(e.getMessage(), "delivery", "jsonerror");
        }
    }

    public void sendSellersNotification(SellerItemData sellerItemData) {
        try {
            log.debug("Sending Kafka message to seller-notification: {}", sellerItemData);
            kafkaTemplate.send(sellerTopic, objectMapper.writeValueAsString(sellerItemData));
        } catch (JsonProcessingException e) {
            throw new BadRequestAlertException(e.getMessage(), "delivery", "jsonerror");
        }
    }
}
