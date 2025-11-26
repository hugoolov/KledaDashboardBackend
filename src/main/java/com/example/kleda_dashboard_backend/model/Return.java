package com.example.kleda_dashboard_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "returns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Return {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "refund_amount", nullable = false)
    private BigDecimal refundAmount;

    private String reason;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @PrePersist
    protected void onCreate() {
        if (returnDate == null) {
            returnDate = LocalDateTime.now();
        }
    }
}