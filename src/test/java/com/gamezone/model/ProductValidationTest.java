package com.gamezone.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductValidationTest {
    @Test
    void acceptsFiniteNonNegativePrices() {
        assertDoesNotThrow(() -> new Console("C1", "Console", 0.0, 1, "Maker", "Model"));
        assertDoesNotThrow(() -> new Console("C1", "Console", 299.99, 1, "Maker", "Model"));
    }

    @Test
    void rejectsNonFiniteOrNegativePricesAtConstruction() {
        assertThrows(IllegalArgumentException.class,
                () -> new Console("C1", "Console", Double.NaN, 1, "Maker", "Model"));
        assertThrows(IllegalArgumentException.class,
                () -> new Console("C1", "Console", Double.POSITIVE_INFINITY, 1, "Maker", "Model"));
        assertThrows(IllegalArgumentException.class,
                () -> new Console("C1", "Console", -0.01, 1, "Maker", "Model"));
    }

    @Test
    void rejectsNonFinitePricesWhenUpdated() {
        Console console = new Console("C1", "Console", 100.0, 1, "Maker", "Model");

        assertThrows(IllegalArgumentException.class, () -> console.setPrice(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> console.setPrice(Double.NEGATIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> console.setPrice(-1.0));
        assertDoesNotThrow(() -> console.setPrice(150.0));
    }
}
