package com.gamezone.service;

import com.gamezone.exception.BusinessRuleException;
import com.gamezone.exception.ResourceNotFoundException;
import com.gamezone.model.Accessory;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.model.SaleLine;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.validation.ReturnValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

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

        List<String> normalized = itemIds.stream().map(String::trim).toList();
        Map<String, Integer> requestedQuantities = countItems(normalized);
        for (Map.Entry<String, Integer> entry : requestedQuantities.entrySet()) {
            String itemId = entry.getKey();
            int soldQuantity = quantitySold(sale, itemId);
            if (soldQuantity == 0) {
                throw new BusinessRuleException("El producto " + itemId + " no pertenece a la venta indicada.");
            }
            int returnedBefore = quantityAlreadyReturned(saleId, itemId);
            if (returnedBefore + entry.getValue() > soldQuantity) {
                throw new BusinessRuleException("La cantidad solicitada para " + itemId + " supera la cantidad vendida disponible para devolución.");
            }
        }

        double refund = calculateHistoricalRefund(sale, normalized);
        double warrantyRefund = 0.0;
        for (String itemId : new LinkedHashSet<>(normalized)) {
            if (productService.exists(itemId) && productService.isConsole(itemId)) {
                warrantyRefund += warrantyService.cancelWarranties(itemId, saleId);
            }
        }
        for (Map.Entry<String, Integer> entry : requestedQuantities.entrySet()) {
            if (productService.exists(entry.getKey())) productService.restoreStock(entry.getKey(), entry.getValue());
            else accessoryService.restoreStock(entry.getKey(), entry.getValue());
        }

        Return result = new Return(UUID.randomUUID().toString(), saleId, LocalDate.now(), normalized,
                reason.trim(), refund + warrantyRefund, warrantyRefund);
        returns.add(result);
        try {
            repository.saveAll(returns);
        } catch (IOException exception) {
            returns.remove(result);
            throw exception;
        }
        return result;
    }

    public String generateReturnReceipt(String returnId) {
        Return returnRecord = returns.stream()
                .filter(value -> value.getId().equals(returnId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("La devolución no existe: " + returnId));
        Sale sale = saleService.getAllSales().stream()
                .filter(value -> value.getId().equals(returnRecord.getSaleId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("La venta asociada no existe: " + returnRecord.getSaleId()));

        Map<String, Double> historicalPrices = new HashMap<>();
        for (String itemId : returnRecord.getItemIds()) {
            SaleLine line = sale.findLine(itemId);
            if (line != null) {
                historicalPrices.put(itemId, line.getUnitPriceAtSale());
            } else {
                historicalPrices.putAll(resolveLegacyPrices(List.of(itemId)));
            }
        }
        return returnRecord.generateReturnReceipt(historicalPrices);
    }

    public List<Return> viewAllReturns() { return new ArrayList<>(returns); }
    public List<Return> viewReturnsByCustomer(String customerId) {
        return returns.stream().filter(r -> saleService.getAllSales().stream()
                .anyMatch(s -> s.getId().equals(r.getSaleId()) && s.getCustomerId().equals(customerId))).toList();
    }
    public List<Return> viewReturnsBySale(String saleId) { return returns.stream().filter(r -> r.getSaleId().equals(saleId)).toList(); }
    public double calculateMonthlySales(int month, int year) {
        return saleService.getAllSales().stream().filter(s -> s.getDate().getMonthValue() == month && s.getDate().getYear() == year).mapToDouble(Sale::getTotal).sum();
    }
    public double calculateMonthlyReturns(int month, int year) {
        return returns.stream().filter(r -> r.getDate().getMonthValue() == month && r.getDate().getYear() == year).mapToDouble(Return::getRefundAmount).sum();
    }
    public double generateMonthlyBalance(int month, int year) { return calculateMonthlySales(month, year) - calculateMonthlyReturns(month, year); }

    private Map<String, Integer> countItems(List<String> itemIds) {
        Map<String, Integer> quantities = new LinkedHashMap<>();
        for (String itemId : itemIds) quantities.merge(itemId, 1, Integer::sum);
        return quantities;
    }

    private int quantitySold(Sale sale, String itemId) {
        SaleLine line = sale.findLine(itemId);
        if (line != null) return line.getQuantity();
        return (int) sale.getProductIds().stream().filter(itemId::equals).count();
    }

    private int quantityAlreadyReturned(String saleId, String itemId) {
        return returns.stream()
                .filter(value -> value.getSaleId().equals(saleId))
                .mapToInt(value -> (int) value.getItemIds().stream().filter(itemId::equals).count())
                .sum();
    }

    private double calculateHistoricalRefund(Sale sale, List<String> itemIds) {
        if (!sale.getLines().isEmpty()) {
            double refund = 0.0;
            for (String itemId : itemIds) {
                SaleLine line = sale.findLine(itemId);
                if (line == null) throw new BusinessRuleException("No existe información histórica del producto " + itemId + " en la venta.");
                refund += line.getLineTotal() / line.getQuantity();
            }
            return refund;
        }
        return Return.calculateRefundAmount(sale, itemIds, resolveLegacyPrices(itemIds));
    }

    private Map<String, Double> resolveLegacyPrices(List<String> ids) {
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
