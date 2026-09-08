package com.gamezone.persistence;

import com.gamezone.model.Product;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/** Persists products using Java serialization in a local data file. */
public class ProductRepository {
    private final Path file;
    /** Creates a repository backed by the supplied file. */
    public ProductRepository(Path file) { this.file = file; }
    /** Saves the complete product collection. */
    public void saveAll(List<Product> products) throws IOException {
        Files.createDirectories(file.getParent());
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) { out.writeObject(new ArrayList<>(products)); }
    }
    /** Loads all stored products, returning an empty list when the file does not exist. */
    @SuppressWarnings("unchecked")
    public List<Product> loadAll() throws IOException {
        if (!Files.exists(file)) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            return new ArrayList<>((List<Product>) in.readObject());
        } catch (ClassNotFoundException exception) { throw new IOException("Unable to read products.", exception); }
    }
}
