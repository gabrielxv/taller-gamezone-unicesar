package com.gamezone.persistence;

import com.gamezone.model.Return;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Persists return records as CSV. */
public class ReturnRepository {
    private final Path path;

    public ReturnRepository(Path path) { this.path = path; }

    public void saveAll(List<Return> returns) throws IOException {
        if (path.getParent() != null) Files.createDirectories(path.getParent());
        List<String> lines = new ArrayList<>();
        for (Return value : returns) {
            String items = String.join("|", value.getItemIds());
            String reason = value.getReason().replace(",", " ");
            lines.add(String.join(",", value.getId(), value.getSaleId(), value.getDate().toString(),
                    items, reason, Double.toString(value.getRefundAmount()), Double.toString(value.getWarrantyRefund())));
        }
        Files.write(path, lines);
    }

    public List<Return> loadAll() throws IOException {
        if (!Files.exists(path)) return new ArrayList<>();
        List<Return> returns = new ArrayList<>();
        for (String line : Files.readAllLines(path)) {
            if (line.isBlank() || line.startsWith("id,")) continue;
            String[] v = line.split(",", -1);
            if (v.length < 7) continue;
            List<String> items = Arrays.stream(v[3].split("\\|"))
                    .filter(s -> !s.isBlank()).toList();
            returns.add(new Return(v[0], v[1], LocalDate.parse(v[2]), items, v[4],
                    Double.parseDouble(v[5]), Double.parseDouble(v[6])));
        }
        return returns;
    }
}
