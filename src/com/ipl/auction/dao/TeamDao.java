package com.ipl.auction.dao;

import com.ipl.auction.db.ConnectionManager;
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
}


