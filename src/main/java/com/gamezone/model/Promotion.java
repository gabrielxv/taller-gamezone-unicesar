package com.gamezone.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

/** Represents a time-bounded promotion strategy. */
public abstract class Promotion implements Serializable {
    private final String id;
    private final String name;
    private final LocalDate startDate;
    private final LocalDate endDate;

    protected Promotion(String id, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    /** Returns whether the promotion is active on the supplied date. */
    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /** Calculates the monetary discount for a sale using resolved item data. */
    public abstract double calculateDiscount(Sale sale, Map<String, Double> prices, Map<String, String> categories);

    /** Returns the promotion discriminator used by persistence. */
    public abstract String getType();
}
