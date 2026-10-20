package com.gamezone.validation;

import com.gamezone.exception.InvalidDataException;
import java.util.List;

/** Validates return request data before any inventory or persistence changes occur. */
public final class ReturnValidator {
    private ReturnValidator() { }

    /**
     * Each occurrence of an item ID represents one unit being returned.
     * The service validates that the requested quantity does not exceed the sale quantity.
     */
    public static void validateItems(List<String> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            throw new InvalidDataException("At least one item must be returned.");
        }
        for (String itemId : itemIds) {
            if (itemId == null || itemId.isBlank()) {
                throw new InvalidDataException("Returned item IDs cannot be blank.");
            }
        }
    }

    public static void validateReason(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new InvalidDataException("Return reason is required.");
        }
    }
}
