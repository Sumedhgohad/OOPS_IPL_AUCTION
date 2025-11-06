package com.ipl.auction.ui;

import com.ipl.auction.dao.PlayerDao;
import com.ipl.auction.dao.TeamDao;
import com.ipl.auction.model.Player;
import com.ipl.auction.model.PlayerStatus;
import com.ipl.auction.model.Team;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardPage extends JPanel {
    
    private PlayerDao playerDao = new PlayerDao();
    private TeamDao teamDao = new TeamDao();
    
    private JLabel totalPlayersLabel;
    private JLabel availablePlayersLabel;
    private JLabel soldPlayersLabel;
    private JLabel totalTeamsLabel;
    private JLabel totalBudgetLabel;
    
    public DashboardPage() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        statsPanel.setBackground(new Color(245, 245, 250));
        
        statsPanel.add(createStatCard("Total Players", totalPlayersLabel = new JLabel("0")));
        statsPanel.add(createStatCard("Available Players", availablePlayersLabel = new JLabel("0")));
        statsPanel.add(createStatCard("Sold Players", soldPlayersLabel = new JLabel("0")));
        statsPanel.add(createStatCard("Total Teams", totalTeamsLabel = new JLabel("0")));
        statsPanel.add(createStatCard("Total Budget", totalBudgetLabel = new JLabel("₹0")));
        statsPanel.add(createStatCard("Remaining Budget", new JLabel("Calculating...")));
        
        add(statsPanel, BorderLayout.CENTER);
        
        // Refresh button
        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("🔄 Refresh Data");
        refreshBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshBtn.addActionListener(e -> refresh());
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);
        
        refresh();
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(new Color(30, 144, 255));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    public void refresh() {
        try {
            List<Player> players = playerDao.findAll();
            List<Team> teams = teamDao.findAll();
            
            int total = players.size();
            int available = 0;
            int sold = 0;
            double totalBudget = 0;
            double remainingBudget = 0;
            
            for (Player p : players) {
                if (p.getStatus() == PlayerStatus.AVAILABLE) {
                    available++;
                } else if (p.getStatus() == PlayerStatus.SOLD) {
                    sold++;
                }
            }
            
            for (Team t : teams) {
                totalBudget += 90000000; // Assuming initial budget
                remainingBudget += t.getBudget();
            }
            
            totalPlayersLabel.setText(String.valueOf(total));
            availablePlayersLabel.setText(String.valueOf(available));
            soldPlayersLabel.setText(String.valueOf(sold));
            totalTeamsLabel.setText(String.valueOf(teams.size()));
            totalBudgetLabel.setText("₹" + String.format("%,.0f", totalBudget));
            
            // Update remaining budget
            Component[] components = ((JPanel) getComponent(1)).getComponents();
            if (components.length > 4) {
                JPanel lastCard = (JPanel) components[5];
                JLabel remainingLabel = (JLabel) ((BorderLayout) lastCard.getLayout()).getLayoutComponent(BorderLayout.CENTER);
                if (remainingLabel != null) {
                    remainingLabel.setText("₹" + String.format("%,.0f", remainingBudget));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

