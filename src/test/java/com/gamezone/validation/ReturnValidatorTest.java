package com.gamezone.validation;

import com.gamezone.exception.InvalidDataException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReturnValidatorTest {
    @Test
    void acceptsDistinctNonBlankItemIds() {
        assertDoesNotThrow(() -> ReturnValidator.validateItems(List.of("P1", "A1")));
    }

    @Test
    void rejectsNullOrEmptyItemList() {
        assertThrows(InvalidDataException.class, () -> ReturnValidator.validateItems(null));
        assertThrows(InvalidDataException.class, () -> ReturnValidator.validateItems(List.of()));
    }

    @Test
    void rejectsBlankItemIds() {
        assertThrows(InvalidDataException.class, () -> ReturnValidator.validateItems(List.of("P1", "  ")));
    }

    @Test
    void rejectsDuplicateIdsEvenWhenWhitespaceDiffers() {
        assertThrows(InvalidDataException.class, () -> ReturnValidator.validateItems(List.of("P1", " P1 ")));
    }

    @Test
    void rejectsNullItemId() {
        assertThrows(InvalidDataException.class, () -> ReturnValidator.validateItems(java.util.Arrays.asList("P1", null)));
    }
}
