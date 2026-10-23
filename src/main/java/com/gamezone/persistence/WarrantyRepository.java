package com.gamezone.persistence;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Warranty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Persists warranties as discriminated CSV records. */
public class WarrantyRepository {
    private final Path path;

    public WarrantyRepository(Path path) { this.path = path; }

    public void saveAll(List<Warranty> warranties) throws IOException {
        if (path.getParent() != null) Files.createDirectories(path.getParent());
        List<String> lines = new ArrayList<>();
        for (Warranty warranty : warranties) {
            lines.add(String.join(",", warranty instanceof ExtendedWarranty ? "EXTENDED" : "BASIC",
                    warranty.getId(), warranty.getProductId(), warranty.getSaleId(),
                    Double.toString(warranty.getProductPrice()), warranty.getStartDate().toString()));
        }
        Files.write(path, lines);
    }

    public List<Warranty> loadAll() throws IOException {
        if (!Files.exists(path)) return new ArrayList<>();
        List<Warranty> warranties = new ArrayList<>();
        for (String line : Files.readAllLines(path)) {
            if (line.isBlank() || line.startsWith("type,")) continue;
            String[] v = line.split(",", -1);
            if (v.length < 6) continue;
            LocalDate start = LocalDate.parse(v[5]);
            double price = Double.parseDouble(v[4]);
            if ("EXTENDED".equals(v[0])) {
                warranties.add(new ExtendedWarranty(v[1], v[2], v[3], price, start));
            } else if ("BASIC".equals(v[0])) {
                warranties.add(new BasicWarranty(v[1], v[2], v[3], price, start));
            }
        }
        return warranties;
    }
}
