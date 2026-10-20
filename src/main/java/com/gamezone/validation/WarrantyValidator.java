package com.gamezone.validation;

import com.gamezone.exception.InvalidDataException;

import java.time.LocalDate;

/** Validates warranty input data. */
public final class WarrantyValidator {
    private WarrantyValidator() { }

    public static void validateStartDate(LocalDate startDate) {
        if (startDate == null) throw new InvalidDataException("Warranty start date is required.");
    }

    public static void validateProductPrice(double price) {
        if (!Double.isFinite(price) || price < 0) throw new InvalidDataException("Product price must be finite and non-negative.");
    }
}
