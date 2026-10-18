package com.gamezone.validation;

import com.gamezone.exception.InvalidDataException;

import java.util.List;

/** Validates return request data. */
public final class ReturnValidator {
    private ReturnValidator() { }

    public static void validateItems(List<String> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) throw new InvalidDataException("At least one item must be returned.");
    }

    public static void validateReason(String reason) {
        if (reason == null || reason.isBlank()) throw new InvalidDataException("Return reason is required.");
    }
}
