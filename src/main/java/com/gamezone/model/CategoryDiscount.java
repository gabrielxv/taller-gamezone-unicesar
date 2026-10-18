package com.gamezone.model;

import java.time.LocalDate;
import java.util.Map;

/** Applies a percentage discount only to items in a target category. */
public class CategoryDiscount extends Promotion {
    private double percentage;
    private String category;

    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                            double percentage, String category) {
        super(id, name, startDate, endDate);
        setPercentage(percentage);
        setCategory(category);
    }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) {
        if (!Double.isFinite(percentage) || percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        this.percentage = percentage;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) {
        if (category == null || (!"VIDEOGAME".equalsIgnoreCase(category) && !"CONSOLE".equalsIgnoreCase(category))) {
            throw new IllegalArgumentException("Category must be VIDEOGAME or CONSOLE.");
        }
        this.category = category.trim().toUpperCase();
    }

    @Override
    public double calculateDiscount(Sale sale, Map<String, Double> prices, Map<String, String> categories) {
        double eligible = sale.getProductIds().stream()
                .filter(id -> category.equalsIgnoreCase(categories.get(id)))
                .mapToDouble(id -> prices.getOrDefault(id, 0.0))
                .sum();
        return eligible * percentage / 100.0;
    }

    @Override
    public String getType() { return "CATEGORY"; }
}
