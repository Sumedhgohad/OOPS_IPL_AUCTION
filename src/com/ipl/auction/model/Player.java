
package com.ipl.auction.model;

/**
 * Player class demonstrating:
 * 1. INHERITANCE - extends Person class
 * 2. MULTIPLE INHERITANCE - implements Valuable and Auctionable interfaces
 * 3. POLYMORPHISM - overrides methods from parent class and interfaces
 * 4. ENCAPSULATION - private fields with public getters/setters
 */
public class Player extends Person implements Valuable, Auctionable {
    
    private final PlayerRole role;
    private final double basePrice;
    private PlayerStatus status;
    private double marketValue;

    public Player(int id, String name, String country, PlayerRole role, double basePrice) {
        super(id, name, country); // Call to parent class constructor
        this.role = role;
        this.basePrice = basePrice;
        this.status = PlayerStatus.AVAILABLE;
        this.marketValue = basePrice;
    }

    // Implementing abstract method from Person class (POLYMORPHISM)
    @Override
    public String getRole() {
        return role.toString();
    }
    
    // Method overloading - same name, different parameters (POLYMORPHISM)
    public PlayerRole getRole(boolean asEnum) {
        return role;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    // Implementing Valuable interface methods (MULTIPLE INHERITANCE)
    @Override
    public double getMarketValue() {
        return marketValue;
    }

    @Override
    public void updateValue(double newValue) {
        this.marketValue = newValue;
    }

    // Implementing Auctionable interface methods (MULTIPLE INHERITANCE)
    @Override
    public PlayerStatus getAuctionStatus() {
        return status;
    }

    @Override
    public void setAuctionStatus(PlayerStatus status) {
        this.status = status;
    }

    // Method overriding - overrides displayInfo() from Person class (POLYMORPHISM)
    @Override
    public void displayInfo() {
        System.out.println("Player: " + name + " (" + role + ") from " + country + 
                          " - Base Price: ₹" + basePrice);
    }

    @Override
    public String toString() {
        return String.format("ID: %-3d | Name: %-20s | Role: %-12s | Country: %-15s | Base Price: ₹%,.0f",
                id, name, role, country, basePrice);
    }
}
