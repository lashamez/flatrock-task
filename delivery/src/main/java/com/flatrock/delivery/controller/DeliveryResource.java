package com.flatrock.delivery.controller;

import com.flatrock.common.errors.BadRequestAlertException;
import com.flatrock.delivery.domain.Delivery;
import com.flatrock.delivery.service.DeliveryService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class DeliveryResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryResource.class);

    private static final String ENTITY_NAME = "delivery";

    private final DeliveryService deliveryService;

    public DeliveryResource(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }
  /**
     * {@code PUT  /delivery/:id} : Updates an existing delivery.
     *
     * @param id the id of the order to save.
     * @param delivery the stock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stock,
     * or with status {@code 400 (Bad Request)} if the delivery is not valid,
     * or with status {@code 500 (Internal Server Error)} if the delivery couldn't be updated.
     */
    @PutMapping(value = "/delivery/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Delivery> updateDeliveryStatus(
        @PathVariable(value = "id") final Long id,
        @NotNull @RequestBody Delivery delivery
    ) {
        log.debug("REST request to update order {} status {}", id, delivery.getOrderStatus());
        if (delivery.getOrderId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, delivery.getOrderId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        Delivery updatedDelivery = deliveryService.updateDeliveryByOrderId(delivery);

        return ResponseEntity.ok(updatedDelivery);
    }

}
