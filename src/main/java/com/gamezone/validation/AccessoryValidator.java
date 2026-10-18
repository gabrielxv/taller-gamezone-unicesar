package com.gamezone.validation;

import com.gamezone.exception.InvalidDataException;

/** Validates accessory input data before service operations. */
public final class AccessoryValidator {
    private AccessoryValidator() { }

    public static void validateId(String id) {
        if (id == null || id.isBlank()) throw new InvalidDataException("Accessory ID is required.");
    }

    public static void validateStock(int stock) {
        if (stock < 0) throw new InvalidDataException("Accessory stock cannot be negative.");
    }

    public static void validatePrice(double price) {
        if (!Double.isFinite(price) || price < 0) throw new InvalidDataException("Accessory price must be finite and non-negative.");
    }
}
