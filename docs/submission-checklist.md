# Submission Readiness Checklist

Use this checklist immediately before the presentation. Do not mark a verification step complete until the evidence exists on the current branch.

## Repository and build

- [x] Maven project targets Java 17.
- [x] Four application layers exist and are documented.
- [x] CI workflow runs `mvn --batch-mode clean verify`.
- [x] CI passed for the integration commit in PR #12.
- [ ] Confirm a successful CI run for the latest release PR #13 commit before publishing to `main`.
- [ ] A team member runs `mvn --batch-mode clean verify` on a local checkout.
- [ ] The app starts from a clean checkout with only tracked seed data.

## Requirements 1–8

- [x] R1: accessory model, persistence/service, compatibility query, menu, and sales integration are present.
- [x] R2: percentage, category, and bulk promotion strategies and persistence are present.
- [x] R3: returns, stock restoration, receipts, monthly reporting, and cumulative partial-quantity checks are present.
- [x] Added a service test for two separate unit returns and rejection of an over-return.
- [x] R4: basic/extended warranties, persistence, lookup, and cancellation are present.
- [x] R5: the sale workflow integrates products, accessories, promotions, and warranties.
- [x] R6: validators and a domain exception hierarchy are present.
- [x] R7: GRASP responsibility mapping is documented.
- [x] R8: SOLID audit and design choices are documented.
- [ ] Verify every original clause against the full text of the assignment PDFs; exact separate PDFs for all requirements were not available during this documentation pass.
- [ ] Run the mixed sale/return scenario in `presentation-guide.md` and record actual observed results.

## Documentation

- [x] README describes all seven console modules and run/build commands.
- [x] Integrated class and layer diagrams use Mermaid.
- [x] Sale/return sequence diagrams and a presentation script are present.
- [x] Requirement traceability and A1–A7 integration adjustments are documented.
- [x] AI usage log files exist.
- [ ] Each teammate reviews and completes their own AI log accurately; do not invent another member's decisions or commits.

## Team and Git workflow

- [x] Names confirmed in `TEAM.md`: Ángel Vargas and Diego Canales.
- [x] Added example four-digit identifiers (4827 for Ángel Vargas, 7314 for Diego Canales), explicitly marked fictitious/provisional; these are not official student codes.
- [ ] If the instructor requires official university codes, replace the provisional examples with the real codes before submission.
- [ ] Confirm whether the instructor requires a third team member; only two names were confirmed.
- [ ] Confirm the required personal commit and approved/merged PR counts from the assignment.
- [x] Diego Canales submitted an independent approval on PR #12.
- [x] PR #12 merged into `develop`.
- [ ] Review the final diff and CI checks on PR #13 before merging `develop` into `main`.
- [ ] Delete remote feature branches only after their changes are merged and the course rules allow it.

## Data and demo

- [ ] Confirm the accessory-category promotion dates include the presentation date.
- [ ] Use a disposable working copy of `data/` for the live demo to avoid changing committed seed data.
- [ ] Record the sale ID and return ID during the demo so the receipt and monthly report can be demonstrated.

## Important data note

The codes 4827 and 7314 are invented sample identifiers for documentation only. They must not be represented as official university registration codes.
