/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doctor;

import homepage.HospitalHomePage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class DoctorLoginSystem extends JFrame {
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnLogin, btnForgot, btnBack;
    private JLabel lblStatus;

    public DoctorLoginSystem() {
        setTitle("Doctor Login - Hospital Management System");
        setSize(480, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
        initListeners();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 14, 14, 14);

        JLabel lblTitle = new JLabel("Doctor Login");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(25, 118, 210));

        tfEmail = new JTextField(22);
        pfPassword = new JPasswordField(22);
        btnLogin = new JButton("Login");
        btnForgot = new JButton("Forgot Password?");
        btnBack = new JButton("Back");
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(new Color(198, 40, 40));

        btnLogin.setBackground(new Color(56, 142, 60));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);

        btnForgot.setForeground(new Color(25, 118, 210));
        btnForgot.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnForgot.setBorderPainted(false);
        btnForgot.setContentAreaFilled(false);
        btnForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.setBackground(new Color(211, 47, 47));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy++; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(tfEmail, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(pfPassword, gbc);

        gbc.gridx = 1; gbc.gridy++; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnLogin, gbc);
        gbc.gridy++;
        panel.add(btnForgot, gbc);
        gbc.gridy++;
        panel.add(btnBack, gbc);
        gbc.gridy++;
        panel.add(lblStatus, gbc);

        add(panel);
        setVisible(true);
    }

    private void initListeners() {
        btnLogin.addActionListener(e -> login());
        btnForgot.addActionListener(e -> forgotPassword());
        btnBack.addActionListener(e -> {
            dispose();
            new HospitalHomePage().setVisible(true);
        });
    }

    private void login() {
        lblStatus.setText(" ");
        String email = tfEmail.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please enter both email and password.");
            return;
        }

        JDialog loading = createLoadingDialog("Sending OTP to email...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            String doctorName = "", doctorId = "", otp = "";

            @Override
            protected Void doInBackground() {
                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
                     PreparedStatement pst = con.prepareStatement("SELECT * FROM doctor WHERE email=? AND password=?")) {
                    pst.setString(1, email);
                    pst.setString(2, password);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        doctorName = rs.getString("name");
                        doctorId = rs.getString("doctor_id");
                        otp = generateOTP();
                        sendOTPEmail(email, otp);
                    } else {
                        lblStatus.setText("Invalid email or password.");
                    }

                } catch (SQLException | MessagingException e) {
                    lblStatus.setText("Error: " + e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();

                if (!otp.isEmpty()) {
                    String inputOtp = JOptionPane.showInputDialog(DoctorLoginSystem.this, "Enter OTP sent to your email:");
                    if (inputOtp != null && otp.equals(inputOtp.trim())) {
                        try {
                            sendLoginSuccessEmail(email, doctorName);
                        } catch (MessagingException e) {
                            System.out.println("Login success email failed: " + e.getMessage());
                        }
                        dispose();
                        new DoctorDashboard(doctorId).setVisible(true);
                    } else {
                        lblStatus.setText("Incorrect or cancelled OTP.");
                    }
                }
            }
        };
        worker.execute();
        loading.setVisible(true);
    }

    private void forgotPassword() {
        lblStatus.setText(" ");
        String email = tfEmail.getText().trim();

        if (email.isEmpty()) {
            lblStatus.setText("Please enter your registered email.");
            return;
        }

        JDialog loading = createLoadingDialog("Sending OTP to email...");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            String doctorName = "", otp = "";
            boolean found = false;

            @Override
            protected Void doInBackground() {
                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
                     PreparedStatement pst = con.prepareStatement("SELECT * FROM doctor WHERE email=?")) {
                    pst.setString(1, email);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        found = true;
                        doctorName = rs.getString("name");
                        otp = generateOTP();
                        sendOTPEmail(email, otp);
                    } else {
                        lblStatus.setText("Email not registered.");
                    }
                } catch (SQLException | MessagingException e) {
                    lblStatus.setText("Error: " + e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
                if (found) {
                    String inputOtp = JOptionPane.showInputDialog(DoctorLoginSystem.this, "Enter OTP sent to your email:");
                    if (inputOtp != null && otp.equals(inputOtp.trim())) {
                        String newPass = JOptionPane.showInputDialog(DoctorLoginSystem.this, "Enter new password:");
                        if (newPass != null && !newPass.trim().isEmpty()) {
                            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "")) {
                                PreparedStatement update = con.prepareStatement("UPDATE doctor SET password=? WHERE email=?");
                                update.setString(1, newPass.trim());
                                update.setString(2, email);
                                update.executeUpdate();

                                sendPasswordResetSuccessEmail(email, doctorName);
                                JOptionPane.showMessageDialog(DoctorLoginSystem.this, "Password reset successful.");
                            } catch (Exception ex) {
                                lblStatus.setText("Failed to reset password.");
                                ex.printStackTrace();
                            }
                        } else {
                            lblStatus.setText("Password cannot be empty.");
                        }
                    } else {
                        lblStatus.setText("Incorrect or cancelled OTP.");
                    }
                }
            }
        };
        worker.execute();
        loading.setVisible(true);
    }

    private String generateOTP() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    private void sendOTPEmail(String to, String otp) throws MessagingException {
        final String from = "aroramayank488@gmail.com";
        final String pass = "zojw kkfv hdyl etih";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("OTP Verification - Hospital Management System");
        message.setText("Dear Doctor,\n\nYour OTP is: " + otp + "\n\nDo not share it with anyone.\n\nRegards,\nHospital Admin");
        Transport.send(message);
    }

    private void sendLoginSuccessEmail(String to, String name) throws MessagingException {
        final String from = "aroramayank488@gmail.com";
        final String pass = "zojw kkfv hdyl etih";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("Login Successful - HMS");
        message.setText("Dear Dr. " + name + ",\n\nYou have successfully logged in.\n\nRegards,\nHospital Admin");
        Transport.send(message);
    }

    private void sendPasswordResetSuccessEmail(String to, String name) throws MessagingException {
        final String from = "aroramayank488@gmail.com";
        final String pass = "zojw kkfv hdyl etih";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("Password Reset - HMS");
        message.setText("Dear Dr. " + name + ",\n\nYour password has been successfully reset.\n\nRegards,\nHospital Admin");
        Transport.send(message);
    }

    private JDialog createLoadingDialog(String message) {
        JDialog dialog = new JDialog(this, "Please Wait...", true);
        JPanel panel = new JPanel();
        panel.add(new JLabel(message));
        dialog.getContentPane().add(panel);
        dialog.setSize(280, 90);
        dialog.setLocationRelativeTo(this);
        return dialog;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DoctorLoginSystem());
    }
}

