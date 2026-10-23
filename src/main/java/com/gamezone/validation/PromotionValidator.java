package com.gamezone.validation;

import com.gamezone.exception.InvalidDataException;

import java.time.LocalDate;

/** Validates promotion input data. */
public final class PromotionValidator {
    private PromotionValidator() { }

    public static void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null || end.isBefore(start)) throw new InvalidDataException("Promotion dates are invalid.");
    }

    public static void validatePercentage(double percentage) {
        if (!Double.isFinite(percentage) || percentage < 0 || percentage > 100) throw new InvalidDataException("Discount percentage must be between 0 and 100.");
    }
}
