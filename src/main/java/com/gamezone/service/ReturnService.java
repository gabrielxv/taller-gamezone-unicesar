package com.gamezone.service;

import com.gamezone.exception.BusinessRuleException;
import com.gamezone.exception.ResourceNotFoundException;
import com.gamezone.model.Accessory;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.validation.ReturnValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Coordinates return rules, inventory restoration, refunds, and monthly reporting. */
public class ReturnService {
    private final ReturnRepository repository;
    private final SaleService saleService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final WarrantyService warrantyService;
    private final List<Return> returns;

    public ReturnService(ReturnRepository repository, SaleService saleService, ProductService productService,
                         AccessoryService accessoryService, WarrantyService warrantyService) throws IOException {
        this.repository = repository;
        this.saleService = saleService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.warrantyService = warrantyService;
        this.returns = repository.loadAll();
    }

    public Return registerReturn(String saleId, List<String> itemIds, String reason) throws IOException {
        Sale sale = saleService.getAllSales().stream().filter(s -> s.getId().equals(saleId)).findFirst().orElse(null);
        if (sale == null) throw new ResourceNotFoundException("La venta no existe: " + saleId);
        if (!sale.canBeReturned()) throw new BusinessRuleException("La devolución supera el plazo de 30 días.");
        ReturnValidator.validateItems(itemIds);
        ReturnValidator.validateReason(reason);

        List<String> normalized = itemIds.stream().map(String::trim).filter(s -> !s.isBlank()).toList();
        for (String itemId : normalized) {
            if (!sale.getProductIds().contains(itemId)) throw new BusinessRuleException("El producto " + itemId + " no pertenece a la venta indicada.");
            if (alreadyReturned(saleId, itemId)) throw new BusinessRuleException("El producto " + itemId + " ya fue devuelto.");
        }

        Map<String, Double> prices = resolvePrices(normalized);
        double refund = Return.calculateRefundAmount(sale, normalized, prices);
        double warrantyRefund = 0.0;
        for (String itemId : normalized) {
            if (productService.isConsole(itemId)) warrantyRefund += warrantyService.cancelWarranties(itemId, saleId);
            if (productService.exists(itemId)) productService.restoreStock(itemId, 1);
            else accessoryService.restoreStock(itemId, 1);
        }

        Return result = new Return(UUID.randomUUID().toString(), saleId, LocalDate.now(), normalized,
                reason.trim(), refund + warrantyRefund, warrantyRefund);
        returns.add(result);
        repository.saveAll(returns);
        return result;
    }

    public List<Return> viewAllReturns() { return new ArrayList<>(returns); }

    public List<Return> viewReturnsByCustomer(String customerId) {
        return returns.stream().filter(r -> saleService.getAllSales().stream()
                .anyMatch(s -> s.getId().equals(r.getSaleId()) && s.getCustomerId().equals(customerId))).toList();
    }

    public List<Return> viewReturnsBySale(String saleId) { return returns.stream().filter(r -> r.getSaleId().equals(saleId)).toList(); }

    public double calculateMonthlySales(int month, int year) {
        return saleService.getAllSales().stream()
                .filter(s -> s.getDate().getMonthValue() == month && s.getDate().getYear() == year)
                .mapToDouble(Sale::getTotal).sum();
    }

    public double calculateMonthlyReturns(int month, int year) {
        return returns.stream()
                .filter(r -> r.getDate().getMonthValue() == month && r.getDate().getYear() == year)
                .mapToDouble(Return::getRefundAmount).sum();
    }

    public double generateMonthlyBalance(int month, int year) {
        return calculateMonthlySales(month, year) - calculateMonthlyReturns(month, year);
    }

    private boolean alreadyReturned(String saleId, String itemId) {
        return returns.stream().filter(r -> r.getSaleId().equals(saleId)).anyMatch(r -> r.getItemIds().contains(itemId));
    }

    private Map<String, Double> resolvePrices(List<String> ids) {
        Map<String, Double> prices = new HashMap<>();
        for (String id : ids) {
            if (productService.exists(id)) prices.put(id, productService.getPrice(id));
            else {
                Accessory accessory = accessoryService.findById(id);
                if (accessory == null) throw new ResourceNotFoundException("El producto o accesorio no existe: " + id);
                prices.put(id, accessory.getPrice());
            }
        }
        return prices;
    }
}
