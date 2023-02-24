package com.flatrock.order.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrock.common.model.OrderItemDto;
import com.flatrock.common.model.event.CanceledOrderEvent;
import com.flatrock.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderCanceledListener {
    private final Logger log = LoggerFactory.getLogger(OrderCanceledListener.class);
    private final ObjectMapper objectMapper;

    private final OrderService orderService;

    private final OrderReversalProducer reversalProducer;

    public OrderCanceledListener(ObjectMapper objectMapper, OrderService orderService, OrderReversalProducer reversalProducer) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
        this.reversalProducer = reversalProducer;
    }


    @KafkaListener(topics = "${application.topic.order-canceled}", groupId = "notification")
    public void listen(String json) throws JsonProcessingException {
        log.debug("Kafka received order-canceled notification :{}", json);
        CanceledOrderEvent orderStatusEvent = objectMapper.readValue(json, new TypeReference<>() {});
        Long orderId = orderStatusEvent.getOrderId();
        List<OrderItemDto> orderItems = orderService.findOrderItemsById(orderId);
        reversalProducer.send(orderItems);
    }

}
