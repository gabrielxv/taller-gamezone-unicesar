package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;
import com.gamezone.persistence.AccessoryRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Applies business rules and coordinates persistence for accessories. */
public class AccessoryService {
    private final AccessoryRepository repository;
    private final List<Accessory> accessories;

    /** Creates the service and loads persisted accessories. */
    public AccessoryService(AccessoryRepository repository) throws IOException {
        this.repository = repository;
        this.accessories = repository.loadAll();
    }

    /** Registers a controller accessory. */
    public Accessory registerController(String id, String name, double price, int stock,
                                        List<String> compatibleConsoleIds, String connectionType) throws IOException {
        ensureUnique(id);
        Accessory accessory = new Controller(id, name, price, stock, compatibleConsoleIds, connectionType);
        accessories.add(accessory);
        save();
        return accessory;
    }

    /** Registers a cable accessory. */
    public Accessory registerCable(String id, String name, double price, int stock,
                                   List<String> compatibleConsoleIds, double lengthMeters,
                                   String connectorType) throws IOException {
        ensureUnique(id);
        Accessory accessory = new Cable(id, name, price, stock, compatibleConsoleIds, lengthMeters, connectorType);
        accessories.add(accessory);
        save();
        return accessory;
    }

    /** Registers a memory accessory. */
    public Accessory registerMemory(String id, String name, double price, int stock,
                                    List<String> compatibleConsoleIds, int capacityGb,
                                    String memoryType) throws IOException {
        ensureUnique(id);
        Accessory accessory = new Memory(id, name, price, stock, compatibleConsoleIds, capacityGb, memoryType);
        accessories.add(accessory);
        save();
        return accessory;
    }

    /** Returns a copy of all accessories. */
    public List<Accessory> listAllAccessories() { return new ArrayList<>(accessories); }

    /** Returns accessories filtered by controller, cable, or memory type. */
    public List<Accessory> listAccessoriesByType(String type) {
        if (type == null || type.isBlank()) return new ArrayList<>();
        String normalized = type.trim().toUpperCase(Locale.ROOT);
        return accessories.stream()
                .filter(accessory -> accessory.getClass().getSimpleName().toUpperCase(Locale.ROOT).equals(normalized))
                .toList();
    }

    /** Returns accessories compatible with the specified console identifier. */
    public List<Accessory> findAccessoriesCompatibleWith(String consoleId) {
        if (consoleId == null || consoleId.isBlank()) return new ArrayList<>();
        return accessories.stream().filter(accessory -> accessory.isCompatibleWith(consoleId)).toList();
    }

    /** Finds an accessory by identifier, or returns null when it does not exist. */
    public Accessory findById(String id) {
        if (id == null) return null;
        String normalized = id.trim();
        return accessories.stream().filter(accessory -> accessory.getId().equals(normalized)).findFirst().orElse(null);
    }

    /** Changes accessory stock by a signed quantity. */
    public void updateStock(String accessoryId, int quantity) throws IOException {
        Accessory accessory = requireAccessory(accessoryId);
        int next = accessory.getStock() + quantity;
        if (next < 0) throw new IllegalArgumentException("Insufficient stock for accessory: " + accessoryId);
        accessory.setStock(next);
        save();
    }

    private Accessory requireAccessory(String id) {
        Accessory accessory = findById(id);
        if (accessory == null) throw new IllegalArgumentException("Accessory does not exist: " + id);
        return accessory;
    }

    private void ensureUnique(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Accessory ID is required.");
        if (findById(id) != null) throw new IllegalArgumentException("Accessory ID already exists.");
    }

    private void save() throws IOException { repository.saveAll(accessories); }
}