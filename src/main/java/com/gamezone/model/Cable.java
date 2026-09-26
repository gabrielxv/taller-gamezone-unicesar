package com.gamezone.model;

import java.util.List;

/** Represents a cable accessory. */
public class Cable extends Accessory {
    private static final long serialVersionUID = 1L;
    private double lengthMeters;
    private String connectorType;

    /** Creates a cable accessory. */
    public Cable(String id, String name, double price, int stock, List<String> compatibleConsoleIds,
                 double lengthMeters, String connectorType) {
        super(id, name, price, stock, compatibleConsoleIds);
        setLengthMeters(lengthMeters);
        setConnectorType(connectorType);
    }

    public double getLengthMeters() { return lengthMeters; }

    public void setLengthMeters(double lengthMeters) {
        if (lengthMeters <= 0) throw new IllegalArgumentException("Cable length must be greater than zero.");
        this.lengthMeters = lengthMeters;
    }

    public String getConnectorType() { return connectorType; }

    public void setConnectorType(String connectorType) {
        if (connectorType == null || connectorType.isBlank()) throw new IllegalArgumentException("Connector type is required.");
        this.connectorType = connectorType.trim();
    }

    @Override
    public String getDescription() {
        return "Cable | length=" + lengthMeters + "m | connector=" + connectorType
                + " | compatible consoles=" + getCompatibleConsoleIds();
    }
}