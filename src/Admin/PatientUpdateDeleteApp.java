/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package admin;

import Admin.InsertDoctorApp;
import Admin.PatientMainDashboard;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.Properties;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class PatientUpdateDeleteApp extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private JTextField patientIdField, nameField, ageField, dobField, contactField, emailField, aadhaarField, guardianField, insuranceField;
    private JComboBox<String> genderCombo, bloodGroupCombo;
    private JTextArea addressArea;
    private JLabel imageLabel;
    private File selectedImageFile;
    private String currentImagePath;

    public PatientUpdateDeleteApp() {
        setTitle("Patient Update / Delete");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 840);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        Font headerFont = new Font("Segoe UI", Font.BOLD, 26);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setLayout(new BorderLayout(0, 20));

        // Header Label
        JLabel headerLabel = new JLabel("Patient Management");
        headerLabel.setFont(headerFont);
        headerLabel.setForeground(new Color(33, 150, 243));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form Panel for inputs
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(245, 247, 250));

        GroupLayout layout = new GroupLayout(formPanel);
        formPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        patientIdField = new JTextField(15);
        patientIdField.setFont(fieldFont);

        nameField = new JTextField(20);
        nameField.setFont(fieldFont);

        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setFont(fieldFont);

        ageField = new JTextField(5);
        ageField.setFont(fieldFont);

        dobField = new JTextField(10);
        dobField.setFont(fieldFont);

        bloodGroupCombo = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        bloodGroupCombo.setFont(fieldFont);

        contactField = new JTextField(15);
        contactField.setFont(fieldFont);

        emailField = new JTextField(20);
        emailField.setFont(fieldFont);

        addressArea = new JTextArea(4, 20);
        addressArea.setFont(fieldFont);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);

        aadhaarField = new JTextField(20);
        aadhaarField.setFont(fieldFont);

        guardianField = new JTextField(20);
        guardianField.setFont(fieldFont);

        insuranceField = new JTextField(25);
        insuranceField.setFont(fieldFont);

        imageLabel = new JLabel("No Image");
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(230, 230, 230));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        imageLabel.setForeground(Color.DARK_GRAY);

        JButton searchBtn = createStyledButton("Search", new Color(33, 150, 243));
        JButton browseBtn = createStyledButton("Browse", new Color(100, 181, 246));
        JButton updateBtn = createStyledButton("Update", new Color(76, 175, 80));
        JButton deleteBtn = createStyledButton("Delete", new Color(244, 67, 54));
        JButton backBtn = createStyledButton("Back", new Color(117, 117, 117));

        JLabel pidLabel = new JLabel("Patient ID:");
        pidLabel.setFont(labelFont);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(labelFont);
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        JLabel dobLabel = new JLabel("DOB (YYYY-MM-DD):");
        dobLabel.setFont(labelFont);
        JLabel bgLabel = new JLabel("Blood Group:");
        bgLabel.setFont(labelFont);
        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setFont(labelFont);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(labelFont);
        JLabel aadhaarLabel = new JLabel("Aadhaar:");
        aadhaarLabel.setFont(labelFont);
        JLabel guardianLabel = new JLabel("Guardian:");
        guardianLabel.setFont(labelFont);
        JLabel insuranceLabel = new JLabel("Insurance:");
        insuranceLabel.setFont(labelFont);
        JLabel imageTextLabel = new JLabel("Image:");
        imageTextLabel.setFont(labelFont);

        JScrollPane addressScroll = new JScrollPane(addressArea);

        // Horizontal layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pidLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(patientIdField, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchBtn, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
            )
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(nameLabel)
                    .addComponent(genderLabel)
                    .addComponent(ageLabel)
                    .addComponent(dobLabel)
                    .addComponent(bgLabel)
                    .addComponent(contactLabel)
                    .addComponent(emailLabel)
                    .addComponent(addressLabel)
                    .addComponent(aadhaarLabel)
                    .addComponent(guardianLabel)
                    .addComponent(insuranceLabel)
                    .addComponent(imageTextLabel)
                )
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(nameField, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addComponent(genderCombo, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                    .addComponent(ageField, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                    .addComponent(dobField, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                    .addComponent(bloodGroupCombo, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactField, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                    .addComponent(emailField, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressScroll, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addComponent(aadhaarField, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                    .addComponent(guardianField, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                    .addComponent(insuranceField, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(imageLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(browseBtn, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                    )
                )
            )
            .addGroup(layout.createSequentialGroup()
                .addGap(100)
                .addComponent(updateBtn, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                .addGap(40)
                .addComponent(deleteBtn, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                .addGap(40)
                .addComponent(backBtn, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
            )
        );

        // Vertical layout
        layout.setVerticalGroup(
            layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(pidLabel)
                .addComponent(patientIdField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(searchBtn, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(nameLabel)
                .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(genderLabel)
                .addComponent(genderCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(ageLabel)
                .addComponent(ageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(dobLabel)
                .addComponent(dobField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(bgLabel)
                .addComponent(bloodGroupCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(contactLabel)
                .addComponent(contactField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(emailLabel)
                .addComponent(emailField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(addressLabel)
                .addComponent(addressScroll, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(aadhaarLabel)
                .addComponent(aadhaarField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(guardianLabel)
                .addComponent(guardianField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(insuranceLabel)
                .addComponent(insuranceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(imageTextLabel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(imageLabel)
                    .addComponent(browseBtn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
            )
            .addGap(35)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(updateBtn, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addComponent(deleteBtn, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                .addComponent(backBtn, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
            )
        );

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Actions
        browseBtn.addActionListener(e -> browseImage());
        searchBtn.addActionListener(e -> searchPatient());
        updateBtn.addActionListener(e -> updatePatient());
        deleteBtn.addActionListener(e -> deletePatient());
        backBtn.addActionListener(e -> {
            dispose();
            new PatientMainDashboard().setVisible(true);
        
        });
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new LineBorder(bg.darker()));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    private void browseImage() {
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = chooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(new ImageIcon(selectedImageFile.getAbsolutePath())
                .getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH));
            imageLabel.setIcon(icon);
            imageLabel.setText(null);
        }
    }

    private void searchPatient() {
        String pid = patientIdField.getText().trim();
        if (pid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Patient ID to search.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM patients WHERE patient_id = ?");
            ps.setString(1, pid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                genderCombo.setSelectedItem(rs.getString("gender"));
                ageField.setText(rs.getString("age"));
                dobField.setText(rs.getString("dob"));
                bloodGroupCombo.setSelectedItem(rs.getString("blood_group"));
                contactField.setText(rs.getString("contact"));
                emailField.setText(rs.getString("email"));
                addressArea.setText(rs.getString("address"));
                aadhaarField.setText(rs.getString("aadhaar"));
                guardianField.setText(rs.getString("guardian"));
                insuranceField.setText(rs.getString("insurance"));
                currentImagePath = rs.getString("image_path");
                if (currentImagePath != null && !currentImagePath.isEmpty()) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(currentImagePath)
                        .getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH));
                    imageLabel.setIcon(icon);
                    imageLabel.setText(null);
                } else {
                    imageLabel.setIcon(null);
                    imageLabel.setText("No Image");
                }
                selectedImageFile = null; // reset file chooser state
            } else {
                JOptionPane.showMessageDialog(this, "Patient not found.", "Info", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching patient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePatient() {
        String pid = patientIdField.getText().trim();
        if (pid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient ID is required to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int age = Integer.parseInt(ageField.getText().trim());
            if(age < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String imagePathToSave = currentImagePath;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            if (selectedImageFile != null) {
                String imgDir = "patient_images";
                new File(imgDir).mkdir();
                String ext = selectedImageFile.getName().substring(selectedImageFile.getName().lastIndexOf("."));
                String dest = imgDir + "/" + pid + ext;
                Files.copy(selectedImageFile.toPath(), Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
                imagePathToSave = dest;
            }

            PreparedStatement ps = conn.prepareStatement(
                "UPDATE patients SET name=?, gender=?, age=?, dob=?, blood_group=?, contact=?, email=?, address=?, aadhaar=?, guardian=?, insurance=?, image_path=? WHERE patient_id=?"
            );
            ps.setString(1, nameField.getText().trim());
            ps.setString(2, (String) genderCombo.getSelectedItem());
            ps.setInt(3, Integer.parseInt(ageField.getText().trim()));
            ps.setString(4, dobField.getText().trim());
            ps.setString(5, (String) bloodGroupCombo.getSelectedItem());
            ps.setString(6, contactField.getText().trim());
            ps.setString(7, emailField.getText().trim());
            ps.setString(8, addressArea.getText().trim());
            ps.setString(9, aadhaarField.getText().trim());
            ps.setString(10, guardianField.getText().trim());
            ps.setString(11, insuranceField.getText().trim());
            ps.setString(12, imagePathToSave);
            ps.setString(13, pid);

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Patient updated successfully.");
                sendEmail(emailField.getText(), "Patient Updated", "Your patient record has been updated.");
            } else {
                JOptionPane.showMessageDialog(this, "Update failed. Patient ID may not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        String pid = patientIdField.getText().trim();
        if (pid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient ID is required to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete patient " + pid + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM patients WHERE patient_id = ?");
            ps.setString(1, pid);
            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                JOptionPane.showMessageDialog(this, "Patient deleted successfully.");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed. Patient ID may not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        genderCombo.setSelectedIndex(0);
        ageField.setText("");
        dobField.setText("");
        bloodGroupCombo.setSelectedIndex(0);
        contactField.setText("");
        emailField.setText("");
        addressArea.setText("");
        aadhaarField.setText("");
        guardianField.setText("");
        insuranceField.setText("");
        imageLabel.setIcon(null);
        imageLabel.setText("No Image");
        selectedImageFile = null;
        currentImagePath = null;
    }

    private void sendEmail(String toEmail, String subject, String body) {
        if (toEmail == null || toEmail.isEmpty()) return;

        final String fromEmail = "aroramayank488@gmail.com"; // Replace with your email
        final String password = "zojw kkfv hdyl etih";    // Replace with your email password or app-specific password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // For Gmail SMTP
        props.put("mail.smtp.port", "587"); 
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail, "Hospital Management System"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject(subject);
            msg.setText(body);
            Transport.send(msg);
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Load JDBC driver if needed (depending on your setup)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found.");
        }

       SwingUtilities.invokeLater(PatientUpdateDeleteApp::new);
    }
}
