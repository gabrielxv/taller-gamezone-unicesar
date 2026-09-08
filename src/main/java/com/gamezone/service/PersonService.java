package com.gamezone.service;

import com.gamezone.model.Client;
import com.gamezone.model.Person;
import com.gamezone.model.Seller;
import com.gamezone.persistence.PersonRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Applies people business rules and coordinates people persistence. */
public class PersonService implements PeopleGateway {
    private final PersonRepository repository;
    private final List<Person> people;

    /** Creates the service and loads persisted people. */
    public PersonService(PersonRepository repository) throws IOException { this.repository = repository; this.people = repository.loadAll(); }
    /** Registers a customer. */
    public Client registerClient(String id, String name, String email, String phone, String membershipLevel) throws IOException {
        ensureUnique(id); Client client = new Client(id, name, email, phone, membershipLevel); people.add(client); save(); return client;
    }
    /** Adds an initial seller when that seller is not already stored. */
    public Seller addSeller(String id, String name, String email, String phone, String employeeCode) throws IOException {
        if (findNullable(id) != null) return (Seller) find(id); Seller seller = new Seller(id, name, email, phone, employeeCode); people.add(seller); save(); return seller;
    }
    /** Returns all registered customers. */
    public List<Client> getClients() { return people.stream().filter(Client.class::isInstance).map(Client.class::cast).toList(); }
    /** Returns all registered sellers. */
    public List<Seller> getSellers() { return people.stream().filter(Seller.class::isInstance).map(Seller.class::cast).toList(); }
    private void ensureUnique(String id) { if (findNullable(id) != null) throw new IllegalArgumentException("Person ID already exists."); }
    private Person find(String id) { Person p = findNullable(id); if (p == null) throw new IllegalArgumentException("Person does not exist: " + id); return p; }
    private Person findNullable(String id) { if (id == null) return null; return people.stream().filter(p -> p.getId().equals(id.trim())).findFirst().orElse(null); }
    private void save() throws IOException { repository.saveAll(people); }
    @Override public boolean customerExists(String customerId) { return people.stream().anyMatch(p -> p instanceof Client && p.getId().equals(customerId)); }
    @Override public boolean sellerExists(String sellerId) { return people.stream().anyMatch(p -> p instanceof Seller && p.getId().equals(sellerId)); }
}
