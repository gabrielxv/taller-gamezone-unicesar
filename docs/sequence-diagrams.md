# Integrated Sequence Diagrams

## Register a sale

```mermaid
sequenceDiagram
    actor User
    participant UI as ConsoleUI
    participant Sale as SaleService
    participant People as PeopleGateway
    participant Products as ProductGateway
    participant Accessories as AccessoryService
    participant Promo as PromotionService
    participant Warranty as WarrantyService
    participant Repo as SaleRepository

    User->>UI: Enter customer, seller, item IDs
    UI->>Sale: registerSale(...)
    Sale->>People: Validate customer and seller
    Sale->>Sale: Normalize IDs and calculate quantities
    Sale->>Products: Validate product stock
    Sale->>Accessories: Validate accessory stock
    Sale->>Sale: Calculate subtotal
    Sale->>Promo: Find best active promotion
    Promo-->>Sale: Promotion / no promotion
    Sale->>Sale: Create historical sale lines and discount allocation
    Sale->>Warranty: Assign console warranties
    Sale->>Products: Decrease product stock
    Sale->>Accessories: Decrease accessory stock
    Sale->>Repo: Save sales
    Repo-->>Sale: Save completed
    Sale-->>UI: Sale and receipt details
    UI-->>User: Display receipt and sale ID
```

## Register a return

```mermaid
sequenceDiagram
    actor User
    participant UI as ConsoleUI
    participant Return as ReturnService
    participant Sale as SaleService
    participant Product as ProductService
    participant Accessory as AccessoryService
    participant Warranty as WarrantyService
    participant Repo as ReturnRepository

    User->>UI: Enter sale ID, item IDs, reason
    UI->>Return: registerReturn(...)
    Return->>Sale: Find original sale
    Return->>Return: Validate period, membership, duplicate return
    Return->>Return: Calculate refund from historical lines
    loop Each returned item
        alt Console
            Return->>Warranty: cancelWarranties(productId, saleId)
            Warranty-->>Return: Refundable extended warranty cost
        end
        alt Product
            Return->>Product: Restore stock
        else Accessory
            Return->>Accessory: Restore stock
        end
    end
    Return->>Repo: Persist return record
    Repo-->>Return: Save completed
    Return-->>UI: Return record and refund amount
    UI-->>User: Display result and return ID
```

## Consistency note

These diagrams describe the intended happy path. The current file-based design writes to several separate files; those writes do not form one atomic transaction. Error-path testing and local demonstration are required before the system is treated as production-ready.
