# SOLID Refactor Log

| ID | Principle | Area | Change |
|---|---|---|---|
| S-01 | SRP | Sale | Separated sale orchestration from persistence and UI. |
| S-02 | OCP | Promotions | Added polymorphic promotion strategies. |
| S-03 | LSP | Warranties | Added basic and extended warranty implementations under one contract. |
| S-04 | ISP | Integration | Kept product and people gateways focused on client needs. |
| S-05 | DIP | Sale | Sale coordination depends on gateways and injected services. |
| S-06 | SRP | Returns | Separated return persistence from return business rules. |
| S-07 | SRP | Warranties | Separated warranty persistence from warranty business rules. |

All changes are internal reorganizations or additive integrations intended to preserve the existing console use cases while supporting Requirements 1-8.
