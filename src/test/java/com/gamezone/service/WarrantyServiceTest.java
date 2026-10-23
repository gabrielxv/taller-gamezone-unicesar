package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.Console;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Sale;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.WarrantyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class WarrantyServiceTest {
    @TempDir
    Path tempDir;

    @Test
    void assignmentIsIdempotentAndKeepsBasicAndExtendedTypesSeparate() throws IOException {
        ProductService products = new ProductService(new ProductRepository(tempDir.resolve("products.dat")));
        Console console = (Console) products.registerConsole(
                "CON-1", "Test console", 100.0, 3, "GameZone", "Model X");
        Sale sale = new Sale("SALE-1", LocalDateTime.now(), "CLIENT-1", "SELLER-1",
                List.of(console.getId()), console.getPrice());
        WarrantyService warranties = new WarrantyService(
                new WarrantyRepository(tempDir.resolve("warranties.csv")), products);

        BasicWarranty firstBasic = warranties.assignBasicWarranty(console, sale, LocalDate.now());
        BasicWarranty secondBasic = warranties.assignBasicWarranty(console, sale, LocalDate.now());
        ExtendedWarranty firstExtended = warranties.assignExtendedWarranty(console, sale, LocalDate.now());
        ExtendedWarranty secondExtended = warranties.assignExtendedWarranty(console, sale, LocalDate.now());

        assertSame(firstBasic, secondBasic);
        assertSame(firstExtended, secondExtended);
        assertEquals(2, warranties.listAllWarranties().size());
        assertEquals(0.0, firstBasic.getAdditionalCost());
        assertEquals(10.0, firstExtended.getAdditionalCost(), 0.0001);

        WarrantyService reloaded = new WarrantyService(
                new WarrantyRepository(tempDir.resolve("warranties.csv")), products);
        assertEquals(2, reloaded.listAllWarranties().size());
    }
}
