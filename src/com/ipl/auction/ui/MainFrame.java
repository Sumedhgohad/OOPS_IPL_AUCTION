package com.ipl.auction.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private DashboardPage dashboardPage;
    private PlayersPage playersPage;
    private TeamsPage teamsPage;
    private AuctionPage auctionPage;
    
    public MainFrame() {
        super("IPL Auction Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        
        // Initialize pages
        dashboardPage = new DashboardPage();
        playersPage = new PlayersPage();
        teamsPage = new TeamsPage();
        auctionPage = new AuctionPage();
        
        // Setup CardLayout for page switching
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(dashboardPage, "DASHBOARD");
        cardPanel.add(playersPage, "PLAYERS");
        cardPanel.add(teamsPage, "TEAMS");
        cardPanel.add(auctionPage, "AUCTION");
        
        // Create navigation sidebar
        JPanel navPanel = createNavigationPanel();
        
        // Main layout
        setLayout(new BorderLayout());
        add(navPanel, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
        
        // Show dashboard by default
        cardLayout.show(cardPanel, "DASHBOARD");
    }
    
    private JPanel createNavigationPanel() {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        nav.setBackground(new Color(45, 45, 45));
        nav.setPreferredSize(new Dimension(200, 0));
        
        JLabel title = new JLabel("IPL AUCTION");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        nav.add(title);
        nav.add(Box.createVerticalStrut(30));
        
        nav.add(createNavButton("🏠 Dashboard", "DASHBOARD"));
        nav.add(Box.createVerticalStrut(10));
        nav.add(createNavButton("👥 Players", "PLAYERS"));
        nav.add(Box.createVerticalStrut(10));
        nav.add(createNavButton("🏏 Teams", "TEAMS"));
        nav.add(Box.createVerticalStrut(10));
        nav.add(createNavButton("🔨 Auction", "AUCTION"));
        
        nav.add(Box.createVerticalGlue());
        
        return nav;
    }
    
    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        
        btn.addActionListener(e -> {
            cardLayout.show(cardPanel, cardName);
            // Refresh page data when switching
            if (cardName.equals("DASHBOARD")) {
                dashboardPage.refresh();
            } else if (cardName.equals("PLAYERS")) {
                playersPage.refresh();
            } else if (cardName.equals("TEAMS")) {
                teamsPage.refresh();
            } else if (cardName.equals("AUCTION")) {
                auctionPage.refresh();
            }
        });
        
        return btn;
    }
    
    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
}
