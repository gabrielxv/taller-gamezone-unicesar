package com.gamezone.model;

import java.util.List;

/** Represents a controller accessory. */
public class Controller extends Accessory {
    private static final long serialVersionUID = 1L;
    private String connectionType;

    /** Creates a controller accessory. */
    public Controller(String id, String name, double price, int stock, List<String> compatibleConsoleIds, String connectionType) {
        super(id, name, price, stock, compatibleConsoleIds);
        setConnectionType(connectionType);
    }

    public String getConnectionType() { return connectionType; }

    public void setConnectionType(String connectionType) {
        if (connectionType == null || connectionType.isBlank()) throw new IllegalArgumentException("Connection type is required.");
        this.connectionType = connectionType.trim();
    }

    @Override
    public String getDescription() {
        return "Controller | connection=" + connectionType + " | compatible consoles=" + getCompatibleConsoleIds();
    }
}