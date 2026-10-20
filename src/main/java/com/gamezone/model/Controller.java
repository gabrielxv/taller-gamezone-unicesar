package com.gamezone.model;

import java.util.List;

/**
 * Represents a controller accessory.
 */
public class Controller extends Accessory {
    private static final long serialVersionUID = 1L;
    private String connectionType;

    /**
     * Creates a controller accessory.
     *
     * @param id the controller identifier
     * @param name the controller name
     * @param price the controller price
     * @param stock the available inventory quantity
     * @param compatibleConsoleIds the identifiers of compatible consoles
     * @param connectionType the connection type, either Wired or Wireless
     */
    public Controller(String id, String name, double price, int stock,
                      List<String> compatibleConsoleIds, String connectionType) {
        super(id, name, price, stock, compatibleConsoleIds);
        setConnectionType(connectionType);
    }

    /**
     * Returns the controller connection type.
     *
     * @return Wired or Wireless
     */
    public String getConnectionType() {
        return connectionType;
    }

    /**
     * Sets the controller connection type.
     *
     * @param connectionType Wired or Wireless, case-insensitive
     * @throws IllegalArgumentException if the value is not Wired or Wireless
     */
    public void setConnectionType(String connectionType) {
        if (connectionType == null || connectionType.isBlank()) {
            throw new IllegalArgumentException("Connection type must be Wired or Wireless.");
        }
        String normalizedType = connectionType.trim();
        if ("wired".equalsIgnoreCase(normalizedType)) {
            this.connectionType = "Wired";
        } else if ("wireless".equalsIgnoreCase(normalizedType)) {
            this.connectionType = "Wireless";
        } else {
            throw new IllegalArgumentException("Connection type must be Wired or Wireless.");
        }
    }

    /**
     * Returns a description of the controller, including its connection type.
     *
     * @return the controller description
     */
    @Override
    public String getDescription() {
        return "Controller | connection=" + connectionType
                + " | compatible consoles=" + getCompatibleConsoleIds();
    }
}
