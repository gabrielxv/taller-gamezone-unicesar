package com.gamezone.service;

/**
 * Defines the minimum people validation operations required by the sales module.
 */
public interface PeopleGateway {
    boolean customerExists(String customerId);
    boolean sellerExists(String sellerId);
}
