package com.ipl.auction.dao;

import com.ipl.auction.db.ConnectionManager;
import com.ipl.auction.model.Player;
import com.ipl.auction.model.PlayerRole;
import com.ipl.auction.model.PlayerStatus;
import com.ipl.auction.model.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamDao {

    public List<Team> findAll() {
        String sql = "SELECT id, name, budget FROM teams ORDER BY id";
        List<Team> teams = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double budget = rs.getDouble("budget");
                teams.add(new Team(id, name, budget));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load teams from database", ex);
        }
        return teams;
    }

    /**
     * Updates the budget of a team in the database.
     * @param teamId The ID of the team to update
     * @param newBudget The new budget amount
     */
    public void updateTeamBudget(int teamId, double newBudget) {
        String sql = "UPDATE teams SET budget = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBudget);
            ps.setInt(2, teamId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Failed to update team budget - team not found");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update team budget in database", ex);
        }
    }

    /**
     * Adds a player to a team's roster in the database.
     * @param teamId The ID of the team
     * @param playerId The ID of the player
     * @param purchasePrice The price the player was purchased for
     */
    public void addPlayerToTeam(int teamId, int playerId, double purchasePrice) {
        String sql = "INSERT INTO team_players (team_id, player_id, purchase_price) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            ps.setInt(2, playerId);
            ps.setDouble(3, purchasePrice);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to add player to team in database", ex);
        }
    }

    /**
     * Gets all players for a specific team.
     * @param teamId The ID of the team
     * @return List of players in the team
     */
    public List<Player> getTeamPlayers(int teamId) {
        String sql = "SELECT p.id, p.name, p.country, p.role, p.base_price, p.status " +
                     "FROM players p " +
                     "INNER JOIN team_players tp ON p.id = tp.player_id " +
                     "WHERE tp.team_id = ?";
        List<Player> players = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String country = rs.getString("country");
                    String roleStr = rs.getString("role");
                    double basePrice = rs.getDouble("base_price");
                    String statusStr = rs.getString("status");

                    PlayerRole role = PlayerRole.valueOf(roleStr);
                    Player player = new Player(id, name, country, role, basePrice);
                    if (statusStr != null && !statusStr.isEmpty()) {
                        try {
                            player.setStatus(PlayerStatus.valueOf(statusStr));
                        } catch (IllegalArgumentException ignored) {
                            // default AVAILABLE if unknown
                        }
                    }
                    players.add(player);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load team players from database", ex);
        }
        return players;
    }
}


