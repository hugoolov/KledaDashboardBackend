package com.example.kleda_dashboard_backend.repos;


import com.example.kleda_dashboard_backend.model.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReturnRepository extends JpaRepository<Return, Long> {

    List<Return> findBySaleProductBrandId(Long brandId);

    @Query("SELECT COUNT(r) FROM Return r WHERE r.sale.product.brand.id = :brandId")
    Long countByBrandId(@Param("brandId") Long brandId);

    @Query("SELECT SUM(r.refundAmount) FROM Return r WHERE r.sale.product.brand.id = :brandId")
    BigDecimal sumRefundAmountByBrandId(@Param("brandId") Long brandId);
}