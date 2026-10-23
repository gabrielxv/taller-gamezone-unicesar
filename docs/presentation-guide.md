# GameZone — Presentation and Acceptance Guide

## 1. Before presenting

1. Check out the final `main` branch and pull the latest commit.
2. Confirm the latest GitHub Actions run for that exact commit is successful.
3. Record the Java and Maven versions, then run `mvn --batch-mode clean verify`.
4. Start the app with `mvn exec:java -Dexec.mainClass="com.gamezone.Main"`.
5. Use a disposable copy of the repository for the live demo so generated `*.dat`, return, and warranty records do not alter the reviewed source branch.
6. Confirm the current date is inside the selected promotion's active window in `data/promotions.csv`. The accessory-category promotion is active from **19 October to 31 December 2026**; the general, videogame, and bulk seed promotions end on 31 December 2026. If dates must be adjusted, change only the disposable demo copy and explain the adjustment.
7. Record the generated sale ID and return ID during the demonstration.

## 2. Suggested end-to-end scenario

The seeded accessory IDs are:

- `A001` — wireless controller, compatible with console IDs `C001` and `C002`.
- `A002` — HDMI cable, compatible with `C001` and `C002`.
- `A003` — microSD card, compatible with `C001`.

The seed promotion file contains percentage, video-game category, bulk, and accessory-category promotions. Product and console seed records may be created through the Products menu if the working data directory is empty.

### Step A — Prepare catalogue and customer

1. In **Products**, register a console with ID `C001` if it does not already exist and register one video game.
2. In **People**, register a client and note the client ID. Use a seeded seller such as `S001`.
3. In **Accessories**, list all accessories and query compatibility for `C001`.

### Step B — Register a mixed sale

1. Open **Sales → Register sale**.
2. Enter the client ID and seller ID.
3. Add the console ID, video-game ID, and accessory ID `A001`; enter `0` to finish item entry.
4. Select the extended warranty when prompted for the console.
5. Explain the receipt fields: subtotal, selected promotion/discount, extended warranty cost, and final total.
6. Record the sale ID shown by the UI.

Expected invariant: the system validates all stock before it starts decreasing inventory, and the receipt uses historical prices and discount allocation.

### Step C — Check warranty and inventory

1. Open **Warranties → List all** and show the basic six-month warranty and selected extended twelve-month warranty.
2. Open **Products** and **Accessories** and show the updated stock.
3. Explain that warranty types use polymorphism and that the sale workflow coordinates them through services.

### Step D — Return the console and accessory

1. Open **Returns → Register return**.
2. Enter the sale ID from Step B.
3. Enter the console ID and accessory ID separated by a comma.
4. Provide a short reason and record the return ID.
5. Explain the prorated discount refund, restored inventory, and cancellation/refund of the extended warranty.
6. Generate the return receipt using the recorded return ID.

Expected invariant: an item must belong to the selected sale and cumulative returned quantity cannot exceed quantity sold.

### Step E — Monthly balance

1. Open **Returns → Monthly balance**.
2. Enter the month and year matching the sale/return date.
3. Explain the three figures: total final sales, total returns, and net balance.

## 3. What to explain in the oral defense

- **R1:** accessory inheritance, compatibility, CSV persistence, and stock integration.
- **R2:** promotion strategy subclasses, active date windows, and selecting the best applicable discount.
- **R3:** sale ownership, 30-day validation, historical refund calculation, inventory restoration, and monthly report.
- **R4:** basic versus extended warranty, duration/cost polymorphism, persistence, and cancellation on return.
- **R5:** why sale operations run in a specific order.
- **R6:** why validation and exceptions are separated from UI input/output.
- **R7:** how GRASP distributes responsibilities among services, entities, repositories, and `Main`.
- **R8:** how strategy classes, gateway interfaces, and constructor injection support SOLID.

## 4. Evidence to capture

- CI run for the exact final `main` commit.
- Output of `mvn --batch-mode clean verify` from a team member's machine.
- Mixed sale receipt and sale ID.
- Warranty listing before return.
- Return receipt, restored stock, and cancelled warranty evidence.
- Monthly balance output.
- Pull-request review/merge history and each member's accurate AI log.

## 5. Do not claim

- Do not claim a build passed until CI/local output confirms it for the exact commit.
- Do not claim filesystem operations are a single atomic transaction.
- Do not claim all original assignment clauses have been verified until the team checks the original PDFs.
- Do not invent teammate names, student codes, commits, or reviews.
