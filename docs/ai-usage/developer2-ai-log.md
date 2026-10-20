# AI Usage Log — Developer 2

- AI assistance was used to implement the Person hierarchy and its persistence/service integration.
- Assistance covered Java inheritance, abstract behavior, validation, file serialization, and the PeopleGateway contract used by sales.
- The resulting module contains `Person`, `Client`, `Seller`, `PersonRepository`, and `PersonService`.

## Integration review — 2026-10-20

- **Tool:** ChatGPT (OpenAI).
- **Phase / branch:** Requirements 1–8 integration review; `feature/leader-dev2-requirements-1-8`.
- **Objective:** Validate Developer 2's warranty-service responsibilities and remove duplicate warranty assignment when the same console identifier appears more than once in a sale.
- **Consultation:** Asked for the integrated repository to be finalized for presentation, including code, comments/documentation, and Mermaid diagrams.
- **Response summary:** The review found that warranty lookup returned the first warranty of any type and cast it to `BasicWarranty`, and that extended warranties could be added repeatedly for the same product/sale.
- **Decision:** Basic and extended warranties are now queried by their concrete type and assignment is idempotent per product/sale/type. The sale integration deduplicates repeated console identifiers before warranty assignment. Added `WarrantyServiceTest` for idempotency, distinct warranty types, cost calculation, and CSV reload.
- **Related commits:** See the commit history of `feature/leader-dev2-requirements-1-8` for the commits made on 2026-10-20. The branch CI run must be green on the final head before release.
