package com.ipl.auction.data;

import com.ipl.auction.dao.PlayerDao;
import com.ipl.auction.dao.TeamDao;
import com.ipl.auction.model.Player;
import com.ipl.auction.model.PlayerRole;
import com.ipl.auction.model.Team;


import java.util.ArrayList;
import java.util.List;


public class DataLoader {

    public static List<Player> loadPlayers() {
        try {
            // Try DB first
            PlayerDao playerDao = new PlayerDao();
            List<Player> dbPlayers = playerDao.findAll();
            if (!dbPlayers.isEmpty()) {
                return dbPlayers;
            }
        } catch (RuntimeException ex) {
            // Fallback to static data if DB unavailable
        }
        List<Player> fallback = new ArrayList<>();
        fallback.add(new Player(1, "Virat Kohli", "India", PlayerRole.BATSMAN, 20000000));
        fallback.add(new Player(2, "Rohit Sharma", "India", PlayerRole.BATSMAN, 18000000));
        fallback.add(new Player(3, "Jasprit Bumrah", "India", PlayerRole.BOWLER, 15000000));
        fallback.add(new Player(4, "Hardik Pandya", "India", PlayerRole.ALL_ROUNDER, 16000000));
        fallback.add(new Player(5, "MS Dhoni", "India", PlayerRole.WICKETKEEPER, 10000000));
        fallback.add(new Player(6, "Pat Cummins", "Australia", PlayerRole.BOWLER, 12000000));
        fallback.add(new Player(7, "Rashid Khan", "Afghanistan", PlayerRole.BOWLER, 14000000));
        fallback.add(new Player(8, "Ben Stokes", "England", PlayerRole.ALL_ROUNDER, 13000000));
        return fallback;
    }

    public static List<Team> loadTeams() {
        try {
            TeamDao teamDao = new TeamDao();
            List<Team> dbTeams = teamDao.findAll();
            if (!dbTeams.isEmpty()) {
                return dbTeams;
            }
        } catch (RuntimeException ex) {
            // Fallback to static data
        }
        List<Team> fallback = new ArrayList<>();
        fallback.add(new Team(1, "Mumbai Indians", 90000000));
        fallback.add(new Team(2, "Chennai Super Kings", 85000000));
        fallback.add(new Team(3, "Royal Challengers Bangalore", 87000000));
        return fallback;
    }
}
