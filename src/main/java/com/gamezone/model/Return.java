package com.gamezone.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Represents a partial or complete return associated with a sale. */
public class Return implements Serializable {
    private final String id;
    private final String saleId;
    private final LocalDate date;
    private final List<String> itemIds;
    private final String reason;
    private final double refundAmount;
    private final double warrantyRefund;

    public Return(String id, String saleId, LocalDate date, List<String> itemIds,
                  String reason, double refundAmount, double warrantyRefund) {
        this.id = id;
        this.saleId = saleId;
        this.date = date;
        this.itemIds = new ArrayList<>(itemIds);
        this.reason = reason;
        this.refundAmount = refundAmount;
        this.warrantyRefund = warrantyRefund;
    }

    public String getId() { return id; }
    public String getSaleId() { return saleId; }
    public LocalDate getDate() { return date; }
    public List<String> getItemIds() { return Collections.unmodifiableList(itemIds); }
    public String getReason() { return reason; }
    public double getRefundAmount() { return refundAmount; }
    public double getWarrantyRefund() { return warrantyRefund; }

    /** Calculates a proportional refund when the original sale had a discount. */
    public static double calculateRefundAmount(Sale sale, List<String> returnedIds, Map<String, Double> prices) {
        double subtotal = sale.getSubtotal() > 0 ? sale.getSubtotal() : sale.getTotal();
        double factor = subtotal <= 0 ? 1.0 : 1.0 - (sale.getDiscountAmount() / subtotal);
        return returnedIds.stream().mapToDouble(id -> prices.getOrDefault(id, 0.0) * factor).sum();
    }

    public String generateReturnReceipt(Map<String, Double> prices) {
        double subtotal = itemIds.stream().mapToDouble(id -> prices.getOrDefault(id, 0.0)).sum();
        double itemRefund = Math.max(0.0, refundAmount - warrantyRefund);
        double factor = subtotal <= 0 ? 1.0 : Math.max(0.0, Math.min(1.0, itemRefund / subtotal));
        StringBuilder receipt = new StringBuilder("Recibo de devolución\\n");
        for (String itemId : itemIds) {
            double price = prices.getOrDefault(itemId, 0.0);
            receipt.append(itemId).append(" | precio=").append(price)
                    .append(" | descuento proporcional=").append(price - price * factor)
                    .append(" | reembolso=").append(price * factor).append('\\n');
        }
        receipt.append("Reembolso de garantía: ").append(warrantyRefund).append('\\n');
        receipt.append("Total reembolsado: ").append(refundAmount);
        return receipt.toString();
    }
}
