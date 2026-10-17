package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Sale;
import com.gamezone.persistence.SaleRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Applies business rules and coordinates persistence for sales. */
public class SaleService {
    private final SaleRepository saleRepository;
    private final ProductGateway productGateway;
    private final PeopleGateway peopleGateway;
    private final AccessoryService accessoryService;
    private final List<Sale> sales;

    public SaleService(SaleRepository saleRepository) throws IOException {
        this(saleRepository, null, null, null);
    }

    /** Creates the service using the existing product and people modules. */
    public SaleService(SaleRepository saleRepository, ProductGateway productGateway,
                       PeopleGateway peopleGateway) throws IOException {
        this(saleRepository, productGateway, peopleGateway, null);
    }

    /** Creates the service with product, people, and accessory modules integrated. */
    public SaleService(SaleRepository saleRepository, ProductGateway productGateway,
                       PeopleGateway peopleGateway, AccessoryService accessoryService) throws IOException {
        this.saleRepository = saleRepository;
        this.productGateway = productGateway;
        this.peopleGateway = peopleGateway;
        this.accessoryService = accessoryService;
        this.sales = saleRepository.loadAll();
    }

    /** Registers a sale containing any combination of products and accessories. */
    public Sale registerSale(String customerId, String sellerId, List<String> productIds) throws IOException {
        requireText(customerId, "A customer is required.");
        requireText(sellerId, "A seller is required.");
        if (productIds == null || productIds.isEmpty()) throw new IllegalArgumentException("A sale must contain at least one item.");
        requireIntegration();
        if (!peopleGateway.customerExists(customerId)) throw new IllegalArgumentException("Customer does not exist.");
        if (!peopleGateway.sellerExists(sellerId)) throw new IllegalArgumentException("Seller does not exist.");

        Map<String, Integer> quantities = new LinkedHashMap<>();
        for (String productId : productIds) {
            requireText(productId, "Item ID is required.");
            quantities.merge(productId.trim(), 1, Integer::sum);
        }

        double total = 0;
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            String itemId = entry.getKey();
            int quantity = entry.getValue();
            if (productGateway.exists(itemId)) {
                if (!productGateway.hasStock(itemId, quantity)) throw new IllegalArgumentException("Insufficient stock for product: " + itemId);
                total += productGateway.getPrice(itemId) * quantity;
            } else {
                Accessory accessory = accessoryService.findById(itemId);
                if (accessory == null) throw new IllegalArgumentException("Product or accessory does not exist: " + itemId);
                if (accessory.getStock() < quantity) throw new IllegalArgumentException("Insufficient stock for accessory: " + itemId);
                total += accessory.getPrice() * quantity;
            }
        }

        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            if (productGateway.exists(entry.getKey())) productGateway.decreaseStock(entry.getKey(), entry.getValue());
            else accessoryService.updateStock(entry.getKey(), -entry.getValue());
        }

        Sale sale = new Sale(UUID.randomUUID().toString(), LocalDateTime.now(), customerId, sellerId, productIds, total);
        sales.add(sale);
        saleRepository.saveAll(sales);
        return sale;
    }

    /** Registers a pre-calculated sale for compatibility during staged integration. */
    public Sale registerSale(String customerId, String sellerId, List<String> productIds, double total) throws IOException {
        requireText(customerId, "A customer is required.");
        requireText(sellerId, "A seller is required.");
        if (productIds == null || productIds.isEmpty()) throw new IllegalArgumentException("A sale must contain at least one item.");
        if (total < 0) throw new IllegalArgumentException("The sale total cannot be negative.");
        Sale sale = new Sale(UUID.randomUUID().toString(), LocalDateTime.now(), customerId, sellerId, productIds, total);
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
        if (productGateway == null || peopleGateway == null || accessoryService == null) {
            throw new IllegalStateException("Product, People, and Accessory modules must be integrated before registering a sale.");
        }
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(message);
    }
}