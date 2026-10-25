# Diagrama SOLID — estrategias y límites de dependencia

El siguiente diagrama destaca los puntos de extensión y las abstracciones de integración identificadas en el código. Las relaciones deben revisarse si el programador 1 cambia los contratos de accesorios.

```mermaid
classDiagram
    class Promotion {
      <<abstract>>
      +calculateDiscount(...)
      +isActive(date)
    }
    class PercentageDiscount
    class CategoryDiscount
    class BulkPurchaseDiscount
    Promotion <|-- PercentageDiscount
    Promotion <|-- CategoryDiscount
    Promotion <|-- BulkPurchaseDiscount

    class Warranty {
      <<abstract>>
      +getDurationMonths()
      +getAdditionalCost()
      +isActive(date)
    }
    class BasicWarranty
    class ExtendedWarranty
    Warranty <|-- BasicWarranty
    Warranty <|-- ExtendedWarranty

    class ProductGateway {
      <<interface>>
      +findById(id)
      +updateStock(id, delta)
    }
    class PeopleGateway {
      <<interface>>
      +customerExists(id)
      +sellerExists(id)
    }
    class ProductService
    class PersonService
    class SaleService
    class AccessoryService
    class PromotionService
    class WarrantyService
    class ReturnService
    class ProductRepository
    class AccessoryRepository
    class PersonRepository
    class SaleRepository
    class PromotionRepository
    class WarrantyRepository
    class ReturnRepository

    ProductService ..|> ProductGateway
    PersonService ..|> PeopleGateway
    SaleService --> ProductGateway
    SaleService --> PeopleGateway
    SaleService --> AccessoryService
    SaleService --> PromotionService
    SaleService --> WarrantyService
    ReturnService --> SaleService
    ReturnService --> ProductService
    ReturnService --> AccessoryService
    ReturnService --> WarrantyService
    ProductService --> ProductRepository
    AccessoryService --> AccessoryRepository
    PersonService --> PersonRepository
    SaleService --> SaleRepository
    PromotionService --> PromotionRepository
    WarrantyService --> WarrantyRepository
    ReturnService --> ReturnRepository
```

## Lectura SOLID
- **SRP:** presentación, servicios de aplicación, dominio y persistencia se mantienen en módulos separados.
- **OCP:** las promociones y garantías se representan con jerarquías polimórficas.
- **LSP:** cada subtipo debe respetar el contrato de su abstracción; la aceptación requiere pruebas por subtipo.
- **ISP:** ProductGateway y PeopleGateway exponen contratos acotados a la integración de ventas.
- **DIP:** SaleService depende de gateways para productos y personas. Algunos servicios aún conocen servicios concretos y los repositorios dependen de archivos; es una simplificación aceptable para una demo local, no una arquitectura de producción plenamente desacoplada.

## Pendientes de verificación
- [ ] Pruebas de contrato de cada implementación de Promotion.
- [ ] Pruebas de contrato de BasicWarranty y ExtendedWarranty.
- [ ] Pruebas de integración de los gateways.
- [ ] Verificar que ningún controlador de UI acceda directamente a repositorios.
- [ ] Volver a generar el diagrama si cambian las firmas reales.
