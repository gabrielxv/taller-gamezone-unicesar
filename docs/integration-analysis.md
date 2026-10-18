# GameZone Integrated Requirements 1-8

## Scope
This branch integrates the Leader Technical responsibilities and Developer 2 responsibilities. The existing accessory model classes are inherited from `feature/accessory-module` and are not rewritten here.

## Requirement 1 - Accessories
- Developer 2 persistence and service responsibilities are retained in `AccessoryRepository` and `AccessoryService`.
- Leader integration is implemented in `SaleService`, `Main`, and `ConsoleUI`.
- Accessory stock is restored by `AccessoryService.restoreStock` during returns.

## Requirement 2 - Promotions
- `Promotion` uses a strategy hierarchy for percentage, category, and bulk discounts.
- `PromotionRepository` stores the discriminator and promotion-specific fields in CSV.
- `PromotionService.findBestPromotionFor` evaluates all active promotions and chooses the highest monetary discount.
- The leader applies the selected promotion to the sale subtotal.

## Requirement 3 - Returns
- `ReturnRepository` persists return records.
- `ReturnService` validates the thirty-day window and sale membership, calculates proportional refunds, restores inventory, and reports the monthly balance.
- Returned consoles trigger warranty cancellation.

## Requirement 4 - Warranties
- `Warranty` uses a polymorphic hierarchy with `BasicWarranty` and `ExtendedWarranty`.
- `WarrantyRepository` persists both types using a discriminator.
- `WarrantyService` assigns six-month basic warranties automatically and twelve-month extended warranties at ten percent of the product price.

## Requirement 5 - Unified integration
The sale workflow is ordered as follows:
1. Validate customer, seller, and items.
2. Resolve products and accessories and validate stock.
3. Create the sale subtotal.
4. Apply the best active promotion to the subtotal.
5. Assign basic and requested extended warranties.
6. Calculate `subtotal - discount + extended warranty cost`.
7. Update product and accessory stock.
8. Persist the sale.

## Requirement 6 - Validation and exceptions
A dedicated GameZone exception hierarchy was added for resource, business-rule, invalid-data, and persistence failures. The integrated services distinguish missing resources and business-rule failures from ordinary input errors.

## Requirement 7 - GRASP
The integration concentrates coordination in application services while keeping entities responsible for their own state and polymorphic calculations. The audit records the responsibility boundaries used by the Leader and Developer 2.

## Requirement 8 - SOLID
Promotion strategies use OCP and polymorphism. `ProductGateway` and `PeopleGateway` reduce high-level dependencies on concrete modules. Return, warranty, promotion, and accessory persistence are separated from business services, supporting SRP and DIP.
