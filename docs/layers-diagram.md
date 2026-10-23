# Layer Architecture

The arrows show the allowed dependency direction. UI code delegates business decisions to services; services coordinate model objects and repository operations; repositories serialize or parse model data.

```mermaid
flowchart TB
    subgraph UI["Presentation layer"]
        CUI["ConsoleUI"]
    end

    subgraph SERVICES["Application / business layer"]
        PS["ProductService"]
        PeS["PersonService"]
        AS["AccessoryService"]
        PrS["PromotionService"]
        WS["WarrantyService"]
        SS["SaleService"]
        RS["ReturnService"]
    end

    subgraph PERSISTENCE["Persistence layer"]
        PR["ProductRepository"]
        PeR["PersonRepository"]
        AR["AccessoryRepository"]
        PrR["PromotionRepository"]
        SR["SaleRepository"]
        WR["WarrantyRepository"]
        RR["ReturnRepository"]
    end

    subgraph MODEL["Domain model"]
        Product["Product / Console / VideoGame"]
        Accessory["Accessory / Controller / Cable / Memory"]
        Person["Person / Client / Seller"]
        Sale["Sale / SaleLine"]
        Promotion["Promotion strategies"]
        Warranty["Warranty / Basic / Extended"]
        Return["Return"]
    end

    CUI --> PS
    CUI --> PeS
    CUI --> AS
    CUI --> PrS
    CUI --> SS
    CUI --> WS
    CUI --> RS

    PS --> PR
    PeS --> PeR
    AS --> AR
    PrS --> PrR
    SS --> SR
    WS --> WR
    RS --> RR

    PS --> Product
    PeS --> Person
    AS --> Accessory
    PrS --> Promotion
    SS --> Sale
    WS --> Warranty
    RS --> Return

    PR --> Product
    PeR --> Person
    AR --> Accessory
    PrR --> Promotion
    SR --> Sale
    WR --> Warranty
    RR --> Return
```

## Dependency rules

1. `ConsoleUI` contains interaction logic only; it does not implement promotion, refund, or inventory rules.
2. Business rules live in the relevant service/model classes.
3. Repositories own file I/O and do not display console prompts.
4. `SaleService` consumes `ProductGateway` and `PeopleGateway` contracts rather than depending on concrete repositories.
