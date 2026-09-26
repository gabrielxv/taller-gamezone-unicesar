# GameZone Unicesar

Console-based Java/Maven application for managing products, accessories, people, inventory, and sales for the GameZone Unicesar workshop.

## Requirements
- Java 17 or later
- Maven 3.9+

## Build

```bash
mvn clean package
```

## Run

```bash
mvn exec:java -Dexec.mainClass="com.gamezone.Main"
```

If the Maven Exec plugin is unavailable in a local setup, run the compiled `com.gamezone.Main` class from the IDE.

## Architecture

The project uses four layers:

- `model`: domain entities and inheritance hierarchies.
- `persistence`: file-based repositories.
- `service`: business rules and module integration.
- `ui`: console interaction.

Dependencies follow `ui -> service -> persistence -> model`, with services also depending on model classes.

## Functional operations

1. Register video game
2. Register console
3. List products
4. Update product stock
5. Register controller
6. Register cable
7. Register memory
8. List all accessories
9. List accessories by type
10. List accessories compatible with a console
11. Register client
12. List clients
13. List sellers
14. Register sale with products and/or accessories
15. View all sales
16. View customer purchase history
17. View seller sales history

## Accessory module

The accessory module contains the abstract `Accessory` class and the concrete `Controller`, `Cable`, and `Memory` classes. Accessories inherit the common sellable fields from `Product` and keep a list of compatible console identifiers.

Accessory persistence uses `data/accessories.csv` with a type discriminator. The service provides registration, filtering by type, compatibility queries, identifier lookup, and stock updates.

Sales accept any combination of existing products and accessories. Product inventory is updated through `ProductService`, while accessory inventory is updated through `AccessoryService`.

## Data

Data is stored under `data/` and loaded automatically at startup. The accessory module includes three preloaded records in `data/accessories.csv`, one controller, one cable, and one memory.

## Documentation

- `docs/accessory-analysis.md`: analysis of the accessory design and integration.
- `docs/accessory-class-diagram.md`: Mermaid diagram for the accessory module and its integration.
- `docs/class-diagram.md`: updated system class diagram.
- `docs/layers-diagram.md`: updated four-layer architecture diagram.
