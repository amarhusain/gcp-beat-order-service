package com.beat.gcp_order_service.order.controller;

import com.beat.gcp_order_service.order.dto.OrderCreateRequest;
import com.beat.gcp_order_service.order.dto.OrderResponse;
import com.beat.gcp_order_service.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderCreateRequest request) {
        OrderResponse created = service.create(request);
        return ResponseEntity
                .created(URI.create("/api/v1/orders/" + created.id()))
                .body(created);
    }
}

