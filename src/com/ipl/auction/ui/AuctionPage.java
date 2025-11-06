package com.ipl.auction.ui;

import com.ipl.auction.dao.PlayerDao;
import com.ipl.auction.dao.TeamDao;
import com.ipl.auction.data.DataLoader;
import com.ipl.auction.model.Player;
import com.ipl.auction.model.PlayerStatus;
import com.ipl.auction.model.Team;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AuctionPage extends JPanel {
    
    private PlayerDao playerDao = new PlayerDao();
    private TeamDao teamDao = new TeamDao();
    
    private JComboBox<Player> playerCombo;
    private JComboBox<Team> teamCombo;
    private JTextField bidField;
    private JTextArea logArea;
    private JLabel currentPlayerLabel;
    private JLabel basePriceLabel;
    
    public AuctionPage() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        header.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        JLabel title = new JLabel("Auction Room");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);
        
        // Main content
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left: Auction controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Place Bid"));
        controlPanel.setPreferredSize(new Dimension(350, 0));
        
        controlPanel.add(Box.createVerticalStrut(10));
        
        JLabel playerLabel = new JLabel("Select Player:");
        playerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(playerLabel);
        controlPanel.add(Box.createVerticalStrut(5));
        
        playerCombo = new JComboBox<>();
        playerCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        playerCombo.addActionListener(e -> updatePlayerInfo());
        controlPanel.add(playerCombo);
        controlPanel.add(Box.createVerticalStrut(15));
        
        currentPlayerLabel = new JLabel("Player: -");
        currentPlayerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        controlPanel.add(currentPlayerLabel);
        
        basePriceLabel = new JLabel("Base Price: -");
        basePriceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        controlPanel.add(basePriceLabel);
        
        controlPanel.add(Box.createVerticalStrut(20));
        
        JLabel teamLabel = new JLabel("Select Team:");
        teamLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(teamLabel);
        controlPanel.add(Box.createVerticalStrut(5));
        
        teamCombo = new JComboBox<>();
        teamCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        controlPanel.add(teamCombo);
        
        controlPanel.add(Box.createVerticalStrut(20));
        
        JLabel bidLabel = new JLabel("Bid Amount (₹):");
        bidLabel.setFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.add(bidLabel);
        controlPanel.add(Box.createVerticalStrut(5));
        
        bidField = new JTextField();
        bidField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        controlPanel.add(bidField);
        
        controlPanel.add(Box.createVerticalStrut(20));
        
        JButton placeBidBtn = new JButton("Place Bid");
        placeBidBtn.setFont(new Font("Arial", Font.BOLD, 14));
        placeBidBtn.setBackground(new Color(30, 144, 255));
        placeBidBtn.setForeground(Color.WHITE);
        placeBidBtn.addActionListener(e -> placeBid());
        controlPanel.add(placeBidBtn);
        
        controlPanel.add(Box.createVerticalStrut(10));
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> refresh());
        controlPanel.add(refreshBtn);
        
        // Right: Auction log
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Auction Log"));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        logArea.setBackground(new Color(250, 250, 250));
        JScrollPane logScroll = new JScrollPane(logArea);
        logPanel.add(logScroll, BorderLayout.CENTER);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, controlPanel, logPanel);
        splitPane.setDividerLocation(400);
        content.add(splitPane, BorderLayout.CENTER);
        
        add(content, BorderLayout.CENTER);
        
        refresh();
    }
    
    private void updatePlayerInfo() {
        Player selected = (Player) playerCombo.getSelectedItem();
        if (selected != null) {
            currentPlayerLabel.setText("Player: " + selected.getName() + " (" + selected.getRole() + ")");
            basePriceLabel.setText("Base Price: ₹" + String.format("%,.0f", selected.getBasePrice()));
        }
    }
    
    private void placeBid() {
        Player player = (Player) playerCombo.getSelectedItem();
        Team team = (Team) teamCombo.getSelectedItem();
        
        if (player == null || team == null) {
            JOptionPane.showMessageDialog(this, "Please select both player and team!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double bidAmount = Double.parseDouble(bidField.getText());
            
            if (bidAmount < player.getBasePrice()) {
                JOptionPane.showMessageDialog(this, 
                    "Bid must be at least the base price: ₹" + String.format("%,.0f", player.getBasePrice()),
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (bidAmount > team.getBudget()) {
                JOptionPane.showMessageDialog(this, 
                    team.getName() + " doesn't have enough budget!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (player.getStatus() != PlayerStatus.AVAILABLE) {
                JOptionPane.showMessageDialog(this, 
                    "This player is not available for auction!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Process bid
            if (team.addPlayer(player, bidAmount)) {
                player.setStatus(PlayerStatus.SOLD);
                logArea.append(String.format("[%s] %s sold to %s for ₹%,.0f\n",
                    new java.util.Date().toString().substring(11, 19),
                    player.getName(), team.getName(), bidAmount));
                logArea.setCaretPosition(logArea.getDocument().getLength());
                
                JOptionPane.showMessageDialog(this, 
                    "Bid successful! " + player.getName() + " sold to " + team.getName(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                bidField.setText("");
                refresh();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid bid amount!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refresh() {
        try {
            List<Player> players = playerDao.findAll();
            List<Team> teams = teamDao.findAll();
            
            DefaultComboBoxModel<Player> playerModel = new DefaultComboBoxModel<>();
            for (Player p : players) {
                if (p.getStatus() == PlayerStatus.AVAILABLE) {
                    playerModel.addElement(p);
                }
            }
            playerCombo.setModel(playerModel);
            
            DefaultComboBoxModel<Team> teamModel = new DefaultComboBoxModel<>();
            for (Team t : teams) {
                teamModel.addElement(t);
            }
            teamCombo.setModel(teamModel);
            
            if (playerCombo.getItemCount() > 0) {
                playerCombo.setSelectedIndex(0);
                updatePlayerInfo();
            }
        } catch (Exception e) {
            // Fallback
            List<Player> players = DataLoader.loadPlayers();
            List<Team> teams = DataLoader.loadTeams();
            
            DefaultComboBoxModel<Player> playerModel = new DefaultComboBoxModel<>();
            for (Player p : players) {
                if (p.getStatus() == PlayerStatus.AVAILABLE) {
                    playerModel.addElement(p);
                }
            }
            playerCombo.setModel(playerModel);
            
            DefaultComboBoxModel<Team> teamModel = new DefaultComboBoxModel<>();
            for (Team t : teams) {
                teamModel.addElement(t);
            }
            teamCombo.setModel(teamModel);
        }
    }
}

