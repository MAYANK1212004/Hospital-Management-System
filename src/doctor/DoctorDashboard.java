/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doctor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class DoctorDashboard extends JFrame {

    private String doctorName = "Loading...";
    private String doctorId;
    private Connection conn;
    private JLabel header;

    public DoctorDashboard(String doctorId) {
        this.doctorId = doctorId;

        setTitle("Doctor Dashboard - Hospital Management System");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initial placeholder header
        header = new JLabel("Welcome Dr. " + doctorName, SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setOpaque(true);
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(getWidth(), 70));
        add(header, BorderLayout.NORTH);

        // Run DB logic in background
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                connectDB();
                fetchDoctorName();
                return null;
            }

            @Override
            protected void done() {
                header.setText("Welcome Dr. " + doctorName);
                buildDashboard(); // UI continues after DB operation
            }
        }.execute();

        setVisible(true);
    }

    private void buildDashboard() {
        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(52, 73, 94));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        JButton btnLogout = createLogoutButton("Logout");
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(btnLogout);
        menuPanel.add(Box.createVerticalGlue());
        add(menuPanel, BorderLayout.WEST);

        // Content Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;

        String[] buttonLabels = {
            "View Appointments",
            "Confirm Appointment",
            "Add Medical Record",
            "Patient History",
            "Prescribe Medicine",
            "Update Profile"
        };

        JButton[] buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = createMainButton(buttonLabels[i]);
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            contentPanel.add(buttons[i], gbc);
        }

        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();

        // Button Actions
        buttons[0].addActionListener(e -> {
            dispose();
            new ViewDoctorAppointmentsModern(doctorId).setVisible(true);
        });
        buttons[1].addActionListener(e -> {
            dispose();
            new DoctorConfirmAppointment(doctorId);
        });
        buttons[2].addActionListener(e -> {
            dispose();
            new AddMedicalRecord(doctorId).setVisible(true);
        });
        buttons[3].addActionListener(e -> {
            dispose();
            new DoctorPatientHistoryBySelection(doctorId);
        });
        buttons[4].addActionListener(e -> {
            dispose();
            new AddPrescriptionForConfirmedPatients(doctorId);
        });
        buttons[5].addActionListener(e -> {
            dispose();
            new UpdateDoctorProfile(doctorId);
        });

        btnLogout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new homepage.HospitalHomePage().setVisible(true);
            }
        });
    }

    private void connectDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fetchDoctorName() {
        try (PreparedStatement pst = conn.prepareStatement("SELECT name FROM doctor WHERE doctor_id = ?")) {
            pst.setString(1, doctorId);
            ResultSet rs = pst.executeQuery();
            doctorName = rs.next() ? rs.getString("name") : "Unknown";
        } catch (SQLException e) {
            doctorName = "Unknown";
            e.printStackTrace();
        }
    }

    private JButton createMainButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(250, 80));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 230, 240));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
        return button;
    }

    private JButton createLogoutButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(192, 57, 43));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(231, 76, 60));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(192, 57, 43));
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DoctorDashboard("DOC1134"));
    }
}
