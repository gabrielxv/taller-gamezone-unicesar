package com.gamezone.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Sale variant that keeps immutable historical line snapshots for returns and reporting. */
public final class HistoricalSale extends Sale {
    private static final long serialVersionUID = 1L;
    private final List<SaleLine> lines;

    public HistoricalSale(String id, LocalDateTime date, String customerId, String sellerId,
                          List<String> productIds, List<SaleLine> lines, double total) {
        super(id, date, customerId, sellerId, productIds, total);
        this.lines = new ArrayList<>(lines);
    }

    public List<SaleLine> getLines() {
        return Collections.unmodifiableList(new ArrayList<>(lines));
    }

    public SaleLine findLine(String productId) {
        if (productId == null) return null;
        return lines.stream().filter(line -> line.getProductId().equals(productId.trim())).findFirst().orElse(null);
    }
}
