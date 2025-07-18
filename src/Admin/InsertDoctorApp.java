/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class InsertDoctorApp extends JFrame {

    private JTextField tfDoctorId, tfName, tfEmail, tfQualification;
    private JPasswordField pfPass, pfConfirm;
    private JComboBox<String> cbGender, cbSpeciality;
    private JLabel lblImage;
    private File selectedFile;
    private JButton btnUpload, btnSubmit, btnBack;

    public InsertDoctorApp() {
        setTitle("Doctor Registration");
        setSize(950, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Doctor Registration Form", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 32));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(new Color(40, 60, 90));
        header.setPreferredSize(new Dimension(getWidth(), 80));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);
        Dimension fieldDim = new Dimension(320, 35);
        int y = 0;

        // Doctor ID
        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Doctor ID:", JLabel.LEFT), gbc);
        gbc.gridx = 1;
        tfDoctorId = createTextField(fieldFont, fieldDim);
        tfDoctorId.setEditable(false);
        formPanel.add(tfDoctorId, gbc);

        // Name
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Name:", JLabel.LEFT), gbc);
        gbc.gridx = 1;
        tfName = createTextField(fieldFont, fieldDim);
        formPanel.add(tfName, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Email:", JLabel.LEFT), gbc);
        gbc.gridx = 1;
        tfEmail = createTextField(fieldFont, fieldDim);
        formPanel.add(tfEmail, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Password:", JLabel.LEFT), gbc);
        gbc.gridx = 1;
        pfPass = new JPasswordField();
        pfPass.setFont(fieldFont);
        pfPass.setPreferredSize(fieldDim);
        formPanel.add(pfPass, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Confirm Password:", JLabel.LEFT), gbc);
        gbc.gridx = 1;
        pfConfirm = new JPasswordField();
        pfConfirm.setFont(fieldFont);
        pfConfirm.setPreferredSize(fieldDim);
        formPanel.add(pfConfirm, gbc);

        // Gender
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Gender:", JLabel.LEFT), gbc);
        gbc.gridx = 1;
        cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cbGender.setFont(fieldFont);
        cbGender.setPreferredSize(fieldDim);
        formPanel.add(cbGender, gbc);

        // Qualification
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Qualification:", JLabel.LEFT), gbc);
        gbc.gridx = 1;
        tfQualification = createTextField(fieldFont, fieldDim);
        formPanel.add(tfQualification, gbc);

        // SPECIALITY: ComboBox instead of TextField
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Speciality:", JLabel.LEFT), gbc);
        gbc.gridx = 1;
        String[] specialities = {
            "Cardiologist", "Dermatologist", "Neurologist", "Pediatrician",
            "Psychiatrist", "Radiologist", "Surgeon", "General Physician",
            "Orthopedic", "ENT Specialist"
        };
        cbSpeciality = new JComboBox<>(specialities);
        cbSpeciality.setFont(fieldFont);
        cbSpeciality.setPreferredSize(fieldDim);
        formPanel.add(cbSpeciality, gbc);

        // Image Upload
        gbc.gridx = 0; gbc.gridy = ++y;
        formPanel.add(new JLabel("Image:", JLabel.LEFT), gbc);
        gbc.gridx = 1;
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setPreferredSize(fieldDim);
        lblImage = new JLabel("No file selected", JLabel.LEFT);
        lblImage.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        btnUpload = new JButton("Upload");
        styleButton(btnUpload, new Color(100, 118, 255));
        imgPanel.add(lblImage, BorderLayout.CENTER);
        imgPanel.add(btnUpload, BorderLayout.EAST);
        formPanel.add(imgPanel, gbc);

        // Submit Button
        gbc.gridx = 0; gbc.gridy = ++y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSubmit = new JButton("Register Doctor");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSubmit.setPreferredSize(new Dimension(300, 45));
        styleButton(btnSubmit, new Color(45, 180, 90));
        formPanel.add(btnSubmit, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Back Button
        btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        styleButton(btnBack, new Color(230, 70, 70));
        btnBack.setPreferredSize(new Dimension(100, 35));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(250, 250, 250));
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);

        generateDoctorId();

        btnUpload.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fc.getSelectedFile();
                lblImage.setText(selectedFile.getName());
            }
        });

        btnSubmit.addActionListener(e -> registerDoctor());

        btnBack.addActionListener(e -> {
            dispose();
            new DoctorMainDashboard(); // Assume this exists elsewhere
        });

        setVisible(true);
    }

    private JTextField createTextField(Font font, Dimension size) {
        JTextField tf = new JTextField();
        tf.setFont(font);
        tf.setPreferredSize(size);
        return tf;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });
    }

    private void generateDoctorId() {
        String id = "DOC" + (int) (Math.random() * 9000 + 1000);
        tfDoctorId.setText(id);
    }

    private void registerDoctor() {
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hospital_db", "root", "")) {

            String doctorId = tfDoctorId.getText().trim();
            String name = tfName.getText().trim();
            String email = tfEmail.getText().trim();
            String password = new String(pfPass.getPassword());
            String confirm = new String(pfConfirm.getPassword());
            String gender = (String) cbGender.getSelectedItem();
            String qualification = tfQualification.getText().trim();
            String speciality = (String) cbSpeciality.getSelectedItem(); // now from combo box

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                confirm.isEmpty() || qualification.isEmpty() ||
                speciality.isEmpty() || selectedFile == null) {
                JOptionPane.showMessageDialog(this,
                    "Please fill in all fields and upload an image.");
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Invalid email format.");
                return;
            }

            String sql = "INSERT INTO doctor "
                       + "(doctor_id, name, email, password, gender, qualification, speciality, image) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, doctorId);
            pst.setString(2, name);
            pst.setString(3, email);
            pst.setString(4, password);
            pst.setString(5, gender);
            pst.setString(6, qualification);
            pst.setString(7, speciality);
            pst.setBinaryStream(8,
                new FileInputStream(selectedFile),
                (int) selectedFile.length());

            int result = pst.executeUpdate();
            if (result > 0) {
                sendEmail(email, doctorId);
                JOptionPane.showMessageDialog(this,
                    "Doctor registered successfully!");
                clearForm();
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this,
                "Doctor with this email already exists.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage());
        }
    }

    private void sendEmail(String to, String doctorId) {
        final String fromEmail = "aroramayank488@gmail.com";
        final String password = "zojw kkfv hdyl etih";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new jakarta.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "Hospital Admin"));
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(to)
            );
            message.setSubject("Doctor Registration Successful");
            message.setText("Hello Doctor,\n\nYour Doctor ID is: "
                            + doctorId + "\n\nThank you for joining us!");
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        tfName.setText("");
        tfEmail.setText("");
        pfPass.setText("");
        pfConfirm.setText("");
        tfQualification.setText("");
        cbSpeciality.setSelectedIndex(0); // reset combo box
        lblImage.setText("No file selected");
        cbGender.setSelectedIndex(0);
        selectedFile = null;
        generateDoctorId();
    }

    public static void main(String[] args) {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (Exception e) { /* ignore */ }
        SwingUtilities.invokeLater(InsertDoctorApp::new);
    }
}


