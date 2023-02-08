package com.flatrock.delivery.repository;

import com.flatrock.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findDeliveryByOrderId(Long orderId);

    boolean existsDeliveryByOrderId(long orderId);
}
