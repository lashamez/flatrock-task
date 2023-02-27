package com.flatrock.delivery.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.common.model.OrderSellersData;
import com.flatrock.common.model.OrderStatus;
import com.flatrock.common.model.event.OrderCreatedEvent;
import com.flatrock.common.model.event.OrderStatusEvent;
import com.flatrock.delivery.domain.Delivery;
import com.flatrock.delivery.repository.DeliveryRepository;
import com.flatrock.delivery.rest.OrderServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {
    private final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);


    private final ObjectMapper objectMapper;

    private final DeliveryRepository deliveryRepository;

    private final NotificationMessageSender notificationMessageSender;

    private final OrderServiceClient request;


    public OrderCreatedListener(ObjectMapper objectMapper, DeliveryRepository deliveryRepository, NotificationMessageSender notificationMessageSender, OrderServiceClient request) {
        this.objectMapper = objectMapper;
        this.deliveryRepository = deliveryRepository;
        this.notificationMessageSender = notificationMessageSender;
        this.request = request;
    }

    @KafkaListener(topics = "${application.topic.order-created}", groupId = "order")
    public void listen(String orderCreated) throws JsonProcessingException {
        log.debug("Kafka received order creation notification :{}", orderCreated);
        OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(orderCreated, new TypeReference<>() {});
        Delivery delivery = deliveryRepository.save(new Delivery(orderCreatedEvent.getCustomerId(), orderCreatedEvent.getOrderId(), OrderStatus.PENDING));
        notificationMessageSender.sendCustomerNotification(new OrderStatusEvent(delivery.getOrderId(), delivery.getCustomerId(),
            delivery.getOrderStatus(), orderCreatedEvent.getTotalPrice()));
        OrderSellersData sellerData = request.getSellerData(orderCreatedEvent.getOrderId());
        sellerData.getSellerItems().forEach(seller -> {
            seller.setOrderStatus(delivery.getOrderStatus());
            notificationMessageSender.sendSellersNotification(seller);
        });
    }

}
