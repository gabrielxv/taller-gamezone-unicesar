# Team Work Zones — Partial Exam

## Developer 1 — Practical Draft / Domain Models
**Workspace:** model-layer implementation and first-pass business rules. Work on an assigned feature branch from `develop`; do not commit to `main` or `develop`.

### Responsibilities
- Accessories domain hierarchy: `Accessory`, `Controller`, `Cable`, `Memory`.
- Promotions domain hierarchy: `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount`.
- Returns domain: `Return` and additive `Sale.canBeReturned(...)` behavior.
- Warranties domain: `Warranty`, `BasicWarranty`, `ExtendedWarranty`.
- Keep attributes encapsulated, implement constructors/getters and specified domain methods, and validate invariants that belong to the model.
- Draft/update model-level Mermaid diagrams and leave notes about assumptions or integration contracts.

### Definition of ready for review
- Classes compile against agreed interfaces.
- No persistence or console input/output logic inside model classes.
- Each feature has focused tests or reproducible scenarios.
- Atomic Conventional Commit messages in English.

## Developer 2 — Clean Code / Persistence and Services
**Workspace:** refactor-ready implementation of repositories, services, validation and persistence. Build against agreed model contracts; avoid duplicating business logic.

### Responsibilities
- Repositories/services for accessories, promotions, returns and warranties.
- CSV persistence and seed data where required.
- Return rules: 30-day limit, sale membership, refund calculation, stock restoration and monthly report.
- Promotion selection: choose the highest monetary discount among applicable active promotions.
- Warranty lookup, active/expiring queries and resolution of sale/product IDs without circular repository dependencies.
- Refactor ProductService, PersonService and SaleService to use dedicated validators and typed exceptions once Developer 1's exception/validator contracts are agreed.

### Definition of ready for review
- Persistence failures are wrapped as contextual `PersistenceException`.
- Services coordinate repositories and model objects; repositories do not call services in circular dependency chains.
- Methods have clear names, limited responsibilities and no duplicate validation logic.
- Tests cover invalid inputs, missing resources, business-rule violations and persistence failures.

## Technical Lead — Integration, UI and quality gate
**Workspace:** integration branch `feature/partial-exam-integration`; coordinate interfaces and review PRs.

### Responsibilities
- Keep the four-layer dependency direction: `ui -> service -> persistence -> model` (services may use model types).
- Review accessory PR before integration; coordinate promotions, warranties and returns in the required order.
- Integrate the single sale-registration flow and ensure totals, stock, warranties, discounts and returns remain consistent.
- Refactor `ConsoleMenu`/existing console UI for new operations and differentiated exception messages.
- Review PRs, update README and all required English analysis/diagram documents.
- Run `mvn clean package`, regression scenarios for the ten original operations, and cross-module tests.
- Maintain a requirements checklist and report blockers rather than marking incomplete work as done.

## Collaboration and Git rules
- Create work branches from the current `develop` after syncing.
- Use atomic commits with Conventional Commit prefixes in English; push after each commit.
- Open PRs to `develop`; a different teammate reviews each PR.
- Never commit directly to `main` or `develop`; never use `push --force`.
- Meet the supplied requirement of at least six real atomic commits per member and three approved/merged PRs. Do not manufacture history or reviews.
- Suggested branch families: `feature/accessory-module`, `feature/promotion-module`, `feature/warranty-module`, `feature/return-module`, `feature/exception-handling`, plus named `fix/`, `refactor/`, and `docs/` branches for integration fixes.
