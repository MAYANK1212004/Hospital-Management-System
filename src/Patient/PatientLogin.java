/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Patient;

import homepage.HospitalHomePage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class PatientLogin extends JFrame {
    private JTextField emailField, otpField;
    private JPasswordField passwordField, newPassField, confirmPassField;
    private JButton sendOtpBtn, loginBtn, forgotPassBtn, verifyOtpBtn, changePassBtn, backBtn;
    private String generatedOTP, currentEmail;
    private boolean isLoginOTPVerified = false;

    public PatientLogin() {
        setTitle("Patient Login Portal");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(225, 245, 254)); // Soft blue

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setPreferredSize(new Dimension(500, 600));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("Patient Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(33, 150, 243));
        contentPanel.add(title, gbc);

        // Email
        gbc.gridy++; gbc.gridwidth = 1;
        contentPanel.add(new JLabel("Email:"), gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        contentPanel.add(emailField, gbc);

        // Send OTP
        gbc.gridy++; gbc.gridx = 1;
        sendOtpBtn = createStyledButton("Send OTP", new Color(30, 136, 229));
        sendOtpBtn.addActionListener(e -> sendLoginOTP(emailField.getText()));
        contentPanel.add(sendOtpBtn, gbc);

        // OTP
        gbc.gridy++; gbc.gridx = 0;
        contentPanel.add(new JLabel("OTP:"), gbc);

        otpField = new JTextField(20);
        gbc.gridx = 1;
        contentPanel.add(otpField, gbc);

        // Verify OTP
        gbc.gridy++; gbc.gridx = 1;
        verifyOtpBtn = createStyledButton("Verify OTP", new Color(67, 160, 71));
        verifyOtpBtn.addActionListener(e -> verifyLoginOTP());
        contentPanel.add(verifyOtpBtn, gbc);

        // Password
        gbc.gridy++; gbc.gridx = 0;
        contentPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        contentPanel.add(passwordField, gbc);

        // Login
        gbc.gridy++; gbc.gridx = 1;
        loginBtn = createStyledButton("Login", new Color(0, 172, 193));
        loginBtn.addActionListener(e -> loginUser());
        contentPanel.add(loginBtn, gbc);

        // Forgot password
        gbc.gridy++;
        forgotPassBtn = new JButton("Forgot Password?");
        forgotPassBtn.setForeground(new Color(33, 150, 243));
        forgotPassBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        forgotPassBtn.setContentAreaFilled(false);
        forgotPassBtn.setBorderPainted(false);
        forgotPassBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPassBtn.addActionListener(e -> showOtpForReset());
        contentPanel.add(forgotPassBtn, gbc);

        // New password
        gbc.gridy++;
        newPassField = new JPasswordField(20);
        newPassField.setVisible(false);
        contentPanel.add(newPassField, gbc);

        // Confirm password
        gbc.gridy++;
        confirmPassField = new JPasswordField(20);
        confirmPassField.setVisible(false);
        contentPanel.add(confirmPassField, gbc);

        // Change password button
        gbc.gridy++;
        changePassBtn = createStyledButton("Change Password", new Color(255, 112, 67));
        changePassBtn.setVisible(false);
        changePassBtn.addActionListener(e -> changePassword());
        contentPanel.add(changePassBtn, gbc);

        // Back
        gbc.gridy++;
        backBtn = createStyledButton("Back", new Color(120, 144, 156));
        backBtn.addActionListener(e -> {
            dispose();
            new HospitalHomePage().setVisible(true);
        });
        contentPanel.add(backBtn, gbc);

        mainPanel.add(contentPanel);
        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color bgColor) {
    JButton btn = new JButton(text);
    btn.setBackground(bgColor);
    btn.setForeground(Color.WHITE);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btn.setFocusPainted(false);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btn.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(bgColor.darker(), 1),
        BorderFactory.createEmptyBorder(10, 20, 10, 20)
    ));

    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn.setBackground(bgColor.darker());
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn.setBackground(bgColor);
        }
    });

    return btn;
}


    private void sendLoginOTP(String email) {
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter your email first.");
            return;
        }
        generatedOTP = String.valueOf(new Random().nextInt(899999) + 100000);
        currentEmail = email;

        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM patients WHERE email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sendEmail(email, generatedOTP, "Login OTP");
                JOptionPane.showMessageDialog(this, "OTP sent to your email.");
            } else {
                JOptionPane.showMessageDialog(this, "Email not registered.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void verifyLoginOTP() {
        if (otpField.getText().trim().equals(generatedOTP)) {
            isLoginOTPVerified = true;
            JOptionPane.showMessageDialog(this, "OTP verified. Proceed to login.");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect OTP.");
        }
    }

    private void loginUser() {
        if (!isLoginOTPVerified) {
            JOptionPane.showMessageDialog(this, "Please verify OTP first.");
            return;
        }

        String email = emailField.getText().trim();
        String password = String.valueOf(passwordField.getPassword());

        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM patients WHERE email=? AND password=?");
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String patientId = rs.getString("patient_id");
                JOptionPane.showMessageDialog(this, "Login successful! Welcome " + patientId);
                dispose();
                new PatientDashboard(patientId).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showOtpForReset() {
        String email = JOptionPane.showInputDialog(this, "Enter your registered email:");
        if (email == null || email.trim().isEmpty()) return;

        generatedOTP = String.valueOf(new Random().nextInt(899999) + 100000);
        currentEmail = email;

        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM patients WHERE email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sendEmail(email, generatedOTP, "Password Reset OTP");
                String enteredOtp = JOptionPane.showInputDialog(this, "Enter OTP sent to your email:");
                if (enteredOtp != null && enteredOtp.equals(generatedOTP)) {
                    newPassField.setVisible(true);
                    confirmPassField.setVisible(true);
                    changePassBtn.setVisible(true);
                    revalidate();
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect OTP.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Email not registered.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void changePassword() {
        String newPass = String.valueOf(newPassField.getPassword());
        String confirmPass = String.valueOf(confirmPassField.getPassword());

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE patients SET password=? WHERE email=?");
            ps.setString(1, newPass);
            ps.setString(2, currentEmail);
            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Password updated. You can now login.");
                dispose();
                new PatientLogin().setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendEmail(String to, String otp, String subject) {
        final String from = "aroramayank488@gmail.com";
        final String pass = "zojw kkfv hdyl etih";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText("Your OTP is: " + otp);
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PatientLogin().setVisible(true));
    }
}





