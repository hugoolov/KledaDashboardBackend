package com.example.kleda_dashboard_backend.repos;

import com.example.kleda_dashboard_backend.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByProductBrandId(Long brandId);

    List<Sale> findByProductId(Long productId);

    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.product.brand.id = :brandId")
    Long countByBrandId(@Param("brandId") Long brandId);

    @Query("SELECT SUM(s.totalPrice) FROM Sale s WHERE s.product.brand.id = :brandId")
    BigDecimal sumTotalPriceByBrandId(@Param("brandId") Long brandId);
}

