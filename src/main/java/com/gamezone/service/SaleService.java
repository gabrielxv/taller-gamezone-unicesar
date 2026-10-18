package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.persistence.SaleRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Coordinates the complete sale workflow across products, promotions, warranties, and accessories. */
public class SaleService {
    private final SaleRepository saleRepository;
    private final ProductGateway productGateway;
    private final PeopleGateway peopleGateway;
    private final AccessoryService accessoryService;
    private final PromotionService promotionService;
    private final WarrantyService warrantyService;
    private final List<Sale> sales;

    public SaleService(SaleRepository saleRepository) throws IOException {
        this(saleRepository, null, null, null, null, null);
    }

    public SaleService(SaleRepository saleRepository, ProductGateway productGateway,
                       PeopleGateway peopleGateway, AccessoryService accessoryService) throws IOException {
        this(saleRepository, productGateway, peopleGateway, accessoryService, null, null);
    }

    public SaleService(SaleRepository saleRepository, ProductGateway productGateway,
                       PeopleGateway peopleGateway, AccessoryService accessoryService,
                       PromotionService promotionService, WarrantyService warrantyService) throws IOException {
        this.saleRepository = saleRepository;
        this.productGateway = productGateway;
        this.peopleGateway = peopleGateway;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.warrantyService = warrantyService;
        this.sales = saleRepository.loadAll();
    }

    /** Registers a sale without optional extended warranties. */
    public Sale registerSale(String customerId, String sellerId, List<String> itemIds) throws IOException {
        return registerSale(customerId, sellerId, itemIds, Map.of());
    }

    /** Registers a sale using the integrated eight-requirement workflow. */
    public Sale registerSale(String customerId, String sellerId, List<String> itemIds,
                             Map<String, Boolean> extendedWarrantySelections) throws IOException {
        requireText(customerId, "A customer is required.");
        requireText(sellerId, "A seller is required.");
        if (itemIds == null || itemIds.isEmpty()) throw new IllegalArgumentException("A sale must contain at least one item.");
        requireIntegration();
        if (!peopleGateway.customerExists(customerId)) throw new IllegalArgumentException("Customer does not exist.");
        if (!peopleGateway.sellerExists(sellerId)) throw new IllegalArgumentException("Seller does not exist.");

        Map<String, Integer> quantities = resolveQuantities(itemIds);
        validateStock(quantities);
        double subtotal = calculateSubtotal(quantities);
        Sale sale = new Sale(UUID.randomUUID().toString(), LocalDateTime.now(), customerId, sellerId, itemIds, subtotal);
        sale.setSubtotal(subtotal);

        Promotion bestPromotion = promotionService == null ? null : promotionService.findBestPromotionFor(sale);
        double discount = promotionService == null ? 0.0 : promotionService.calculateDiscount(bestPromotion, sale);
        sale.setDiscountAmount(discount);
        sale.setAppliedPromotionName(bestPromotion == null ? null : bestPromotion.getName());

        double warrantyCost = assignWarranties(sale, extendedWarrantySelections == null ? Map.of() : extendedWarrantySelections);
        sale.setWarrantyCost(warrantyCost);
        sale.setTotal(subtotal - discount + warrantyCost);

        updateInventory(quantities);
        sales.add(sale);
        saleRepository.saveAll(sales);
        return sale;
    }

    public Sale registerSale(String customerId, String sellerId, List<String> itemIds, double total) throws IOException {
        Sale sale = registerSale(customerId, sellerId, itemIds, Map.of());
        sale.setSubtotal(total);
        sale.setDiscountAmount(0);
        sale.setWarrantyCost(0);
        sale.setTotal(total);
        saleRepository.saveAll(sales);
        return sale;
    }

    public List<Sale> getAllSales() { return new ArrayList<>(sales); }

    public List<Sale> getSalesByCustomer(String customerId) {
        return sales.stream().filter(s -> s.getCustomerId().equals(customerId)).toList();
    }

    public List<Sale> getSalesBySeller(String sellerId) {
        return sales.stream().filter(s -> s.getSellerId().equals(sellerId)).toList();
    }

    private Map<String, Integer> resolveQuantities(List<String> itemIds) {
        Map<String, Integer> quantities = new LinkedHashMap<>();
        for (String itemId : itemIds) {
            requireText(itemId, "Item ID is required.");
            quantities.merge(itemId.trim(), 1, Integer::sum);
        }
        return quantities;
    }

    private void validateStock(Map<String, Integer> quantities) {
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            String id = entry.getKey();
            int quantity = entry.getValue();
            if (productGateway.exists(id)) {
                if (!productGateway.hasStock(id, quantity)) throw new IllegalArgumentException("Insufficient stock for product: " + id);
                continue;
            }
            Accessory accessory = accessoryService.findById(id);
            if (accessory == null) throw new IllegalArgumentException("Product or accessory does not exist: " + id);
            if (accessory.getStock() < quantity) throw new IllegalArgumentException("Insufficient stock for accessory: " + id);
        }
    }

    private double calculateSubtotal(Map<String, Integer> quantities) {
        double subtotal = 0.0;
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            String id = entry.getKey();
            double price = productGateway.exists(id) ? productGateway.getPrice(id) : accessoryService.findById(id).getPrice();
            subtotal += price * entry.getValue();
        }
        return subtotal;
    }

    private double assignWarranties(Sale sale, Map<String, Boolean> selections) throws IOException {
        if (warrantyService == null) return 0.0;
        double cost = 0.0;
        ProductService productService = warrantyService.getProductService();
        for (String id : sale.getProductIds()) {
            if (!productGateway.exists(id) || !productGateway.isConsole(id)) continue;
            Product product = productService.findById(id);
            warrantyService.assignBasicWarranty(product, sale, LocalDate.now());
            if (Boolean.TRUE.equals(selections.get(id))) {
                cost += warrantyService.assignExtendedWarranty(product, sale, LocalDate.now()).getAdditionalCost();
            }
        }
        return cost;
    }

    private void updateInventory(Map<String, Integer> quantities) throws IOException {
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            if (productGateway.exists(entry.getKey())) productGateway.decreaseStock(entry.getKey(), entry.getValue());
            else accessoryService.updateStock(entry.getKey(), -entry.getValue());
        }
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
