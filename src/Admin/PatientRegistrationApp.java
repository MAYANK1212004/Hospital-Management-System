/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import jakarta.mail.*;
import jakarta.mail.internet.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.*;
import java.sql.Date;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class PatientRegistrationApp extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final String EMAIL_USERNAME = "aroramayank488@gmail.com";
    private static final String EMAIL_PASSWORD = "zojw kkfv hdyl etih";

    private JTextField patientIdField, nameField, ageField, dobField, contactField, emailField, aadhaarField, guardianField, insuranceField;
    private JComboBox<String> genderCombo, bloodGroupCombo;
    private JTextArea addressArea;
    private JLabel imageLabel;
    private File selectedImageFile;
    private String generatedOTP; // Store the generated OTP for verification

    public PatientRegistrationApp() {
        setTitle("Patient Registration");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(900, 820));
        setLocationRelativeTo(null);
        setLightThemeUI();

        initComponents();
        generatePatientId();

        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 248, 250));

        // Title
        JLabel title = new JLabel("Patient Registration Form", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 32));
        title.setForeground(new Color(33, 37, 41));
        title.setBorder(new EmptyBorder(10, 0, 25, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Split Panel using GridBagLayout for 60/40 ratio
        JPanel splitPanel = new JPanel(new GridBagLayout());
        splitPanel.setBackground(new Color(245, 248, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Panel - 60%
        JPanel leftPanel = createLeftPanel();
        gbc.gridx = 0;
        gbc.weightx = 0.6;
        splitPanel.add(leftPanel, gbc);

        // Right Panel - 40%
        JPanel rightPanel = createRightPanel();
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        splitPanel.add(rightPanel, gbc);

        mainPanel.add(splitPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        pack();
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new RoundedBorder(12, new Color(200, 210, 218)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 30, 12, 30);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        row = addField(panel, gbc, row, "Patient ID:", patientIdField = createDisabledField());
        row = addField(panel, gbc, row, "Full Name:", nameField = new JTextField());
        row = addField(panel, gbc, row, "Gender:", genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"}));
        row = addField(panel, gbc, row, "Age:", ageField = new JTextField());
        row = addField(panel, gbc, row, "DOB (YYYY-MM-DD):", dobField = new JTextField());
        row = addField(panel, gbc, row, "Blood Group:", bloodGroupCombo = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"}));
        row = addField(panel, gbc, row, "Contact:", contactField = new JTextField());
        row = addField(panel, gbc, row, "Email:", emailField = new JTextField());

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(new Color(70, 70, 70));
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(addressLabel, gbc);

        gbc.gridy = row++;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        addressArea = new JTextArea(4, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addressArea.setBackground(Color.WHITE);
        addressArea.setForeground(new Color(40, 40, 40));
        addressArea.setCaretColor(new Color(40, 40, 40));
        JScrollPane scroll = new JScrollPane(addressArea);
        scroll.setBorder(new RoundedBorder(8, new Color(200, 210, 218)));
        panel.add(scroll, gbc);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new RoundedBorder(12, new Color(200, 210, 218)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 30, 15, 30);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        row = addField(panel, gbc, row, "Aadhaar No:", aadhaarField = new JTextField());
        row = addField(panel, gbc, row, "Guardian Name:", guardianField = new JTextField());
        row = addField(panel, gbc, row, "Insurance Info:", insuranceField = new JTextField());

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel imageLabelTitle = new JLabel("Patient Photo:");
        imageLabelTitle.setForeground(new Color(70, 70, 70));
        imageLabelTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(imageLabelTitle, gbc);

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(180, 180));
        imageLabel.setBorder(new RoundedBorder(8, new Color(180, 190, 210)));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(imageLabel, gbc);

        gbc.gridy = ++row;
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        JButton browseBtn = new JButton("Upload Image");
        styleButton(browseBtn, new Color(0, 123, 255));
        browseBtn.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
        browseBtn.setPreferredSize(new Dimension(160, 42));
        browseBtn.addActionListener(e -> chooseImage());
        panel.add(browseBtn, gbc);

        gbc.gridy = ++row;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(30, 30, 10, 30);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton registerBtn = new JButton("Register Patient");
        styleButton(registerBtn, new Color(40, 167, 69));
        registerBtn.setPreferredSize(new Dimension(200, 52));
        registerBtn.addActionListener(e -> startRegistrationProcess());

        JButton backBtn = new JButton("Back");
        styleButton(backBtn, new Color(220, 53, 69));
        backBtn.setPreferredSize(new Dimension(120, 52));
        backBtn.addActionListener(e -> {
           
            dispose();

            new PatientMainDashboard().setVisible(true);
        });

        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);

        panel.add(buttonPanel, gbc);

        return panel;
    }

    private int addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent input) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(new Color(70, 70, 70));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        input.setPreferredSize(new Dimension(240, 32));
        input.setBackground(Color.WHITE);
        input.setForeground(new Color(50, 50, 50));
        input.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        if (input instanceof JTextField) {
            ((JTextField) input).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 210, 218)),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        } else if (input instanceof JComboBox) {
            input.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 218)));
        }
        panel.add(input, gbc);

        return row + 1;
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new RoundedBorder(12, color.darker()));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
    }

    private JTextField createDisabledField() {
        JTextField field = new JTextField();
        field.setEditable(false);
        field.setBackground(new Color(240, 240, 240));
        field.setForeground(new Color(130, 130, 130));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(240, 32));
        field.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        return field;
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = chooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(new ImageIcon(selectedImageFile.getAbsolutePath())
                    .getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH));
            imageLabel.setIcon(icon);
        }
    }

    private void generatePatientId() {
        String id = "PAT" + (10000 + new Random().nextInt(90000));
        patientIdField.setText(id);
    }

    private void startRegistrationProcess() {
        String aadhaar = aadhaarField.getText().trim();
        String email = emailField.getText().trim();

        if (isPatientExists(aadhaar, email)) {
            JOptionPane.showMessageDialog(this, "Patient already exists with this Aadhaar or Email.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        generatedOTP = generateOTP();
        if (sendEmail(email, generatedOTP)) {
            String enteredOTP = JOptionPane.showInputDialog(this, "An OTP has been sent to your email. Please enter the OTP:");
            if (enteredOTP != null && enteredOTP.equals(generatedOTP)) {
                String password = JOptionPane.showInputDialog(this, "OTP verified successfully. Please set your password:");
                if (password != null && !password.trim().isEmpty()) {
                    if (insertPatientData(password)) {
                        JOptionPane.showMessageDialog(this, "Patient registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        generatePatientId();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to register patient.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid OTP. Registration cancelled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to send OTP. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isPatientExists(String aadhaar, String email) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM patients WHERE aadhaar = ? OR email = ?")) {
            stmt.setString(1, aadhaar);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private boolean sendEmail(String recipient, String otp) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Your OTP for Patient Registration");
            message.setText("Your OTP is: " + otp);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean insertPatientData(String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO patients " +
                     "(patient_id, name, gender, age, dob, blood_group, contact, email, aadhaar, guardian, insurance, address, password, image_path) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setString(1, patientIdField.getText());
            stmt.setString(2, nameField.getText());
            stmt.setString(3, (String) genderCombo.getSelectedItem());
            stmt.setInt(4, Integer.parseInt(ageField.getText()));
            stmt.setDate(5, Date.valueOf(dobField.getText()));
            stmt.setString(6, (String) bloodGroupCombo.getSelectedItem());
            stmt.setString(7, contactField.getText());
            stmt.setString(8, emailField.getText());
            stmt.setString(9, aadhaarField.getText());
            stmt.setString(10, guardianField.getText());
            stmt.setString(11, insuranceField.getText());
            stmt.setString(12, addressArea.getText());
            stmt.setString(13, password);

            if (selectedImageFile != null) {
                stmt.setString(14, selectedImageFile.getAbsolutePath());
            } else {
                stmt.setNull(14, java.sql.Types.VARCHAR);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void clearForm() {
        nameField.setText("");
        genderCombo.setSelectedIndex(0);
        ageField.setText("");
        dobField.setText("");
        bloodGroupCombo.setSelectedIndex(0);
        contactField.setText("");
        emailField.setText("");
        aadhaarField.setText("");
        guardianField.setText("");
        insuranceField.setText("");
        addressArea.setText("");
        imageLabel.setIcon(null);
        selectedImageFile = null;
    }

    private void setLightThemeUI() {
        
        try {
           // UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color borderColor;

        RoundedBorder(int radius, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(borderColor);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius, radius);
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PatientRegistrationApp::new);
    }
}


