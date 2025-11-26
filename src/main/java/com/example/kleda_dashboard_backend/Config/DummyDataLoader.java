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
        System.out.println("Loading dummy data...");

        // Create Brands
        List<Brand> brands = createBrands();

        // Create Products
        List<Product> products = createProducts(brands);

        // Create Sales
        List<Sale> sales = createSales(products);

        // Create Returns
        createReturns(sales);

        // Create Carts
        createCarts(products);

        System.out.println("Dummy data loaded successfully!");
    }

    private List<Brand> createBrands() {
        List<Brand> brands = new ArrayList<>();

        Brand nike = new Brand(null, "Nike", "Athletic apparel and footwear", "USA", null);
        Brand adidas = new Brand(null, "Adidas", "Sports clothing and accessories", "Germany", null);
        Brand zara = new Brand(null, "Zara", "Fast fashion clothing", "Spain", null);
        Brand hm = new Brand(null, "H&M", "Affordable fashion", "Sweden", null);

        brands.add(brandRepository.save(nike));
        brands.add(brandRepository.save(adidas));
        brands.add(brandRepository.save(zara));
        brands.add(brandRepository.save(hm));

        return brands;
    }

    private List<Product> createProducts(List<Brand> brands) {
        List<Product> products = new ArrayList<>();
        String[] categories = {"T-Shirt", "Jeans", "Jacket", "Shoes", "Dress", "Hoodie"};
        String[] sizes = {"XS", "S", "M", "L", "XL", "XXL"};
        String[] colors = {"Black", "White", "Blue", "Red", "Green", "Grey"};

        for (Brand brand : brands) {
            for (int i = 0; i < 8; i++) {
                String category = categories[random.nextInt(categories.length)];
                Product product = new Product(
                        null,
                        brand.getName() + " " + category + " " + (i + 1),
                        "Premium quality " + category.toLowerCase(),
                        BigDecimal.valueOf(29.99 + random.nextInt(200)),
                        category,
                        sizes[random.nextInt(sizes.length)],
                        colors[random.nextInt(colors.length)],
                        random.nextInt(100) + 10,
                        brand,
                        null
                );
                products.add(productRepository.save(product));
            }
        }

        return products;
    }

    private List<Sale> createSales(List<Product> products) {
        List<Sale> sales = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Product product = products.get(random.nextInt(products.size()));
            int quantity = random.nextInt(3) + 1;
            BigDecimal unitPrice = product.getPrice();

            Sale sale = new Sale(
                    null,
                    product,
                    quantity,
                    unitPrice,
                    unitPrice.multiply(BigDecimal.valueOf(quantity)),
                    "CUST-" + (1000 + random.nextInt(50)),
                    LocalDateTime.now().minusDays(random.nextInt(90)),
                    null
            );
            sales.add(saleRepository.save(sale));
        }

        return sales;
    }

    private void createReturns(List<Sale> sales) {
        String[] reasons = {
                "Wrong size",
                "Defective product",
                "Changed mind",
                "Not as described"
        };

        // Create returns for about 15% of sales
        for (int i = 0; i < 15; i++) {
            Sale sale = sales.get(random.nextInt(sales.size()));
            int returnQuantity = random.nextInt(sale.getQuantity()) + 1;
            BigDecimal refundAmount = sale.getUnitPrice()
                    .multiply(BigDecimal.valueOf(returnQuantity));

            Return returnItem = new Return(
                    null,
                    sale,
                    returnQuantity,
                    refundAmount,
                    reasons[random.nextInt(reasons.length)],
                    sale.getSaleDate().plusDays(random.nextInt(14) + 1),
                    null
            );
            returnRepository.save(returnItem);
        }
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
                    true,
                    null
            );
            cartRepository.save(cart);
        }
    }
}