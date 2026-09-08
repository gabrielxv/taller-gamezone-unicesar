# GameZone Unicesar

Console-based Java/Maven application for managing products, people, inventory and sales for the GameZone Unicesar workshop.

## Requirements
- Java 17 or later
- Maven 3.9+

## Build

```bash
mvn clean package
```

## Run

```bash
mvn exec:java -Dexec.mainClass="com.gamezone.Main"
```

If the Maven Exec plugin is unavailable in a local setup, run the compiled `com.gamezone.Main` class from the IDE.

## Architecture

The project uses four layers:

- `model`: domain entities and inheritance hierarchies.
- `persistence`: file-based repositories.
- `service`: business rules and module integration.
- `ui`: console interaction.

Dependencies follow `ui -> service -> persistence -> model`, with services also depending on model classes.

## Functional operations

1. Register video game
2. Register console
3. List products
4. Register client
5. List clients
6. List sellers
7. Register sale
8. View all sales
9. View customer purchase history
10. View seller sales history

Data is stored under `data/` and loaded automatically at startup. Three sellers are seeded automatically on the first run.

## Project structure

```text
src/main/java/com/gamezone/
├── model/
├── persistence/
├── service/
├── ui/
└── Main.java
docs/
├── analysis.md
├── hierarchy-diagram.md
├── class-diagram.md
└── layers-diagram.md
```
