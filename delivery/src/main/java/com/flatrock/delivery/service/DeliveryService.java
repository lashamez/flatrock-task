package com.flatrock.delivery.service;

import com.flatrock.common.errors.BadRequestAlertException;
import com.flatrock.common.model.OrderSellersData;
import com.flatrock.common.model.OrderStatus;
import com.flatrock.common.model.SellerItemData;
import com.flatrock.common.model.event.CanceledOrderEvent;
import com.flatrock.common.model.event.OrderStatusEvent;
import com.flatrock.delivery.domain.Delivery;
import com.flatrock.delivery.message.NotificationMessageSender;
import com.flatrock.delivery.message.OrderCanceledMessageSender;
import com.flatrock.delivery.repository.DeliveryRepository;
import com.flatrock.delivery.rest.OrderServiceClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final NotificationMessageSender notificationMessageSender;
    private final OrderCanceledMessageSender orderCanceledMessageSender;
    private final OrderServiceClient orderServiceClient;

    private static final String ENTITY = "delivery";

    public DeliveryService(DeliveryRepository deliveryRepository, NotificationMessageSender notificationMessageSender, OrderCanceledMessageSender orderCanceledMessageSender, OrderServiceClient orderServiceClient) {
        this.deliveryRepository = deliveryRepository;
        this.notificationMessageSender = notificationMessageSender;
        this.orderCanceledMessageSender = orderCanceledMessageSender;
        this.orderServiceClient = orderServiceClient;
    }

    public Delivery updateDeliveryByOrderId(Delivery delivery) {
        Optional<Delivery> deliveryByOrderId = deliveryRepository.findDeliveryByOrderId(delivery.getOrderId());
        return deliveryByOrderId.map(orderDelivery -> {
            if (orderDelivery.getOrderStatus() != OrderStatus.PENDING) {
                throw new BadRequestAlertException("Order already finalized", ENTITY, "orderfinalized");
            }
            if (orderDelivery.getOrderStatus() == delivery.getOrderStatus()) {
                throw new BadRequestAlertException("Order already has the same status, nothing to update", "delivery", "deliverynoupdate");
            }
            orderDelivery.setOrderStatus(delivery.getOrderStatus());
            Delivery updatedDelivery = deliveryRepository.save(orderDelivery);
            sendNotifications(updatedDelivery);
            if (delivery.getOrderStatus() == OrderStatus.CANCELED) {
                orderCanceledMessageSender.sendCancelOrderMessage(new CanceledOrderEvent(delivery.getOrderId()));
            }
            return updatedDelivery;
        }).orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY, "idnotfound"));

    }

    private void sendNotifications(Delivery delivery) {
        Long orderId = delivery.getOrderId();
        Long customerId = delivery.getCustomerId();
        OrderSellersData sellerData = orderServiceClient.getSellerData(orderId);
        notificationMessageSender.sendCustomerNotification(new OrderStatusEvent(orderId, customerId, delivery.getOrderStatus(), null));
        sellerData.getSellerItems().forEach(seller -> notificationMessageSender.sendSellersNotification(new SellerItemData(seller.getSellerId(), seller.getOrderItems(), delivery.getOrderStatus())));
    }

}
