package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.validation.WarrantyValidator;

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

    /** Exposes product lookup to the coordinated sale workflow without exposing the repository. */
    public ProductService getProductService() { return productService; }

    /** Creates a single basic warranty for a console within a given sale. */
    public BasicWarranty assignBasicWarranty(Product product, Sale sale, LocalDate startDate) throws IOException {
        validateAssignment(product, sale, startDate);
        requireConsole(product);
        return warranties.stream()
                .filter(w -> w instanceof BasicWarranty)
                .filter(w -> w.getProductId().equals(product.getId()) && w.getSaleId().equals(sale.getId()))
                .map(BasicWarranty.class::cast)
                .findFirst()
                .orElseGet(() -> {
                    BasicWarranty warranty = new BasicWarranty(UUID.randomUUID().toString(), product.getId(), sale.getId(), product.getPrice(), startDate);
                    warranties.add(warranty);
                    try {
                        save();
                    } catch (IOException exception) {
                        warranties.remove(warranty);
                        throw new java.io.UncheckedIOException(exception);
                    }
                    return warranty;
                });
    }

    /** Creates a single extended warranty for a console within a given sale. */
    public ExtendedWarranty assignExtendedWarranty(Product product, Sale sale, LocalDate startDate) throws IOException {
        validateAssignment(product, sale, startDate);
        requireConsole(product);
        ExtendedWarranty existing = warranties.stream()
                .filter(w -> w instanceof ExtendedWarranty)
                .filter(w -> w.getProductId().equals(product.getId()) && w.getSaleId().equals(sale.getId()))
                .map(ExtendedWarranty.class::cast)
                .findFirst().orElse(null);
        if (existing != null) return existing;
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
        double refundable = warranties.stream().filter(w -> w.getProductId().equals(productId) && w.getSaleId().equals(saleId))
                .filter(w -> w instanceof ExtendedWarranty).mapToDouble(Warranty::getAdditionalCost).sum();
        warranties.removeIf(w -> w.getProductId().equals(productId) && w.getSaleId().equals(saleId));
        save();
        return refundable;
    }

    private void validateAssignment(Product product, Sale sale, LocalDate startDate) {
        if (sale == null) throw new IllegalArgumentException("Sale is required.");
        validateProduct(product, startDate);
    }

    private void validateProduct(Product product, LocalDate startDate) {
        if (product == null) throw new IllegalArgumentException("Product is required.");
        WarrantyValidator.validateStartDate(startDate);
        WarrantyValidator.validateProductPrice(product.getPrice());
    }

    private void requireConsole(Product product) {
        if (!(product instanceof com.gamezone.model.Console)) throw new IllegalArgumentException("Only consoles can have warranties.");
    }

    private void save() throws IOException { repository.saveAll(warranties); }
}
