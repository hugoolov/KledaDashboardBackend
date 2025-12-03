package com.example.kleda_dashboard_backend.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandAnalyticsDTO {

    private Long brandId;
    private String brandName;
    private Long totalSales;
    private BigDecimal totalRevenue;
    private Long totalReturns;
    private BigDecimal totalRefunded;
    private Long productsInCarts;
    private Integer uniqueProductCount;
    private BigDecimal averageOrderValue;
    private Double returnRate;
    private Double conversionRate; // Percentage of cart additions that converted to sales
}

