/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doctor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

public class UpdateDoctorProfile extends JFrame {
    private String doctorId;
    private Connection conn;

    private JTextField tfName, tfEmail, tfQualification, tfSpeciality;
    private JPasswordField pfPassword;
    private JComboBox<String> cbGender;
    private JLabel lblImage;
    private JButton btnUploadImage, btnSave, btnBack;
    private byte[] imageBytes; // store image bytes to save
    private File selectedImageFile;

    public UpdateDoctorProfile(String doctorId) {
        this.doctorId = doctorId;

        setTitle("Update Doctor Profile");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        connectDB();
        loadDoctorData();

        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Change background color here
        panel.setBackground(new Color(200, 230, 255)); // light blue shade

        JLabel lblTitle = new JLabel("Update Profile");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(170, 20, 200, 30);
        panel.add(lblTitle);

        // Name
        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 80, 100, 25);
        panel.add(lblName);
        tfName = new JTextField();
        tfName.setBounds(160, 80, 250, 25);
        panel.add(tfName);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 120, 100, 25);
        panel.add(lblPassword);
        pfPassword = new JPasswordField();
        pfPassword.setBounds(160, 120, 250, 25);
        panel.add(pfPassword);

        // Gender
        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(50, 160, 100, 25);
        panel.add(lblGender);
        cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cbGender.setBounds(160, 160, 250, 25);
        panel.add(cbGender);

        // Qualification
        JLabel lblQualification = new JLabel("Qualification:");
        lblQualification.setBounds(50, 200, 100, 25);
        panel.add(lblQualification);
        tfQualification = new JTextField();
        tfQualification.setBounds(160, 200, 250, 25);
        panel.add(tfQualification);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 240, 100, 25);
        panel.add(lblEmail);
        tfEmail = new JTextField();
        tfEmail.setBounds(160, 240, 250, 25);
        panel.add(tfEmail);

        // Specialit
        JLabel lblSpeciality = new JLabel("Speciality:");
        lblSpeciality.setBounds(50, 280, 100, 25);
        panel.add(lblSpeciality);
        tfSpeciality = new JTextField();
        tfSpeciality.setBounds(160, 280, 250, 25);
        panel.add(tfSpeciality);

        // Image label + Upload Button
        lblImage = new JLabel();
        lblImage.setBounds(180, 320, 140, 140);
        lblImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblImage);

        btnUploadImage = new JButton("Upload Image");
        btnUploadImage.setBounds(180, 470, 140, 30);
        panel.add(btnUploadImage);
        btnUploadImage.addActionListener(e -> chooseImage());

        // Save button
        btnSave = new JButton("Save Changes");
        btnSave.setBounds(320, 520, 140, 35);
        btnSave.setBackground(new Color(52, 152, 219));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        panel.add(btnSave);
        btnSave.addActionListener(e -> saveProfile());

        // Back button - added here
        btnBack = new JButton("Back");
        btnBack.setBounds(50, 520, 100, 35);
        btnBack.setBackground(new Color(192, 57, 43));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        panel.add(btnBack);
        btnBack.addActionListener(e -> {
            // Close this window on back
            this.dispose();
            new DoctorDashboard( doctorId).setVisible(true);
        });

        add(panel, BorderLayout.CENTER);
    }

    private void connectDB() {
        try {
            String url = "jdbc:mysql://localhost:3306/hospital_db";
            String user = "root";
            String pass = "";
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadDoctorData() {
        String sql = "SELECT * FROM doctor WHERE doctor_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, doctorId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                tfName.setText(rs.getString("name"));
                pfPassword.setText(rs.getString("password"));
                cbGender.setSelectedItem(rs.getString("gender"));
                tfQualification.setText(rs.getString("qualification"));
                tfEmail.setText(rs.getString("email"));
                tfSpeciality.setText(rs.getString("speciality"));

                Blob blob = rs.getBlob("image");
                if (blob != null) {
                    imageBytes = blob.getBytes(1, (int) blob.length());
                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image img = icon.getImage().getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(img));
                } else {
                    lblImage.setText("No Image");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load doctor data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = chooser.getSelectedFile();
            try {
                ImageIcon icon = new ImageIcon(selectedImageFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FileInputStream fis = new FileInputStream(selectedImageFile);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, read);
                }
                fis.close();
                imageBytes = baos.toByteArray();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to load image: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveProfile() {
        String name = tfName.getText().trim();
        String password = new String(pfPassword.getPassword());
        String gender = (String) cbGender.getSelectedItem();
        String qualification = tfQualification.getText().trim();
        String email = tfEmail.getText().trim();
        String speciality = tfSpeciality.getText().trim();

        if (name.isEmpty() || password.isEmpty() || email.isEmpty() || speciality.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Password, Email, and Speciality are required fields.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.matches("^\\S+@\\S+\\.\\S+$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "UPDATE doctor SET name = ?, password = ?, gender = ?, qualification = ?, email = ?, speciality = ?, image = ? WHERE doctor_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, password);
            pst.setString(3, gender);
            pst.setString(4, qualification);
            pst.setString(5, email);
            pst.setString(6, speciality);

            if (imageBytes != null) {
                pst.setBytes(7, imageBytes);
            } else {
                pst.setNull(7, Types.BLOB);
            }

            pst.setString(8, doctorId);

            int updated = pst.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No changes made or update failed.",
                        "Update Failed", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving profile: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // For testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateDoctorProfile("DOC1134"));  // replace with valid doctor_id
    }
}


