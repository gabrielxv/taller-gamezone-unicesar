package com.gamezone.persistence;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Persists promotions as discriminated CSV records. */
public class PromotionRepository {
    private final Path path;

    public PromotionRepository(Path path) { this.path = path; }

    public void saveAll(List<Promotion> promotions) throws IOException {
        if (path.getParent() != null) Files.createDirectories(path.getParent());
        List<String> lines = new ArrayList<>();
        for (Promotion promotion : promotions) {
            String base = String.join(",", promotion.getType(), promotion.getId(), promotion.getName(),
                    promotion.getStartDate().toString(), promotion.getEndDate().toString());
            if (promotion instanceof PercentageDiscount p) {
                lines.add(base + "," + p.getPercentage());
            } else if (promotion instanceof CategoryDiscount p) {
                lines.add(base + "," + p.getPercentage() + "," + p.getCategory());
            } else if (promotion instanceof BulkPurchaseDiscount p) {
                lines.add(base + "," + p.getMinimumQuantity() + "," + p.getPercentage());
            }
        }
        Files.write(path, lines);
    }

    public List<Promotion> loadAll() throws IOException {
        if (!Files.exists(path)) return new ArrayList<>();
        List<Promotion> promotions = new ArrayList<>();
        for (String line : Files.readAllLines(path)) {
            if (line.isBlank() || line.startsWith("type,")) continue;
            String[] v = line.split(",", -1);
            if (v.length < 6) continue;
            String type = v[0];
            String id = v[1];
            String name = v[2];
            LocalDate start = LocalDate.parse(v[3]);
            LocalDate end = LocalDate.parse(v[4]);
            switch (type) {
                case "PERCENTAGE" -> promotions.add(new PercentageDiscount(id, name, start, end, Double.parseDouble(v[5])));
                case "CATEGORY" -> promotions.add(new CategoryDiscount(id, name, start, end, Double.parseDouble(v[5]), v[6]));
                case "BULK" -> promotions.add(new BulkPurchaseDiscount(id, name, start, end, Integer.parseInt(v[5]), Double.parseDouble(v[6])));
                default -> { }
            }
        }
        return promotions;
    }
}
