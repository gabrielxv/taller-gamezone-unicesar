package com.gamezone;

import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.PromotionRepository;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;
import com.gamezone.ui.ConsoleUI;
import com.gamezone.web.WebServer;

import java.io.IOException;
import java.nio.file.Path;

/** Starts GameZone and wires the shared application services. */
public class Main {
    public static void main(String[] args) {
        try {
            ProductService productService = new ProductService(new ProductRepository(Path.of("data", "products.dat")));
            AccessoryService accessoryService = new AccessoryService(new AccessoryRepository(Path.of("data", "accessories.csv")));
            PersonService personService = new PersonService(new PersonRepository(Path.of("data", "people.dat")));
            preloadSellers(personService);

            PromotionService promotionService = new PromotionService(
                    new PromotionRepository(Path.of("data", "promotions.csv")), productService, accessoryService);
            WarrantyService warrantyService = new WarrantyService(
                    new WarrantyRepository(Path.of("data", "warranties.csv")), productService);
            SaleService saleService = new SaleService(
                    new SaleRepository(Path.of("data", "sales.dat")), productService, personService,
                    accessoryService, promotionService, warrantyService);
            ReturnService returnService = new ReturnService(
                    new ReturnRepository(Path.of("data", "returns.csv")), saleService, productService,
                    accessoryService, warrantyService);

            if (args.length > 0 && "--web".equalsIgnoreCase(args[0])) {
                int port = Integer.parseInt(System.getenv().getOrDefault("GAMEZONE_PORT", "8080"));
                new WebServer(productService, accessoryService, personService, promotionService,
                        saleService, returnService, warrantyService).start(port);
                return;
            }

            new ConsoleUI(productService, accessoryService, personService, saleService,
                    promotionService, warrantyService, returnService).start();
        } catch (IOException | RuntimeException exception) {
            System.err.println("Unable to start GameZone: " + exception.getMessage());
        }
    }

    private static void preloadSellers(PersonService service) throws IOException {
        service.addSeller("S001", "Laura Gomez", "laura@gamezone.com", "3001001001", "EMP-001");
        service.addSeller("S002", "Carlos Perez", "carlos@gamezone.com", "3001001002", "EMP-002");
        service.addSeller("S003", "Andres Ruiz", "andres@gamezone.com", "3001001003", "EMP-003");
    }
}
