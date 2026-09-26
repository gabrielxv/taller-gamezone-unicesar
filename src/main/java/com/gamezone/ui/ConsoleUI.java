package com.gamezone.ui;

import com.gamezone.model.Accessory;
import com.gamezone.model.Person;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/** Provides the complete console interface for GameZone products, accessories, people, and sales. */
public class ConsoleUI {
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final PersonService personService;
    private final SaleService saleService;
    private final Scanner scanner = new Scanner(System.in);

    /** Creates the console interface with the application services. */
    public ConsoleUI(ProductService productService, AccessoryService accessoryService,
                     PersonService personService, SaleService saleService) {
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.personService = personService;
        this.saleService = saleService;
    }

    /** Starts the main menu loop. */
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("
=== GameZone Unicesar ===");
            System.out.println("1. Products
2. Accessories
3. People
4. Sales
0. Exit");
            switch (scanner.nextLine().trim()) {
                case "1" -> productsMenu();
                case "2" -> accessoriesMenu();
                case "3" -> peopleMenu();
                case "4" -> salesMenu();
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void productsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("
--- Products ---
1. Register video game
2. Register console
3. List products
4. Update stock
0. Back");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> registerVideoGame();
                    case "2" -> registerConsole();
                    case "3" -> printProducts(productService.getAllProducts());
                    case "4" -> updateStock();
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (IOException | RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void registerVideoGame() throws IOException {
        String id = ask("ID: ");
        String name = ask("Name: ");
        double price = askDouble("Price: ");
        int stock = askInt("Stock: ");
        String platform = ask("Platform: ");
        String genre = ask("Genre: ");
        productService.registerVideoGame(id, name, price, stock, platform, genre);
        System.out.println("Video game registered.");
    }

    private void registerConsole() throws IOException {
        String id = ask("ID: ");
        String name = ask("Name: ");
        double price = askDouble("Price: ");
        int stock = askInt("Stock: ");
        String manufacturer = ask("Manufacturer: ");
        String model = ask("Model: ");
        productService.registerConsole(id, name, price, stock, manufacturer, model);
        System.out.println("Console registered.");
    }

    private void updateStock() throws IOException {
        productService.updateStock(ask("Product ID: "), askInt("Quantity change: "));
        System.out.println("Stock updated.");
    }

    private void accessoriesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("
--- Gestión de accesorios ---");
            System.out.println("1. Registrar un nuevo control");
            System.out.println("2. Registrar un nuevo cable");
            System.out.println("3. Registrar una nueva memoria");
            System.out.println("4. Listar todos los accesorios");
            System.out.println("5. Listar accesorios por tipo");
            System.out.println("6. Consultar accesorios compatibles con una consola");
            System.out.println("0. Volver");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> registerController();
                    case "2" -> registerCable();
                    case "3" -> registerMemory();
                    case "4" -> printAccessories(accessoryService.listAllAccessories());
                    case "5" -> printAccessories(accessoryService.listAccessoriesByType(ask("Tipo (CONTROLLER/CABLE/MEMORY): ")));
                    case "6" -> printAccessories(accessoryService.findAccessoriesCompatibleWith(ask("Console ID: ")));
                    case "0" -> back = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (IOException | RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void registerController() throws IOException {
        accessoryService.registerController(ask("ID: "), ask("Nombre: "), askDouble("Precio: "), askInt("Cantidad: "),
                askConsoleIds(), ask("Tipo de conexión (inalámbrico/alámbrico): "));
        System.out.println("Control registrado correctamente.");
    }

    private void registerCable() throws IOException {
        accessoryService.registerCable(ask("ID: "), ask("Nombre: "), askDouble("Precio: "), askInt("Cantidad: "),
                askConsoleIds(), askDouble("Longitud en metros: "), ask("Tipo de conector: "));
        System.out.println("Cable registrado correctamente.");
    }

    private void registerMemory() throws IOException {
        accessoryService.registerMemory(ask("ID: "), ask("Nombre: "), askDouble("Precio: "), askInt("Cantidad: "),
                askConsoleIds(), askInt("Capacidad en GB: "), ask("Tipo de memoria: "));
        System.out.println("Memoria registrada correctamente.");
    }

    private List<String> askConsoleIds() {
        String value = ask("IDs de consolas compatibles separados por coma (opcional): ");
        if (value.isBlank()) return new ArrayList<>();
        return Arrays.stream(value.split(",")).map(String::trim).filter(id -> !id.isBlank()).collect(Collectors.toList());
    }

    private void peopleMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("
--- People ---
1. Register client
2. List clients
3. List sellers
0. Back");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> registerClient();
                    case "2" -> printPeople(personService.getClients());
                    case "3" -> printPeople(personService.getSellers());
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (IOException | RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void registerClient() throws IOException {
        String id = ask("ID: "), name = ask("Name: "), email = ask("Email: "), phone = ask("Phone: "), level = ask("Membership level: ");
        personService.registerClient(id, name, email, phone, level);
        System.out.println("Client registered.");
    }

    private void salesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("
--- Sales ---
1. Register sale
2. All sales
3. Client purchase history
4. Seller sales history
0. Back");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> registerSale();
                    case "2" -> printSales(saleService.getAllSales());
                    case "3" -> printSales(saleService.getSalesByCustomer(ask("Customer ID: ")));
                    case "4" -> printSales(saleService.getSalesBySeller(ask("Seller ID: ")));
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (IOException | RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void registerSale() throws IOException {
        String customer = ask("Customer ID: ");
        String seller = ask("Seller ID: ");
        List<String> ids = new ArrayList<>();
        System.out.println("Ingrese IDs de productos o accesorios; escriba 0 para finalizar.");
        while (true) {
            String id = ask("Product/accessory ID: ");
            if ("0".equals(id)) break;
            if (!id.isBlank()) ids.add(id);
        }
        Sale sale = saleService.registerSale(customer, seller, ids);
        System.out.println("Venta registrada. Total: " + sale.getTotal());
    }

    private String ask(String prompt) { System.out.print(prompt); return scanner.nextLine().trim(); }
    private int askInt(String prompt) { return Integer.parseInt(ask(prompt)); }
    private double askDouble(String prompt) { return Double.parseDouble(ask(prompt)); }
    private void printProducts(List<Product> products) { if (products.isEmpty()) System.out.println("No products found."); else products.forEach(System.out::println); }
    private void printAccessories(List<Accessory> accessories) { if (accessories.isEmpty()) System.out.println("No accessories found."); else accessories.forEach(System.out::println); }
    private void printPeople(List<? extends Person> people) { if (people.isEmpty()) System.out.println("No people found."); else people.forEach(System.out::println); }
    private void printSales(List<Sale> sales) { if (sales.isEmpty()) System.out.println("No sales found."); else sales.forEach(s -> System.out.println(s.getId()+" | "+s.getDate()+" | customer="+s.getCustomerId()+" | seller="+s.getSellerId()+" | items="+s.getProductIds()+" | total="+s.getTotal())); }
}