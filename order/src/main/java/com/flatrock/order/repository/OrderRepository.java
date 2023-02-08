package com.flatrock.order.repository;

import com.flatrock.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.orderEntries WHERE o.id = :id")
    Optional<Order> findByIdWithEntries(@Param("id") Long id);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderEntries")
    List<Order> findAllWithEntries();
}
