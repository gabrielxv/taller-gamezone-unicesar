package com.gamezone.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryDiscountTest {
    @Test
    void appliesDiscountToAccessoryCategory() {
        CategoryDiscount promotion = new CategoryDiscount(
                "P1", "Accessory sale", LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 31), 10.0, " accessory ");
        Sale sale = new Sale("S1", LocalDateTime.of(2026, 10, 20, 12, 0),
                "C1", "E1", List.of("A1", "C1"), 200.0);

        double discount = promotion.calculateDiscount(
                sale,
                Map.of("A1", 50.0, "C1", 150.0),
                Map.of("A1", "ACCESSORY", "C1", "CONSOLE"));

        assertEquals("ACCESSORY", promotion.getCategory());
        assertEquals(5.0, discount, 0.0001);
    }

    @Test
    void rejectsUnsupportedCategory() {
        assertThrows(IllegalArgumentException.class, () -> new CategoryDiscount(
                "P1", "Bad category", LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 31), 10.0, "BOOK"));
    }
}
