package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.persistence.PromotionRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Coordinates promotion rules and persistence. */
public class PromotionService {
    private final PromotionRepository repository;
    private final ProductGateway productGateway;
    private final AccessoryService accessoryService;
    private final List<Promotion> promotions;

    public PromotionService(PromotionRepository repository, ProductGateway productGateway,
                            AccessoryService accessoryService) throws IOException {
        this.repository = repository;
        this.productGateway = productGateway;
        this.accessoryService = accessoryService;
        this.promotions = repository.loadAll();
    }

    public Promotion registerPercentageDiscount(String id, String name, LocalDate start, LocalDate end, double percentage) throws IOException {
        ensureUnique(id);
        Promotion promotion = new com.gamezone.model.PercentageDiscount(id, name, start, end, percentage);
        promotions.add(promotion); save(); return promotion;
    }

    public Promotion registerCategoryDiscount(String id, String name, LocalDate start, LocalDate end,
                                              double percentage, String category) throws IOException {
        ensureUnique(id);
        Promotion promotion = new com.gamezone.model.CategoryDiscount(id, name, start, end, percentage, category);
        promotions.add(promotion); save(); return promotion;
    }

    public Promotion registerBulkPurchaseDiscount(String id, String name, LocalDate start, LocalDate end,
                                                  int minimumQuantity, double percentage) throws IOException {
        ensureUnique(id);
        Promotion promotion = new com.gamezone.model.BulkPurchaseDiscount(id, name, start, end, minimumQuantity, percentage);
        promotions.add(promotion); save(); return promotion;
    }

    public List<Promotion> listAllPromotions() { return new ArrayList<>(promotions); }

    public List<Promotion> listActivePromotions() {
        LocalDate today = LocalDate.now();
        return promotions.stream().filter(p -> p.isActive(today)).toList();
    }

    /** Returns the active promotion with the highest monetary discount. */
    public Promotion findBestPromotionFor(Sale sale) {
        Map<String, Double> prices = new HashMap<>();
        Map<String, String> categories = new HashMap<>();
        for (String id : sale.getProductIds()) {
            if (productGateway.exists(id)) {
                prices.put(id, productGateway.getPrice(id));
                categories.put(id, productGateway.getCategory(id));
            } else {
                Accessory accessory = accessoryService.findById(id);
                if (accessory != null) {
                    prices.put(id, accessory.getPrice());
                    categories.put(id, "ACCESSORY");
                }
            }
        }

        Promotion best = null;
        double bestDiscount = 0.0;
        for (Promotion promotion : listActivePromotions()) {
            double discount = promotion.calculateDiscount(sale, prices, categories);
            if (discount > bestDiscount) {
                bestDiscount = discount;
                best = promotion;
            }
        }
        return best;
    }

    public double calculateDiscount(Promotion promotion, Sale sale) {
        if (promotion == null) return 0.0;
        Map<String, Double> prices = new HashMap<>();
        Map<String, String> categories = new HashMap<>();
        for (String id : sale.getProductIds()) {
            if (productGateway.exists(id)) {
                prices.put(id, productGateway.getPrice(id));
                categories.put(id, productGateway.getCategory(id));
            } else {
                Accessory accessory = accessoryService.findById(id);
                if (accessory != null) {
                    prices.put(id, accessory.getPrice());
                    categories.put(id, "ACCESSORY");
                }
            }
        }
        return promotion.calculateDiscount(sale, prices, categories);
    }

    public Promotion findById(String id) {
        if (id == null) return null;
        return promotions.stream().filter(p -> p.getId().equals(id.trim())).findFirst().orElse(null);
    }

    private void ensureUnique(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Promotion ID is required.");
        if (findById(id) != null) throw new IllegalArgumentException("Promotion ID already exists.");
    }

    private void save() throws IOException { repository.saveAll(promotions); }
}
