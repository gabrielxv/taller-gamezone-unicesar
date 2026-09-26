# Accessory Module Analysis

## 1. Integration with the existing Product hierarchy

Accessories should extend the existing `Product` hierarchy. `Product` already contains the information shared by every sellable item: identifier, name, price, and stock. Extending `Product` avoids duplicating those attributes and allows `SaleService` to treat products and accessories as sellable inventory items while preserving the existing model. The concrete accessory classes only need to add their specific characteristics.

## 2. Common and specific attributes

The three accessory types share the inherited `Product` attributes: `id`, `name`, `price`, and `stock`. They also share a list of compatible console identifiers, which is represented in the abstract `Accessory` class.

`Controller` adds `connectionType`. `Cable` adds `lengthMeters` and `connectorType`. `Memory` adds `capacityGb` and `memoryType`. This structure keeps common state in the abstract class and specific state in each concrete subclass.

## 3. Compatibility representation and persistence

Compatibility is represented as a list of console identifiers inside `Accessory`. The relationship is therefore stored from the accessory side, because the required operation is to find all accessories compatible with a given console. The CSV persistence stores the identifiers in the `compatibleConsoleIds` field separated by `|`. This keeps the model independent from file access and allows the service to filter accessories without adding persistence logic to the model classes.

## 4. Changes required in SaleService

`SaleService.registerSale` must accept a list containing identifiers of both existing products and accessories. For every identifier, the service first resolves the item as an existing product and, when it is not a product, resolves it through `AccessoryService`. Stock is validated before any inventory is changed. The price is obtained from the corresponding item type and contributes to the same sale total. Finally, inventory is decreased through `ProductService` for products or `AccessoryService` for accessories. This preserves the existing product behavior while adding accessory sales to the same transaction.

## 5. Placement in the four-layer architecture

The new classes follow the existing four-layer architecture. `Accessory`, `Controller`, `Cable`, and `Memory` belong to `model` because they represent domain entities. `AccessoryRepository` belongs to `persistence` because it reads and writes `data/accessories.csv`. `AccessoryService` belongs to `service` because it applies business rules and coordinates the repository. `ConsoleUI` belongs to `ui` because it handles user interaction. The resulting dependency direction remains `ui -> service -> persistence -> model`, with service classes also using model entities.
