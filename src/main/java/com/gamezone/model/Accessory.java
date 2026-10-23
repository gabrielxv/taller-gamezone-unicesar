package com.gamezone.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a video game accessory and the consoles it is compatible with.
 */
public abstract class Accessory extends Product {
    private static final long serialVersionUID = 1L;
    private List<String> compatibleConsoleIds = new ArrayList<>();

    /**
     * Creates an accessory with its common product information and compatible consoles.
     *
     * @param id the unique accessory identifier
     * @param name the accessory name
     * @param price the accessory price
     * @param stock the available inventory quantity
     * @param compatibleConsoleIds the identifiers of compatible consoles
     */
    protected Accessory(String id, String name, double price, int stock,
                        List<String> compatibleConsoleIds) {
        super(id, name, price, stock);
        setCompatibleConsoleIds(compatibleConsoleIds);
    }

    /**
     * Returns an unmodifiable snapshot of the compatible console identifiers.
     *
     * @return the compatible console identifiers
     */
    public List<String> getCompatibleConsoleIds() {
        return Collections.unmodifiableList(new ArrayList<>(compatibleConsoleIds));
    }

    /**
     * Replaces the compatible console identifiers.
     *
     * @param compatibleConsoleIds the new compatible console identifiers; null means no compatible consoles
     * @throws IllegalArgumentException if an identifier is null or blank
     */
    public void setCompatibleConsoleIds(List<String> compatibleConsoleIds) {
        List<String> normalizedIds = new ArrayList<>();
        if (compatibleConsoleIds != null) {
            for (String consoleId : compatibleConsoleIds) {
                if (consoleId == null || consoleId.isBlank()) {
                    throw new IllegalArgumentException("Console ID is required.");
                }
                String normalizedId = consoleId.trim();
                if (!normalizedIds.contains(normalizedId)) {
                    normalizedIds.add(normalizedId);
                }
            }
        }
        this.compatibleConsoleIds = normalizedIds;
    }

    /**
     * Adds a console to the compatibility list if it is not already present.
     *
     * @param consoleId the console identifier to add
     * @throws IllegalArgumentException if the identifier is null or blank
     */
    public void addCompatibleConsole(String consoleId) {
        if (consoleId == null || consoleId.isBlank()) {
            throw new IllegalArgumentException("Console ID is required.");
        }
        String normalizedId = consoleId.trim();
        if (!compatibleConsoleIds.contains(normalizedId)) {
            compatibleConsoleIds.add(normalizedId);
        }
    }

    /**
     * Removes a console from the compatibility list.
     *
     * @param consoleId the console identifier to remove
     * @return true if the identifier was removed; false if it was not present
     */
    public boolean removeCompatibleConsole(String consoleId) {
        return consoleId != null && compatibleConsoleIds.remove(consoleId.trim());
    }

    /**
     * Indicates whether this accessory is compatible with the specified console.
     *
     * @param consoleId the console identifier to check
     * @return true if the accessory is compatible with the console
     */
    public boolean isCompatibleWith(String consoleId) {
        return consoleId != null && compatibleConsoleIds.contains(consoleId.trim());
    }

    /**
     * Returns the common accessory description.
     * Concrete accessory classes should extend this description with their own attributes.
     *
     * @return a description containing the compatible consoles
     */
    @Override
    public String getDescription() {
        return "Accessory | compatible consoles=" + compatibleConsoleIds;
    }
}
