

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doctor;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AddPrescriptionForConfirmedPatients extends JFrame {
    private JComboBox<String> cbPatients, cbMedicines;
    private JTextField tfDosage, tfQuantity;
    private JButton btnPrescribe, btnBack;
    private JLabel lblStatus;  // For feedback messages

    private String doctorId;
    private Map<String, String> patientMap = new HashMap<>();
    private Map<String, Integer> appointmentMap = new HashMap<>();
    private Map<String, Integer> medicineMap = new HashMap<>();

    public AddPrescriptionForConfirmedPatients(String doctorId) {
        this.doctorId = doctorId;

        setTitle("Add Prescription for Confirmed Patients");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Prescribe Medicine");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(130, 10, 300, 30);
        add(lblTitle);

        JLabel lblPatient = new JLabel("Confirmed Patient:");
        lblPatient.setBounds(50, 60, 150, 25);
        add(lblPatient);

        cbPatients = new JComboBox<>();
        cbPatients.setBounds(200, 60, 220, 25);
        add(cbPatients);

        JLabel lblMedicine = new JLabel("Select Medicine:");
        lblMedicine.setBounds(50, 100, 150, 25);
        add(lblMedicine);

        cbMedicines = new JComboBox<>();
        cbMedicines.setBounds(200, 100, 220, 25);
        add(cbMedicines);

        JLabel lblDosage = new JLabel("Dosage:");
        lblDosage.setBounds(50, 140, 150, 25);
        add(lblDosage);

        tfDosage = new JTextField();
        tfDosage.setBounds(200, 140, 220, 25);
        add(tfDosage);

        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setBounds(50, 180, 150, 25);
        add(lblQuantity);

        tfQuantity = new JTextField();
        tfQuantity.setBounds(200, 180, 220, 25);
        add(tfQuantity);

        btnPrescribe = new JButton("Prescribe");
        btnPrescribe.setBounds(120, 250, 120, 35);
        btnPrescribe.setBackground(new Color(0, 128, 0));
        btnPrescribe.setForeground(Color.WHITE);
        add(btnPrescribe);

        btnBack = new JButton("Back");
        btnBack.setBounds(260, 250, 120, 35);
        btnBack.setBackground(new Color(178, 34, 34));
        btnBack.setForeground(Color.WHITE);
        add(btnBack);

        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setForeground(Color.RED);
        lblStatus.setBounds(50, 300, 400, 25);
        add(lblStatus);

        btnPrescribe.addActionListener(e -> prescribe());
        btnBack.addActionListener(e -> {
            dispose();
            new DoctorDashboard(doctorId).setVisible(true);
        });

        loadConfirmedPatients();
        loadMedicines();

        setVisible(true);
    }

    private void loadConfirmedPatients() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "")) {
            String query = "SELECT a.appointment_id, p.patient_id, p.name FROM appointments a " +
                    "JOIN patients p ON a.patient_id = p.patient_id " +
                    "WHERE a.doctor_id = ? AND a.status = 'Confirmed'";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, doctorId);
            ResultSet rs = pst.executeQuery();

            cbPatients.removeAllItems();
            patientMap.clear();
            appointmentMap.clear();

            while (rs.next()) {
                String patientName = rs.getString("name");
                String patientId = rs.getString("patient_id");
                int appointmentId = rs.getInt("appointment_id");

                String label = patientName + " (ID: " + patientId + ")";
                cbPatients.addItem(label);

                patientMap.put(label, patientId);
                appointmentMap.put(label, appointmentId);
            }

            if (cbPatients.getItemCount() == 0) {
                lblStatus.setText("No confirmed patients found.");
                btnPrescribe.setEnabled(false);
            } else {
                lblStatus.setText("");
                btnPrescribe.setEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading confirmed patients.");
        }
    }

    private void loadMedicines() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "")) {
            String query = "SELECT medicine_id, name, quantity, expiry_date FROM medicines WHERE expiry_date >= CURDATE() AND quantity > 0 ORDER BY name";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            cbMedicines.removeAllItems();
            medicineMap.clear();

            while (rs.next()) {
                int medicineId = rs.getInt("medicine_id");
                String medicineName = rs.getString("name");
                int qty = rs.getInt("quantity");

                String label = qty < 10 ? medicineName + " ⚠ (" + qty + " left)" : medicineName;
                cbMedicines.addItem(label);
                medicineMap.put(label, medicineId);
            }

            if (cbMedicines.getItemCount() == 0) {
                lblStatus.setText("No medicines available.");
                btnPrescribe.setEnabled(false);
            } else {
                lblStatus.setText("");
                btnPrescribe.setEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading medicines.");
        }
    }

    private void prescribe() {
        lblStatus.setForeground(Color.RED); // default to error color

        String selectedPatient = (String) cbPatients.getSelectedItem();
        String selectedMedicine = (String) cbMedicines.getSelectedItem();
        String dosage = tfDosage.getText().trim();
        String quantityStr = tfQuantity.getText().trim();

        if (selectedPatient == null || selectedMedicine == null || dosage.isEmpty() || quantityStr.isEmpty()) {
            lblStatus.setText("All fields are required.");
            return;
        }

        // Basic dosage validation - pattern like "1-0-1" or "0-0-1"
        if (!dosage.matches("[0-9]-[0-9]-[0-9]")) {
            lblStatus.setText("Dosage must be in format '1-0-1'.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                lblStatus.setText("Quantity must be a positive integer.");
                return;
            }
        } catch (NumberFormatException e) {
            lblStatus.setText("Quantity must be a valid number.");
            return;
        }

        try {
            String patientId = patientMap.get(selectedPatient);
            int appointmentId = appointmentMap.get(selectedPatient);
            int medicineId = medicineMap.get(selectedMedicine);

            LocalDate date = LocalDate.now();

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "")) {
                // Check stock
                String stockQuery = "SELECT quantity FROM medicines WHERE medicine_id = ?";
                PreparedStatement stockStmt = con.prepareStatement(stockQuery);
                stockStmt.setInt(1, medicineId);
                ResultSet stockRs = stockStmt.executeQuery();

                if (stockRs.next()) {
                    int availableStock = stockRs.getInt("quantity");

                    if (quantity > availableStock) {
                        lblStatus.setText("Prescribed quantity (" + quantity + ") exceeds available stock (" + availableStock + ").");
                        return; // Exit without inserting
                    }

                    // Insert prescription (NO stock update)
                    String insertQuery = "INSERT INTO prescriptions (appointment_id, patient_id, doctor_id, medicine_id, prescription_date, dosage, quantity, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, 'Pending')";
                    PreparedStatement pst = con.prepareStatement(insertQuery);
                    pst.setInt(1, appointmentId);
                    pst.setString(2, patientId);
                    pst.setString(3, doctorId);
                    pst.setInt(4, medicineId);
                    pst.setDate(5, Date.valueOf(date));
                    pst.setString(6, dosage);
                    pst.setInt(7, quantity);

                    int result = pst.executeUpdate();
                    if (result > 0) {
                        lblStatus.setForeground(new Color(0, 128, 0)); // success color
                        lblStatus.setText("Prescription saved successfully.");
                        tfDosage.setText("");
                        tfQuantity.setText("");
                    } else {
                        lblStatus.setText("Failed to save prescription.");
                    }

                } else {
                    lblStatus.setText("Medicine not found.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            lblStatus.setText("Error saving prescription.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddPrescriptionForConfirmedPatients("DOC1134"));
    }
}



