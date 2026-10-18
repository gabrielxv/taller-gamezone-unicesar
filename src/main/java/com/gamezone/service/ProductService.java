package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Applies product business rules and coordinates product persistence. */
public class ProductService implements ProductGateway {
    private final ProductRepository repository;
    private final List<Product> products;

    public ProductService(ProductRepository repository) throws IOException {
        this.repository = repository;
        this.products = repository.loadAll();
    }

    public Product registerVideoGame(String id, String name, double price, int stock,
                                     String platform, String genre) throws IOException {
        ensureUnique(id);
        Product product = new VideoGame(id, name, price, stock, platform, genre);
        products.add(product);
        save();
        return product;
    }

    public Product registerConsole(String id, String name, double price, int stock,
                                   String manufacturer, String model) throws IOException {
        ensureUnique(id);
        Product product = new Console(id, name, price, stock, manufacturer, model);
        products.add(product);
        save();
        return product;
    }

    public List<Product> getAllProducts() { return new ArrayList<>(products); }

    public Product findById(String id) { return findNullable(id); }

    public void updateStock(String id, int quantity) throws IOException {
        Product product = require(id);
        int next = product.getStock() + quantity;
        if (next < 0) throw new IllegalArgumentException("Insufficient stock.");
        product.setStock(next);
        save();
    }

    /** Restores inventory after a successful return. */
    public void restoreStock(String productId, int quantity) throws IOException {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        updateStock(productId, quantity);
    }

    private void ensureUnique(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Product ID is required.");
        if (findNullable(id) != null) throw new IllegalArgumentException("Product ID already exists.");
    }

    private Product require(String id) {
        Product product = findNullable(id);
        if (product == null) throw new IllegalArgumentException("Product does not exist: " + id);
        return product;
    }

    private Product findNullable(String id) {
        if (id == null) return null;
        return products.stream().filter(p -> p.getId().equals(id.trim())).findFirst().orElse(null);
    }

    private void save() throws IOException { repository.saveAll(products); }

    @Override
    public boolean exists(String productId) { return findNullable(productId) != null; }

    @Override
    public double getPrice(String productId) { return require(productId).getPrice(); }

    @Override
    public String getCategory(String productId) {
        Product product = require(productId);
        if (product instanceof Console) return "CONSOLE";
        if (product instanceof VideoGame) return "VIDEOGAME";
        return "PRODUCT";
    }

    @Override
    public boolean isConsole(String productId) { return require(productId) instanceof Console; }

    @Override
    public boolean hasStock(String productId, int quantity) {
        return quantity > 0 && require(productId).getStock() >= quantity;
    }

    @Override
    public void decreaseStock(String productId, int quantity) throws IOException {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        updateStock(productId, -quantity);
    }
}
