package com.gamezone.ui;

import com.gamezone.model.Person;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Provides the complete console interface for the three GameZone modules. */
public class ConsoleUI {
    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;
    private final Scanner scanner = new Scanner(System.in);

    /** Creates the console interface with the application services. */
    public ConsoleUI(ProductService productService, PersonService personService, SaleService saleService) {
        this.productService = productService; this.personService = personService; this.saleService = saleService;
    }
    /** Starts the main menu loop. */
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== GameZone Unicesar ===");
            System.out.println("1. Products\n2. People\n3. Sales\n0. Exit");
            switch (scanner.nextLine().trim()) {
                case "1" -> productsMenu(); case "2" -> peopleMenu(); case "3" -> salesMenu();
                case "0" -> running = false; default -> System.out.println("Invalid option.");
            }
        }
    }
    private void productsMenu() {
        boolean back = false; while (!back) {
            System.out.println("\n--- Products ---\n1. Register video game\n2. Register console\n3. List products\n4. Update stock\n0. Back");
            try { switch (scanner.nextLine().trim()) {
                case "1" -> registerVideoGame(); case "2" -> registerConsole(); case "3" -> printProducts(productService.getAllProducts());
                case "4" -> updateStock(); case "0" -> back = true; default -> System.out.println("Invalid option.");
            }} catch (IOException | RuntimeException e) { System.out.println("Error: " + e.getMessage()); }
        }
    }
    private void registerVideoGame() throws IOException {
        String id = ask("ID: "), name = ask("Name: "); double price = askDouble("Price: "); int stock = askInt("Stock: ");
        String platform = ask("Platform: "), genre = ask("Genre: "); productService.registerVideoGame(id, name, price, stock, platform, genre); System.out.println("Video game registered.");
    }
    private void registerConsole() throws IOException {
        String id = ask("ID: "), name = ask("Name: "); double price = askDouble("Price: "); int stock = askInt("Stock: ");
        String manufacturer = ask("Manufacturer: "), model = ask("Model: "); productService.registerConsole(id, name, price, stock, manufacturer, model); System.out.println("Console registered.");
    }
    private void updateStock() throws IOException { productService.updateStock(ask("Product ID: "), askInt("Quantity change: ")); System.out.println("Stock updated."); }
    private void peopleMenu() {
        boolean back = false; while (!back) {
            System.out.println("\n--- People ---\n1. Register client\n2. List clients\n3. List sellers\n0. Back");
            try { switch (scanner.nextLine().trim()) {
                case "1" -> registerClient(); case "2" -> printPeople(personService.getClients()); case "3" -> printPeople(personService.getSellers());
                case "0" -> back = true; default -> System.out.println("Invalid option.");
            }} catch (IOException | RuntimeException e) { System.out.println("Error: " + e.getMessage()); }
        }
    }
    private void registerClient() throws IOException {
        String id = ask("ID: "), name = ask("Name: "), email = ask("Email: "), phone = ask("Phone: "), level = ask("Membership level: ");
        personService.registerClient(id, name, email, phone, level); System.out.println("Client registered.");
    }
    private void salesMenu() {
        boolean back = false; while (!back) {
            System.out.println("\n--- Sales ---\n1. Register sale\n2. All sales\n3. Client purchase history\n4. Seller sales history\n0. Back");
            try { switch (scanner.nextLine().trim()) {
                case "1" -> registerSale(); case "2" -> printSales(saleService.getAllSales());
                case "3" -> printSales(saleService.getSalesByCustomer(ask("Customer ID: ")));
                case "4" -> printSales(saleService.getSalesBySeller(ask("Seller ID: ")));
                case "0" -> back = true; default -> System.out.println("Invalid option.");
            }} catch (IOException | RuntimeException e) { System.out.println("Error: " + e.getMessage()); }
        }
    }
    private void registerSale() throws IOException {
        String customer = ask("Customer ID: "), seller = ask("Seller ID: "); List<String> ids = new ArrayList<>();
        System.out.println("Enter product IDs; enter 0 to finish."); while (true) { String id = ask("Product ID: "); if ("0".equals(id)) break; if (!id.isBlank()) ids.add(id); }
        Sale sale = saleService.registerSale(customer, seller, ids); System.out.println("Sale registered. Total: " + sale.getTotal());
    }
    private String ask(String prompt) { System.out.print(prompt); return scanner.nextLine().trim(); }
    private int askInt(String prompt) { return Integer.parseInt(ask(prompt)); }
    private double askDouble(String prompt) { return Double.parseDouble(ask(prompt)); }
    private void printProducts(List<Product> products) { if (products.isEmpty()) { System.out.println("No products found."); return; } products.forEach(System.out::println); }
    private void printPeople(List<? extends Person> people) { if (people.isEmpty()) { System.out.println("No people found."); return; } people.forEach(System.out::println); }
    private void printSales(List<Sale> sales) { if (sales.isEmpty()) { System.out.println("No sales found."); return; } sales.forEach(s -> System.out.println(s.getId()+" | "+s.getDate()+" | customer="+s.getCustomerId()+" | seller="+s.getSellerId()+" | products="+s.getProductIds()+" | total="+s.getTotal())); }
}
