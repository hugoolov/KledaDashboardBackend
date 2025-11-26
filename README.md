graph TB
subgraph "Client Layer"
Dashboard[Dashboard Application<br/>Not implemented yet]
end

    subgraph "Spring Boot Backend"
        subgraph "Controller Layer"
            BrandController[Brand Controller]
            SalesController[Sales Controller]
            ProductController[Product Controller]
            CartController[Cart Controller]
        end
        
        subgraph "Service Layer"
            BrandService[Brand Service]
            SalesService[Sales Service]
            ProductService[Product Service]
            CartService[Cart Service]
            AnalyticsService[Analytics Service]
        end
        
        subgraph "Repository Layer"
            BrandRepo[Brand Repository]
            SalesRepo[Sales Repository]
            ProductRepo[Product Repository]
            CartRepo[Cart Repository]
        end
        
        subgraph "Data Layer"
            DummyData[Dummy Data Generator]
            H2DB[(H2 In-Memory DB)]
        end
    end
    
    Dashboard -->|HTTP GET /api/brands/{id}/sales| BrandController
    Dashboard -->|HTTP GET /api/brands/{id}/returns| BrandController
    Dashboard -->|HTTP GET /api/brands/{id}/carts| BrandController
    Dashboard -->|HTTP GET /api/products| ProductController
    Dashboard -->|HTTP GET /api/analytics/{brandId}| SalesController
    
    BrandController --> BrandService
    SalesController --> SalesService
    ProductController --> ProductService
    CartController --> CartService
    
    BrandService --> BrandRepo
    SalesService --> SalesRepo
    ProductService --> ProductRepo
    CartService --> CartRepo
    AnalyticsService --> SalesRepo
    AnalyticsService --> CartRepo
    
    BrandRepo --> H2DB
    SalesRepo --> H2DB
    ProductRepo --> H2DB
    CartRepo --> H2DB
    
    DummyData -.->|Initializes| H2DB
    
    style Dashboard fill:#e1f5ff
    style H2DB fill:#ffe1e1
    style DummyData fill:#fff4e1
```

## Suggested Project Structure
```
clothing-sales-platform/
├── src/main/java/com/yourcompany/platform/
│   ├── PlatformApplication.java
│   ├── controller/
│   │   ├── BrandController.java
│   │   ├── SalesController.java
│   │   ├── ProductController.java
│   │   └── CartController.java
│   ├── service/
│   │   ├── BrandService.java
│   │   ├── SalesService.java
│   │   ├── ProductService.java
│   │   ├── CartService.java
│   │   └── AnalyticsService.java
│   ├── repository/
│   │   ├── BrandRepository.java
│   │   ├── SalesRepository.java
│   │   ├── ProductRepository.java
│   │   └── CartRepository.java
│   ├── model/
│   │   ├── Brand.java
│   │   ├── Sale.java
│   │   ├── Product.java
│   │   ├── Cart.java
│   │   └── Return.java
│   ├── dto/
│   │   ├── BrandAnalyticsDTO.java
│   │   ├── SalesReportDTO.java
│   │   └── CartSummaryDTO.java
│   └── config/
│       └── DummyDataLoader.java
└── src/main/resources/
└── application.properties





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

## Next Steps

This prototype provides:
1. ✅ RESTful API for data retrieval
2. ✅ Dummy data generation
3. ✅ Brand analytics aggregation
4. ✅ Support for sales, returns, carts, and products

To extend this system:
- Add authentication/authorization for brand access
- Implement time-based filtering (daily, weekly, monthly reports)
- Add more analytics endpoints (trending products, customer insights)
- Implement data export functionality
- Add POST endpoints for creating new data
- Connect to a persistent database (PostgreSQL, MySQL)
- Build the frontend dashboard




<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
http://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.0.0</version>
        <relativePath/>
    </parent>

    <groupId>com.platform</groupId>
    <artifactId>clothing-sales-platform</artifactId>
    <version>1.0.0</version>
    <name>Clothing Sales Platform</name>
    <description>Backend platform for clothing sales analytics</description>

    <properties>
        <java.version>21</java.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- H2 Database -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Lombok (optional, but useful) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Spring Boot Starter Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>




# Server Configuration
server.port=8080

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:clothingdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console (optional - useful for debugging)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Logging
logging.level.com.platform=DEBUG
logging.level.org.springframework.web=INFO