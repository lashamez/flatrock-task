package com.flatrock.order.service;

import com.flatrock.common.errors.BadRequestAlertException;
import com.flatrock.common.model.OrderItemDto;
import com.flatrock.common.model.OrderSellersData;
import com.flatrock.common.model.ProductAvailabilityRequest;
import com.flatrock.common.model.ProductAvailabilityResponse;
import com.flatrock.common.model.event.OrderCreatedEvent;
import com.flatrock.order.domain.Order;
import com.flatrock.order.domain.OrderEntry;
import com.flatrock.order.message.OrderCreatedProducer;
import com.flatrock.order.repository.OrderRepository;
import com.flatrock.order.rest.ProductServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class OrderService {
    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final OrderCreatedProducer orderProducer;

    private final ProductServiceClient productServiceClient;

    public OrderService(OrderRepository orderRepository, OrderCreatedProducer orderProducer, ProductServiceClient productServiceClient) {
        this.orderRepository = orderRepository;
        this.orderProducer = orderProducer;
        this.productServiceClient = productServiceClient;
    }

    public Order validateAndSave(Order order) {
        double totalPrice = validateProductAvailabilityAndGetTotal(order.getOrderEntries());
        order.setTimestamp(LocalDateTime.now());
        order = orderRepository.save(order);
        sendOrderCreatedMessage(order, totalPrice);
        return order;
    }

    private void sendOrderCreatedMessage(Order order, double totalPrice) {
        log.info("Sending order created event: {}", order);
        List<OrderItemDto> productIds = order.getOrderEntries().stream()
            .map(entry -> new OrderItemDto(entry.getProductId(), entry.getQuantity()))
            .toList();
        orderProducer.send(new OrderCreatedEvent(order.getId(), order.getUserId(), order.getTimestamp(), productIds, totalPrice));
    }

    private double validateProductAvailabilityAndGetTotal(List<OrderEntry> orderEntries) {
        List<ProductAvailabilityRequest> requests = orderEntries.stream()
            .map(entry -> new ProductAvailabilityRequest(entry.getProductId(), entry.getQuantity()))
            .toList();
        ResponseEntity<List<ProductAvailabilityResponse>> response = productServiceClient.validateProduct(requests);
        List<ProductAvailabilityResponse> responses = Objects.requireNonNull(response.getBody());
        List<ProductAvailabilityResponse> unavailableProducts = responses.stream()
            .filter(product -> !product.isAvailable()).toList();
        if (!unavailableProducts.isEmpty()) {
            throw new BadRequestAlertException("Products not available " + unavailableProducts, "product", "productnotinstock");
        }
        return responses.stream().mapToDouble(ProductAvailabilityResponse::getTotalPrice).sum();
    }

    public List<OrderItemDto> findOrderItemsById(Long orderId) {
        return orderRepository.findByIdWithEntries(orderId)
            .map(Order::getOrderEntries)
            .map(entries -> entries.stream().map(entry -> new OrderItemDto(entry.getProductId(), entry.getQuantity())).toList())
            .orElseThrow(() ->new BadRequestAlertException("Order not found", "order", "ordernotfound"));
    }

    public ResponseEntity<OrderSellersData> getOrderSellerData(long orderId) {
        List<OrderItemDto> orderItemsById = findOrderItemsById(orderId);
        return productServiceClient.getSellerData(orderItemsById);
    }

    public void deleteById(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
