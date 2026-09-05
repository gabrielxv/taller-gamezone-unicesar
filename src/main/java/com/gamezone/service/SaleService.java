package com.gamezone.service;

import com.gamezone.model.Sale;
import com.gamezone.persistence.SaleRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Applies business rules and coordinates persistence for sales.
 */
public class SaleService {
    private final SaleRepository saleRepository;
    private final ProductGateway productGateway;
    private final PeopleGateway peopleGateway;
    private final List<Sale> sales;

    public SaleService(SaleRepository saleRepository) throws IOException {
        this(saleRepository, null, null);
    }

    /**
     * Creates the service and loads stored sales.
     */
    public SaleService(SaleRepository saleRepository, ProductGateway productGateway,
                       PeopleGateway peopleGateway) throws IOException {
        this.saleRepository = saleRepository;
        this.productGateway = productGateway;
        this.peopleGateway = peopleGateway;
        this.sales = saleRepository.loadAll();
    }

    /**
     * Registers a sale using the integrated Product and People gateways.
     */
    public Sale registerSale(String customerId, String sellerId, List<String> productIds) throws IOException {
        requireText(customerId, "A customer is required.");
        requireText(sellerId, "A seller is required.");
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("A sale must contain at least one product.");
        }
        requireIntegration();
        if (!peopleGateway.customerExists(customerId)) {
            throw new IllegalArgumentException("Customer does not exist.");
        }
        if (!peopleGateway.sellerExists(sellerId)) {
            throw new IllegalArgumentException("Seller does not exist.");
        }

        Map<String, Integer> quantities = new LinkedHashMap<>();
        for (String productId : productIds) {
            requireText(productId, "Product ID is required.");
            quantities.merge(productId, 1, Integer::sum);
        }

        double total = 0;
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();
            if (!productGateway.exists(productId)) {
                throw new IllegalArgumentException("Product does not exist: " + productId);
            }
            if (!productGateway.hasStock(productId, quantity)) {
                throw new IllegalArgumentException("Insufficient stock for product: " + productId);
            }
            total += productGateway.getPrice(productId) * quantity;
        }

        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            productGateway.decreaseStock(entry.getKey(), entry.getValue());
        }

        Sale sale = new Sale(UUID.randomUUID().toString(), LocalDateTime.now(), customerId,
                sellerId, productIds, total);
        sales.add(sale);
        saleRepository.saveAll(sales);
        return sale;
    }

    /** Registers a pre-calculated sale for compatibility during staged integration. */
    public Sale registerSale(String customerId, String sellerId, List<String> productIds, double total)
            throws IOException {
        requireText(customerId, "A customer is required.");
        requireText(sellerId, "A seller is required.");
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("A sale must contain at least one product.");
        }
        if (total < 0) {
            throw new IllegalArgumentException("The sale total cannot be negative.");
        }
        Sale sale = new Sale(UUID.randomUUID().toString(), LocalDateTime.now(), customerId,
                sellerId, productIds, total);
        sales.add(sale);
        saleRepository.saveAll(sales);
        return sale;
    }

    public List<Sale> getAllSales() { return new ArrayList<>(sales); }

    public List<Sale> getSalesByCustomer(String customerId) {
        return sales.stream().filter(sale -> sale.getCustomerId().equals(customerId)).toList();
    }

    public List<Sale> getSalesBySeller(String sellerId) {
        return sales.stream().filter(sale -> sale.getSellerId().equals(sellerId)).toList();
    }

    private void requireIntegration() {
        if (productGateway == null || peopleGateway == null) {
            throw new IllegalStateException("Product and People modules must be integrated before registering a sale.");
        }
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(message);
    }
}
