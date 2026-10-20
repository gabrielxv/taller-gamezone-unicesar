# Handoff for Developer 1

## Scope

Developer 1 can continue the accessory-specific implementation without modifying the leader/Developer 2 integration branch responsibilities.

## Expected accessory work

- Continue the accessory model and behavior required by the assignment.
- Preserve the existing `Accessory` inheritance hierarchy and compatibility behavior.
- Keep accessory persistence compatible with `data/accessories.csv`.
- Add or update tests for accessory-specific rules.

## Integration contract

The integrated sales flow already consumes `AccessoryService` for:

- lookup by identifier;
- stock validation;
- stock decrement after sale;
- stock restoration after return.

Avoid changing these public behaviors unless the change is required by the accessory requirements. If signatures must change, update the integration points together and run `mvn clean verify`.

## Historical sales

Sales now support `SaleLine` snapshots containing the item identifier, name, category, unit price at sale, quantity, discount, and line total. Developer 1 should not replace this mechanism with current catalog prices when working on accessories.

## Maven verification

Run:

```bash
mvn clean verify
```

The repository also contains a GitHub Actions workflow that runs the same verification for feature branches and pull requests targeting `develop`.

## Integration sequence

1. Developer 1 completes and tests accessory-specific work in their own branch.
2. Open a pull request against the integration/develop workflow used by the team.
3. Resolve any conflicts at the integration boundary only.
4. Confirm `mvn clean verify` passes.
5. Review the combined requirements before merging into `develop`.
