/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class DoctorMainDashboard extends JFrame {

    public DoctorMainDashboard() {
        setTitle("👨‍⚕️ Doctor Management Dashboard");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 650));
        setLayout(new BorderLayout());

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(30, 41, 59));
        topBar.setPreferredSize(new Dimension(0, 60));
        topBar.setBorder(new MatteBorder(0, 0, 2, 0, new Color(80, 120, 210)));

        JLabel heading = new JLabel("Doctor Management Dashboard");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI Semibold", Font.BOLD, 26));
        heading.setBorder(new EmptyBorder(0, 25, 0, 0));
        topBar.add(heading, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        // Main Container
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 40, 30, 40);

        JPanel card1 = createCard("Doctor Registration", "\u270D", e -> {
            dispose();
            new InsertDoctorApp();
        });

        JPanel card2 = createCard("Doctor Management", "\u2699", e -> {
            dispose();
            new UpdateDeleteDoctorApp();
        });

        JPanel card3 = createCard("View Doctors", "\uD83D\uDC65", e -> {
            dispose();
            JFrame frame = new JFrame("Doctor Directory");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(960, 600);
            frame.setLocationRelativeTo(null);
            frame.add(new DoctorDisplayModern());
            frame.setVisible(true);
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(card1, gbc);

        gbc.gridx = 1;
        mainPanel.add(card2, gbc);

        gbc.gridx = 2;
        mainPanel.add(card3, gbc);

        // Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 247, 250));
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.setBackground(new Color(220, 53, 69));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setPreferredSize(new Dimension(130, 45));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> {
        
            DoctorMainDashboard.this.dispose();
            new AdminDashboardModernUI().setVisible(true);

        });

        setVisible(true);
    }

    private JPanel createCard(String title, String iconUnicode, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(280, 180));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Icon
        JLabel iconLabel = new JLabel(iconUnicode, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        iconLabel.setForeground(new Color(0, 120, 215));
        card.add(iconLabel, BorderLayout.CENTER);

        // Title
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(25, 50, 100));
        titleLabel.setBorder(new EmptyBorder(15, 0, 0, 0));
        card.add(titleLabel, BorderLayout.SOUTH);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(230, 245, 255));
                card.setBorder(new CompoundBorder(
                        new LineBorder(new Color(0, 120, 215), 2, true),
                        new EmptyBorder(20, 20, 20, 20)
                ));
            }

            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(new CompoundBorder(
                        new LineBorder(new Color(180, 180, 180), 1, true),
                        new EmptyBorder(20, 20, 20, 20)
                ));
            }

            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(null);
            }
        });

        return card;
    }

    public static void main(String[] args) {
        try {
          //  UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(DoctorMainDashboard::new);
    }
}
