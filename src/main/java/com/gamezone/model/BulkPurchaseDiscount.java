package com.gamezone.model;

import java.time.LocalDate;
import java.util.Map;

/** Applies a percentage discount when a sale reaches a minimum item count. */
public class BulkPurchaseDiscount extends Promotion {
    private int minimumQuantity;
    private double percentage;

    public BulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                                int minimumQuantity, double percentage) {
        super(id, name, startDate, endDate);
        setMinimumQuantity(minimumQuantity);
        setPercentage(percentage);
    }

    public int getMinimumQuantity() { return minimumQuantity; }
    public void setMinimumQuantity(int minimumQuantity) {
        if (minimumQuantity <= 0) throw new IllegalArgumentException("Minimum quantity must be positive.");
        this.minimumQuantity = minimumQuantity;
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
        if (sale.getProductIds().size() < minimumQuantity) return 0.0;
        double subtotal = sale.getProductIds().stream().mapToDouble(id -> prices.getOrDefault(id, 0.0)).sum();
        return subtotal * percentage / 100.0;
    }

    @Override
    public String getType() { return "BULK"; }
}
