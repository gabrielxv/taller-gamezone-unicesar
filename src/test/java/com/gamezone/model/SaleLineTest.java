package com.gamezone.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaleLineTest {
    @Test
    void storesHistoricalPriceAndApportionsDiscount() {
        SaleLine line = new SaleLine("P1", "Console", "CONSOLE", 100.0, 2, 20.0);

        assertEquals(100.0, line.getUnitPriceAtSale());
        assertEquals(2, line.getQuantity());
        assertEquals(200.0, line.getLineSubtotal());
        assertEquals(20.0, line.getDiscountAmount());
        assertEquals(90.0, line.getFinalUnitPrice());
        assertEquals(180.0, line.getLineTotal());
    }
}
