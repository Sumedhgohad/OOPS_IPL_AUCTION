package com.ipl.auction.model;

/**
 * Base class demonstrating INHERITANCE.
 * This is the parent class that provides common attributes and methods
 * for all people involved in the IPL auction (Players)
 */
public abstract class Person {
    protected int id;
    protected String name;
    protected String country;
    
    public Person(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }
    
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCountry() {
        return country;
    }
    
    
    public abstract String getRole();
    
    
    public void displayInfo() {
        System.out.println("Name: " + name + ", Country: " + country);
    }
}
