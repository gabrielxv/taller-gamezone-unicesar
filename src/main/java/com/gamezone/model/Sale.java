package com.gamezone.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a completed sales transaction in the GameZone system.
 */
public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final LocalDateTime date;
    private final String customerId;
    private final String sellerId;
    private final List<String> productIds;
    private final double total;

    /**
     * Creates a sale.
     *
     * @param id unique sale identifier
     * @param date transaction date
     * @param customerId customer identifier
     * @param sellerId seller identifier
     * @param productIds purchased product identifiers
     * @param total calculated sale total
     */
    public Sale(String id, LocalDateTime date, String customerId, String sellerId,
                List<String> productIds, double total) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.productIds = new ArrayList<>(productIds);
        this.total = total;
    }

    /** @return the sale identifier. */
    public String getId() { return id; }

    /** @return the transaction date. */
    public LocalDateTime getDate() { return date; }

    /** @return the customer identifier. */
    public String getCustomerId() { return customerId; }

    /** @return the seller identifier. */
    public String getSellerId() { return sellerId; }

    /** @return an unmodifiable view of purchased product identifiers. */
    public List<String> getProductIds() { return Collections.unmodifiableList(productIds); }

    /** @return the calculated sale total. */
    public double getTotal() { return total; }
}
