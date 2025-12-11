package com.example.kleda_dashboard_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatsDTO {
    private Long productId;
    private String productName;
    private String brandName;
    private List<PeriodStatsDTO> stats;
}


