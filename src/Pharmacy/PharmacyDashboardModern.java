/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pharmacy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PharmacyDashboardModern extends JFrame {

    public PharmacyDashboardModern() {
        setTitle("Pharmacy Dashboard - Hospital Management System");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top header
        JLabel header = new JLabel("Pharmacy Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setOpaque(true);
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(getWidth(), 70));
        add(header, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(52, 73, 94));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        JButton btnLogout = createLogoutButton("Logout");
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalGlue());
        add(sidebar, BorderLayout.WEST);

        // Center Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;

        String[] buttonLabels = {
            "View Medicines",
            "Add Medicine",
            "Inventory Management",
            "View Prescriptions",
            "Generate Bills",
            "Stock Alerts",
            "Expired Medicines"
        };

        JButton[] buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = createMainButton(buttonLabels[i]);
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            contentPanel.add(buttons[i], gbc);
        }

        // Actions (Replace JOptionPane with your actual classes/forms)
        buttons[0].addActionListener(e ->{
            dispose();
            new ViewMedicinesModernViewOnly().setVisible(true);
        });
        buttons[1].addActionListener(e -> {
            dispose();
            new AddMedicineModern().setVisible(true);
        });
        buttons[2].addActionListener(e -> {
            dispose();
            new InventoryManagement().setVisible(true);
        });
        buttons[3].addActionListener(e ->{
            dispose();
            new ViewPrescriptionsPharmacyPanel().setVisible(true);
        });
        buttons[4].addActionListener(e -> {
            dispose();
            new PharmacyBillingSystem().setVisible(true);
        });
        buttons[5].addActionListener(e -> {
            dispose();
            new StockAlertPanel().setVisible(true);
        });
        buttons[6].addActionListener(e -> {
            dispose();
            new ExpiredMedicinesModule().setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Do you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new homepage.HospitalHomePage().setVisible(true);
            }
        });

        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton createMainButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(250, 80));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 230, 240));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    private JButton createLogoutButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(192, 57, 43));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(231, 76, 60));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(192, 57, 43));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PharmacyDashboardModern::new);
    }
}
