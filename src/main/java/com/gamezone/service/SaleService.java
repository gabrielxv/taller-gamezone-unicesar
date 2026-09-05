package com.gamezone.service;

import com.gamezone.model.Sale;
import com.gamezone.persistence.SaleRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Applies business rules and coordinates persistence for sales.
 */
public class SaleService {
    private final SaleRepository saleRepository;
    private final List<Sale> sales;

    /**
     * Creates the service and loads previously stored sales.
     *
     * @param saleRepository repository used for persistence
     * @throws IOException if sales cannot be loaded
     */
    public SaleService(SaleRepository saleRepository) throws IOException {
        this.saleRepository = saleRepository;
        this.sales = saleRepository.loadAll();
    }

    /**
     * Registers a sale after validating the minimum product rule.
     * Product and stock validation must be coordinated with the ProductService
     * when that module is integrated.
     *
     * @param customerId selected customer identifier
     * @param sellerId selected seller identifier
     * @param productIds identifiers of purchased products
     * @param total calculated total supplied by the integration layer
     * @return the registered sale
     * @throws IOException if the sale cannot be persisted
     */
    public Sale registerSale(String customerId, String sellerId, List<String> productIds, double total)
            throws IOException {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("A customer is required.");
        }
        if (sellerId == null || sellerId.isBlank()) {
            throw new IllegalArgumentException("A seller is required.");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("A sale must contain at least one product.");
        }
        if (total < 0) {
            throw new IllegalArgumentException("The sale total cannot be negative.");
        }

        Sale sale = new Sale(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                customerId,
                sellerId,
                productIds,
                total
        );
        sales.add(sale);
        saleRepository.saveAll(sales);
        return sale;
    }

    /** @return a copy of the complete sale history. */
    public List<Sale> getAllSales() {
        return new ArrayList<>(sales);
    }

    /**
     * Returns sales made by a specific customer.
     *
     * @param customerId customer identifier
     * @return matching sales
     */
    public List<Sale> getSalesByCustomer(String customerId) {
        return sales.stream().filter(sale -> sale.getCustomerId().equals(customerId)).toList();
    }

    /**
     * Returns sales attended by a specific seller.
     *
     * @param sellerId seller identifier
     * @return matching sales
     */
    public List<Sale> getSalesBySeller(String sellerId) {
        return sales.stream().filter(sale -> sale.getSellerId().equals(sellerId)).toList();
    }
}
