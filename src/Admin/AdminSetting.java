/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminSetting extends JFrame {

    private JLabel lblName, lblEmail;
    private JPasswordField currentPassField, newPassField;
    private JButton updatePassBtn, logoutBtn, backBtn;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public AdminSetting() {
        setTitle("Admin Settings");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String loggedInEmail = SessionManager.getLoggedInEmail();

        JLabel heading = new JLabel("Admin Settings");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(heading, gbc);

        // Fetch admin info
        String name = "Unknown";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT name FROM admin_users WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, loggedInEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Email
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        lblEmail = new JLabel(loggedInEmail);
        gbc.gridx = 1;
        panel.add(lblEmail, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Name:"), gbc);
        lblName = new JLabel(name);
        gbc.gridx = 1;
        panel.add(lblName, gbc);

        // Current Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Current Password:"), gbc);
        currentPassField = new JPasswordField();
        gbc.gridx = 1;
        panel.add(currentPassField, gbc);

        // New Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("New Password:"), gbc);
        newPassField = new JPasswordField();
        gbc.gridx = 1;
        panel.add(newPassField, gbc);

        // Update button
        updatePassBtn = new JButton("Update Password");
        updatePassBtn.setBackground(new Color(0, 153, 76));
        updatePassBtn.setForeground(Color.WHITE);
        updatePassBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(updatePassBtn, gbc);

        // Logout button
        logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.RED);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 6;
        panel.add(logoutBtn, gbc);

        // Back button
        backBtn = new JButton("Back");
        backBtn.setBackground(Color.GRAY);
        backBtn.setForeground(Color.WHITE);
        gbc.gridy = 7;
        panel.add(backBtn, gbc);

        // Events
        updatePassBtn.addActionListener(e -> updatePassword());
        logoutBtn.addActionListener(e -> {
            SessionManager.clearSession();
            dispose();
            new AdminLogin().setVisible(true);
        });
        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboardModernUI().setVisible(true);
        });

        setVisible(true);
    }

    private void updatePassword() {
        String currentPass = new String(currentPassField.getPassword()).trim();
        String newPass = new String(newPassField.getPassword()).trim();

        if (currentPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String email = SessionManager.getLoggedInEmail();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT password_hash FROM admin_users WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String dbHash = rs.getString("password_hash");
                if (!dbHash.equals(sha256(currentPass))) {
                    JOptionPane.showMessageDialog(this, "Incorrect current password!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Update password
            String update = "UPDATE admin_users SET password_hash = ? WHERE email = ?";
            PreparedStatement updatePs = conn.prepareStatement(update);
            updatePs.setString(1, sha256(newPass));
            updatePs.setString(2, email);
            updatePs.executeUpdate();

            JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            currentPassField.setText("");
            newPassField.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String sha256(String base) throws Exception {
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(base.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash)
            hexString.append(String.format("%02x", b));
        return hexString.toString();
    }

    public static void main(String[] args) {
        new AdminSetting();
    }
}
