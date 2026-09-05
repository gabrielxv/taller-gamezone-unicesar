package com.gamezone.service;

/**
 * Defines the minimum product operations required by the sales module.
 */
public interface ProductGateway {
    boolean exists(String productId);
    double getPrice(String productId);
    boolean hasStock(String productId, int quantity);
    void decreaseStock(String productId, int quantity);
}
