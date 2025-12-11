package com.example.kleda_dashboard_backend.services;

import com.example.kleda_dashboard_backend.dtos.ProductStatsDTO;
import com.example.kleda_dashboard_backend.dtos.PeriodStatsDTO;
import com.example.kleda_dashboard_backend.model.Product;
import com.example.kleda_dashboard_backend.model.Return;
import com.example.kleda_dashboard_backend.model.Sale;
import com.example.kleda_dashboard_backend.repos.ProductRepository;
import com.example.kleda_dashboard_backend.repos.ReturnRepository;
import com.example.kleda_dashboard_backend.repos.SaleRepository;
import org.springframework.data.jpa.repository.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductStatsService {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final ReturnRepository returnRepository;

    public ProductStatsDTO getProductStats(Long productId, String period) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: id" + productId));

        List<Sale> sales = saleRepository.findByProductId(productId);
        List<Return> returns = returnRepository.findBySaleProductId(productId);

        List<PeriodStatsDTO> stats;

        switch (period.toLowerCase()) {
            case "week":
                stats = calculateWeeklyStats(sales, returns, 12);
                break;
            case "month":
                stats = calculateMonthlyStats(sales, returns, 12);
                break;
            case "year":
                stats = calculateYearlyStats(sales, returns, 5);
                break;
            default:
                stats = calculateMonthlyStats(sales, returns, 12);
        }

        return new ProductStatsDTO(
                productId,
                product.getName(),
                product.getBrand().getName(),
                stats.stream()
                        .map(s -> new PeriodStatsDTO(
                                s.getPeriod(), s.getLabel(), s.getSold(), s.getReturned()))
                        .collect(Collectors.toList())
        );
    }

    private List<PeriodStatsDTO> calculateWeeklyStats(List<Sale> sales, List<Return> returns, int weeksCount) {
        LocalDateTime now = LocalDateTime.now();
        Map<String, PeriodStatsDTO> statsMap = new LinkedHashMap<>();

        // Initialize last N weeks
        for (int i = weeksCount - 1; i >= 0; i--) {
            LocalDateTime weekDate = now.minusWeeks(i);
            int year = weekDate.getYear();
            int week = weekDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            String periodKey = String.format("%d-W%02d", year, week);
            String label = String.format("Uke %d", week);
            statsMap.put(periodKey, new PeriodStatsDTO(periodKey, label, 0, 0));
        }

        // Count sales
        for (Sale sale : sales) {
            int year = sale.getSaleDate().getYear();
            int week = sale.getSaleDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            String periodKey = String.format("%d-W%02d", year, week);

            if (statsMap.containsKey(periodKey)) {
                PeriodStatsDTO stats = statsMap.get(periodKey);
                stats.setSold(stats.getSold() + sale.getQuantity());
            }
        }

        // Count returns
        for (Return ret : returns) {
            int year = ret.getReturnDate().getYear();
            int week = ret.getReturnDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            String periodKey = String.format("%d-W%02d", year, week);

            if (statsMap.containsKey(periodKey)) {
                PeriodStatsDTO stats = statsMap.get(periodKey);
                stats.setReturned(stats.getReturned() + ret.getQuantity());
            }
        }

        return new ArrayList<>(statsMap.values());
    }

    private List<PeriodStatsDTO> calculateMonthlyStats(List<Sale> sales, List<Return> returns, int monthsCount) {
        LocalDateTime now = LocalDateTime.now();
        Map<String, PeriodStatsDTO> statsMap = new LinkedHashMap<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");

        // Initialize last N months
        for (int i = monthsCount - 1; i >= 0; i--) {
            LocalDateTime monthDate = now.minusMonths(i);
            String periodKey = monthDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            String label = monthDate.format(monthFormatter);
            statsMap.put(periodKey, new PeriodStatsDTO(periodKey, label, 0, 0));
        }

        // Count sales
        for (Sale sale : sales) {
            String periodKey = sale.getSaleDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));

            if (statsMap.containsKey(periodKey)) {
                PeriodStatsDTO stats = statsMap.get(periodKey);
                stats.setSold(stats.getSold() + sale.getQuantity());
            }
        }

        // Count returns
        for (Return ret : returns) {
            String periodKey = ret.getReturnDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));

            if (statsMap.containsKey(periodKey)) {
                PeriodStatsDTO stats = statsMap.get(periodKey);
                stats.setReturned(stats.getReturned() + ret.getQuantity());
            }
        }

        return new ArrayList<>(statsMap.values());
    }

    private List<PeriodStatsDTO> calculateYearlyStats(List<Sale> sales, List<Return> returns, int yearsCount) {
        LocalDateTime now = LocalDateTime.now();
        Map<String, PeriodStatsDTO> statsMap = new LinkedHashMap<>();

        // Initialize last N years
        for (int i = yearsCount - 1; i >= 0; i--) {
            int year = now.minusYears(i).getYear();
            String periodKey = String.valueOf(year);
            statsMap.put(periodKey, new PeriodStatsDTO(periodKey, periodKey, 0, 0));
        }

        // Count sales
        for (Sale sale : sales) {
            String periodKey = String.valueOf(sale.getSaleDate().getYear());

            if (statsMap.containsKey(periodKey)) {
                PeriodStatsDTO stats = statsMap.get(periodKey);
                stats.setSold(stats.getSold() + sale.getQuantity());
            }
        }

        // Count returns
        for (Return ret : returns) {
            String periodKey = String.valueOf(ret.getReturnDate().getYear());

            if (statsMap.containsKey(periodKey)) {
                PeriodStatsDTO stats = statsMap.get(periodKey);
                stats.setReturned(stats.getReturned() + ret.getQuantity());
            }
        }

        return new ArrayList<>(statsMap.values());
    }
}