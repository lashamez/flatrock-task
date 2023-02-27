package com.flatrock.notification.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.common.model.ContactDto;
import com.flatrock.common.model.SellerItemData;
import com.flatrock.notification.rest.UserServiceClient;
import com.flatrock.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SellerNotificationListener {
    private final Logger log = LoggerFactory.getLogger(SellerNotificationListener.class);
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    private final UserServiceClient request;

    public SellerNotificationListener(ObjectMapper objectMapper, NotificationService notificationService, UserServiceClient request) {
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
        this.request = request;
    }

    @KafkaListener(topics = "${application.topic.seller-notification}", groupId = "notification")
    public void listen(String json) throws JsonProcessingException {
        log.debug("Kafka received seller notification :{}", json);
        SellerItemData sellerItemData = objectMapper.readValue(json, new TypeReference<>() {});
        long userId = sellerItemData.getSellerId();
        ContactDto sellerContact = request.getUserContactById(userId);
        notificationService.sendSellerEmail(sellerContact.getEmail(), sellerItemData.getOrderItems());
    }

}
