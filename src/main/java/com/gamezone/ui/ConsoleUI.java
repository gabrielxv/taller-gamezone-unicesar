package com.gamezone.ui;

import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/** Provides the Spanish console interface for all integrated GameZone modules. */
public class ConsoleUI {
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final PersonService personService;
    private final SaleService saleService;
    private final PromotionService promotionService;
    private final WarrantyService warrantyService;
    private final ReturnService returnService;
    private final Scanner scanner = new Scanner(System.in);

    /** Backward-compatible constructor for the original three-module UI. */
    public ConsoleUI(ProductService productService, PersonService personService, SaleService saleService) {
        this(productService, null, personService, saleService, null, null, null);
    }

    public ConsoleUI(ProductService productService, AccessoryService accessoryService,
                     PersonService personService, SaleService saleService,
                     PromotionService promotionService, WarrantyService warrantyService,
                     ReturnService returnService) {
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.personService = personService;
        this.saleService = saleService;
        this.promotionService = promotionService;
        this.warrantyService = warrantyService;
        this.returnService = returnService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== GameZone Unicesar ===");
            System.out.println("1. Gestión de productos\n2. Gestión de accesorios\n3. Gestión de personas\n"
                    + "4. Gestión de ventas\n5. Gestión de promociones\n6. Gestión de devoluciones\n"
                    + "7. Gestión de garantías\n0. Salir");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> productsMenu();
                    case "2" -> accessoriesMenu();
                    case "3" -> peopleMenu();
                    case "4" -> salesMenu();
                    case "5" -> promotionsMenu();
                    case "6" -> returnsMenu();
                    case "7" -> warrantiesMenu();
                    case "0" -> running = false;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }

    private void productsMenu() throws IOException {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Gestión de productos ---\n1. Registrar videojuego\n2. Registrar consola\n3. Listar productos\n4. Actualizar stock\n0. Volver");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> registerVideoGame();
                    case "2" -> registerConsole();
                    case "3" -> productService.getAllProducts().forEach(System.out::println);
                    case "4" -> productService.updateStock(ask("ID del producto: "), askInt("Cambio de stock: "));
                    case "0" -> back = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception exception) { System.out.println("Error: " + exception.getMessage()); }
        }
    }

    private void registerVideoGame() throws IOException {
        productService.registerVideoGame(ask("ID: "), ask("Nombre: "), askDouble("Precio: "), askInt("Stock: "), ask("Plataforma: "), ask("Género: "));
        System.out.println("Videojuego registrado correctamente.");
    }

    private void registerConsole() throws IOException {
        productService.registerConsole(ask("ID: "), ask("Nombre: "), askDouble("Precio: "), askInt("Stock: "), ask("Fabricante: "), ask("Modelo: "));
        System.out.println("Consola registrada correctamente.");
    }

    private void accessoriesMenu() throws IOException {
        requireModule(accessoryService, "AccessoryService");
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Gestión de accesorios ---\n1. Registrar control\n2. Registrar cable\n3. Registrar memoria\n4. Listar todos\n5. Listar por tipo\n6. Consultar compatibles con consola\n0. Volver");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> accessoryService.registerController(ask("ID: "), ask("Nombre: "), askDouble("Precio: "), askInt("Stock: "), askIds(), ask("Tipo de conexión: "));
                    case "2" -> accessoryService.registerCable(ask("ID: "), ask("Nombre: "), askDouble("Precio: "), askInt("Stock: "), askIds(), askDouble("Longitud en metros: "), ask("Tipo de conector: "));
                    case "3" -> accessoryService.registerMemory(ask("ID: "), ask("Nombre: "), askDouble("Precio: "), askInt("Stock: "), askIds(), askInt("Capacidad en GB: "), ask("Tipo de memoria: "));
                    case "4" -> accessoryService.listAllAccessories().forEach(System.out::println);
                    case "5" -> accessoryService.listAccessoriesByType(ask("Tipo (CONTROLLER/CABLE/MEMORY): ")).forEach(System.out::println);
                    case "6" -> accessoryService.findAccessoriesCompatibleWith(ask("ID de consola: ")).forEach(System.out::println);
                    case "0" -> back = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception exception) { System.out.println("Error: " + exception.getMessage()); }
        }
    }

    private void peopleMenu() throws IOException {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Gestión de personas ---\n1. Registrar cliente\n2. Listar clientes\n3. Listar vendedores\n0. Volver");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> personService.registerClient(ask("ID: "), ask("Nombre: "), ask("Correo: "), ask("Teléfono: "), ask("Nivel de membresía: "));
                    case "2" -> personService.getClients().forEach(System.out::println);
                    case "3" -> personService.getSellers().forEach(System.out::println);
                    case "0" -> back = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception exception) { System.out.println("Error: " + exception.getMessage()); }
        }
    }

    private void salesMenu() throws IOException {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Gestión de ventas ---\n1. Registrar venta\n2. Listar ventas\n3. Historial por cliente\n4. Historial por vendedor\n0. Volver");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> registerSale();
                    case "2" -> saleService.getAllSales().forEach(s -> System.out.println(s.generateReceipt() + "\nID: " + s.getId()));
                    case "3" -> saleService.getSalesByCustomer(ask("ID del cliente: ")).forEach(s -> System.out.println(s.generateReceipt()));
                    case "4" -> saleService.getSalesBySeller(ask("ID del vendedor: ")).forEach(s -> System.out.println(s.generateReceipt()));
                    case "0" -> back = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception exception) { System.out.println("Error: " + exception.getMessage()); }
        }
    }

    private void registerSale() throws IOException {
        String customerId = ask("ID del cliente: ");
        String sellerId = ask("ID del vendedor: ");
        List<String> ids = new ArrayList<>();
        while (true) {
            String id = ask("ID de producto/accesorio (0 para terminar): ");
            if ("0".equals(id)) break;
            if (!id.isBlank()) ids.add(id);
        }
        Map<String, Boolean> warranties = new HashMap<>();
        for (String id : ids) {
            if (productService.exists(id) && productService.isConsole(id)) {
                warranties.put(id, ask("¿Garantía extendida para " + id + "? (s/n): ").equalsIgnoreCase("s"));
            }
        }
        Sale sale = saleService.registerSale(customerId, sellerId, ids, warranties);
        System.out.println(sale.generateReceipt());
    }

    private void promotionsMenu() throws IOException {
        requireModule(promotionService, "PromotionService");
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Gestión de promociones ---\n1. Registrar porcentaje\n2. Registrar categoría\n3. Registrar volumen\n4. Listar todas\n5. Listar vigentes\n0. Volver");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> promotionService.registerPercentageDiscount(ask("ID: "), ask("Nombre: "), askDate("Inicio (AAAA-MM-DD): "), askDate("Fin (AAAA-MM-DD): "), askDouble("Porcentaje: "));
                    case "2" -> promotionService.registerCategoryDiscount(ask("ID: "), ask("Nombre: "), askDate("Inicio (AAAA-MM-DD): "), askDate("Fin (AAAA-MM-DD): "), askDouble("Porcentaje: "), ask("Categoría (VIDEOGAME/CONSOLE/ACCESSORY): "));
                    case "3" -> promotionService.registerBulkPurchaseDiscount(ask("ID: "), ask("Nombre: "), askDate("Inicio (AAAA-MM-DD): "), askDate("Fin (AAAA-MM-DD): "), askInt("Cantidad mínima: "), askDouble("Porcentaje: "));
                    case "4" -> promotionService.listAllPromotions().forEach(p -> System.out.println(p.getId() + " | " + p.getName() + " | " + p.getType()));
                    case "5" -> promotionService.listActivePromotions().forEach(p -> System.out.println(p.getId() + " | " + p.getName() + " | " + p.getType()));
                    case "0" -> back = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception exception) { System.out.println("Error: " + exception.getMessage()); }
        }
    }

    private void returnsMenu() throws IOException {
        requireModule(returnService, "ReturnService");
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Gestión de devoluciones ---\n1. Registrar devolución\n2. Listar todas\n3. Por cliente\n4. Por venta\n5. Balance mensual\n0. Volver");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> registerReturn();
                    case "2" -> returnService.viewAllReturns().forEach(r -> System.out.println(r.getId() + " | " + r.getSaleId() + " | " + r.getRefundAmount()));
                    case "3" -> returnService.viewReturnsByCustomer(ask("ID del cliente: ")).forEach(r -> System.out.println(r.getId() + " | " + r.getRefundAmount()));
                    case "4" -> returnService.viewReturnsBySale(ask("ID de la venta: ")).forEach(r -> System.out.println(r.getId() + " | " + r.getRefundAmount()));
                    case "5" -> showMonthlyBalance();
                    case "0" -> back = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception exception) { System.out.println("Error: " + exception.getMessage()); }
        }
    }

    private void registerReturn() throws IOException {
        String saleId = ask("ID de la venta: ");
        List<String> ids = Arrays.stream(ask("IDs a devolver separados por coma: ").split(","))
                .map(String::trim).filter(s -> !s.isBlank()).toList();
        Return value = returnService.registerReturn(saleId, ids, ask("Motivo: "));
        System.out.println("Devolución registrada por " + value.getRefundAmount());
    }

    private void showMonthlyBalance() {
        int month = askInt("Mes: ");
        int year = askInt("Año: ");
        double sales = returnService.calculateMonthlySales(month, year);
        double returns = returnService.calculateMonthlyReturns(month, year);
        System.out.println("Ventas: " + sales + "\nDevoluciones: " + returns + "\nBalance neto: " + (sales - returns));
    }

    private void warrantiesMenu() {
        requireModule(warrantyService, "WarrantyService");
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Gestión de garantías ---\n1. Listar todas\n2. Listar vigentes\n3. Próximas a vencer\n0. Volver");
            try {
                switch (scanner.nextLine().trim()) {
                    case "1" -> warrantyService.listAllWarranties().forEach(w -> System.out.println(w.generateWarrantyCertificate()));
                    case "2" -> warrantyService.listActiveWarranties().forEach(w -> System.out.println(w.generateWarrantyCertificate()));
                    case "3" -> warrantyService.listWarrantiesExpiringSoon(askInt("Días: ")).forEach(w -> System.out.println(w.generateWarrantyCertificate()));
                    case "0" -> back = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception exception) { System.out.println("Error: " + exception.getMessage()); }
        }
    }

    private List<String> askIds() {
        String value = ask("IDs de consolas compatibles separados por coma: ");
        if (value.isBlank()) return List.of();
        return Arrays.stream(value.split(",")).map(String::trim).filter(s -> !s.isBlank()).toList();
    }

    private void requireModule(Object module, String name) {
        if (module == null) throw new IllegalStateException(name + " is not configured.");
    }

    private String ask(String prompt) { System.out.print(prompt); return scanner.nextLine().trim(); }
    private int askInt(String prompt) { return Integer.parseInt(ask(prompt)); }
    private double askDouble(String prompt) { return Double.parseDouble(ask(prompt)); }
    private LocalDate askDate(String prompt) { return LocalDate.parse(ask(prompt)); }
}
