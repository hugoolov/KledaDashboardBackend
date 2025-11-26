package com.example.kleda_dashboard_backend.services;

import com.example.kleda_dashboard_backend.dtos.SalesReportDTO;
import com.example.kleda_dashboard_backend.model.Sale;
import com.example.kleda_dashboard_backend.repos.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public List<Sale> getSalesByBrandId(Long brandId) {
        return saleRepository.findByProductBrandId(brandId);
    }

    public List<SalesReportDTO> getSalesReportByBrandId(Long brandId) {
        return saleRepository.findByProductBrandId(brandId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Sale> getSalesBetweenDates(LocalDateTime start, LocalDateTime end) {
        return saleRepository.findBySaleDateBetween(start, end);
    }

    public Sale createSale(Sale sale) {
        return saleRepository.save(sale);
    }

    private SalesReportDTO convertToDTO(Sale sale) {
        return new SalesReportDTO(
                sale.getId(),
                sale.getProduct().getName(),
                sale.getProduct().getBrand().getName(),
                sale.getQuantity(),
                sale.getUnitPrice(),
                sale.getTotalPrice(),
                sale.getSaleDate(),
                sale.getCustomerId()
        );
    }
}

