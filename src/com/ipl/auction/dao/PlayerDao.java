package com.ipl.auction.dao;

import com.ipl.auction.db.ConnectionManager;
import com.ipl.auction.model.Player;
import com.ipl.auction.model.PlayerRole;
import com.ipl.auction.model.PlayerStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDao {

    public List<Player> findAll() {
        String sql = "SELECT id, name, country, role, base_price, status FROM players ORDER BY id";
        List<Player> players = new ArrayList<>();
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to load players from database", ex);
        }
        return players;
    }

    /**
     * Updates the status of a player in the database.
     * @param playerId The ID of the player to update
     * @param status The new status
     */
    public void updatePlayerStatus(int playerId, PlayerStatus status) {
        String sql = "UPDATE players SET status = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, playerId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Failed to update player status - player not found");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to update player status in database", ex);
        }
    }
}


