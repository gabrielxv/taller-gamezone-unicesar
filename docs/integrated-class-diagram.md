# Integrated Class Diagram

The diagram summarizes the domain hierarchy, service coordination, gateways, and persistence boundaries for Requirements 1–8. It is a high-level diagram; individual classes remain documented in their Java source files.

```mermaid
classDiagram
    class Product {
        <<abstract>>
        -String id
        -String name
        -double price
        -int stock
    }
    class Console
    class VideoGame
    class Accessory {
        <<abstract>>
        -List~String~ compatibleConsoleIds
    }
    class Controller
    class Cable
    class Memory
    Product <|-- Console
    Product <|-- VideoGame
    Product <|-- Accessory
    Accessory <|-- Controller
    Accessory <|-- Cable
    Accessory <|-- Memory

    class Person {
        <<abstract>>
    }
    class Client
    class Seller
    Person <|-- Client
    Person <|-- Seller

    class Sale {
        -String id
        -LocalDateTime date
        -double subtotal
        -double discountAmount
        -double warrantyCost
        -double total
    }
    class SaleLine {
        -String productId
        -double unitPriceAtSale
        -int quantity
        -double discountAmount
    }
    Sale "1" *-- "1..*" SaleLine : historical lines

    class Promotion {
        <<abstract>>
    }
    class PercentageDiscount
    class CategoryDiscount
    class BulkPurchaseDiscount
    Promotion <|-- PercentageDiscount
    Promotion <|-- CategoryDiscount
    Promotion <|-- BulkPurchaseDiscount

    class Warranty {
        <<abstract>>
    }
    class BasicWarranty
    class ExtendedWarranty
    Warranty <|-- BasicWarranty
    Warranty <|-- ExtendedWarranty

    class Return {
        -String id
        -String saleId
        -double refundAmount
        -double warrantyRefundAmount
    }

    class ProductGateway {
        <<interface>>
    }
    class PeopleGateway {
        <<interface>>
    }
    class ProductService
    class PersonService
    class AccessoryService
    class PromotionService
    class WarrantyService
    class SaleService
    class ReturnService
    class ConsoleUI
    class Main

    class ProductRepository
    class PersonRepository
    class AccessoryRepository
    class PromotionRepository
    class SaleRepository
    class WarrantyRepository
    class ReturnRepository

    ProductService ..|> ProductGateway
    PersonService ..|> PeopleGateway
    SaleService --> ProductGateway : lookup and stock
    SaleService --> PeopleGateway : customer/seller validation
    SaleService --> AccessoryService : accessory lookup/stock
    SaleService --> PromotionService : best active promotion
    SaleService --> WarrantyService : console warranties
    SaleService --> SaleRepository : persist sales
    ReturnService --> SaleService : sale history
    ReturnService --> ProductService : restore product stock
    ReturnService --> AccessoryService : restore accessory stock
    ReturnService --> WarrantyService : cancel console warranties
    ReturnService --> ReturnRepository : persist returns
    PromotionService --> PromotionRepository
    WarrantyService --> WarrantyRepository
    AccessoryService --> AccessoryRepository
    ProductService --> ProductRepository
    PersonService --> PersonRepository
    ConsoleUI --> ProductService
    ConsoleUI --> AccessoryService
    ConsoleUI --> PersonService
    ConsoleUI --> SaleService
    ConsoleUI --> PromotionService
    ConsoleUI --> ReturnService
    ConsoleUI --> WarrantyService
    Main ..> ConsoleUI : wires dependencies
```

## Key design decisions

- Inheritance models product, person, promotion, and warranty variations.
- `SaleLine` preserves historical item prices and allocated discounts so later refunds do not depend on current catalogue prices.
- Services coordinate business workflows; repositories isolate storage; the UI delegates to services.
- Gateway interfaces reduce coupling between sale coordination and the product/people implementations.
