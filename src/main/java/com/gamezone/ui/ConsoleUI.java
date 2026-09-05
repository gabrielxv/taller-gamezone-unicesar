package com.gamezone.ui;

import com.gamezone.service.SaleService;

import java.io.IOException;
import java.util.Scanner;

/**
 * Provides the console entry menu for the GameZone application.
 */
public class ConsoleUI {
    private final SaleService saleService;
    private final Scanner scanner;

    /**
     * Creates the console user interface.
     *
     * @param saleService service used by the sales module
     */
    public ConsoleUI(SaleService saleService) {
        this.saleService = saleService;
        this.scanner = new Scanner(System.in);
    }

    /** Starts the console application menu. */
    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String option = scanner.nextLine().trim();
            switch (option) {
                case "1" -> printIntegrationPending("Products");
                case "2" -> printIntegrationPending("People");
                case "3" -> showSalesMenu();
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    /** Prints the main menu. */
    public void printMainMenu() {
        System.out.println("\n=== GameZone Unicesar ===");
        System.out.println("1. Products");
        System.out.println("2. People");
        System.out.println("3. Sales");
        System.out.println("0. Exit");
        System.out.print("Option: ");
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
            String option = scanner.nextLine().trim();
            try {
                switch (option) {
                    case "1" -> System.out.println("Sale registration will be completed when ProductService and PersonService are integrated.");
                    case "2" -> saleService.getAllSales().forEach(System.out::println);
                    case "3" -> {
                        System.out.print("Customer ID: ");
                        saleService.getSalesByCustomer(scanner.nextLine().trim()).forEach(System.out::println);
                    }
                    case "4" -> {
                        System.out.print("Seller ID: ");
                        saleService.getSalesBySeller(scanner.nextLine().trim()).forEach(System.out::println);
                    }
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (RuntimeException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }

    private void printIntegrationPending(String module) {
        System.out.println(module + " menu will be available after module integration.");
    }
}
