# Technical Lead — Execution and Integration Checklist

## Purpose
This file tracks only the technical-lead work for the partial exam. It does not replace the implementation tasks assigned to Developer 1 or Developer 2.

## 1. Branch and pull-request control
- [ ] Keep integration work on `feature/partial-exam-integration`; do not commit directly to `main` or `develop`.
- [ ] Review PR #10 (`feature/accessory-module`) for model, persistence, service, UI, README, and regression compatibility.
- [ ] Do not integrate the accessory module until its PR is reviewed and the author addresses blocking findings.
- [ ] Require each feature PR to target `develop`, include a concise summary, tests/scenarios, and any migration/data-file notes.
- [ ] Check the changed-file list and diff before approving; never approve based only on the PR description.

## 2. Integration contracts
- [ ] Keep the dependency direction `ui -> service -> persistence -> model`; model classes must not depend on UI or repositories.
- [ ] Agree on IDs and method signatures before integrating services.
- [ ] Avoid circular dependencies between WarrantyService, ReturnService, SaleService, and repositories.
- [ ] Keep sale registration as one orchestrated flow in `SaleService`; do not duplicate sale calculation in `ConsoleMenu`.
- [ ] Validate all sale lines and stock before changing any stock. Persist changes only after validations pass; handle persistence failures without leaving partially updated inventory.

## 3. Cross-module business rules to verify
- [ ] A sale may contain videogames, consoles, and accessories.
- [ ] Select at most one promotion: the applicable promotion with the greatest monetary discount.
- [ ] Calculate promotion discount from item subtotal, before extended-warranty fees.
- [ ] Final total = item subtotal - promotion discount + extended-warranty fees.
- [ ] Create the basic warranty for each eligible console; extended warranty is optional and costs 10% of that console's price.
- [ ] Allow partial returns only within 30 calendar days and only for items in the original sale.
- [ ] Returning an item restores the correct stock and computes the refund consistently with the original discount.
- [ ] Define and test the policy for cancelling/reconciling warranty coverage and fees when a console is returned.
- [ ] Monthly net balance includes sales, refunds, discounts, and warranty amounts consistently.
- [ ] Preserve all original menu workflows while adding new ones.

## 4. UI and error handling
- [ ] Keep prompts and input parsing in `ui`; delegate business rules to services.
- [ ] Present differentiated, safe messages for missing resources, invalid data, business-rule violations, and persistence failures.
- [ ] Do not expose raw stack traces or internal file paths to the user.
- [ ] Verify menu numbering and all old/new flows after each integration.

## 5. Verification gate
Run from the project root with Java 17 and Maven:
```bash
mvn clean package
```

If the environment cannot run Maven, record that explicitly and do not mark the build as passed.

### Regression scenarios
- [ ] Register/list videogames and consoles; update stock.
- [ ] Register/list clients and sellers.
- [ ] Register a sale with only products, only accessories, and a mixed cart.
- [ ] Reject a sale with an unknown item, invalid quantity, or insufficient stock without changing inventory.
- [ ] Compare multiple applicable promotions and confirm only the highest monetary discount is applied.
- [ ] Register a console sale with basic warranty and optional extended warranty; check the final total.
- [ ] Return one line from a multi-item sale; verify date limit, original-sale membership, refund, and stock restoration.
- [ ] Reject returns after 30 calendar days and returns of items not present in the sale.
- [ ] Verify warranty-expiration query and monthly net balance.
- [ ] Exercise every original menu operation and every newly added menu operation.

## 6. Documentation and release
- [ ] Update README menu instructions, data files, and run instructions after the actual code is integrated.
- [ ] Keep class/layer diagrams aligned with the implemented code, not planned classes.
- [ ] Fill in real team identities in `TEAM.md`; do not invent names or student codes.
- [ ] Keep a truthful requirements checklist: mark an item complete only after reviewing code and test evidence.
- [ ] Once feature PRs are integrated into `develop`, run the full verification gate and prepare a release PR from `develop` to `main`.

## Current status
This checklist is a coordination and quality-gate artifact. It is not evidence that the new modules have been implemented, that Maven passes, or that PR #10 has been merged.
