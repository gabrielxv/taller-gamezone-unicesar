package com.gamezone.model;

import java.util.List;

/** Represents a memory accessory. */
public class Memory extends Accessory {
    private static final long serialVersionUID = 1L;
    private int capacityGb;
    private String memoryType;

    /** Creates a memory accessory. */
    public Memory(String id, String name, double price, int stock, List<String> compatibleConsoleIds,
                  int capacityGb, String memoryType) {
        super(id, name, price, stock, compatibleConsoleIds);
        setCapacityGb(capacityGb);
        setMemoryType(memoryType);
    }

    public int getCapacityGb() { return capacityGb; }

    public void setCapacityGb(int capacityGb) {
        if (capacityGb <= 0) throw new IllegalArgumentException("Memory capacity must be greater than zero.");
        this.capacityGb = capacityGb;
    }

    public String getMemoryType() { return memoryType; }

    public void setMemoryType(String memoryType) {
        if (memoryType == null || memoryType.isBlank()) throw new IllegalArgumentException("Memory type is required.");
        this.memoryType = memoryType.trim();
    }

    @Override
    public String getDescription() {
        return "Memory | capacity=" + capacityGb + "GB | type=" + memoryType
                + " | compatible consoles=" + getCompatibleConsoleIds();
    }
}