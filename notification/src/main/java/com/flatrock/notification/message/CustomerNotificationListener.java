package com.flatrock.notification.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.common.model.ContactDto;
import com.flatrock.common.model.event.OrderStatusEvent;
import com.flatrock.notification.rest.UserServiceClient;
import com.flatrock.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerNotificationListener {
    private final Logger log = LoggerFactory.getLogger(CustomerNotificationListener.class);
    private final ObjectMapper objectMapper;

    private final NotificationService notificationService;

    private final UserServiceClient request;

    public CustomerNotificationListener(ObjectMapper objectMapper, NotificationService notificationService, UserServiceClient request) {
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
        this.request = request;
    }

    @KafkaListener(topics = "${application.topic.customer-notification}", groupId = "notification")
    public void listen(String json) throws JsonProcessingException {
        log.debug("Kafka received customer notification :{}", json);
        OrderStatusEvent orderStatusEvent = objectMapper.readValue(json, new TypeReference<>() {});
        Long userId = orderStatusEvent.getUserId();
        ContactDto customerContact = request.getUserContactById(userId);
        notificationService.sendCustomerSms(customerContact.getPhone(), orderStatusEvent);
    }

}
