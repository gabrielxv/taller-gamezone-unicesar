# GameZone Unicesar

**Integrated Java 17/Maven console application** for managing products, accessories, people, promotions, sales, warranties, returns, and inventory.

## Requirements

- Java 17+
- Maven 3.9+
- Internet access on the first Maven build if dependencies are not cached

## Build, test, and run

From the repository root:

```bash
mvn --batch-mode clean verify
mvn exec:java -Dexec.mainClass="com.gamezone.Main"
```

If the Exec Maven plugin is unavailable in an IDE, run `com.gamezone.Main` from the IDE after compiling the project. The Maven CI workflow runs `mvn --batch-mode clean verify` on pushes to develop/feature/fix branches, and on pull requests targeting either `develop` or `main`.

## Architecture

The system uses four layers and keeps business rules out of the console UI:

- **UI:** `ConsoleUI` — input, output, and menu navigation.
- **Service:** `ProductService`, `PersonService`, `AccessoryService`, `PromotionService`, `WarrantyService`, `SaleService`, and `ReturnService`.
- **Persistence:** repositories for products, people, sales, accessories, promotions, warranties, and returns.
- **Model:** products, people, sales/lines, accessories, promotion strategies, warranty types, and return records.

The intended dependency direction is `ui → service → persistence`; services and repositories use model objects. The sale integration uses `ProductGateway` and `PeopleGateway` to reduce coupling to the product and people modules.

See [the layer diagram](docs/layers-diagram.md) and [the integrated class diagram](docs/integrated-class-diagram.md).

## Console menu

The main menu opens seven modules:

1. **Products:** register video games and consoles, list products, update stock.
2. **Accessories:** register controllers, cables, and memories; list by type; query compatibility with a console.
3. **People:** register/list customers and list sellers.
4. **Sales:** register sales, list receipts, and view sales by customer or seller.
5. **Promotions:** register percentage, category, and bulk-purchase discounts; list all/active promotions.
6. **Returns:** register partial returns, list returns, query by customer/sale, generate return receipts, and view monthly sales/returns/net balance.
7. **Warranties:** list all/active warranties and those expiring soon.

## Integrated business rules

- A sale can contain video games, consoles, and accessories.
- Sale registration validates customer, seller, item existence, and available stock before the inventory update.
- The promotion service selects the highest applicable active discount; category promotions support `VIDEOGAME`, `CONSOLE`, and `ACCESSORY`.
- Consoles receive a basic six-month warranty. An extended twelve-month warranty costs 10% of the console's price when selected.
- Returns validate the 30-day window and sale membership, apply historical sale-line prices/discount allocation, restore stock, cancel console warranties, and include refundable extended-warranty cost.
- Monthly reporting shows total final sales, returns, and net balance.

## Data files

Files live under `data/` and load automatically at startup:

- `products.dat`, `people.dat`, `sales.dat`: Java-serialized application data created at runtime.
- `accessories.csv`: initial controller/cable/memory catalogue.
- `promotions.csv`: initial percentage, video-game category, bulk, and accessory-category promotions.
- `warranties.csv`, `returns.csv`: persisted warranty and return records created as the application is used.
- `seed-sellers.txt`: seller seed reference; `Main` ensures three sellers exist.

**Demo note:** the accessory-category promotion in `data/promotions.csv` is active from 19 October through 31 December 2026. Change the dates if the assignment requires a different promotion window.

## Test coverage

JUnit 5 tests cover accessory validations/persistence, category discounts, product price validation, sale-line behavior, return validation/receipts, and warranty assignment/persistence. Do not treat the project as release-ready until CI passes for the exact release commit and the main sale/return workflow has been exercised from a clean data directory.

## Documentation index

- [Requirement traceability 1–8](docs/requirements-traceability.md)
- [Integration analysis and adjustments A1–A7](docs/integration-analysis.md)
- [Integrated class diagram](docs/integrated-class-diagram.md)
- [Layer architecture](docs/layers-diagram.md)
- [Sale/return sequence diagrams](docs/sequence-diagrams.md)
- [Presentation and demo guide](docs/presentation-guide.md)
- [Accessory analysis](docs/accessory-analysis.md)
- [Accessory class diagram](docs/accessory-class-diagram.md)
- [GRASP responsibility audit](docs/grasp-refactor-log.md)
- [SOLID audit](docs/solid-analysis.md)
- [Submission checklist](docs/submission-checklist.md)
- [Internal technical review and release gates](docs/internal-review-pr12.md)
- [Team roles and identity fields](TEAM.md)
- [AI usage logs](docs/ai-usage/)
