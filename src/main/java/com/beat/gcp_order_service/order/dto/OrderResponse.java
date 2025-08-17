package com.beat.gcp_order_service.order.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String customerEmail,
        String description,
        BigDecimal totalAmount,
        String currency,
        String status,
        OffsetDateTime createdAt
) {}

