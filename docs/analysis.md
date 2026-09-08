# GameZone Unicesar — Analysis

## 1. Common and specific person attributes
All people share an identifier, name, email, and phone number. A `Client` adds a membership level, while a `Seller` adds an employee code. These responsibilities are represented through the abstract `Person` base class and two concrete subclasses.

## 2. Generic person
A generic person should not be instantiated because the business process always needs a specific role. `Person` is therefore abstract. This also gives the hierarchy a clear common contract through `getRoleDescription()`.

## 3. Common and specific product attributes
All products have an identifier, name, price, and stock. A `VideoGame` adds platform and genre, while a `Console` adds manufacturer and model. The abstract `Product` class stores the shared inventory information.

## 4. Product description and polymorphism
`Product` declares the abstract `getDescription()` method. Each concrete product implements it with `@Override`, so the same operation produces a type-specific description. This applies abstraction, inheritance, and polymorphism.

## 5. Sale relationships
A sale is associated with one client, one seller, and one or more products. These are business associations rather than inheritance relationships. The sale stores the identifiers of the related records, while services coordinate validation and inventory changes.

## 6. Sale total responsibility
The sale service calculates the total while registering the transaction because it has access to current product prices and stock. The `Sale` domain object remains focused on representing the completed transaction.

## 7. Minimum one product
`SaleService.registerSale` rejects an empty product list before creating the transaction. This rule belongs in the service layer because it is a business constraint rather than a file or UI concern.

## 8. Inventory update
When a sale is registered, `SaleService` validates every requested product and its stock through `ProductGateway`, calculates the total, decreases stock through the product service, and then persists the sale. This keeps the UI independent from persistence.

## 9. Four-layer architecture
The model contains domain entities (`Product`, `VideoGame`, `Console`, `Person`, `Client`, `Seller`, `Sale`). Persistence contains repositories for files. Services contain business rules and coordinate repositories. UI contains the console menu. A class belongs to the layer that owns its responsibility.

## 10. Why file access is outside the domain
Domain classes should not know how data is stored. Keeping file operations in repositories preserves separation of concerns, makes the model easier to test and change, and prevents storage details from spreading through business objects.

## 11. Allowed dependencies
The permitted direction is `ui -> service -> persistence -> model`; services may also depend directly on model classes. UI must not access persistence directly, persistence must not depend on UI or services, and model must remain independent of all other layers. This direction keeps business rules centralized and reduces coupling.
