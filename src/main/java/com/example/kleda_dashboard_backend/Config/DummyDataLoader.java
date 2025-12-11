package com.example.kleda_dashboard_backend.Config;

import com.example.kleda_dashboard_backend.model.*;
import com.example.kleda_dashboard_backend.repos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final ReturnRepository returnRepository;
    private final CartRepository cartRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) {

        if (brandRepository.count() > 0) {
            System.out.println("Database already contains data. Skipping.");
            return;
        }
        System.out.println("Loading dummy data...");

        // Create Brands
        List<Brand> brands = createBrands();

        // Create Products
        List<Product> products = createProducts(brands);

        // Create Sales with historical data
        List<Sale> sales = createSalesWithHistory(products);

        // Create Returns with historical data
        createReturnsWithHistory(sales);

        // Create Carts
        createCarts(products);

        System.out.println("Dummy data loaded successfully!");
    }

    private List<Brand> createBrands() {
        List<Brand> brands = new ArrayList<>();

        Brand nike = new Brand(null ,"Nike", "Athletic apparel and footwear", "USA");

        brands.add(brandRepository.save(nike));

        return brands;
    }

    private List<Product> createProducts(List<Brand> brands) {
        List<Product> products = new ArrayList<>();
        // Get the single brand we created in createBrands()
        Brand brand = brands.get(0);

        String[] categories = {"Hettegensere og sweatshirts", "Bukser", "Leggings", "Matchende sett",
                "Overdeler og T-skjorter", "Shorts", "Sports-BH-er", "Tilbehør", "Sport"};
        String[] sizes = {"XS", "S", "M", "L", "XL", "XXL"};
        String[] colors = {"Black", "White", "Blue", "Red", "Green", "Grey"};

        // 1. Loop through each Category
        for (String category : categories) {
            // 2. Inner loop to create exactly 5 products per category
            for (int i = 1; i <= 5; i++) {

                Product product = new Product(
                        null,
                        // Use the category name in the product name for clarity
                        brand.getName() + " " + category + " " + (i),
                        "Premium quality " + category.toLowerCase(),
                        // Price between 29.99 and 229.99
                        BigDecimal.valueOf(29.99 + random.nextInt(200)),
                        category, // Assign the current category
                        sizes[random.nextInt(sizes.length)],
                        colors[random.nextInt(colors.length)],
                        "put image url here",
                        // Stock between 10 and 110
                        random.nextInt(100) + 10,
                        brand, // Assign the single 'Nike' brand
                        null
                );
                products.add(productRepository.save(product));
            }
        }

        System.out.println("Created " + products.size() + " products across " + categories.length + " categories.");
        return products;
    }

    private List<Sale> createSalesWithHistory(List<Product> products) {
        List<Sale> sales = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Create sales data for the past 365 days with varying quantities per day
        for (int daysAgo = 0; daysAgo < 365; daysAgo++) {
            LocalDateTime saleDate = now.minusDays(daysAgo);

            // Create 3-8 sales per day
            int salesPerDay = 3 + random.nextInt(6);

            for (int i = 0; i < salesPerDay; i++) {
                Product product = products.get(random.nextInt(products.size()));
                int quantity = random.nextInt(3) + 1;
                BigDecimal unitPrice = product.getPrice();

                Sale sale = new Sale(
                        null,
                        product,
                        quantity,
                        unitPrice,
                        unitPrice.multiply(BigDecimal.valueOf(quantity)),
                        "CUST-" + (1000 + random.nextInt(200)),
                        saleDate.minusHours(random.nextInt(24)).minusMinutes(random.nextInt(60))
                );
                sales.add(saleRepository.save(sale));
            }
        }

        System.out.println("Created " + sales.size() + " sales records");
        return sales;
    }

    private void createReturnsWithHistory(List<Sale> sales) {
        String[] reasons = {
                "Wrong size",
                "Defective product",
                "Changed mind",
                "Not as described",
                "Color mismatch",
                "Quality issues"
        };

        int returnCount = 0;

        // Create returns for about 15% of sales
        for (Sale sale : sales) {
            // 15% chance of return
            if (random.nextDouble() < 0.15) {
                int returnQuantity = random.nextInt(sale.getQuantity()) + 1;
                BigDecimal refundAmount = sale.getUnitPrice()
                        .multiply(BigDecimal.valueOf(returnQuantity));

                // Return happens 1-30 days after sale
                LocalDateTime returnDate = sale.getSaleDate()
                        .plusDays(1 + random.nextInt(30))
                        .plusHours(random.nextInt(24))
                        .plusMinutes(random.nextInt(60));

                Return returnItem = new Return(
                        null,
                        sale,
                        returnQuantity,
                        refundAmount,
                        reasons[random.nextInt(reasons.length)],
                        returnDate
                );
                returnRepository.save(returnItem);
                returnCount++;
            }
        }

        System.out.println("Created " + returnCount + " return records");
    }

    private void createCarts(List<Product> products) {
        for (int i = 0; i < 30; i++) {
            Product product = products.get(random.nextInt(products.size()));

            Cart cart = new Cart(
                    null,
                    "CUST-" + (1000 + random.nextInt(50)),
                    product,
                    random.nextInt(3) + 1,
                    LocalDateTime.now().minusDays(random.nextInt(7)),
                    true
            );
            cartRepository.save(cart);
        }
    }
}