package com.example.kleda_dashboard_backend.controllers;

import com.example.kleda_dashboard_backend.dtos.BrandAnalyticsDTO;
import com.example.kleda_dashboard_backend.dtos.CartSummaryDTO;
import com.example.kleda_dashboard_backend.dtos.SalesReportDTO;
import com.example.kleda_dashboard_backend.model.Brand;
import com.example.kleda_dashboard_backend.model.Return;
import com.example.kleda_dashboard_backend.repos.ReturnRepository;
import com.example.kleda_dashboard_backend.services.AnalyticsService;
import com.example.kleda_dashboard_backend.services.BrandService;
import com.example.kleda_dashboard_backend.services.CartService;
import com.example.kleda_dashboard_backend.services.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final SaleService salesService;
    private final CartService cartService;
    private final AnalyticsService analyticsService;
    private final ReturnRepository returnRepository;

    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id) {
        return brandService.getBrandById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/sales")
    public ResponseEntity<List<SalesReportDTO>> getBrandSales(@PathVariable Long id) {
        return ResponseEntity.ok(salesService.getSalesReportByBrandId(id));
    }

    @GetMapping("/{id}/returns")
    public ResponseEntity<List<Return>> getBrandReturns(@PathVariable Long id) {
        return ResponseEntity.ok(returnRepository.findBySaleProductBrandId(id));
    }

    @GetMapping("/{id}/carts")
    public ResponseEntity<List<CartSummaryDTO>> getBrandCarts(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartSummaryByBrandId(id));
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<BrandAnalyticsDTO> getBrandAnalytics(@PathVariable Long id) {
        return ResponseEntity.ok(analyticsService.getBrandAnalytics(id));
    }
}
