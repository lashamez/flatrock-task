package com.flatrock.notification.service;

import com.flatrock.common.model.OrderItemDto;
import com.flatrock.common.model.event.OrderStatusEvent;
import com.flatrock.notification.message.SellerNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public void sendCustomerSms(String phone, OrderStatusEvent event) {
        log.debug("SMS to customer: {}, OrderId: {}, totalPrice: {}, status: {}",
            phone, event.getOrderId(), event.getTotalPrice(), event.getOrderStatus());
    }

    @Override
    public void sendSellerEmail(String email, List<OrderItemDto> orderItems) {
        log.debug("Email to seller: {}, orderedItems: {}",
            email, orderItems);
    }
}
