package com.gamezone.model;

import java.time.LocalDate;
import java.util.Map;

/** Applies a percentage discount to the complete sale subtotal. */
public class PercentageDiscount extends Promotion {
    private double percentage;

    public PercentageDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double percentage) {
        super(id, name, startDate, endDate);
        setPercentage(percentage);
    }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) {
        if (!Double.isFinite(percentage) || percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        this.percentage = percentage;
    }

    @Override
    public double calculateDiscount(Sale sale, Map<String, Double> prices, Map<String, String> categories) {
        double subtotal = sale.getProductIds().stream().mapToDouble(id -> prices.getOrDefault(id, 0.0)).sum();
        return subtotal * percentage / 100.0;
    }

    @Override
    public String getType() { return "PERCENTAGE"; }
}
