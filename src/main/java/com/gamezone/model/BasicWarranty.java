package com.gamezone.model;

import java.time.LocalDate;

/** Provides the free six-month factory warranty. */
public class BasicWarranty extends Warranty {
    public BasicWarranty(String id, String productId, String saleId, double productPrice, LocalDate startDate) {
        super(id, productId, saleId, productPrice, startDate);
    }
    @Override public int getDurationInMonths() { return 6; }
    @Override public String getWarrantyType() { return "Garantía Básica"; }
    @Override public double getAdditionalCost() { return 0.0; }
}
