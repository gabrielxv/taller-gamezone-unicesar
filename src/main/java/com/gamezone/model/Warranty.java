package com.gamezone.model;

import java.io.Serializable;
import java.time.LocalDate;

/** Represents a warranty associated with a sold product. */
public abstract class Warranty implements Serializable {
    private final String id;
    private final String productId;
    private final String saleId;
    private final double productPrice;
    private final LocalDate startDate;
    private final LocalDate endDate;

    protected Warranty(String id, String productId, String saleId, double productPrice, LocalDate startDate) {
        this.id = id;
        this.productId = productId;
        this.saleId = saleId;
        this.productPrice = productPrice;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(getDurationInMonths());
    }

    public String getId() { return id; }
    public String getProductId() { return productId; }
    public String getSaleId() { return saleId; }
    public double getProductPrice() { return productPrice; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    public abstract int getDurationInMonths();
    public abstract String getWarrantyType();
    public abstract double getAdditionalCost();

    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public String generateWarrantyCertificate() {
        return "Garantía: " + getWarrantyType() + " | producto=" + productId
                + " | venta=" + saleId + " | inicio=" + startDate + " | fin=" + endDate
                + " | costo adicional=" + getAdditionalCost();
    }
}
