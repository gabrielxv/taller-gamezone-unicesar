package com.gamezone.model;

/** Represents a GameZone seller who is preloaded into the system. */
public class Seller extends Person {
    private static final long serialVersionUID = 1L;
    private String employeeCode;

    /** Creates a seller. */
    public Seller(String id, String name, String email, String phone, String employeeCode) {
        super(id, name, email, phone); setEmployeeCode(employeeCode);
    }
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { if (employeeCode == null || employeeCode.isBlank()) throw new IllegalArgumentException("Employee code is required."); this.employeeCode = employeeCode.trim(); }
    @Override public String getRoleDescription() { return "Seller | employeeCode=" + employeeCode; }
}
