# GRASP Refactor Log

## Leader Technical Responsibilities

| Responsibility | GRASP decision |
|---|---|
| Sale coordination | Controller: `SaleService` coordinates the complete sale use case. |
| Console interaction | Pure Fabrication: `ConsoleUI` handles input/output and delegates business rules. |
| Integration wiring | Creator: `Main` constructs the application object graph. |
| Return workflow | Controller: `ReturnService` coordinates return validation, refund, inventory restoration, and warranty cancellation. |

## Developer 2 Responsibilities

| Responsibility | GRASP decision |
|---|---|
| Accessory persistence | Pure Fabrication: `AccessoryRepository` isolates CSV access. |
| Accessory rules | Information Expert: `AccessoryService` owns accessory inventory and lookup operations. |
| Promotion rules | Polymorphism: each promotion subtype calculates its own discount. |
| Warranty rules | Polymorphism: basic and extended warranties provide their own duration and cost. |
| Return persistence | Pure Fabrication: `ReturnRepository` isolates return storage. |

## Refactor outcome
Responsibilities are kept in services and repositories rather than in the UI or model classes that do not own the required information. The integration flow is explicitly documented to prevent duplicated business logic across modules.
