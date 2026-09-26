package com.gamezone.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Represents a video game accessory and the consoles it is compatible with. */
public abstract class Accessory extends Product {
    private static final long serialVersionUID = 1L;
    private List<String> compatibleConsoleIds;

    /** Creates an accessory with its common product information and compatible consoles. */
    protected Accessory(String id, String name, double price, int stock, List<String> compatibleConsoleIds) {
        super(id, name, price, stock);
        setCompatibleConsoleIds(compatibleConsoleIds);
    }

    /** Returns an unmodifiable list of compatible console identifiers. */
    public List<String> getCompatibleConsoleIds() {
        return Collections.unmodifiableList(compatibleConsoleIds);
    }

    /** Replaces the compatible console identifiers. */
    public void setCompatibleConsoleIds(List<String> compatibleConsoleIds) {
        if (compatibleConsoleIds == null) {
            this.compatibleConsoleIds = new ArrayList<>();
            return;
        }
        this.compatibleConsoleIds = new ArrayList<>();
        for (String consoleId : compatibleConsoleIds) {
            if (consoleId != null && !consoleId.isBlank()) this.compatibleConsoleIds.add(consoleId.trim());
        }
    }

    /** Adds a console to the compatibility list. */
    public void addCompatibleConsole(String consoleId) {
        if (consoleId == null || consoleId.isBlank()) throw new IllegalArgumentException("Console ID is required.");
        String normalizedId = consoleId.trim();
        if (!compatibleConsoleIds.contains(normalizedId)) compatibleConsoleIds.add(normalizedId);
    }

    /** Indicates whether this accessory is compatible with the specified console. */
    public boolean isCompatibleWith(String consoleId) {
        return consoleId != null && compatibleConsoleIds.contains(consoleId.trim());
    }

    @Override
    public String getDescription() {
        return "Accessory | compatible consoles=" + compatibleConsoleIds;
    }
}