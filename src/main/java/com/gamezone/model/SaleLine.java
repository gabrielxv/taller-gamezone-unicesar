package com.gamezone.model;

import java.io.Serializable;

/** Immutable historical snapshot of one product line in a sale. */
public final class SaleLine implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String productId;
    private final String productName;
    private final String category;
    private final double unitPriceAtSale;
    private final int quantity;
    private final double discountAmount;
    private final double finalUnitPrice;
    private final double lineSubtotal;
    private final double lineTotal;

    public SaleLine(String productId, String productName, String category,
                    double unitPriceAtSale, int quantity, double discountAmount) {
        if (productId == null || productId.isBlank()) throw new IllegalArgumentException("Product ID is required.");
        if (productName == null || productName.isBlank()) throw new IllegalArgumentException("Product name is required.");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("Product category is required.");
        if (!Double.isFinite(unitPriceAtSale) || unitPriceAtSale < 0) throw new IllegalArgumentException("Unit price is invalid.");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        if (!Double.isFinite(discountAmount) || discountAmount < 0) throw new IllegalArgumentException("Discount is invalid.");
        this.productId = productId.trim();
        this.productName = productName.trim();
        this.category = category.trim();
        this.unitPriceAtSale = unitPriceAtSale;
        this.quantity = quantity;
        this.discountAmount = Math.min(discountAmount, unitPriceAtSale * quantity);
        this.finalUnitPrice = (unitPriceAtSale * quantity - this.discountAmount) / quantity;
        this.lineSubtotal = unitPriceAtSale * quantity;
        this.lineTotal = this.finalUnitPrice * quantity;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public double getUnitPriceAtSale() { return unitPriceAtSale; }
    public int getQuantity() { return quantity; }
    public double getDiscountAmount() { return discountAmount; }
    public double getFinalUnitPrice() { return finalUnitPrice; }
    public double getLineSubtotal() { return lineSubtotal; }
    public double getLineTotal() { return lineTotal; }
}
