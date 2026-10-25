# Submission Readiness Checklist

Use this checklist immediately before the presentation. Mark a verification step complete only when its evidence exists for the final `main` commit.

## 1. Repository and build

- [x] Maven project targets Java 17.
- [x] Four application layers and run instructions are documented.
- [x] Maven CI runs `mvn --batch-mode clean verify` on pushes to `main`, `develop`, work branches, and PRs targeting `develop` or `main`.
- [x] Integrated requirements branch was merged into `develop`; release PR #13 was merged into `main`.
- [x] GitHub Actions Maven CI passed for an earlier leader/dev2 web-dashboard branch commit (run #110); this is not the final feature-branch head or the final `main` commit.
- [ ] Confirm GitHub Actions run #113 for feature-branch head `aeb43bf7390a1ba7504c6628d6126d57cdd96fc6` completes successfully.
- [ ] Confirm the latest GitHub Actions run for the exact final `main` commit is green.
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
- [x] Expanded GRASP/SOLID audits and a leader/dev2 acceptance plan were added on the web-dashboard branch.
- [ ] Check each original clause against the full text of all assignment PDFs; the traceability matrix is a review aid, not a substitute for the original wording.
- [ ] Run the mixed sale/return scenario in `docs/presentation-guide.md` and record actual results.

## 3. Documentation and evidence

- [x] README includes build, test, and run commands.
- [x] Integrated class/layer diagrams, sale/return sequence diagrams, and dedicated GRASP/SOLID diagrams are documented.
- [x] Requirement traceability, integration analysis, GRASP/SOLID notes, and presentation guide exist.
- [x] AI usage log files are present.
- [x] Local web dashboard/API added on the feature branch; see `docs/web-dashboard.md`.
- [ ] Manually test the web dashboard and each API write endpoint using disposable data; CI verifies compilation/tests but does not prove the browser flow.
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

**Release status:** the earlier application release is published to `main`. Leader/dev2 web-dashboard and expanded audit changes are in PR #19. Maven CI passed on an earlier feature-branch commit; CI run #113 is checking the latest diagrams/docs commit. Final delivery is fully verified only after PR review/merge, CI for the resulting `main` commit, local verification, web endpoint checks, and the end-to-end acceptance scenario are recorded.
