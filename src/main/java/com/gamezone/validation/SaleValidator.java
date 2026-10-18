package com.gamezone.validation;

import com.gamezone.exception.InvalidDataException;

import java.util.List;

/** Validates common sale input data. */
public final class SaleValidator {
    private SaleValidator() { }

    public static void validateRequiredText(String value, String message) {
        if (value == null || value.isBlank()) throw new InvalidDataException(message);
    }

    public static void validateItems(List<String> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) throw new InvalidDataException("A sale must contain at least one item.");
    }
}
