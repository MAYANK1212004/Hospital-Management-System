/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pharmacy;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class InventoryManagement extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfSearch;
    private JButton btnAdd, btnEdit, btnDelete, btnBack, btnRefresh;

    public InventoryManagement() {
        setTitle("Pharmacy Inventory Management");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Pharmacy Inventory Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBounds(0, 20, 950, 40);
        title.setForeground(new Color(40, 55, 71));
        add(title);

        tfSearch = new JTextField();
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tfSearch.setBounds(50, 80, 300, 35);
        add(tfSearch);

        btnRefresh = createButton("Search", new Color(52, 152, 219));
        btnRefresh.setBounds(370, 80, 100, 35);
        btnRefresh.addActionListener(e -> loadMedicines(tfSearch.getText().trim()));
        add(btnRefresh);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Brand", "Qty", "Price", "Expiry"}, 0);
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(50, 130, 840, 300);
        add(scroll);

        btnAdd = createButton("Add", new Color(39, 174, 96));
        btnAdd.setBounds(100, 460, 100, 40);
        btnAdd.addActionListener(e -> new AddMedicineModern().setVisible(true));
        add(btnAdd);

        btnEdit = createButton("Edit", new Color(241, 196, 15));
        btnEdit.setBounds(220, 460, 100, 40);
        btnEdit.addActionListener(e -> editSelected());
        add(btnEdit);

        btnDelete = createButton("Delete", new Color(231, 76, 60));
        btnDelete.setBounds(340, 460, 100, 40);
        btnDelete.addActionListener(e -> deleteSelected());
        add(btnDelete);

        btnBack = createButton("Back", new Color(149, 165, 166));
        btnBack.setBounds(460, 460, 100, 40);
        btnBack.addActionListener(e -> {
            dispose();
            new PharmacyDashboardModern().setVisible(true);
        });
        add(btnBack);

        loadMedicines("");
        setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        return button;
    }

    private void loadMedicines(String keyword) {
        model.setRowCount(0);
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
             PreparedStatement pst = con.prepareStatement(
                     "SELECT * FROM medicines WHERE name LIKE ? ORDER BY expiry_date ASC")) {
            pst.setString(1, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("medicine_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("brand"));
                row.add(rs.getInt("quantity"));
                row.add(rs.getDouble("price"));
                row.add(rs.getDate("expiry_date"));
                model.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data.");
            e.printStackTrace();
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine to edit.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String name = (String) model.getValueAt(row, 1);
        String brand = (String) model.getValueAt(row, 2);
        int quantity = (int) model.getValueAt(row, 3);
        double price = (double) model.getValueAt(row, 4);
        String expiry = model.getValueAt(row, 5).toString();

        String newQty = JOptionPane.showInputDialog(this, "New Quantity:", quantity);
        String newPrice = JOptionPane.showInputDialog(this, "New Price:", price);
        String newExpiry = JOptionPane.showInputDialog(this, "New Expiry Date (yyyy-mm-dd):", expiry);

        if (newQty == null || newPrice == null || newExpiry == null) return;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
             PreparedStatement pst = con.prepareStatement("UPDATE medicines SET quantity=?, price=?, expiry_date=? WHERE medicine_id=?")) {
            pst.setInt(1, Integer.parseInt(newQty));
            pst.setDouble(2, Double.parseDouble(newPrice));
            pst.setString(3, newExpiry);
            pst.setInt(4, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Updated successfully.");
            loadMedicines(tfSearch.getText().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine to delete.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this medicine?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
                 PreparedStatement pst = con.prepareStatement("DELETE FROM medicines WHERE medicine_id=?")) {
                pst.setInt(1, id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Deleted successfully.");
                loadMedicines(tfSearch.getText().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryManagement::new);
    }
}

