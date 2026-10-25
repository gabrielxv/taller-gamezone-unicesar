package com.gamezone.web;

import com.gamezone.model.Product;
import com.gamezone.service.*;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;

/** HTTP adapter for the existing GameZone services. Business rules remain in service/model classes. */
public final class WebServer {
    private final ProductService products;
    private final AccessoryService accessories;
    private final PersonService people;
    private final PromotionService promotions;
    private final SaleService sales;
    private final ReturnService returns;
    private final WarrantyService warranties;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (v, t, c) -> new JsonPrimitive(v.toString()))
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (v, t, c) -> new JsonPrimitive(v.toString()))
            .create();

    public WebServer(ProductService products, AccessoryService accessories, PersonService people,
                     PromotionService promotions, SaleService sales, ReturnService returns, WarrantyService warranties) {
        this.products = products; this.accessories = accessories; this.people = people;
        this.promotions = promotions; this.sales = sales; this.returns = returns; this.warranties = warranties;
    }

    public void start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/", this::handleApi);
        server.createContext("/", this::serveUi);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("GameZone web dashboard: http://localhost:" + port);
    }

    private void serveUi(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) { send(exchange, 405, error("Use GET")); return; }
        try (InputStream input = WebServer.class.getResourceAsStream("/web/index.html")) {
            if (input == null) { send(exchange, 500, error("No se encontró /web/index.html en el classpath.")); return; }
            byte[] bytes = input.readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            exchange.getResponseHeaders().set("Cache-Control", "no-store");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream out = exchange.getResponseBody()) { out.write(bytes); }
        }
    }

    private void handleApi(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath().substring("/api".length());
        String method = exchange.getRequestMethod().toUpperCase(Locale.ROOT);
        try {
            if ("GET".equals(method)) {
                Object result = switch (path) {
                    case "/health" -> Map.of("status", "UP", "application", "GameZone Unicesar");
                    case "/dashboard" -> dashboard();
                    case "/products" -> products.getAllProducts();
                    case "/accessories" -> accessories.listAllAccessories();
                    case "/clients" -> people.getClients();
                    case "/sellers" -> people.getSellers();
                    case "/promotions" -> promotions.listAllPromotions();
                    case "/sales" -> sales.getAllSales();
                    case "/returns" -> returns.viewAllReturns();
                    case "/warranties" -> warranties.listAllWarranties();
                    default -> null;
                };
                if (result == null) send(exchange, 404, error("Endpoint no encontrado: " + path));
                else send(exchange, 200, gson.toJson(result));
                return;
            }
            if (!"POST".equals(method)) { send(exchange, 405, error("Use GET o POST")); return; }
            JsonObject body = JsonParser.parseString(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8)).getAsJsonObject();
            Object result = switch (path) {
                case "/products" -> createProduct(body);
                case "/accessories" -> createAccessory(body);
                case "/clients" -> people.registerClient(required(body,"id"), required(body,"name"), required(body,"email"), required(body,"phone"), value(body,"membershipLevel","STANDARD"));
                case "/promotions" -> createPromotion(body);
                case "/sales" -> createSale(body);
                case "/returns" -> returns.registerReturn(required(body,"saleId"), list(body.getAsJsonArray("itemIds")), required(body,"reason"));
                default -> null;
            };
            if (result == null) send(exchange, 404, error("Endpoint no encontrado: " + path));
            else send(exchange, 201, gson.toJson(result));
        } catch (IllegalArgumentException e) {
            send(exchange, 400, error(e.getMessage()));
        } catch (Exception e) {
            send(exchange, 500, error("No se pudo completar la operación: " + e.getMessage()));
        }
    }

    private Object createProduct(JsonObject b) throws IOException {
        String type = required(b,"type").toUpperCase(Locale.ROOT);
        String id = required(b,"id"), name = required(b,"name");
        double price = number(b,"price"); int stock = integer(b,"stock");
        return switch (type) {
            case "VIDEOGAME" -> products.registerVideoGame(id,name,price,stock,required(b,"platform"),required(b,"genre"));
            case "CONSOLE" -> products.registerConsole(id,name,price,stock,required(b,"manufacturer"),required(b,"model"));
            default -> throw new IllegalArgumentException("type debe ser VIDEOGAME o CONSOLE.");
        };
    }

    private Object createAccessory(JsonObject b) throws IOException {
        String type = required(b,"type").toUpperCase(Locale.ROOT);
        String id = required(b,"id"), name = required(b,"name");
        double price = number(b,"price"); int stock = integer(b,"stock");
        List<String> compatible = b.has("compatibleConsoleIds") ? list(b.getAsJsonArray("compatibleConsoleIds")) : List.of();
        return switch (type) {
            case "CONTROLLER" -> accessories.registerController(id,name,price,stock,compatible,required(b,"connectionType"));
            case "CABLE" -> accessories.registerCable(id,name,price,stock,compatible,number(b,"lengthMeters"),required(b,"connectorType"));
            case "MEMORY" -> accessories.registerMemory(id,name,price,stock,compatible,integer(b,"capacityGb"),required(b,"memoryType"));
            default -> throw new IllegalArgumentException("type debe ser CONTROLLER, CABLE o MEMORY.");
        };
    }

    private Object createPromotion(JsonObject b) throws IOException {
        String type = required(b,"type").toUpperCase(Locale.ROOT), id = required(b,"id"), name = required(b,"name");
        LocalDate start = LocalDate.parse(required(b,"startDate")), end = LocalDate.parse(required(b,"endDate"));
        double percent = number(b,"percentage");
        return switch (type) {
            case "PERCENTAGE" -> promotions.registerPercentageDiscount(id,name,start,end,percent);
            case "CATEGORY" -> promotions.registerCategoryDiscount(id,name,start,end,percent,required(b,"category"));
            case "BULK" -> promotions.registerBulkPurchaseDiscount(id,name,start,end,integer(b,"minimumQuantity"),percent);
            default -> throw new IllegalArgumentException("type debe ser PERCENTAGE, CATEGORY o BULK.");
        };
    }

    private Object createSale(JsonObject b) throws IOException {
        List<String> items = list(b.getAsJsonArray("itemIds"));
        Map<String,Boolean> extended = new HashMap<>();
        if (b.has("extendedWarrantyIds") && b.get("extendedWarrantyIds").isJsonArray())
            for (String id : list(b.getAsJsonArray("extendedWarrantyIds"))) extended.put(id,true);
        return sales.registerSale(required(b,"customerId"),required(b,"sellerId"),items,extended);
    }

    private JsonObject dashboard() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue(), year = now.getYear();
        int productStock = products.getAllProducts().stream().mapToInt(Product::getStock).sum();
        int accessoryStock = accessories.listAllAccessories().stream().mapToInt(a -> a.getStock()).sum();
        long lowProducts = products.getAllProducts().stream().filter(p -> p.getStock() <= 3).count();
        long lowAccessories = accessories.listAllAccessories().stream().filter(a -> a.getStock() <= 3).count();
        double monthlySales = returns.calculateMonthlySales(month,year), monthlyReturns = returns.calculateMonthlyReturns(month,year);
        JsonObject out = new JsonObject();
        out.addProperty("products",products.getAllProducts().size()); out.addProperty("accessories",accessories.listAllAccessories().size());
        out.addProperty("clients",people.getClients().size()); out.addProperty("sellers",people.getSellers().size());
        out.addProperty("promotions",promotions.listAllPromotions().size()); out.addProperty("activePromotions",promotions.listActivePromotions().size());
        out.addProperty("sales",sales.getAllSales().size()); out.addProperty("returns",returns.viewAllReturns().size());
        out.addProperty("activeWarranties",warranties.listActiveWarranties().size()); out.addProperty("inventoryUnits",productStock+accessoryStock);
        out.addProperty("lowStockItems",lowProducts+lowAccessories); out.addProperty("monthlySales",monthlySales);
        out.addProperty("monthlyReturns",monthlyReturns); out.addProperty("monthlyBalance",monthlySales-monthlyReturns);
        return out;
    }

    private List<String> list(JsonArray array) {
        if (array == null) throw new IllegalArgumentException("Falta una lista requerida.");
        List<String> values = new ArrayList<>();
        for (JsonElement e : array) { String v=e.getAsString().trim(); if (!v.isBlank()) values.add(v); }
        return values;
    }
    private String required(JsonObject b,String key) {
        String v=value(b,key,"").trim();
        if (v.isBlank()) throw new IllegalArgumentException("El campo '"+key+"' es obligatorio.");
        return v;
    }
    private String value(JsonObject b,String key,String fallback) { return b.has(key)&&!b.get(key).isJsonNull()?b.get(key).getAsString():fallback; }
    private double number(JsonObject b,String key) {
        if (!b.has(key)||!b.get(key).isJsonPrimitive()) throw new IllegalArgumentException("El campo numérico '"+key+"' es obligatorio.");
        double v=b.get(key).getAsDouble();
        if (!Double.isFinite(v)) throw new IllegalArgumentException("El campo '"+key+"' debe ser finito.");
        return v;
    }
    private int integer(JsonObject b,String key) {
        double v=number(b,key);
        if (v!=Math.rint(v)||v<Integer.MIN_VALUE||v>Integer.MAX_VALUE) throw new IllegalArgumentException("El campo '"+key+"' debe ser entero.");
        return (int)v;
    }
    private String error(String message) { JsonObject e=new JsonObject(); e.addProperty("error",message==null?"Error desconocido":message); return gson.toJson(e); }
    private void send(HttpExchange exchange,int status,String body) throws IOException {
        byte[] bytes=body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type","application/json; charset=utf-8");
        exchange.sendResponseHeaders(status,bytes.length);
        try(OutputStream out=exchange.getResponseBody()){out.write(bytes);}
    }
}
