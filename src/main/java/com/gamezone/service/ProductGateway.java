package com.gamezone.service;

import java.io.IOException;

/** Defines the minimum product operations required by integration services. */
public interface ProductGateway {
    boolean exists(String productId);
    double getPrice(String productId);
    String getCategory(String productId);
    boolean isConsole(String productId);
    boolean hasStock(String productId, int quantity);
    void decreaseStock(String productId, int quantity) throws IOException;
}
