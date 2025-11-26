package com.example.kleda_dashboard_backend.repos;

import com.example.kleda_dashboard_backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByCustomerId(String customerId);

    List<Cart> findByProductBrandIdAndIsActiveTrue(Long brandId);

    @Query("SELECT COUNT(c) FROM Cart c WHERE c.product.brand.id = :brandId AND c.isActive = true")
    Long countActiveCartItemsByBrandId(@Param("brandId") Long brandId);
}