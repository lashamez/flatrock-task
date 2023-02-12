package com.flatrock.order.controller;

import com.flatrock.common.errors.BadRequestAlertException;
import com.flatrock.common.model.OrderSellersData;
import com.flatrock.common.util.ResponseUtil;
import com.flatrock.order.domain.Order;
import com.flatrock.order.service.OrderService;
import com.flatrock.order.repository.OrderRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    private static final String ENTITY_NAME = "Order";

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    public OrderController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) throws URISyntaxException {
        log.debug("REST request to save Order : {}", order);
        if (order.getId() != null) {
            throw new BadRequestAlertException("A new order cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Order result = orderService.validateAndSave(order);

        return ResponseEntity
            .created(new URI("/api/order/" + result.getId()))
            .body(result);
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<Order> updateOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Order order
    ) {
        log.debug("REST request to update Order : {}, {}", id, order);
        if (order.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, order.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Order result = orderRepository.save(order);
        return ResponseEntity
            .ok()
            .body(result);
    }

    @GetMapping("/order")
    public List<Order> getAllOrders() {
        log.debug("REST request to get all OrderEntries");
        return orderRepository.findAllWithEntries();
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        Optional<Order> order = orderRepository.findByIdWithEntries(id);
        return ResponseUtil.wrapOrNotFound(order);
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.debug("REST request to delete Order : {}", id);
        orderRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .build();
    }

    @GetMapping("/order/{id}/seller")
    public ResponseEntity<OrderSellersData> orderSellers(@PathVariable Long id) {
       return orderService.getOrderSellerData(id);
    }
}
