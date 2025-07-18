/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import javax.swing.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class AdminLogin extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "aroramayank488@gmail.com";
    private static final String EMAIL_PASSWORD = "zojw kkfv hdyl etih"; // Use App Password

    private JTextField emailField, otpField;
    private JPasswordField passwordField;
    private JButton loginBtn, verifyOtpBtn, forgotPassBtn, backBtn;
    private JLabel lblOtp;

    private String generatedOtp;
    private String currentUserEmail;
    private boolean otpSent = false;

    public AdminLogin() {
        setTitle("Admin Login - Hospital Management System");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(230, 245, 255));
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        panel.add(new JLabel("Email:"), getConstraints(gbc, 0, 1));
        emailField = new JTextField(20);
        panel.add(emailField, getConstraints(gbc, 1, 1));

        panel.add(new JLabel("Password:"), getConstraints(gbc, 0, 2));
        passwordField = new JPasswordField(20);
        panel.add(passwordField, getConstraints(gbc, 1, 2));

        loginBtn = new JButton("Login");
        styleButton(loginBtn, new Color(0, 123, 255));
        panel.add(loginBtn, getConstraints(gbc, 0, 3));

        forgotPassBtn = new JButton("Forgot Password?");
        forgotPassBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        forgotPassBtn.setForeground(new Color(0, 102, 204));
        forgotPassBtn.setContentAreaFilled(false);
        forgotPassBtn.setBorderPainted(false);
        forgotPassBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(forgotPassBtn, getConstraints(gbc, 1, 3));

        lblOtp = new JLabel("Enter OTP:");
        lblOtp.setVisible(false);
        panel.add(lblOtp, getConstraints(gbc, 0, 4));

        otpField = new JTextField(20);
        otpField.setVisible(false);
        panel.add(otpField, getConstraints(gbc, 1, 4));

        verifyOtpBtn = new JButton("Verify OTP");
        styleButton(verifyOtpBtn, new Color(40, 167, 69));
        verifyOtpBtn.setVisible(false);
        gbc.gridwidth = 2;
        panel.add(verifyOtpBtn, getConstraints(gbc, 0, 5));

        backBtn = new JButton("← Back");
        styleButton(backBtn, new Color(108, 117, 125));
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(backBtn, gbc);

        loginBtn.addActionListener(e -> handleLogin());
        verifyOtpBtn.addActionListener(e -> verifyOtp());
        forgotPassBtn.addActionListener(e -> forgotPassword());
        backBtn.addActionListener(e -> {
            dispose();
            new homepage.HospitalHomePage().setVisible(true);
        });

        setVisible(true);
    }

    private GridBagConstraints getConstraints(GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        return gbc;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void handleLogin() {
        if (otpSent) {
            JOptionPane.showMessageDialog(this, "Please verify the OTP sent to your email.");
            return;
        }

        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter both email and password.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM admin_users WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Admin not found.");
                return;
            }

            int failedAttempts = rs.getInt("failed_attempts");
            Timestamp lockTime = rs.getTimestamp("lock_time");

            if (lockTime != null && new Date().getTime() - lockTime.getTime() < 3600000) {
                JOptionPane.showMessageDialog(this, "Account is locked. Try after 1 hour.");
                sendEmail(email, "Account Locked", "Your admin account is locked for 1 hour due to 3 failed login attempts.");
                return;
            } else if (lockTime != null) {
                PreparedStatement reset = conn.prepareStatement("UPDATE admin_users SET failed_attempts = 0, lock_time = NULL WHERE email = ?");
                reset.setString(1, email);
                reset.executeUpdate();
            }

            String dbPassHash = rs.getString("password_hash");
            String inputPassHash = sha256(password);

            if (!inputPassHash.equals(dbPassHash)) {
                failedAttempts++;
                PreparedStatement update = conn.prepareStatement("UPDATE admin_users SET failed_attempts = ? WHERE email = ?");
                update.setInt(1, failedAttempts);
                update.setString(2, email);
                update.executeUpdate();

                if (failedAttempts >= 3) {
                    PreparedStatement lock = conn.prepareStatement("UPDATE admin_users SET lock_time = ? WHERE email = ?");
                    lock.setTimestamp(1, new Timestamp(new Date().getTime()));
                    lock.setString(2, email);
                    lock.executeUpdate();

                    sendEmail(email, "Account Locked", "Your admin account has been locked after 3 failed login attempts.");
                    JOptionPane.showMessageDialog(this, "Account locked after 3 failed attempts.");
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect password. Attempts left: " + (3 - failedAttempts));
                }
                return;
            }

            // Successful login
            PreparedStatement reset = conn.prepareStatement("UPDATE admin_users SET failed_attempts = 0, lock_time = NULL WHERE email = ?");
            reset.setString(1, email);
            reset.executeUpdate();

            generatedOtp = generateOtp();
            currentUserEmail = email;
            sendEmail(email, "Admin Login OTP", "Your OTP is: " + generatedOtp);

            otpField.setVisible(true);
            lblOtp.setVisible(true);
            verifyOtpBtn.setVisible(true);
            otpSent = true;
            loginBtn.setEnabled(false);
            forgotPassBtn.setEnabled(false);

            JOptionPane.showMessageDialog(this, "OTP sent to your email.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login Error: " + ex.getMessage());
        }
    }

    private void verifyOtp() {
        String inputOtp = otpField.getText().trim();
        if (inputOtp.equals(generatedOtp)) {
            SessionManager.setLoggedInEmail(currentUserEmail);
            JOptionPane.showMessageDialog(this, "Login Successful!");
            dispose();
            new AdminDashboardModernUI().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid OTP.");
        }
    }

    private void forgotPassword() {
        String email = JOptionPane.showInputDialog(this, "Enter your registered email:");
        if (email == null || email.trim().isEmpty()) return;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM admin_users WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Email not registered.");
                return;
            }

            String resetOtp = generateOtp();
            sendEmail(email, "Password Reset OTP", "Your password reset OTP is: " + resetOtp);

            String enteredOtp = JOptionPane.showInputDialog(this, "Enter the OTP sent to your email:");
            if (!resetOtp.equals(enteredOtp)) {
                JOptionPane.showMessageDialog(this, "Incorrect OTP.");
                return;
            }

            String newPass = JOptionPane.showInputDialog(this, "Enter new password:");
            if (newPass == null || newPass.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password cannot be empty.");
                return;
            }

            PreparedStatement update = conn.prepareStatement("UPDATE admin_users SET password_hash = ? WHERE email = ?");
            update.setString(1, sha256(newPass));
            update.setString(2, email);
            update.executeUpdate();

            sendEmail(email, "Password Reset Successful", "Your admin account password has been reset.");
            JOptionPane.showMessageDialog(this, "Password reset successful.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Reset Error: " + ex.getMessage());
        }
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Error");
        }
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    private void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Email Sending Failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminLogin());
    }
}




