
package com.ipl.auction.model;

public class Player {
    
    private final int id;
    private final String name;
    private final String country;
    private final PlayerRole role;
    private final double basePrice;
    private PlayerStatus status;

    public Player(int id, String name, String country, PlayerRole role, double basePrice) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.role = role;
        this.basePrice = basePrice;
        this.status = PlayerStatus.AVAILABLE; // All players start as available.
    }

    // Public getters provide read-only access to the attributes.
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public PlayerRole getRole() {
        return role;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    // This is the only way to change the player's status, ensuring controlled modification.
    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("ID: %-3d | Name: %-20s | Role: %-12s | Country: %-15s | Base Price: ₹%,.0f",
                id, name, role, country, basePrice);
    }
}
