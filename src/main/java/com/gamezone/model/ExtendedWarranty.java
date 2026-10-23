package com.gamezone.model;

import java.time.LocalDate;

/** Provides twelve months of factory and accidental-damage coverage. */
public class ExtendedWarranty extends Warranty {
    public ExtendedWarranty(String id, String productId, String saleId, double productPrice, LocalDate startDate) {
        super(id, productId, saleId, productPrice, startDate);
    }
    @Override public int getDurationInMonths() { return 12; }
    @Override public String getWarrantyType() { return "Garantía Extendida"; }
    @Override public double getAdditionalCost() { return getProductPrice() * 0.10; }
}
