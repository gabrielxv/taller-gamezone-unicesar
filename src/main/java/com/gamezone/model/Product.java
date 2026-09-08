package com.gamezone.model;

import java.io.Serializable;

/** Represents the common information shared by all products sold by GameZone. */
public abstract class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private double price;
    private int stock;

    /** Creates a product with its common inventory data. */
    protected Product(String id, String name, double price, int stock) {
        setId(id); setName(name); setPrice(price); setStock(stock);
    }
    public String getId() { return id; }
    public void setId(String id) { if (id == null || id.isBlank()) throw new IllegalArgumentException("Product ID is required."); this.id = id.trim(); }
    public String getName() { return name; }
    public void setName(String name) { if (name == null || name.isBlank()) throw new IllegalArgumentException("Product name is required."); this.name = name.trim(); }
    public double getPrice() { return price; }
    public void setPrice(double price) { if (price < 0) throw new IllegalArgumentException("Price cannot be negative."); this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative."); this.stock = stock; }
    /** Returns a human-readable description specific to the concrete product type. */
    public abstract String getDescription();
    @Override public String toString() { return id + " | " + name + " | $" + price + " | stock=" + stock + " | " + getDescription(); }
}
