/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class UpdateDeleteDoctorApp extends JFrame {

    private JTextField tfEmailSearch, tfName, tfEmail, tfQualification;
    private JPasswordField pfPass, pfConfirm;
    private JComboBox<String> cbGender, cbSpeciality;
    private JLabel lblImage, lblDoctorId;
    private JButton btnSearch, btnUpload, btnUpdate, btnDelete, btnBack;
    private File selectedFile = null;
    private byte[] imageBytes = null;
    private String currentDoctorId = null;

    public UpdateDeleteDoctorApp() {
        setTitle("Update/Delete Doctor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        add(mainPanel);

        JLabel title = new JLabel("Doctor Update/Delete Module", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(45, 85, 160));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Doctor Details"));
        centerPanel.add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Search by Email:"), gbc);

        tfEmailSearch = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(tfEmailSearch, gbc);

        btnSearch = new JButton("Search");
        gbc.gridx = 2;
        formPanel.add(btnSearch, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 3;
        lblDoctorId = new JLabel("Doctor ID: ");
        lblDoctorId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDoctorId.setForeground(new Color(0, 120, 215));
        formPanel.add(lblDoctorId, gbc);
        gbc.gridwidth = 1;

        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        tfName = new JTextField(20);
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(tfName, gbc);
        gbc.gridwidth = 1;

        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        tfEmail = new JTextField(20);
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(tfEmail, gbc);
        gbc.gridwidth = 1;

        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Password:"), gbc);
        pfPass = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(pfPass, gbc);
        gbc.gridwidth = 1;

        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        pfConfirm = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(pfConfirm, gbc);
        gbc.gridwidth = 1;

        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Gender:"), gbc);
        cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(cbGender, gbc);
        gbc.gridwidth = 1;

        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Qualification:"), gbc);
        tfQualification = new JTextField(20);
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(tfQualification, gbc);
        gbc.gridwidth = 1;

        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Speciality:"), gbc);
        cbSpeciality = new JComboBox<>(new String[]{
                "Cardiology", "Dermatology", "Neurology", "Pediatrics", "Psychiatry",
                "General Surgery", "Orthopedics", "Radiology", "Oncology", "Gynecology"
        });
        gbc.gridx = 1; gbc.gridwidth = 2;
        formPanel.add(cbSpeciality, gbc);
        gbc.gridwidth = 1;

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        centerPanel.add(rightPanel, BorderLayout.EAST);

        lblImage = new JLabel("No image selected", SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(180, 180));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        rightPanel.add(lblImage);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        btnUpload = new JButton("Upload Image");
        rightPanel.add(btnUpload);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);

        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnBack = new JButton("Back");

        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setBackground(new Color(220, 53, 69));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);

        Dimension btnSize = new Dimension(120, 40);
        btnUpdate.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);

        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnBack);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> searchDoctor());
        btnUpload.addActionListener(e -> uploadImage());
        btnUpdate.addActionListener(e -> updateDoctor());
        btnDelete.addActionListener(e -> deleteDoctor());
        btnBack.addActionListener(e -> {
            dispose();
            new DoctorMainDashboard().setVisible(true);
        });

        setVisible(true);
    }

    private void searchDoctor() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "")) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM doctor WHERE email = ?");
            ps.setString(1, tfEmailSearch.getText().trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentDoctorId = rs.getString("doctor_id");
                lblDoctorId.setText("Doctor ID: " + currentDoctorId);
                tfName.setText(rs.getString("name"));
                tfEmail.setText(rs.getString("email"));
                pfPass.setText(rs.getString("password"));
                pfConfirm.setText(rs.getString("password"));
                cbGender.setSelectedItem(rs.getString("gender"));
                tfQualification.setText(rs.getString("qualification"));
                cbSpeciality.setSelectedItem(rs.getString("speciality"));

                imageBytes = rs.getBytes("image");
                if (imageBytes != null) {
                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image scaled = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(scaled));
                    lblImage.setText("");
                } else {
                    lblImage.setIcon(null);
                    lblImage.setText("No image selected");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No doctor found.");
                clearForm();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void uploadImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            try {
                imageBytes = Files.readAllBytes(selectedFile.toPath());
                ImageIcon icon = new ImageIcon(imageBytes);
                Image scaled = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(scaled));
                lblImage.setText("");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to load image.");
            }
        }
    }

    private void updateDoctor() {
        if (!validateInput()) return;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "")) {
            String sql = "UPDATE doctor SET name=?, email=?, password=?, gender=?, qualification=?, speciality=?, image=? WHERE doctor_id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, tfName.getText().trim());
            ps.setString(2, tfEmail.getText().trim());
            ps.setString(3, new String(pfPass.getPassword()));
            ps.setString(4, cbGender.getSelectedItem().toString());
            ps.setString(5, tfQualification.getText().trim());
            ps.setString(6, cbSpeciality.getSelectedItem().toString());
            ps.setBytes(7, imageBytes);
            ps.setString(8, currentDoctorId);

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Doctor updated successfully.");
                sendEmailNotification();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteDoctor() {
        if (currentDoctorId == null) {
            JOptionPane.showMessageDialog(this, "Please search and select a doctor first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this doctor?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "")) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM doctor WHERE doctor_id = ?");
            ps.setString(1, currentDoctorId);
            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully.");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Deletion failed.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Doctor details Exist with patient");
        }
    }

    private boolean validateInput() {
        if (tfName.getText().trim().isEmpty() || tfEmail.getText().trim().isEmpty() ||
            pfPass.getPassword().length == 0 || pfConfirm.getPassword().length == 0 ||
            tfQualification.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return false;
        }
        if (!new String(pfPass.getPassword()).equals(new String(pfConfirm.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return false;
        }
        return true;
    }

    private void clearForm() {
        tfEmailSearch.setText("");
        tfName.setText("");
        tfEmail.setText("");
        pfPass.setText("");
        pfConfirm.setText("");
        tfQualification.setText("");
        cbGender.setSelectedIndex(0);
        cbSpeciality.setSelectedIndex(0);
        lblImage.setIcon(null);
        lblImage.setText("No image selected");
        lblDoctorId.setText("Doctor ID: ");
        imageBytes = null;
        currentDoctorId = null;
    }

    private void sendEmailNotification() {
        String to = tfEmail.getText().trim();
        String from = "aroramayank488@gmail.com"; // Replace with your email
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "zojw kkfv hdyl etih"); // Use App Password
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject("Doctor Details Updated");
            msg.setText("Dear Dr. " + tfName.getText().trim() + ",\n\nYour details have been successfully updated in our hospital system.\n\nRegards,\nAdmin");

            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new UpdateDeleteDoctorApp();
    }
}





