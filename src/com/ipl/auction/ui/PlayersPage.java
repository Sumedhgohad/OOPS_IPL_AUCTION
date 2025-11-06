package com.ipl.auction.ui;

import com.ipl.auction.dao.PlayerDao;
import com.ipl.auction.data.DataLoader;
import com.ipl.auction.model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.List;

public class PlayersPage extends JPanel {
    
    private PlayerDao playerDao = new PlayerDao();
    private JTable playerTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JComboBox<String> roleFilter;
    private JComboBox<String> statusFilter;
    
    public PlayersPage() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        header.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        JLabel title = new JLabel("Players Management");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);
        
        // Toolbar with filters
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        toolbar.setBackground(Color.WHITE);
        
        toolbar.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        toolbar.add(searchField);
        
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(new JLabel("Role:"));
        roleFilter = new JComboBox<>(new String[]{"All", "BATSMAN", "BOWLER", "ALL_ROUNDER", "WICKETKEEPER"});
        toolbar.add(roleFilter);
        
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(new JLabel("Status:"));
        statusFilter = new JComboBox<>(new String[]{"All", "AVAILABLE", "SOLD", "UNSOLD"});
        toolbar.add(statusFilter);
        
        toolbar.add(Box.createHorizontalStrut(20));
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> refresh());
        toolbar.add(refreshBtn);
        
        add(toolbar, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Name", "Role", "Country", "Base Price (₹)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        playerTable = new JTable(tableModel);
        playerTable.setRowHeight(30);
        playerTable.setFont(new Font("Arial", Font.PLAIN, 13));
        playerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        playerTable.getTableHeader().setBackground(new Color(30, 144, 255));
        playerTable.getTableHeader().setForeground(Color.WHITE);
        playerTable.setSelectionBackground(new Color(200, 220, 255));
        
        sorter = new TableRowSorter<>(tableModel);
        playerTable.setRowSorter(sorter);
        
        JScrollPane scrollPane = new JScrollPane(playerTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
        
        // Setup filters
        Runnable applyFilter = () -> {
            String searchText = searchField.getText().toLowerCase();
            String role = (String) roleFilter.getSelectedItem();
            String status = (String) statusFilter.getSelectedItem();
            
            RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    String name = String.valueOf(entry.getValue(1)).toLowerCase();
                    String roleVal = String.valueOf(entry.getValue(2));
                    String statusVal = String.valueOf(entry.getValue(5));
                    
                    boolean nameMatch = searchText.isEmpty() || name.contains(searchText);
                    boolean roleMatch = "All".equals(role) || roleVal.equals(role);
                    boolean statusMatch = "All".equals(status) || statusVal.equals(status);
                    
                    return nameMatch && roleMatch && statusMatch;
                }
            };
            sorter.setRowFilter(rf);
        };
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilter.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilter.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter.run(); }
        });
        
        roleFilter.addActionListener(e -> applyFilter.run());
        statusFilter.addActionListener(e -> applyFilter.run());
        
        refresh();
    }
    
    public void refresh() {
        try {
            List<Player> players = playerDao.findAll();
            tableModel.setRowCount(0);
            
            for (Player p : players) {
                tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getRole(),
                    p.getCountry(),
                    String.format("%,.0f", p.getBasePrice()),
                    p.getStatus()
                });
            }
        } catch (Exception e) {
            // Fallback to static data
            List<Player> players = DataLoader.loadPlayers();
            tableModel.setRowCount(0);
            for (Player p : players) {
                tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getRole(),
                    p.getCountry(),
                    String.format("%,.0f", p.getBasePrice()),
                    p.getStatus()
                });
            }
        }
    }
}

