package com.gamezone.persistence;

import com.gamezone.model.Person;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/** Persists people using Java serialization in a local data file. */
public class PersonRepository {
    private final Path file;
    /** Creates a repository backed by the supplied file. */
    public PersonRepository(Path file) { this.file = file; }
    /** Saves all people. */
    public void saveAll(List<Person> people) throws IOException {
        Files.createDirectories(file.getParent());
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) { out.writeObject(new ArrayList<>(people)); }
    }
    /** Loads all people or an empty list when no data file exists. */
    @SuppressWarnings("unchecked")
    public List<Person> loadAll() throws IOException {
        if (!Files.exists(file)) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            return new ArrayList<>((List<Person>) in.readObject());
        } catch (ClassNotFoundException exception) { throw new IOException("Unable to read people.", exception); }
    }
}
