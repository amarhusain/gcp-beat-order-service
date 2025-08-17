package com.beat.gcp_order_service.order.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record OrderCreateRequest(
        @Email @NotBlank String customerEmail,
        @NotBlank String description,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal totalAmount,
        @Pattern(regexp = "^[A-Z]{3}$") @NotBlank String currency
) {}

