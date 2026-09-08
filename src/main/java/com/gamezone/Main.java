package com.gamezone;

import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.ui.ConsoleUI;
import java.io.IOException;
import java.nio.file.Path;

/** Starts GameZone and wires all application layers. */
public class Main {
    /** Starts the console application. */
    public static void main(String[] args) {
        try {
            ProductService productService = new ProductService(new ProductRepository(Path.of("data", "products.dat")));
            PersonService personService = new PersonService(new PersonRepository(Path.of("data", "people.dat")));
            preloadSellers(personService);
            SaleService saleService = new SaleService(new SaleRepository(Path.of("data", "sales.dat")), productService, personService);
            new ConsoleUI(productService, personService, saleService).start();
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
