package com.gamezone.service;

import com.gamezone.exception.BusinessRuleException;
import com.gamezone.model.Sale;
import com.gamezone.model.SaleLine;
import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReturnServiceTest {
    @TempDir
    Path tempDir;

    @Test
    void allowsPartialQuantityReturnsButNeverMoreThanWasSold() throws IOException {
        ProductService products = new ProductService(new ProductRepository(tempDir.resolve("products.dat")));
        products.registerConsole("C-1", "Console test", 100.0, 0, "GameZone", "X");
        AccessoryService accessories = new AccessoryService(new AccessoryRepository(tempDir.resolve("accessories.csv")));
        WarrantyService warranties = new WarrantyService(
                new WarrantyRepository(tempDir.resolve("warranties.csv")), products);

        Sale sale = new Sale("S-1", LocalDateTime.now(), "CLIENT-1", "SELLER-1",
                List.of("C-1", "C-1"),
                List.of(new SaleLine("C-1", "Console test", "CONSOLE", 100.0, 2, 20.0)),
                180.0);
        sale.setSubtotal(200.0);
        sale.setDiscountAmount(20.0);
        SaleRepository saleRepository = new SaleRepository(tempDir.resolve("sales.dat"));
        saleRepository.saveAll(List.of(sale));
        SaleService sales = new SaleService(saleRepository);

        ReturnService returns = new ReturnService(
                new ReturnRepository(tempDir.resolve("returns.csv")), sales, products, accessories, warranties);

        com.gamezone.model.Return first = returns.registerReturn("S-1", List.of("C-1"), "First unit");
        assertEquals(90.0, first.getRefundAmount(), 0.0001);
        assertEquals(1, products.findById("C-1").getStock());

        com.gamezone.model.Return second = returns.registerReturn("S-1", List.of("C-1"), "Second unit");
        assertEquals(90.0, second.getRefundAmount(), 0.0001);
        assertEquals(2, products.findById("C-1").getStock());

        assertThrows(BusinessRuleException.class,
                () -> returns.registerReturn("S-1", List.of("C-1"), "Too many units"));
        assertEquals(2, products.findById("C-1").getStock());
        assertEquals(2, returns.viewAllReturns().size());
    }
}
