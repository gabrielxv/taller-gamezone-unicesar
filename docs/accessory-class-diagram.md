# Accessory Class Diagram

```mermaid
classDiagram
    class Product {
        <<abstract>>
        -String id
        -String name
        -double price
        -int stock
        +getId() String
        +getName() String
        +getPrice() double
        +getStock() int
        +getDescription() String
    }

    class Accessory {
        <<abstract>>
        -List~String~ compatibleConsoleIds
        +getCompatibleConsoleIds() List~String~
        +setCompatibleConsoleIds(List~String~) void
        +addCompatibleConsole(String) void
        +isCompatibleWith(String) boolean
        +getDescription() String
    }

    class Controller {
        -String connectionType
        +getConnectionType() String
        +setConnectionType(String) void
        +getDescription() String
    }

    class Cable {
        -double lengthMeters
        -String connectorType
        +getLengthMeters() double
        +setLengthMeters(double) void
        +getConnectorType() String
        +setConnectorType(String) void
        +getDescription() String
    }

    class Memory {
        -int capacityGb
        -String memoryType
        +getCapacityGb() int
        +setCapacityGb(int) void
        +getMemoryType() String
        +setMemoryType(String) void
        +getDescription() String
    }

    class Console
    class VideoGame
    class Sale
    class AccessoryRepository
    class AccessoryService
    class ProductService
    class SaleService
    class ConsoleUI

    Product <|-- VideoGame
    Product <|-- Console
    Product <|-- Accessory
    Accessory <|-- Controller
    Accessory <|-- Cable
    Accessory <|-- Memory

    AccessoryRepository --> Accessory
    AccessoryService --> AccessoryRepository
    AccessoryService --> Accessory
    SaleService --> ProductService
    SaleService --> AccessoryService
    SaleService --> Sale
    ConsoleUI --> AccessoryService
    ConsoleUI --> SaleService
```

The compatibility relation is represented by `Accessory.compatibleConsoleIds`, which stores console identifiers rather than direct console objects. This keeps the model independent from persistence and avoids introducing a repository dependency into the model layer.
