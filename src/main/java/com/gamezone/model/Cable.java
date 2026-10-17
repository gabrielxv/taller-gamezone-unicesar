package com.gamezone.model;

import java.util.List;

/**
 * Represents a cable accessory.
 */
public class Cable extends Accessory {
    private static final long serialVersionUID = 1L;
    private double lengthMeters;
    private String connectorType;

    /**
     * Creates a cable accessory.
     *
     * @param id the cable identifier
     * @param name the cable name
     * @param price the cable price
     * @param stock the available inventory quantity
     * @param compatibleConsoleIds the identifiers of compatible consoles
     * @param lengthMeters the cable length in meters
     * @param connectorType the connector type, such as HDMI, USB, or optical
     */
    public Cable(String id, String name, double price, int stock,
                 List<String> compatibleConsoleIds, double lengthMeters, String connectorType) {
        super(id, name, price, stock, compatibleConsoleIds);
        setLengthMeters(lengthMeters);
        setConnectorType(connectorType);
    }

    /**
     * Returns the cable length in meters.
     *
     * @return the cable length in meters
     */
    public double getLengthMeters() {
        return lengthMeters;
    }

    /**
     * Sets the cable length in meters.
     *
     * @param lengthMeters the length in meters
     * @throws IllegalArgumentException if the length is not finite or is not positive
     */
    public void setLengthMeters(double lengthMeters) {
        if (!Double.isFinite(lengthMeters) || lengthMeters <= 0) {
            throw new IllegalArgumentException("Cable length must be a finite value greater than zero.");
        }
        this.lengthMeters = lengthMeters;
    }

    /**
     * Returns the cable connector type.
     *
     * @return the connector type
     */
    public String getConnectorType() {
        return connectorType;
    }

    /**
     * Sets the cable connector type.
     *
     * @param connectorType the connector type
     * @throws IllegalArgumentException if the connector type is null or blank
     */
    public void setConnectorType(String connectorType) {
        if (connectorType == null || connectorType.isBlank()) {
            throw new IllegalArgumentException("Connector type is required.");
        }
        this.connectorType = connectorType.trim();
    }

    /**
     * Returns a description of the cable, including its length and connector type.
     *
     * @return the cable description
     */
    @Override
    public String getDescription() {
        return "Cable | length=" + lengthMeters + "m | connector=" + connectorType
                + " | compatible consoles=" + getCompatibleConsoleIds();
    }
}
