package com.flatrock.notification.service;

import com.flatrock.common.model.OrderItemDto;
import com.flatrock.common.model.event.OrderStatusEvent;

import java.util.List;

public interface NotificationService {
    void sendCustomerSms(String phone, OrderStatusEvent event);
    void sendSellerEmail(String email, List<OrderItemDto> orderItems);
}
