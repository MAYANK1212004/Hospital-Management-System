/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Patient;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewPatientProfile extends JFrame {

    private final String patientId;
    private Connection conn;

    public ViewPatientProfile(String patientId) {
        this.patientId = patientId;
        setTitle("Patient Profile - " + patientId);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Header
        // Header with Back Button
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 150, 243));
        header.setPreferredSize(new Dimension(0, 70));

// Sub-panel to hold back button + title
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        leftHeaderPanel.setOpaque(false);

// Back Button
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setBackground(new Color(240, 240, 240));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

// Hover effect (optional)
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(220, 220, 220));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(240, 240, 240));
            }
        });

// Action: Close the current window
        backButton.addActionListener(e ->{ 
            dispose();
            new PatientDashboard(patientId).setVisible(true);
        });

// Title Label
        JLabel titleLabel = new JLabel("👤 Patient Profile");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        leftHeaderPanel.add(backButton);
        leftHeaderPanel.add(titleLabel);

        header.add(leftHeaderPanel, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Center Panel for Patient Details
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // Labels and Fields
        String[] labels = {
            "Patient ID", "Name", "Gender", "Age", "Date of Birth", "Blood Group", "Contact",
            "Email", "Address", "Aadhaar", "Guardian", "Insurance"
        };
        JLabel[] labelComponents = new JLabel[labels.length];
        JTextField[] valueComponents = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            labelComponents[i] = new JLabel(labels[i] + ": ");
            labelComponents[i].setFont(new Font("Segoe UI", Font.BOLD, 16));
            contentPanel.add(labelComponents[i], gbc);

            gbc.gridx = 1;
            valueComponents[i] = new JTextField(30);
            valueComponents[i].setFont(new Font("Segoe UI", Font.PLAIN, 16));
            valueComponents[i].setEditable(false);
            contentPanel.add(valueComponents[i], gbc);
        }

        // Image on the right
        JLabel profileImage = new JLabel();
        profileImage.setPreferredSize(new Dimension(180, 180));
        profileImage.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        contentPanel.add(profileImage, gbc);
        gbc.gridheight = 1;

        // Add scroll pane for better responsiveness
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel();
        footer.setPreferredSize(new Dimension(0, 40));
        footer.setBackground(new Color(240, 240, 240));
        JLabel footerLabel = new JLabel("© 2025 HealthCare System");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        // Load Data
        try {
            connectDB();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM patients WHERE patient_id = ?");
            stmt.setString(1, patientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                valueComponents[0].setText(rs.getString("patient_id"));
                valueComponents[1].setText(rs.getString("name"));
                valueComponents[2].setText(rs.getString("gender"));
                valueComponents[3].setText(String.valueOf(rs.getInt("age")));
                valueComponents[4].setText(rs.getString("dob"));
                valueComponents[5].setText(rs.getString("blood_group"));
                valueComponents[6].setText(rs.getString("contact"));
                valueComponents[7].setText(rs.getString("email"));
                valueComponents[8].setText(rs.getString("address"));
                valueComponents[9].setText(rs.getString("aadhaar"));
                valueComponents[10].setText(rs.getString("guardian"));
                valueComponents[11].setText(rs.getString("insurance"));

                String imagePath = rs.getString("image_path");
                if (imagePath != null && !imagePath.isEmpty()) {
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                    profileImage.setIcon(new ImageIcon(img));
                } else {
                    profileImage.setIcon(new ImageIcon(new ImageIcon("default-profile.png")
                            .getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Patient record not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading patient data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);
    }

    private void connectDB() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewPatientProfile("PAT1234"));
    }
}
