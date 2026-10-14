# Partial Exam Implementation Plan — GameZone Unicesar

## Purpose
Integrate Requirements 1–6 into the existing Java 17/Maven application while preserving the four-layer architecture and the ten original menu operations.

## Baseline and branch
- Repository: `gabrielxv/taller-gamezone-unicesar`
- Integration branch created from `develop`: `feature/partial-exam-integration`
- Do not commit directly to `main` or `develop`.
- Review and integrate the open accessory module PR before treating Requirement 1 as complete.

## Scope from the six supplied requirements
1. **Accessories:** `Accessory`, `Controller`, `Cable`, `Memory`, compatibility with consoles, CSV repository/service, menu operations and mixed-item sales.
2. **Promotions:** abstract `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount`, repository/service, best applicable promotion and receipt breakdown.
3. **Returns:** partial returns, 30-calendar-day rule, product-sale membership, stock restoration, refund calculation, customer/sale queries and monthly net balance.
4. **Warranties:** abstract `Warranty`, `BasicWarranty`, `ExtendedWarranty`, warranty repository/service, 6/12-month validity, expiring-soon query and extended warranty fee.
5. **Integration:** coordinate modules in `SaleService.registerSale`; apply promotion to item subtotal, then add extended-warranty costs; persist sale/warranties; restore returned stock and account for discounts and warranty cancellation as required.
6. **Validation and exceptions:** `GameZoneException` with `ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`; module validators; refactor services/repositories; differentiated safe messages in `ConsoleMenu`.

## Required integration order
1. Review the accessory PR and merge it into `develop` only after checking its diff and tests.
2. Integrate accessories.
3. Integrate promotions; extend category discounts to `ACCESSORY`.
4. Integrate warranties; remove circular dependencies by storing sale/product IDs in the warranty repository and resolving references in the service.
5. Integrate returns; restore accessory stock and correctly calculate refunds for discounted sales.
6. Close cross-module cases: cancel/reconcile warranties on returns, monthly balance, data files and console workflows.
7. Add typed validation/exceptions across all modules.
8. Run `mvn clean package`, exercise all ten original operations and all new menu paths, review documentation, then prepare the release PR from `develop` to `main`.

## Integration invariants
- Existing ten menu operations continue to work.
- One sale can contain videogames, consoles and accessories.
- Only the single highest applicable promotion is applied.
- Promotion discount is calculated on item subtotal, before extended-warranty costs.
- Final total = subtotal − discount + extended-warranty costs.
- Basic warranty is created automatically for each console; extended warranty is optional and costs 10% of that console's price.
- Returns are partial, limited to 30 calendar days, and only include items belonging to the original sale.
- Successful returns restore the correct inventory and reconcile refund, promotion and warranty effects according to the supplied integration requirements.
- Persistence errors are translated into contextual `PersistenceException`; users never see raw stack traces.

## Evidence for assessment
- Build output from `mvn clean package`.
- Tests or repeatable test scenarios for each business rule and cross-module case.
- Updated Mermaid class diagrams and English analysis files named by the requirements.
- README with run/build instructions, module summary and architecture.
- Reviewable atomic commits and PRs; do not fabricate commit history, reviews or test results.
