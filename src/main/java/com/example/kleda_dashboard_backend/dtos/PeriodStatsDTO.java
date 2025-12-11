package com.example.kleda_dashboard_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodStatsDTO {
    private String period; // e.g., "2024-W50", "2024-12", "2024"
    private String label; // e.g., "Week 50", "Dec", "2024"
    private Integer sold;
    private Integer returned;
}
