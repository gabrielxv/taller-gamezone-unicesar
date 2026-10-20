# Requirements 1–8 Traceability Matrix

This matrix maps the integrated implementation to the requirement areas used by the team. It is a review aid, not a substitute for the complete original assignment statements.

| Requirement | Expected area | Main implementation | Evidence / review |
|---|---|---|---|
| 1 — Accessories | Controller, cable, memory, compatibility, persistence, sales | `model/Accessory.java`, `Controller.java`, `Cable.java`, `Memory.java`, `AccessoryRepository.java`, `AccessoryService.java`, `ConsoleUI.java`, `SaleService.java` | `AccessoryModuleTest`; `docs/accessory-class-diagram.md` |
| 2 — Promotions | Percentage, category, and volume discounts; active date windows; best promotion | `model/Promotion.java`, `PercentageDiscount.java`, `CategoryDiscount.java`, `BulkPurchaseDiscount.java`, `PromotionRepository.java`, `PromotionService.java` | `CategoryDiscountTest`; inspect `data/promotions.csv` |
| 3 — Returns | Sale membership, 30-day window, partial refund, stock restoration, receipts, monthly balance | `model/Return.java`, `ReturnRepository.java`, `ReturnService.java`, `ConsoleUI.java` | `ReturnValidatorTest`, `ReturnReceiptTest`; manual mixed return |
| 4 — Warranties | Six-month basic and twelve-month extended coverage, persistence, cancellation | `model/Warranty.java`, `BasicWarranty.java`, `ExtendedWarranty.java`, `WarrantyRepository.java`, `WarrantyService.java` | `WarrantyServiceTest`; manual return of console |
| 5 — Integration | One sale flow combines products, accessories, discounts, warranties, inventory, persistence | `SaleService.java`, `Main.java`, `ConsoleUI.java` | CI; full scenario in `presentation-guide.md` |
| 6 — Validation/exceptions | Input validation and typed domain failures | `validation/` and `exception/` packages | Existing validator/model tests; review negative cases |
| 7 — GRASP | Controller, Information Expert, Pure Fabrication, Creator, low coupling | Service/repository responsibilities | `docs/grasp-refactor-log.md` |
| 8 — SOLID | SRP, OCP, LSP, ISP, DIP | Strategy hierarchies, gateways, constructor injection, repositories | `docs/solid-analysis.md` |

## Cross-cutting invariants

1. A sale cannot be accepted if a customer or seller is missing, its item list is empty, an item does not exist, or stock is insufficient.
2. A sale line records the unit price and allocated discount used at sale time; a later catalogue price change must not rewrite the historical receipt/refund basis.
3. A category promotion can target only supported categories, including accessories.
4. A return must refer to an item in the original sale and must not return the same item twice.
5. A returned accessory restores accessory stock; a returned product restores product stock.
6. Returning a console cancels its warranties; only the extended-warranty amount is refundable.
7. Monthly net balance equals final sales total minus recorded return refunds for the selected month/year.
8. Persistence errors must be surfaced to the caller; do not report success if the relevant save failed.

## Known limits

- A single filesystem transaction does not span product, accessory, sale, warranty, and return files. A failure between separate writes can leave partial state; the project must not be described as ACID-transactional.
- The exact original text of all separate Requirements 2–4 and 7–8 was not available during this documentation pass. Review this matrix against those PDFs before claiming line-by-line compliance.
- Real teammate names and student codes must be supplied by the team in `TEAM.md`; they cannot be inferred safely from account handles.
