# Submission Readiness Checklist

Use this checklist immediately before the presentation. Mark a verification step complete only when its evidence exists for the final `main` commit.

## 1. Repository and build

- [x] Maven project targets Java 17.
- [x] Four application layers and run instructions are documented.
- [x] Maven CI runs `mvn --batch-mode clean verify` on pushes to `main`, `develop`, work branches, and PRs targeting `develop` or `main`.
- [x] Integrated requirements branch was merged into `develop`; release PR #13 was merged into `main`.
- [ ] Confirm the latest GitHub Actions run for the exact `main` commit is green.
- [ ] A team member runs `mvn --batch-mode clean verify` on a local checkout of `main`.
- [ ] The application starts from a clean checkout using only tracked seed data.
- [ ] Capture the Java and Maven versions used for the final local verification.

## 2. Requirements 1–8

- [x] R1: accessory model, persistence/service, compatibility query, menu, and sales integration are present.
- [x] R2: percentage, category, and bulk promotion strategies and persistence are present.
- [x] R3: returns, stock restoration, receipts, monthly reporting, and cumulative partial-quantity checks are present.
- [x] R4: basic/extended warranties, persistence, lookup, and cancellation are present.
- [x] R5: the sale workflow integrates products, accessories, promotions, and warranties.
- [x] R6: validators and a domain exception hierarchy are present.
- [x] R7: GRASP responsibility mapping is documented.
- [x] R8: SOLID audit and design choices are documented.
- [ ] Check each original clause against the full text of all assignment PDFs; the traceability matrix is a review aid, not a substitute for the original wording.
- [ ] Run the mixed sale/return scenario in `docs/presentation-guide.md` and record actual results.

## 3. Documentation and evidence

- [x] README includes build, test, and run commands.
- [x] Integrated class/layer diagrams and sale/return sequence diagrams are documented.
- [x] Requirement traceability, integration analysis, GRASP/SOLID notes, and presentation guide exist.
- [x] AI usage log files are present.
- [ ] Each teammate reviews and completes their own AI log accurately; never invent another member's decisions or commits.
- [ ] Capture the CI run URL and output from the final local `mvn clean verify`.
- [ ] Capture a mixed sale receipt, warranty listing, return receipt, restored stock, and monthly balance.

## 4. Team and Git workflow

- [x] The confirmed names in `TEAM.md` are Ángel Vargas and Diego Canales.
- [x] PR #12 merged the integrated requirements into `develop`.
- [x] PR #13 merged the integrated release into `main`.
- [ ] Add official student codes only if the instructor requires them; they were not supplied in this repository review.
- [ ] Confirm whether the assignment requires a third team member; only two names were confirmed.
- [ ] Confirm the required individual commit and approved/merged PR counts against the assignment.
- [ ] Ensure the teammate's review and AI log accurately reflect their actual work.

## 5. Data and demo

- [x] The accessory-category seed promotion is active from 19 October through 31 December 2026.
- [ ] Use a disposable working copy of `data/` for the live demo so generated files do not change the reviewed source branch.
- [ ] Record the generated sale ID and return ID during the demo.
- [ ] If the demo date is outside a seed promotion's active window, change dates only in the disposable demo copy and disclose that setup.

## 6. Known limitation to explain accurately

Filesystem repositories do not provide one ACID transaction across product stock, accessory stock, sales, warranties, and returns. Do not claim global transaction atomicity. Any unverified failure/recovery path must be described as not yet tested.

**Release status:** publication to `main` is complete. Final delivery is fully verified only after the latest CI result for `main`, local verification, and the end-to-end acceptance scenario are recorded.
