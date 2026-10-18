# Integrated Class Diagram

```mermaid
classDiagram
    class Product
    class Console
    class VideoGame
    Product <|-- Console
    Product <|-- VideoGame

    class Accessory
    class Controller
    class Cable
    class Memory
    Accessory <|-- Controller
    Accessory <|-- Cable
    Accessory <|-- Memory

    class Sale
    class Promotion
    class PercentageDiscount
    class CategoryDiscount
    class BulkPurchaseDiscount
    Promotion <|-- PercentageDiscount
    Promotion <|-- CategoryDiscount
    Promotion <|-- BulkPurchaseDiscount

    class Warranty
    class BasicWarranty
    class ExtendedWarranty
    Warranty <|-- BasicWarranty
    Warranty <|-- ExtendedWarranty

    class Return

    class ProductGateway
    class PeopleGateway
    class SaleService
    class AccessoryService
    class PromotionService
    class WarrantyService
    class ReturnService

    SaleService --> ProductGateway
    SaleService --> PeopleGateway
    SaleService --> AccessoryService
    SaleService --> PromotionService
    SaleService --> WarrantyService
    ReturnService --> SaleService
    ReturnService --> ProductGateway
    ReturnService --> AccessoryService
    ReturnService --> WarrantyService
    PromotionService --> Promotion
    WarrantyService --> Warranty
    SaleService --> Sale
    ReturnService --> Return
```
