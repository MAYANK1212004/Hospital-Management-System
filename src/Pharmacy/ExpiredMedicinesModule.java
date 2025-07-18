/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pharmacy;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class ExpiredMedicinesModule extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnRefresh, btnBack;
    private Connection con;

    public ExpiredMedicinesModule() {
        setTitle("Expired Medicines");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // Title label
        JLabel titleLabel = new JLabel("List of Expired Medicines", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 47, 61));
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table setup
        model = new DefaultTableModel(new String[]{"Medicine ID", "Name", "Quantity", "Price", "Expiry Date"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // Non-editable
            }
        };
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(44, 62, 80));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel at bottom
        JPanel btnPanel = new JPanel(new BorderLayout());
        btnPanel.setBackground(Color.WHITE);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.WHITE);
        btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnBack.setBackground(new Color(231, 76, 60));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        leftPanel.add(btnBack);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRefresh.setBackground(new Color(40, 167, 69));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightPanel.add(btnRefresh);

        btnPanel.add(leftPanel, BorderLayout.WEST);
        btnPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);

        connect();
        loadExpiredMedicines();

        btnRefresh.addActionListener(e -> loadExpiredMedicines());
        btnBack.addActionListener(e -> {
            // For now, just close the window
            dispose();
            new PharmacyDashboardModern().setVisible(true);
        });

        setVisible(true);
    }

    private void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "DB Connection Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadExpiredMedicines() {
        if (con == null) return;

        model.setRowCount(0); // Clear existing data
        String sql = "SELECT medicine_id, name, quantity, price, expiry_date FROM medicines WHERE expiry_date < CURDATE() ORDER BY expiry_date ASC";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("medicine_id"));
                row.add(rs.getString("name"));
                row.add(rs.getInt("quantity"));
                row.add(rs.getDouble("price"));

                Date expiry = rs.getDate("expiry_date");
                row.add(expiry != null ? sdf.format(expiry) : "N/A");

                model.addRow(row);
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No expired medicines found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching expired medicines: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpiredMedicinesModule::new);
    }
}
