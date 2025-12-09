# Kleda Dashboard Backend

En Spring Boot backend-applikasjon for å administrere klesalgsdata, inkludert salg, returer, produkter og handlevogner. Designet for å støtte merkevaredashboards med analyser.

## Teknologi
- Java 21
- Spring Boot 3.2.0
- Maven
- H2 In-Memory Database (utvikling) / PostgreSQL (produksjon)
- Spring Data JPA
- Spring Security (BCrypt for passordkryptering)
- Lombok

## Prosjektstruktur
```
src/main/java/com/example/kleda_dashboard_backend/
├── KledaDashboardBackendApplication.java  # Hovedapplikasjon
├── Config/
│   └── DummyDataLoader.java              # Laster inn testdata
├── controllers/                           # REST-kontrollere
│   ├── AuthController.java
│   ├── BrandController.java
│   ├── CartController.java
│   ├── ProductController.java
│   └── SalesController.java
├── dtos/                                  # Data Transfer Objects
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   ├── BrandAnalyticsDTO.java
│   ├── CartSummaryDTO.java
│   └── SalesReportDTO.java
├── model/                                 # JPA-entiteter
│   ├── Brand.java
│   ├── Cart.java
│   ├── Category.java
│   ├── Product.java
│   ├── Return.java
│   ├── Sale.java
│   └── User.java
├── repos/                                 # Database repositories
│   ├── BrandRepository.java
│   ├── CartRepository.java
│   ├── ProductRepository.java
│   ├── ReturnRepository.java
│   ├── SaleRepository.java
│   └── UserRepo.java
└── services/                              # Forretningslogikk
    ├── AnalyticsService.java
    ├── AuthService.java
    ├── BrandService.java
    ├── CartService.java
    ├── ProductService.java
    └── SaleService.java
```

## Kjøre Applikasjonen

### Forutsetninger
- Java 21 installert
- Maven installert

### Lokal Utvikling (H2 Database)

1. Naviger til prosjektmappen:
   ```bash
   cd kleda_dashboard_backend
   ```

2. Bygg prosjektet:
   ```bash
   mvn clean install
   ```

3. Kjør applikasjonen:
   ```bash
   mvn spring-boot:run
   ```

4. Applikasjonen starter på `http://localhost:8080`

## API Endepunkter

### Autentisering

#### Sjekk om brukernavn er tilgjengelig
```bash
GET /api/auth/check-username?username=testuser
```

**Respons:**
```json
true
```

#### Registrer ny bruker
```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "brand": "Nike"
}
```

**Respons:**
```json
{
  "userId": 1,
  "username": "testuser"
}
```

#### Logg inn
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

**Respons:**
```json
{
  "userId": 1,
  "username": "testuser"
}
```

### Merkevarer (Brands)

#### Hent alle merkevarer
```bash
GET /api/brands
```

**Respons:**
```json
[
  {
    "id": 1,
    "name": "Nike",
    "description": "Athletic apparel and footwear",
    "country": "USA"
  },
  {
    "id": 2,
    "name": "Adidas",
    "description": "Sports clothing and accessories",
    "country": "Germany"
  }
]
```

#### Hent spesifikk merkevare
```bash
GET /api/brands/{id}
```

**Eksempel:**
```bash
GET /api/brands/1
```

**Respons:**
```json
{
  "id": 1,
  "name": "Nike",
  "description": "Athletic apparel and footwear",
  "country": "USA"
}
```

#### Hent salg for en merkevare
```bash
GET /api/brands/{id}/sales
```

**Eksempel:**
```bash
GET /api/brands/1/sales
```

**Respons:**
```json
[
  {
    "saleId": 1,
    "productName": "Nike T-Shirt 1",
    "brandName": "Nike",
    "quantity": 2,
    "unitPrice": 89.99,
    "totalPrice": 179.98,
    "saleDate": "2024-11-15T14:30:00",
    "customerId": "CUST-1023"
  }
]
```

#### Hent returer for en merkevare
```bash
GET /api/brands/{id}/returns
```

**Eksempel:**
```bash
GET /api/brands/1/returns
```

**Respons:**
```json
[
  {
    "id": 1,
    "sale": {
      "id": 5,
      "product": {...},
      "quantity": 2,
      "totalPrice": 179.98
    },
    "quantity": 1,
    "refundAmount": 89.99,
    "reason": "Wrong size",
    "returnDate": "2024-11-20T10:15:00"
  }
]
```

#### Hent aktive handlevogner for en merkevare
```bash
GET /api/brands/{id}/carts
```

**Eksempel:**
```bash
GET /api/brands/1/carts
```

**Respons:**
```json
[
  {
    "cartId": 1,
    "customerId": "CUST-1015",
    "productName": "Nike Shoes 3",
    "brandName": "Nike",
    "quantity": 1,
    "pricePerUnit": 129.99,
    "subtotal": 129.99,
    "addedAt": "2024-12-05T16:45:00"
  }
]
```

#### Hent analysedata for en merkevare
```bash
GET /api/brands/{id}/analytics
```

**Eksempel:**
```bash
GET /api/brands/1/analytics
```

**Respons:**
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
  "returnRate": 12.00,
  "conversionRate": 75.76
}
```

**Forklaring av analysemetrikker:**
- `totalSales`: Antall fullførte salgstransaksjoner
- `totalRevenue`: Total omsetning fra salg
- `totalReturns`: Antall returnerte varer
- `totalRefunded`: Totalt beløp refundert
- `productsInCarts`: Antall produkter i aktive handlevogner
- `uniqueProductCount`: Antall unike produkter tilbudt av merkevaren
- `averageOrderValue`: Gjennomsnittlig verdi per salgstransaksjon
- `returnRate`: Prosent av salg som ble returnert
- `conversionRate`: Prosent av handlekurv-tillegg som ble konvertert til faktiske salg

### Produkter

#### Hent alle produkter
```bash
GET /api/products
```

**Respons:**
```json
[
  {
    "id": 1,
    "name": "Nike T-Shirt 1",
    "description": "Premium quality t-shirt",
    "price": 89.99,
    "category": "T-Shirt",
    "size": "M",
    "color": "Black",
    "stockQuantity": 45,
    "brand": {
      "id": 1,
      "name": "Nike",
      "description": "Athletic apparel and footwear",
      "country": "USA"
    },
    "createdAt": "2024-12-08T10:00:00"
  }
]
```

#### Hent produkter for en merkevare
```bash
GET /api/products?brandId={id}
```

**Eksempel:**
```bash
GET /api/products?brandId=1
```

#### Hent spesifikt produkt
```bash
GET /api/products/{id}
```

**Eksempel:**
```bash
GET /api/products/1
```

### Salg

#### Hent alle salg
```bash
GET /api/sales
```

**Respons:**
```json
[
  {
    "id": 1,
    "product": {
      "id": 3,
      "name": "Nike Jeans 2",
      "price": 129.99,
      "brand": {...}
    },
    "quantity": 2,
    "unitPrice": 129.99,
    "totalPrice": 259.98,
    "customerId": "CUST-1023",
    "saleDate": "2024-11-15T14:30:00"
  }
]
```

### Handlevogner

#### Hent alle aktive handlevogner
```bash
GET /api/carts
```

**Respons:**
```json
[
  {
    "id": 1,
    "customerId": "CUST-1015",
    "product": {
      "id": 5,
      "name": "Nike Shoes 3",
      "price": 129.99,
      "brand": {...}
    },
    "quantity": 1,
    "addedAt": "2024-12-05T16:45:00",
    "isActive": true
  }
]
```

## Testdata

Applikasjonen laster automatisk inn testdata ved oppstart:
- 4 merkevarer (Nike, Adidas, Zara, H&M)
- 32 produkter (8 per merkevare)
- 100 salgstransaksjoner
- ~15 returer (15% returrate)
- 30 aktive handlevogner

## H2 Database Console (kun lokal utvikling)

Du kan få tilgang til H2-databasekonsollen på:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:clothingdb`
- Brukernavn: `sa`
- Passord: (la stå tomt)

## Feilhåndtering

API-et returnerer passende HTTP-statuskoder:
- `200 OK` - Vellykket forespørsel
- `400 Bad Request` - Ugyldig forespørsel (f.eks. manglende påkrevde felt)
- `404 Not Found` - Ressurs ikke funnet
- `500 Internal Server Error` - Serverfeil

Feilresponser har følgende format:
```json
{
  "error": "Feilmelding her"
}
```

Eller som ren tekst avhengig av endepunkt.

## Utviklingsnotater

- Passord krypteres med BCrypt før lagring
- H2-database brukes for lokal utvikling (data går tapt ved restart)
- PostgreSQL brukes i Docker/produksjonsmiljø (persistent data)
