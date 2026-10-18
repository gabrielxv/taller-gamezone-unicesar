package com.gamezone.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Represents a completed sales transaction in the GameZone system. */
public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final LocalDateTime date;
    private final String customerId;
    private final String sellerId;
    private final List<String> productIds;
    private double subtotal;
    private double discountAmount;
    private String appliedPromotionName;
    private double warrantyCost;
    private double total;

    public Sale(String id, LocalDateTime date, String customerId, String sellerId,
                List<String> productIds, double total) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.productIds = new ArrayList<>(productIds);
        this.subtotal = total;
        this.total = total;
    }

    public String getId() { return id; }
    public LocalDateTime getDate() { return date; }
    public String getCustomerId() { return customerId; }
    public String getSellerId() { return sellerId; }
    public List<String> getProductIds() { return Collections.unmodifiableList(productIds); }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = Math.max(0, discountAmount); }
    public String getAppliedPromotionName() { return appliedPromotionName; }
    public void setAppliedPromotionName(String appliedPromotionName) { this.appliedPromotionName = appliedPromotionName; }
    public double getWarrantyCost() { return warrantyCost; }
    public void setWarrantyCost(double warrantyCost) { this.warrantyCost = Math.max(0, warrantyCost); }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public boolean canBeReturned() {
        long days = ChronoUnit.DAYS.between(date.toLocalDate(), LocalDateTime.now().toLocalDate());
        return days >= 0 && days <= 30;
    }

    public String generateReceipt() {
        String promotion = appliedPromotionName == null ? "Ninguna" : appliedPromotionName;
        return "Recibo de venta\n" +
                "Subtotal: " + subtotal + "\n" +
                "Promoción: " + promotion + "\n" +
                "Descuento: " + discountAmount + "\n" +
                "Garantías extendidas: " + warrantyCost + "\n" +
                "Total final: " + total;
    }
}
