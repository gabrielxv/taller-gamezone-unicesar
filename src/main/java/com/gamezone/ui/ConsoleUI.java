package com.gamezone.ui;

import com.gamezone.model.Sale;
import com.gamezone.service.SaleService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Provides the console entry menu for the GameZone application. */
public class ConsoleUI {
    private final SaleService saleService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(SaleService saleService) { this.saleService = saleService; }

    /** Starts the application. */
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== GameZone Unicesar ===");
            System.out.println("1. Products");
            System.out.println("2. People");
            System.out.println("3. Sales");
            System.out.println("0. Exit");
            System.out.print("Option: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> printIntegrationPending("Products");
                case "2" -> printIntegrationPending("People");
                case "3" -> showSalesMenu();
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void showSalesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Sales ---");
            System.out.println("1. Register sale");
            System.out.println("2. List all sales");
            System.out.println("3. Sales by customer");
            System.out.println("4. Sales by seller");
            System.out.println("0. Back");
            System.out.print("Option: ");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> registerSale();
                    case "2" -> printSales(saleService.getAllSales());
                    case "3" -> { System.out.print("Customer ID: "); printSales(saleService.getSalesByCustomer(scanner.nextLine().trim())); }
                    case "4" -> { System.out.print("Seller ID: "); printSales(saleService.getSalesBySeller(scanner.nextLine().trim())); }
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (IOException | RuntimeException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }

    private void registerSale() throws IOException {
        System.out.print("Customer ID: ");
        String customerId = scanner.nextLine().trim();
        System.out.print("Seller ID: ");
        String sellerId = scanner.nextLine().trim();
        List<String> productIds = new ArrayList<>();
        System.out.println("Enter product IDs one per line. Enter 0 to finish.");
        while (true) {
            System.out.print("Product ID: ");
            String productId = scanner.nextLine().trim();
            if ("0".equals(productId)) break;
            if (!productId.isBlank()) productIds.add(productId);
        }
        Sale sale = saleService.registerSale(customerId, sellerId, productIds);
        System.out.println("Sale registered successfully. ID: " + sale.getId() + " Total: " + sale.getTotal());
    }

    private void printSales(List<Sale> sales) {
        if (sales.isEmpty()) { System.out.println("No sales found."); return; }
        for (Sale sale : sales) {
            System.out.println("ID: " + sale.getId() + " | Date: " + sale.getDate()
                    + " | Customer: " + sale.getCustomerId() + " | Seller: " + sale.getSellerId()
                    + " | Products: " + sale.getProductIds() + " | Total: " + sale.getTotal());
        }
    }

    private void printIntegrationPending(String module) {
        System.out.println(module + " menu will be available after module integration.");
    }
}
