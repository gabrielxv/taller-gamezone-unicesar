package com.gamezone.model;

import java.util.List;

/**
 * Represents a memory accessory.
 */
public class Memory extends Accessory {
    private static final long serialVersionUID = 1L;
    private int capacityGb;
    private String memoryType;

    /**
     * Creates a memory accessory.
     *
     * @param id the memory identifier
     * @param name the memory name
     * @param price the memory price
     * @param stock the available inventory quantity
     * @param compatibleConsoleIds the identifiers of compatible consoles
     * @param capacityGb the storage capacity in gigabytes
     * @param memoryType the memory type, such as SD, microSD, or internal card
     */
    public Memory(String id, String name, double price, int stock,
                  List<String> compatibleConsoleIds, int capacityGb, String memoryType) {
        super(id, name, price, stock, compatibleConsoleIds);
        setCapacityGb(capacityGb);
        setMemoryType(memoryType);
    }

    /**
     * Returns the storage capacity in gigabytes.
     *
     * @return the capacity in gigabytes
     */
    public int getCapacityGb() {
        return capacityGb;
    }

    /**
     * Sets the storage capacity in gigabytes.
     *
     * @param capacityGb the capacity in gigabytes
     * @throws IllegalArgumentException if the capacity is not positive
     */
    public void setCapacityGb(int capacityGb) {
        if (capacityGb <= 0) {
            throw new IllegalArgumentException("Memory capacity must be greater than zero.");
        }
        this.capacityGb = capacityGb;
    }

    /**
     * Returns the memory type.
     *
     * @return the memory type
     */
    public String getMemoryType() {
        return memoryType;
    }

    /**
     * Sets the memory type.
     *
     * @param memoryType the memory type
     * @throws IllegalArgumentException if the memory type is null or blank
     */
    public void setMemoryType(String memoryType) {
        if (memoryType == null || memoryType.isBlank()) {
            throw new IllegalArgumentException("Memory type is required.");
        }
        this.memoryType = memoryType.trim();
    }

    /**
     * Returns a description of the memory, including its capacity and type.
     *
     * @return the memory description
     */
    @Override
    public String getDescription() {
        return "Memory | capacity=" + capacityGb + "GB | type=" + memoryType
                + " | compatible consoles=" + getCompatibleConsoleIds();
    }
}
