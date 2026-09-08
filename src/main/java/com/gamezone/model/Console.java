package com.gamezone.model;

/** Represents a game console product with manufacturer and model information. */
public class Console extends Product {
    private static final long serialVersionUID = 1L;
    private String manufacturer;
    private String model;

    /** Creates a console product. */
    public Console(String id, String name, double price, int stock, String manufacturer, String model) {
        super(id, name, price, stock); setManufacturer(manufacturer); setModel(model);
    }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { if (manufacturer == null || manufacturer.isBlank()) throw new IllegalArgumentException("Manufacturer is required."); this.manufacturer = manufacturer.trim(); }
    public String getModel() { return model; }
    public void setModel(String model) { if (model == null || model.isBlank()) throw new IllegalArgumentException("Model is required."); this.model = model.trim(); }
    @Override public String getDescription() { return "Console | manufacturer=" + manufacturer + " | model=" + model; }
}
