# Submission Readiness Checklist

Use this checklist immediately before the presentation. Do not mark a verification step complete until the evidence exists on the current branch.

## Repository and build

- [x] Maven project targets Java 17.
- [x] Four application layers exist and are documented.
- [x] CI workflow runs `mvn --batch-mode clean verify`.
- [ ] Latest commit on the final PR has a successful CI run.
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

- [x] Replace name placeholders in `TEAM.md` with the confirmed names Ángel Vargas and Diego Canales.
- [ ] Add official student codes; they were not provided and must not be invented.
- [ ] Confirm whether the instructor requires a third team member; only two names were confirmed.
- [ ] Confirm the required personal commit and approved/merged PR counts from the assignment.
- [ ] Complete genuine cross-review by Diego Canales (requested on PR #12); the author must not submit a review as Diego or treat self-review as approval.
- [ ] Merge feature PRs into `develop`, then publish the verified release from `develop` to `main` using the required PR.
- [ ] Delete remote feature branches only after their changes are merged and the course rules allow it.

## Data and demo

- [ ] Confirm the accessory-category promotion dates include the presentation date.
- [ ] Use a disposable working copy of `data/` for the live demo to avoid changing committed seed data.
- [ ] Record the sale ID and return ID during the demo so the receipt and monthly report can be demonstrated.
