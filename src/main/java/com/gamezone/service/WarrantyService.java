package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.WarrantyRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Coordinates warranty assignment, lookup, cancellation, and persistence. */
public class WarrantyService {
    private final WarrantyRepository repository;
    private final ProductService productService;
    private final List<Warranty> warranties;

    public WarrantyService(WarrantyRepository repository, ProductService productService) throws IOException {
        this.repository = repository;
        this.productService = productService;
        this.warranties = repository.loadAll();
    }

    public ProductService getProductService() { return productService; }

    public BasicWarranty assignBasicWarranty(Product product, Sale sale, LocalDate startDate) throws IOException {
        requireConsole(product);
        Warranty existing = findWarrantyByProduct(product.getId(), sale.getId());
        if (existing != null) return (BasicWarranty) existing;
        BasicWarranty warranty = new BasicWarranty(UUID.randomUUID().toString(), product.getId(), sale.getId(), product.getPrice(), startDate);
        warranties.add(warranty);
        save();
        return warranty;
    }

    public ExtendedWarranty assignExtendedWarranty(Product product, Sale sale, LocalDate startDate) throws IOException {
        requireConsole(product);
        ExtendedWarranty warranty = new ExtendedWarranty(UUID.randomUUID().toString(), product.getId(), sale.getId(), product.getPrice(), startDate);
        warranties.add(warranty);
        save();
        return warranty;
    }

    public Warranty findWarrantyByProduct(String productId, String saleId) {
        return warranties.stream().filter(w -> w.getProductId().equals(productId) && w.getSaleId().equals(saleId)).findFirst().orElse(null);
    }

    public List<Warranty> listAllWarranties() { return new ArrayList<>(warranties); }

    public List<Warranty> listActiveWarranties() {
        LocalDate today = LocalDate.now();
        return warranties.stream().filter(w -> w.isActive(today)).toList();
    }

    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {
        if (daysAhead < 0) throw new IllegalArgumentException("Days ahead cannot be negative.");
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(daysAhead);
        return warranties.stream().filter(w -> !w.getEndDate().isBefore(today) && !w.getEndDate().isAfter(limit)).toList();
    }

    public double cancelWarranties(String productId, String saleId) throws IOException {
        double refundable = warranties.stream()
                .filter(w -> w.getProductId().equals(productId) && w.getSaleId().equals(saleId))
                .filter(w -> w instanceof ExtendedWarranty)
                .mapToDouble(Warranty::getAdditionalCost).sum();
        warranties.removeIf(w -> w.getProductId().equals(productId) && w.getSaleId().equals(saleId));
        save();
        return refundable;
    }

    private void requireConsole(Product product) {
        if (!(product instanceof com.gamezone.model.Console)) throw new IllegalArgumentException("Only consoles can have warranties.");
    }

    private void save() throws IOException { repository.saveAll(warranties); }
}
