# GameZone Integrated Requirements 1–8

## Scope and verification boundary

This branch combines the Leader Technical integration and Developer 2 service/persistence work with the accessory model branch. It covers the cross-module wiring for accessories, promotions, warranties, returns, validation, and the GRASP/SOLID design audit. The original Requerimiento 1 and Requerimientos 5–6 files were available for traceability; the exact full text of separate original Requirements 2–4 and 7–8 was not present in the repository/library when this document was updated. Therefore, this file maps the implemented behavior and known integration adjustments without claiming verbatim compliance with unavailable clauses.

## Requirement 1 — Accessories

- Model hierarchy: `Accessory` extends `Product`; `Controller`, `Cable`, and `Memory` specialize it.
- `AccessoryRepository` persists discriminated CSV records; `AccessoryService` registers, lists, searches, checks compatibility, and changes/restores stock.
- `ConsoleUI` exposes registration, filtering, compatibility queries, and sales integration.
- `SaleService` supports product and accessory IDs in the same sale.
- Related files: `src/main/java/com/gamezone/model/Accessory.java`, `Controller.java`, `Cable.java`, `Memory.java`, `persistence/AccessoryRepository.java`, `service/AccessoryService.java`.

## Requirement 2 — Promotions

- `Promotion` defines the common discount contract; `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount` implement different strategies.
- `PromotionRepository` persists strategy types and their specific fields.
- `PromotionService` lists active promotions and chooses the largest applicable monetary discount.
- Category targeting supports `VIDEOGAME`, `CONSOLE`, and `ACCESSORY`, including the integration extension A1.
- `SaleService` records the applied promotion and discount in the sale and historical lines.

## Requirement 3 — Returns

- `ReturnService` validates the sale, 30-day window, reason, sale membership, and duplicate returns.
- Refunds use the recorded sale-line unit price and allocated discount when historical lines are available.
- Successful returns restore product/accessory stock and persist a return record.
- Monthly reporting exposes sales total, return total, and net balance; return receipts include item and warranty refund breakdowns.
- A remaining release-quality concern is filesystem transactionality across multiple repositories: a write failure after one repository has saved can still require manual reconciliation. Do not describe the multi-file workflow as ACID-transactional.

## Requirement 4 — Warranties

- `Warranty` is the base type with `BasicWarranty` (six months, no additional charge) and `ExtendedWarranty` (twelve months, additional cost of 10% of product price).
- `WarrantyRepository` persists a type discriminator and identifiers without depending on `SaleService`.
- `WarrantyService` assigns, lists, and cancels warranties. Returning a console cancels its warranties and refunds the extended-warranty charge.
- Assignment is idempotent per product/sale/type, avoiding duplicate extended charges when the same console ID occurs more than once in the requested sale items.

## Requirement 5 — Unified integration

The integrated registration path is ordered as follows:

1. Validate customer, seller, and non-empty item list.
2. Resolve repeated IDs into quantities and validate all required stock.
3. Calculate the catalogue subtotal.
4. Find and apply the best active promotion.
5. Build historical sale lines with price and allocated discount.
6. Assign basic and optionally selected extended warranties to consoles.
7. Calculate `subtotal - discount + extended warranty cost`.
8. Update product and accessory stock and persist the sale.

Adjustment A2 removes the warranty-repository dependency on `SaleService`; A3 standardizes this sale sequence.

## Requirement 6 — Validation and exceptions

- Dedicated validators cover sales, returns, and warranties.
- The `com.gamezone.exception` package distinguishes general GameZone failures, business-rule violations, invalid data, missing resources, and persistence failures.
- Inputs that must be rejected include missing IDs, empty item lists, invalid prices, insufficient stock, returns outside the permitted period, items not belonging to the sale, and duplicate returns.
- A6 adds the three monthly report values; A5 keeps discounted refunds proportional to the original sale.

## Requirement 7 — GRASP

- **Controller:** `SaleService` coordinates sale registration; `ReturnService` coordinates returns.
- **Information Expert:** entities own their domain state and polymorphic calculations.
- **Pure Fabrication:** repositories isolate file I/O; services group application-level workflows.
- **Creator:** `Main` builds and injects the application object graph.
- **Low Coupling / High Cohesion:** the UI delegates to services, and `ProductGateway` / `PeopleGateway` narrow integration contracts.

See [GRASP audit](grasp-refactor-log.md).

## Requirement 8 — SOLID

- **SRP:** UI, services, models, and repositories have separate responsibilities.
- **OCP:** promotion and warranty variants use polymorphic hierarchies.
- **LSP:** concrete promotion/warranty implementations follow their base contracts.
- **ISP:** integration uses focused gateway interfaces.
- **DIP:** sale coordination depends on gateway abstractions; repositories are injected into services.

See [SOLID audit](solid-analysis.md). The audit documents the design intent; it does not substitute for code review or tests.

## Integration adjustments A1–A7

| ID | Risk / cause | Integrated response | Verification target |
|---|---|---|---|
| A1 | Category promotions did not include accessories. | Added `ACCESSORY` targeting to category discount logic, registration, and seed data. | `CategoryDiscountTest`; manual accessory-category sale. |
| A2 | Warranty loading could form a dependency cycle through sales. | Warranty persistence stores identifiers; the service receives the product/sale dependencies it needs. | Start-up wiring in `Main`; reload persisted warranties. |
| A3 | Multiple modules changed sale totals and ordering independently. | `SaleService.registerSale` uses one ordered workflow. | Sale tests/manual mixed sale. |
| A4 | Returning an accessory must restore accessory inventory, not product inventory. | Return workflow selects the appropriate service for stock restoration. | Mixed product/accessory return scenario. |
| A5 | Refunding list price after a discount over-refunds the customer. | Historical line totals and allocated discounts are used for the item refund. | `ReturnReceiptTest`; sale with active promotion. |
| A6 | Monthly net balance alone does not show the required totals. | Separate sales and return totals are displayed with the net balance. | Monthly report for a known month/year. |
| A7 | A returned console must not keep its warranty. | Cancellation removes matching warranties and returns the extended charge for refund. | Return a console with both warranty types. |

## Known release gates

- The latest CI run must be green on the latest commit, not only on an earlier commit.
- The team must complete the real names/student codes in `TEAM.md`.
- The project must be reviewed/merged into `develop`, then released to `main` through the required PR process.
- The team must execute the full demo locally and confirm that persisted files reload correctly.
- Filesystem writes across sales, inventory, warranties, and returns are not a single atomic transaction; exercise failure paths and reconcile before production use.
