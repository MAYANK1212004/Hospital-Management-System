/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pharmacy;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class AddMedicineModern extends JFrame {
    private JTextField tfName, tfBrand, tfQuantity, tfPrice;
    private JDateChooser expiryChooser;
    private JButton btnAdd, btnClear, btnBack;

    public AddMedicineModern() {
        setTitle("Add New Medicine");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Add Medicine to Pharmacy");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBounds(270, 30, 450, 40);
        title.setForeground(new Color(40, 55, 71));
        add(title);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        int xLabel = 220, xField = 400, y = 100, gap = 50;

        JLabel lblName = new JLabel("Medicine Name*:");
        lblName.setBounds(xLabel, y, 150, 30);
        lblName.setFont(labelFont);
        add(lblName);

        tfName = new JTextField();
        tfName.setBounds(xField, y, 250, 30);
        add(tfName);

        y += gap;
        JLabel lblBrand = new JLabel("Brand:");
        lblBrand.setBounds(xLabel, y, 150, 30);
        lblBrand.setFont(labelFont);
        add(lblBrand);

        tfBrand = new JTextField();
        tfBrand.setBounds(xField, y, 250, 30);
        add(tfBrand);

        y += gap;
        JLabel lblQuantity = new JLabel("Quantity*:");
        lblQuantity.setBounds(xLabel, y, 150, 30);
        lblQuantity.setFont(labelFont);
        add(lblQuantity);

        tfQuantity = new JTextField();
        tfQuantity.setBounds(xField, y, 250, 30);
        add(tfQuantity);

        y += gap;
        JLabel lblPrice = new JLabel("Price (₹)*:");
        lblPrice.setBounds(xLabel, y, 150, 30);
        lblPrice.setFont(labelFont);
        add(lblPrice);

        tfPrice = new JTextField();
        tfPrice.setBounds(xField, y, 250, 30);
        add(tfPrice);

        y += gap;
        JLabel lblExpiry = new JLabel("Expiry Date*:");
        lblExpiry.setBounds(xLabel, y, 150, 30);
        lblExpiry.setFont(labelFont);
        add(lblExpiry);

        expiryChooser = new JDateChooser();
        expiryChooser.setDateFormatString("yyyy-MM-dd");
        expiryChooser.setBounds(xField, y, 250, 30);
        add(expiryChooser);

        btnAdd = createButton("Add Medicine", new Color(39, 174, 96));
        btnAdd.setBounds(250, 420, 150, 40);
        add(btnAdd);

        btnClear = createButton("Clear", new Color(231, 76, 60));
        btnClear.setBounds(410, 420, 100, 40);
        add(btnClear);

        btnBack = createButton("Back", new Color(52, 152, 219));
        btnBack.setBounds(520, 420, 100, 40);
        add(btnBack);

        btnAdd.addActionListener(e -> addMedicine());
        btnClear.addActionListener(e -> clearForm());
        btnBack.addActionListener(e -> {
            dispose();
            new PharmacyDashboardModern().setVisible(true);
        });

        setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    private void addMedicine() {
        String name = tfName.getText().trim();
        String brand = tfBrand.getText().trim();
        String quantityStr = tfQuantity.getText().trim();
        String priceStr = tfPrice.getText().trim();
        java.util.Date expiryDate = expiryChooser.getDate();

        if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty() || expiryDate == null) {
            JOptionPane.showMessageDialog(this, "Please fill all required (*) fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            double price = Double.parseDouble(priceStr);
            String expiry = new SimpleDateFormat("yyyy-MM-dd").format(expiryDate);

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
            String sql = "INSERT INTO medicines(name, brand, quantity, price, expiry_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, brand);
            pst.setInt(3, quantity);
            pst.setDouble(4, price);
            pst.setString(5, expiry);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Medicine added successfully!");
                clearForm();
            }

            pst.close();
            con.close();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Quantity and Price must be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        tfName.setText("");
        tfBrand.setText("");
        tfQuantity.setText("");
        tfPrice.setText("");
        expiryChooser.setDate(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddMedicineModern::new);
    }
}

