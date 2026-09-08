package com.gamezone.model;

/** Represents a GameZone customer. */
public class Client extends Person {
    private static final long serialVersionUID = 1L;
    private String membershipLevel;

    /** Creates a customer. */
    public Client(String id, String name, String email, String phone, String membershipLevel) {
        super(id, name, email, phone); setMembershipLevel(membershipLevel);
    }
    public String getMembershipLevel() { return membershipLevel; }
    public void setMembershipLevel(String membershipLevel) { if (membershipLevel == null || membershipLevel.isBlank()) throw new IllegalArgumentException("Membership level is required."); this.membershipLevel = membershipLevel.trim(); }
    @Override public String getRoleDescription() { return "Client | membership=" + membershipLevel; }
}
