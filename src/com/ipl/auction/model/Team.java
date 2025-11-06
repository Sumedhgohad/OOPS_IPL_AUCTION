package com.ipl.auction.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cricket team.
 * Demonstrates ENCAPSULATION by managing its own list of players and budget.
 * External classes cannot directly manipulate the budget or player list.
 */
public class Team {
    private final int id;
    private final String name;
    private double budget;
    private final List<Player> players;

    public Team(int id, String name, double budget) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.players = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBudget() {
        return budget;
    }

    public List<Player> getPlayers() {
        // Return a copy to prevent external modification of the original list.
        return new ArrayList<>(players);
    }

    /**
     * Attempts to add a player to the squad and deducts the price from the budget.
     * Contains validation logic to ensure the team has enough funds.
     * @param player The player to add.
     * @param purchasePrice The final price the player was sold for.
     * @return true if the purchase was successful, false otherwise.
     */
    public boolean addPlayer(Player player, double purchasePrice) {
        if (purchasePrice > this.budget) {
            System.out.println("Error: " + this.name + " does not have sufficient funds.");
            return false;
        }
        this.players.add(player);
        this.budget -= purchasePrice;
        return true;
    }

    /**
     * Provides a formatted string of the team's current squad.
     */
    public void printSquadDetails() {
        System.out.println("\n--- Squad for " + name + " ---");
        if (players.isEmpty()) {
            System.out.println("No players bought yet.");
        } else {
            for (Player player : players) {
                System.out.println(player.getName() + " (" + player.getRole() + ")");
            }
        }
        System.out.println("------------------------------------");
    }

    @Override
    public String toString() {
        return String.format("ID: %-3d | Team: %-25s | Budget Remaining: ₹%,.0f",
                id, name, budget);
    }
}
