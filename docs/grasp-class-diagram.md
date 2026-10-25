# Diagrama GRASP — responsabilidades integradas

Este diagrama refleja las responsabilidades de las capas de presentación, aplicación y persistencia. Es un mapa de colaboración, no un sustituto de las clases UML completas.

```mermaid
flowchart TD
    ConsoleUI["ConsoleUI<br/>Interacción por consola"]
    WebServer["WebServer<br/>Adaptador HTTP"]
    Main["Main<br/>Composition Root"]
    ProductService["ProductService<br/>Catálogo y stock"]
    AccessoryService["AccessoryService<br/>Accesorios y compatibilidad"]
    PersonService["PersonService<br/>Clientes y vendedores"]
    PromotionService["PromotionService<br/>Vigencia y selección de descuentos"]
    SaleService["SaleService<br/>Caso de uso: venta"]
    ReturnService["ReturnService<br/>Caso de uso: devolución"]
    WarrantyService["WarrantyService<br/>Asignación/cancelación"]
    ProductRepo["ProductRepository"]
    AccessoryRepo["AccessoryRepository"]
    PersonRepo["PersonRepository"]
    SaleRepo["SaleRepository"]
    PromotionRepo["PromotionRepository"]
    ReturnRepo["ReturnRepository"]
    WarrantyRepo["WarrantyRepository"]

    Main --> ConsoleUI
    Main --> WebServer
    ConsoleUI --> ProductService
    ConsoleUI --> AccessoryService
    ConsoleUI --> PersonService
    ConsoleUI --> PromotionService
    ConsoleUI --> SaleService
    ConsoleUI --> ReturnService
    ConsoleUI --> WarrantyService
    WebServer --> ProductService
    WebServer --> AccessoryService
    WebServer --> PersonService
    WebServer --> PromotionService
    WebServer --> SaleService
    WebServer --> ReturnService
    WebServer --> WarrantyService
    SaleService --> ProductService
    SaleService --> AccessoryService
    SaleService --> PersonService
    SaleService --> PromotionService
    SaleService --> WarrantyService
    ReturnService --> SaleService
    ReturnService --> ProductService
    ReturnService --> AccessoryService
    ReturnService --> WarrantyService
    ProductService --> ProductRepo
    AccessoryService --> AccessoryRepo
    PersonService --> PersonRepo
    SaleService --> SaleRepo
    PromotionService --> PromotionRepo
    ReturnService --> ReturnRepo
    WarrantyService --> WarrantyRepo
```

## Lectura GRASP
- **Controller:** los servicios de casos de uso coordinan venta y devolución.
- **Information Expert:** producto y accesorio administran sus existencias; Promotion delega el descuento en estrategias.
- **Pure Fabrication:** los repositorios aíslan el formato de persistencia.
- **Creator:** Main ensambla dependencias y los servicios crean entidades de sus operaciones.
- **Low Coupling:** los gateways limitan la dependencia de la venta respecto al catálogo y las personas.
- **High Cohesion:** cada servicio agrupa operaciones del mismo concepto de negocio.

## Riesgo de consistencia
Venta y devolución coordinan más de un repositorio. Los archivos separados no ofrecen una transacción ACID global; una futura persistencia transaccional o una compensación explícita debe diseñarse y probarse antes de uso productivo.
