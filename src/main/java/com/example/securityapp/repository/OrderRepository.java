package com.example.securityapp.repository;

import com.example.securityapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientId(Long clientId);
    Optional<Order> findByIdAndClientId(Long orderId, Long clientId);
}

