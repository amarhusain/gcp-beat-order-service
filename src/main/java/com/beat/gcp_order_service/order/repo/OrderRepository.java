package com.beat.gcp_order_service.order.repo;

import com.beat.gcp_order_service.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> { }

