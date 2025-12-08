package com.example.kleda_dashboard_backend.services;

import com.example.kleda_dashboard_backend.dtos.BrandAnalyticsDTO;
import com.example.kleda_dashboard_backend.model.Brand;
import com.example.kleda_dashboard_backend.repos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final BrandRepository brandRepository;
    private final SaleRepository saleRepository;
    private final ReturnRepository returnRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public BrandAnalyticsDTO getBrandAnalytics(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        Long totalSales = saleRepository.countByBrandId(brandId);
        BigDecimal totalRevenue = saleRepository.sumTotalPriceByBrandId(brandId);
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        Long totalReturns = returnRepository.countByBrandId(brandId);
        BigDecimal totalRefunded = returnRepository.sumRefundAmountByBrandId(brandId);
        if (totalRefunded == null) totalRefunded = BigDecimal.ZERO;

        Long productsInCarts = cartRepository.countActiveCartItemsByBrandId(brandId);
        Integer uniqueProductCount = productRepository.findByBrandId(brandId).size();

        BigDecimal averageOrderValue = BigDecimal.ZERO;
        if (totalSales > 0) {
            averageOrderValue = totalRevenue.divide(
                    BigDecimal.valueOf(totalSales),
                    2,
                    RoundingMode.HALF_UP
            );
        }

        Double returnRate = 0.0;
        if (totalSales > 0) {
            returnRate = (totalReturns.doubleValue() / totalSales.doubleValue()) * 100;
            returnRate = Math.round(returnRate * 100.0) / 100.0;
        }

        // Calculate conversion rate: (total sales / (total sales + products in carts)) * 100
        // This shows the percentage of potential customers (those who added to cart) who completed purchases
        Double conversionRate = 0.0;
        long totalCartInteractions = totalSales + productsInCarts;
        if (totalCartInteractions > 0) {
            conversionRate = (totalSales.doubleValue() / totalCartInteractions) * 100;
            conversionRate = Math.round(conversionRate * 100.0) / 100.0;
        }

        return new BrandAnalyticsDTO(
                brandId,
                brand.getName(),
                totalSales,
                totalRevenue,
                totalReturns,
                totalRefunded,
                productsInCarts,
                uniqueProductCount,
                averageOrderValue,
                returnRate,
                conversionRate
        );
    }
}
