package com.gamezone.persistence;

import com.gamezone.model.Sale;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides file-based persistence for sales.
 */
public class SaleRepository {
    private final Path filePath;

    /**
     * Creates a repository using the supplied data file.
     *
     * @param filePath path where sales are stored
     */
    public SaleRepository(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves all sales to the configured file.
     *
     * @param sales sales to persist
     * @throws IOException if the file cannot be written
     */
    public void saveAll(List<Sale> sales) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            output.writeObject(new ArrayList<>(sales));
        }
    }

    /**
     * Loads all previously stored sales.
     *
     * @return stored sales, or an empty list when no data exists
     * @throws IOException if stored data cannot be read
     */
    @SuppressWarnings("unchecked")
    public List<Sale> loadAll() throws IOException {
        if (!Files.exists(filePath) || Files.size(filePath) == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(filePath))) {
            return new ArrayList<>((List<Sale>) input.readObject());
        } catch (EOFException exception) {
            return new ArrayList<>();
        } catch (ClassNotFoundException exception) {
            throw new IOException("Stored sale data is invalid.", exception);
        }
    }
}
