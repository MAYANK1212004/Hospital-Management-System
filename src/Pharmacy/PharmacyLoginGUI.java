/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pharmacy;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

public class PharmacyLoginGUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, forgotButton;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private String generatedOTP = null;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public PharmacyLoginGUI() {
        setTitle("Pharmacy Secure Portal");
        setSize(500, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(createLoginPanel(), "login");

        add(cardPanel);
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(248, 250, 252));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel heading = new JLabel("\uD83C\uDF10 Pharmacy Login Portal");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(new Color(33, 53, 85));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        emailField = new JTextField(22);
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));
        passwordField = new JPasswordField(22);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        loginButton = new JButton("Login");
        forgotButton = new JButton("Forgot Password?");

        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        forgotButton.setForeground(new Color(120, 120, 120));
        loginButton.setFocusPainted(false);
        forgotButton.setFocusPainted(false);

        loginButton.addActionListener(e -> login());
        forgotButton.addActionListener(e -> forgotPassword());

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(heading, gbc);

        gbc.gridy++;
        panel.add(emailField, gbc);

        gbc.gridy++;
        panel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(loginButton, gbc);

        gbc.gridx = 1;
        panel.add(forgotButton, gbc);

        return panel;
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email and password.");
            return;
        }

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement ps = con.prepareStatement("SELECT password FROM pharmacy_users WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getString(1).equals(password)) {
                generatedOTP = generateOTP();
                sendOTPEmail(email, generatedOTP, "Login OTP");

                String input = JOptionPane.showInputDialog(this, "Enter the OTP sent to your email:");
                if (input != null && input.equals(generatedOTP)) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    dispose();
                     new PharmacyDashboardModern().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid OTP.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void forgotPassword() {
        String email = JOptionPane.showInputDialog(this, "Enter your registered email:");
        if (email == null || email.trim().isEmpty()) return;

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM pharmacy_users WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                generatedOTP = generateOTP();
                sendOTPEmail(email, generatedOTP, "Password Reset OTP");

                String input = JOptionPane.showInputDialog(this, "Enter the OTP sent to your email:");
                if (input != null && input.equals(generatedOTP)) {
                    String newPass = JOptionPane.showInputDialog(this, "Enter new password:");
                    if (newPass != null && !newPass.isEmpty()) {
                        PreparedStatement update = con.prepareStatement("UPDATE pharmacy_users SET password = ? WHERE email = ?");
                        update.setString(1, newPass);
                        update.setString(2, email);
                        update.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Password updated successfully!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect OTP.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Email not registered.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private String generateOTP() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    private void sendOTPEmail(String to, String content, String subject) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("aroramayank488@gmail.com", "zojw kkfv hdyl etih");
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("aroramayank488@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText("Your OTP is: " + content);

            Transport.send(message);
        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(this, "Failed to send OTP: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        SwingUtilities.invokeLater(PharmacyLoginGUI::new);
    }
}