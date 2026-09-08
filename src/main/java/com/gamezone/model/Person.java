package com.gamezone.model;

import java.io.Serializable;

/** Represents common information shared by customers and sellers. */
public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String email;
    private String phone;

    /** Creates a person with common contact information. */
    protected Person(String id, String name, String email, String phone) {
        setId(id); setName(name); setEmail(email); setPhone(phone);
    }
    public String getId() { return id; }
    public void setId(String id) { if (id == null || id.isBlank()) throw new IllegalArgumentException("Person ID is required."); this.id = id.trim(); }
    public String getName() { return name; }
    public void setName(String name) { if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required."); this.name = name.trim(); }
    public String getEmail() { return email; }
    public void setEmail(String email) { if (email == null || !email.contains("@")) throw new IllegalArgumentException("A valid email is required."); this.email = email.trim(); }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { if (phone == null || phone.isBlank()) throw new IllegalArgumentException("Phone is required."); this.phone = phone.trim(); }
    /** Returns the role-specific description used by the UI. */
    public abstract String getRoleDescription();
    @Override public String toString() { return id + " | " + name + " | " + email + " | " + phone + " | " + getRoleDescription(); }
}
