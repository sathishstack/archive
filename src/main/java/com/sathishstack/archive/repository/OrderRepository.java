package com.sathishstack.archive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sathishstack.archive.entity.Order;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByIdempotencyKey(String idempotencyKey);
}
