package com.gamezone.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReturnReceiptTest {
    @Test
    void receiptSeparatesItemRefundFromExtendedWarrantyRefund() {
        Return returnRecord = new Return(
                "R1", "S1", LocalDate.of(2026, 10, 20),
                List.of("C1"), "Defective item", 45.0, 10.0);

        String receipt = returnRecord.generateReturnReceipt(Map.of("C1", 100.0));

        assertTrue(receipt.contains("C1 | precio=100.0 | descuento proporcional=65.0 | reembolso=35.0"));
        assertTrue(receipt.contains("Reembolso de garantía: 10.0"));
        assertTrue(receipt.contains("Total reembolsado: 45.0"));
    }
}
