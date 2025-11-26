```
```

# Clothing Sales Platform - Backend Prototype

A Spring Boot backend system for managing clothing sales data, including sales, returns, products, and shopping carts. Designed to support brand dashboards for analytics.

## Tech Stack
- Java 21
- Spring Boot 4.0.0
- Maven
- H2 In-Memory Database
- Spring Data JPA
- Lombok

## Project Structure
```
src/main/java/com/platform/
├── ClothingSalesPlatformApplication.java  # Main application
├── config/
│   └── DummyDataLoader.java              # Populates dummy data
├── controller/                            # REST controllers
│   ├── BrandController.java
│   ├── CartController.java
│   ├── ProductController.java
│   └── SalesController.java
├── dto/                                   # Data Transfer Objects
│   ├── BrandAnalyticsDTO.java
│   ├── CartSummaryDTO.java
│   └── SalesReportDTO.java
├── model/                                 # JPA entities
│   ├── Brand.java
│   ├── Cart.java
│   ├── Product.java
│   ├── Return.java
│   └── Sale.java
├── repository/                            # Data repositories
│   ├── BrandRepository.java
│   ├── CartRepository.java
│   ├── ProductRepository.java
│   ├── ReturnRepository.java
│   └── SaleRepository.java
└── service/                               # Business logic
    ├── AnalyticsService.java
    ├── BrandService.java
    ├── CartService.java
    ├── ProductService.java
    └── SalesService.java
```

## Running the Application

### Prerequisites
- Java 21 installed
- Maven installed

### Steps
1. Navigate to the project directory:
   ```bash
   cd clothing-sales-platform
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. The application will start on `http://localhost:8080`

## API Endpoints

### Brands
- `GET /api/brands` - Get all brands
- `GET /api/brands/{id}` - Get specific brand
- `GET /api/brands/{id}/sales` - Get sales for a brand
- `GET /api/brands/{id}/returns` - Get returns for a brand
- `GET /api/brands/{id}/carts` - Get active carts containing brand products
- `GET /api/brands/{id}/analytics` - Get aggregated analytics for a brand

### Products
- `GET /api/products` - Get all products
- `GET /api/products?brandId={id}` - Get products by brand
- `GET /api/products/{id}` - Get specific product

### Sales
- `GET /api/sales` - Get all sales

### Carts
- `GET /api/carts` - Get all active carts

## Example API Calls

### Get all brands
```bash
curl http://localhost:8080/api/brands
```

### Get brand analytics
```bash
curl http://localhost:8080/api/brands/1/analytics
```

Response example:
```json
{
  "brandId": 1,
  "brandName": "Nike",
  "totalSales": 25,
  "totalRevenue": 3250.50,
  "totalReturns": 3,
  "totalRefunded": 180.00,
  "productsInCarts": 8,
  "uniqueProductCount": 8,
  "averageOrderValue": 130.02,
  "returnRate": 12.00
}
```

### Get brand sales
```bash
curl http://localhost:8080/api/brands/1/sales
```

### Get products by brand
```bash
curl http://localhost:8080/api/products?brandId=1
```

## Dummy Data

The application automatically loads dummy data on startup:
- 4 brands (Nike, Adidas, Zara, H&M)
- 32 products (8 per brand)
- 100 sales transactions
- ~15 returns (15% return rate)
- 30 active shopping carts

## H2 Database Console

You can access the H2 database console at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:clothingdb`
- Username: `sa`
- Password: (leave empty)
