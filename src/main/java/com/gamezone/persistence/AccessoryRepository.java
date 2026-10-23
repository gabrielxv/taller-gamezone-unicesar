package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/** Persists accessories in a CSV file using a type discriminator. */
public class AccessoryRepository {
    private static final String HEADER = "type,id,name,price,stock,compatibleConsoleIds,specificValue1,specificValue2";
    private final Path file;

    /** Creates an accessory repository backed by the supplied CSV file. */
    public AccessoryRepository(Path file) { this.file = file; }

    /** Saves all accessories to the configured CSV file. */
    public void saveAll(List<Accessory> accessories) throws IOException {
        Path parent = file.getParent();
        if (parent != null) Files.createDirectories(parent);
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Accessory accessory : accessories) lines.add(toCsv(accessory));
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    /** Loads all accessories, returning an empty list when the file does not exist. */
    public List<Accessory> loadAll() throws IOException {
        if (!Files.exists(file)) return new ArrayList<>();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<Accessory> accessories = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank() || line.startsWith("type,")) continue;
            accessories.add(fromCsv(line));
        }
        return accessories;
    }

    private String toCsv(Accessory accessory) {
        String compatible = String.join("|", accessory.getCompatibleConsoleIds());
        if (accessory instanceof Controller controller) {
            return String.join(",", "CONTROLLER", safe(accessory.getId()), safe(accessory.getName()),
                    String.valueOf(accessory.getPrice()), String.valueOf(accessory.getStock()), compatible,
                    safe(controller.getConnectionType()), "");
        }
        if (accessory instanceof Cable cable) {
            return String.join(",", "CABLE", safe(accessory.getId()), safe(accessory.getName()),
                    String.valueOf(accessory.getPrice()), String.valueOf(accessory.getStock()), compatible,
                    String.valueOf(cable.getLengthMeters()), safe(cable.getConnectorType()));
        }
        Memory memory = (Memory) accessory;
        return String.join(",", "MEMORY", safe(accessory.getId()), safe(accessory.getName()),
                String.valueOf(accessory.getPrice()), String.valueOf(accessory.getStock()), compatible,
                String.valueOf(memory.getCapacityGb()), safe(memory.getMemoryType()));
    }

    private Accessory fromCsv(String line) {
        String[] values = line.split(",", -1);
        if (values.length != 8) throw new IllegalArgumentException("Invalid accessory CSV row: " + line);
        List<String> compatible = values[5].isBlank()
                ? new ArrayList<>()
                : Arrays.stream(values[5].split("\\|"))
                        .filter(value -> !value.isBlank())
                        .map(String::trim)
                        .collect(Collectors.toList());
        String type = values[0].trim().toUpperCase(Locale.ROOT);
        String id = values[1].trim();
        String name = values[2].trim();
        double price = Double.parseDouble(values[3].trim());
        int stock = Integer.parseInt(values[4].trim());
        return switch (type) {
            case "CONTROLLER" -> new Controller(id, name, price, stock, compatible, values[6].trim());
            case "CABLE" -> new Cable(id, name, price, stock, compatible, Double.parseDouble(values[6].trim()), values[7].trim());
            case "MEMORY" -> new Memory(id, name, price, stock, compatible, Integer.parseInt(values[6].trim()), values[7].trim());
            default -> throw new IllegalArgumentException("Unknown accessory type: " + values[0]);
        };
    }

    private String safe(String value) {
        if (value.contains(",") || value.contains("\n") || value.contains("\r")) {
            throw new IllegalArgumentException("Accessory fields cannot contain commas or line breaks.");
        }
        return value;
    }
}