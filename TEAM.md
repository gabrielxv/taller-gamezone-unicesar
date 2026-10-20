# Team

> Complete the two missing identity fields before the final Aula Web submission. The workshop requires the full name and student code of each of the three members.

| Member | Student code | Role | Module | Feature branch |
|---|---|---|---|---|
| Gabrielxv | **TO COMPLETE** | Technical Leader | Sales & Integration | `feature/integration` |
| **TO COMPLETE** | **TO COMPLETE** | Developer 1 | Products | `feature/product-module` |
| **TO COMPLETE** | **TO COMPLETE** | Developer 2 | People | `feature/person-module` |

## Class distribution

### Technical Leader
- `Sale`
- `SaleRepository`
- `SaleService`
- `ConsoleUI`
- `Main`

### Developer 1 — Products
- `Product`
- `VideoGame`
- `Console`
- `ProductRepository`
- `ProductService`

### Developer 2 — People
- `Person`
- `Client`
- `Seller`
- `PersonRepository`
- `PersonService`

## Committed activities

### Technical Leader
Repository configuration, Maven setup, sales implementation, UI integration, application startup, documentation, and module integration.

### Developer 1
Product hierarchy, abstract description behavior, video game and console specializations, file persistence, product service, stock management, and JavaDoc.

### Developer 2
Person hierarchy, role-specific behavior, client and seller specializations, file persistence, people service, seller preload, and JavaDoc.


## Integration phase — Requirements 1–8

The earlier table records the original workshop module distribution. For the later integration requirements, the team reports the following responsibility split:

| Integration responsibility | Owner role | Main files / evidence |
|---|---|---|
| Accessory model and subtype validations | Developer 1 | `Accessory.java`, `Controller.java`, `Cable.java`, `Memory.java`; model tests |
| Accessory repository/service and cross-module services | Developer 2 | `AccessoryRepository.java`, `AccessoryService.java`, promotion/return/warranty services and related tests |
| Cross-module sale flow, application wiring, UI menus, architecture review and release coordination | Technical Leader | `SaleService.java`, `Main.java`, `ConsoleUI.java`, integration docs and PR review |

The current integration branch was prepared with the user's stated responsibility split: Developer 2 and Technical Leader work were carried by the repository owner; the accessory model work remains attributable to the Developer 1 contribution history. This responsibility note does not replace the official names and student codes required above.

**Before submission:** replace every `TO COMPLETE` field with the real full name and student code supplied by each teammate. Do not infer student codes from GitHub accounts.
