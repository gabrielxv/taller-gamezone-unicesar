package com.gamezone;

import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.SaleService;
import com.gamezone.ui.ConsoleUI;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Starts the GameZone application and wires its initial dependencies.
 */
public class Main {
    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        try {
            SaleRepository saleRepository = new SaleRepository(Path.of("data", "sales.dat"));
            SaleService saleService = new SaleService(saleRepository);
            ConsoleUI consoleUI = new ConsoleUI(saleService);
            consoleUI.start();
        } catch (IOException exception) {
            System.err.println("Unable to start GameZone: " + exception.getMessage());
        }
    }
}
