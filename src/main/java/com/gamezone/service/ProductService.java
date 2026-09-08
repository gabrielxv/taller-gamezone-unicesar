package com.gamezone.service;

import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.model.Console;
import com.gamezone.persistence.ProductRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Applies product business rules and coordinates product persistence. */
public class ProductService implements ProductGateway {
    private final ProductRepository repository;
    private final List<Product> products;

    /** Creates the service and loads persisted products. */
    public ProductService(ProductRepository repository) throws IOException { this.repository = repository; this.products = repository.loadAll(); }
    /** Registers a video game. */
    public Product registerVideoGame(String id, String name, double price, int stock, String platform, String genre) throws IOException {
        ensureUnique(id); Product product = new VideoGame(id, name, price, stock, platform, genre); products.add(product); save(); return product;
    }
    /** Registers a console. */
    public Product registerConsole(String id, String name, double price, int stock, String manufacturer, String model) throws IOException {
        ensureUnique(id); Product product = new Console(id, name, price, stock, manufacturer, model); products.add(product); save(); return product;
    }
    /** Returns a copy of all products. */
    public List<Product> getAllProducts() { return new ArrayList<>(products); }
    /** Updates inventory stock by a signed quantity. */
    public void updateStock(String id, int quantity) throws IOException {
        Product product = find(id); int next = product.getStock() + quantity; if (next < 0) throw new IllegalArgumentException("Insufficient stock."); product.setStock(next); save();
    }
    private void ensureUnique(String id) { if (findNullable(id) != null) throw new IllegalArgumentException("Product ID already exists."); }
    private Product find(String id) { Product p = findNullable(id); if (p == null) throw new IllegalArgumentException("Product does not exist: " + id); return p; }
    private Product findNullable(String id) { if (id == null) return null; return products.stream().filter(p -> p.getId().equals(id.trim())).findFirst().orElse(null); }
    private void save() throws IOException { repository.saveAll(products); }
    @Override public boolean exists(String productId) { return findNullable(productId) != null; }
    @Override public double getPrice(String productId) { return find(productId).getPrice(); }
    @Override public boolean hasStock(String productId, int quantity) { return find(productId).getStock() >= quantity; }
    @Override public void decreaseStock(String productId, int quantity) throws IOException { if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative."); updateStock(productId, -quantity); }
}
