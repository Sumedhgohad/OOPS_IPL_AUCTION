package com.ipl.auction.ui;

import com.ipl.auction.dao.TeamDao;
import com.ipl.auction.data.DataLoader;
import com.ipl.auction.model.Player;
import com.ipl.auction.model.Team;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeamsPage extends JPanel {
    
    private TeamDao teamDao = new TeamDao();
    private JList<Team> teamList;
    private JTable squadTable;
    private DefaultTableModel squadModel;
    private JLabel budgetLabel;
    private JLabel squadSizeLabel;
    
    public TeamsPage() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        header.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        JLabel title = new JLabel("Teams & Squads");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);
        
        // Split pane: teams list on left, squad details on right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left: Teams list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Select Team"));
        
        teamList = new JList<>();
        teamList.setCellRenderer(new TeamListRenderer());
        teamList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Team selected = teamList.getSelectedValue();
                if (selected != null) {
                    showTeamDetails(selected);
                }
            }
        });
        
        JScrollPane listScroll = new JScrollPane(teamList);
        leftPanel.add(listScroll, BorderLayout.CENTER);
        
        // Right: Squad details
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Squad Details"));
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        budgetLabel = new JLabel("Budget: ₹0");
        squadSizeLabel = new JLabel("Squad Size: 0");
        budgetLabel.setFont(new Font("Arial", Font.BOLD, 14));
        squadSizeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(budgetLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(squadSizeLabel);
        rightPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Squad table
        String[] columns = {"ID", "Name", "Role", "Country", "Base Price (₹)"};
        squadModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        squadTable = new JTable(squadModel);
        squadTable.setRowHeight(25);
        squadTable.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane squadScroll = new JScrollPane(squadTable);
        rightPanel.add(squadScroll, BorderLayout.CENTER);
        
        // Refresh button
        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> refresh());
        buttonPanel.add(refreshBtn);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        refresh();
    }
    
    private void showTeamDetails(Team team) {
        // Load players from database
        List<Player> squad = teamDao.getTeamPlayers(team.getId());
        squadModel.setRowCount(0);
        
        for (Player p : squad) {
            squadModel.addRow(new Object[]{
                p.getId(),
                p.getName(),
                p.getRole(),
                p.getCountry(),
                String.format("%,.0f", p.getBasePrice())
            });
        }
        
        budgetLabel.setText("Remaining Budget: ₹" + String.format("%,.0f", team.getBudget()));
        squadSizeLabel.setText("Squad Size: " + squad.size());
    }
    
    public void refresh() {
        try {
            List<Team> teams = teamDao.findAll();
            DefaultListModel<Team> model = new DefaultListModel<>();
            for (Team t : teams) {
                model.addElement(t);
            }
            teamList.setModel(model);
        } catch (Exception e) {
            // Fallback
            List<Team> teams = DataLoader.loadTeams();
            DefaultListModel<Team> model = new DefaultListModel<>();
            for (Team t : teams) {
                model.addElement(t);
            }
            teamList.setModel(model);
        }
    }
    
    private static class TeamListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Team) {
                Team team = (Team) value;
                setText("<html><b>" + team.getName() + "</b><br/>" +
                       "Budget: ₹" + String.format("%,.0f", team.getBudget()) + "</html>");
            }
            return this;
        }
    }
}

