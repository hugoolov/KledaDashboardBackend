package com.example.kleda_dashboard_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryDTO {

    private Long cartId;
    private String customerId;
    private String productName;
    private String brandName;
    private Integer quantity;
    private BigDecimal pricePerUnit;
    private BigDecimal subtotal;
    private LocalDateTime addedAt;
}
