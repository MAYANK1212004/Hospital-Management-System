/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class DoctorPatientHistoryBySelection extends JFrame {

    private JComboBox<String> patientComboBox;
    private DefaultTableModel model;
    private JTable table;
    private JButton backBtn;
    private String doctorId;

    public DoctorPatientHistoryBySelection(String doctorId) {
        this.doctorId = doctorId;

        setTitle("Patient History by Doctor");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
        loadPatients();

        setVisible(true);
    }

    private void initUI() {
    // Top Panel with BorderLayout
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setBackground(new Color(52, 152, 219));
    topPanel.setPreferredSize(new Dimension(getWidth(), 90));

    // Back button directly added to WEST with padding via EmptyBorder
    backBtn = new JButton("← Back");
    backBtn.setFocusPainted(false);
    backBtn.setBackground(new Color(41, 128, 185));
    backBtn.setForeground(Color.WHITE);
    backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    backBtn.setPreferredSize(new Dimension(90, 35));
    backBtn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    backBtn.addActionListener(e -> {
        dispose(); // Close this window
        new DoctorDashboard(doctorId).setVisible(true);
    });
    topPanel.add(backBtn, BorderLayout.WEST);

    // Title label centered
    JLabel titleLabel = new JLabel("Select Patient to View Medical History", JLabel.CENTER);
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
    topPanel.add(titleLabel, BorderLayout.CENTER);

    // Patient combo panel at bottom of top panel
    JPanel comboPanel = new JPanel();
    comboPanel.setBackground(new Color(52, 152, 219));
    comboPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
    JLabel patientLabel = new JLabel("Patient ID: ");
    patientLabel.setForeground(Color.WHITE);
    patientLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    comboPanel.add(patientLabel);

    patientComboBox = new JComboBox<>();
    patientComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    patientComboBox.setPreferredSize(new Dimension(250, 35));
    patientComboBox.addActionListener(e -> {
        if (patientComboBox.getSelectedIndex() > 0) {
            String patientId = (String) patientComboBox.getSelectedItem();
            fetchMedicalHistory(patientId);
        } else {
            model.setRowCount(0);
        }
    });
    comboPanel.add(patientComboBox);

    topPanel.add(comboPanel, BorderLayout.SOUTH);

    add(topPanel, BorderLayout.NORTH);

        // Table setup
        model = new DefaultTableModel(new String[]{
                "Record ID", "Diagnosis", "Treatment", "Notes", "Record Date", "Created At"
        }, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadPatients() {
        patientComboBox.removeAllItems();

        // Add placeholder as first item to force user selection
        patientComboBox.addItem("-- Select Patient --");

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT DISTINCT patient_id FROM medical_records WHERE doctor_id = ? ORDER BY patient_id"
             )) {
            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();

            boolean hasPatients = false;
            while (rs.next()) {
                String patientId = rs.getString("patient_id");
                patientComboBox.addItem(patientId);
                hasPatients = true;
            }

            if (!hasPatients) {
                JOptionPane.showMessageDialog(this, "No patients found for this doctor.");
                patientComboBox.setEnabled(false);
            } else {
                patientComboBox.setEnabled(true);
                patientComboBox.setSelectedIndex(0); // select placeholder
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patients: " + ex.getMessage());
        }
    }

    private void fetchMedicalHistory(String patientId) {
        model.setRowCount(0); // clear previous rows

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT record_id, diagnosis, treatment, notes, record_date, created_at " +
                             "FROM medical_records WHERE doctor_id = ? AND patient_id = ? ORDER BY record_date DESC, created_at DESC"
             )) {
            ps.setString(1, doctorId);
            ps.setString(2, patientId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("record_id"));
                row.add(rs.getString("diagnosis"));
                row.add(rs.getString("treatment"));
                row.add(rs.getString("notes"));
                row.add(rs.getDate("record_date"));
                row.add(rs.getTimestamp("created_at"));
                model.addRow(row);
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No medical records found for patient: " + patientId);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching medical history: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DoctorPatientHistoryBySelection("DOC1134"));
    }
}
