package com.gamezone.validation;

import com.gamezone.exception.InvalidDataException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Validates return request data before any inventory or persistence changes occur. */
public final class ReturnValidator {
    private ReturnValidator() { }

    public static void validateItems(List<String> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            throw new InvalidDataException("At least one item must be returned.");
        }

        Set<String> uniqueIds = new HashSet<>();
        for (String itemId : itemIds) {
            if (itemId == null || itemId.isBlank()) {
                throw new InvalidDataException("Returned item IDs cannot be blank.");
            }

            String normalizedId = itemId.trim();
            if (!uniqueIds.add(normalizedId)) {
                throw new InvalidDataException("An item cannot appear more than once in the same return request: " + normalizedId);
            }
        }
    }

    public static void validateReason(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new InvalidDataException("Return reason is required.");
        }
    }
}
