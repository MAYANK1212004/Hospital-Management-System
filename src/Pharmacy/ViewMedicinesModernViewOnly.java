/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Pharmacy;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewMedicinesModernViewOnly extends JFrame {

    private JTable table;
    private JTextField tfSearch;
    private JButton btnSearch, btnBack;
    private DefaultTableModel model;

    public ViewMedicinesModernViewOnly() {
        setTitle("Pharmacy Inventory - View Only");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(null);

        JLabel title = new JLabel("View Medicine Inventory", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(40, 55, 71));
        title.setBounds(0, 20, 950, 40);
        add(title);

        tfSearch = new JTextField("Search by medicine or brand...");
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tfSearch.setForeground(Color.GRAY);
        tfSearch.setBounds(60, 80, 260, 35);
        tfSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)));
        add(tfSearch);

        // Clear placeholder on focus
        tfSearch.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tfSearch.getText().equals("Search by medicine or brand...")) {
                    tfSearch.setText("");
                    tfSearch.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (tfSearch.getText().trim().isEmpty()) {
                    tfSearch.setText("Search by medicine or brand...");
                    tfSearch.setForeground(Color.GRAY);
                }
            }
        });

        btnSearch = createButton("Search", new Color(52, 152, 219));
        btnSearch.setBounds(330, 80, 100, 35);
        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            if (keyword.equals("Search by medicine or brand...")) keyword = "";
            loadMedicines(keyword);
        });
        add(btnSearch);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Brand", "Quantity", "Price", "Expiry Date"}, 0);
        table = new JTable(model);
        customizeTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(60, 130, 820, 330);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        add(scroll);

        btnBack = createButton("Back", new Color(149, 165, 166));
        btnBack.setBounds(400, 480, 120, 40);
        btnBack.addActionListener(e -> {
            dispose();
            new PharmacyDashboardModern().setVisible(true); // back to dashboard
        });
        add(btnBack);

        loadMedicines("");
        setVisible(true);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void customizeTable() {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(224, 242, 255));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 40));
    }

    private void loadMedicines(String keyword) {
        model.setRowCount(0);
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
             PreparedStatement pst = con.prepareStatement(
                 "SELECT * FROM medicines WHERE name LIKE ? OR brand LIKE ?")) {
            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("medicine_id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getDate("expiry_date")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewMedicinesModernViewOnly::new);
    }
}

