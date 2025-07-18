/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.table.JTableHeader;

public class MedicalHistory extends JFrame {
    private final String patientId;
    private JTable table;

    public MedicalHistory(String patientId) {
        this.patientId = patientId;

        setTitle("🩺 Medical History - " + patientId);
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------- HEADER ----------
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("🩺 Medical History", JLabel.LEFT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel idLabel = new JLabel("Patient ID: " + patientId, JLabel.RIGHT);
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(idLabel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // ---------- TABLE ----------
        table = new JTable();
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        add(scrollPane, BorderLayout.CENTER);

        // ---------- BACK BUTTON ----------
        JButton backButton = new JButton("🔙 Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setBackground(new Color(244, 67, 54));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        backButton.addActionListener(e -> {
            dispose();
            new PatientDashboard(patientId);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // ---------- LOAD DATA ----------
        loadMedicalHistory();

        setVisible(true);
    }

    private void loadMedicalHistory() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "Record ID", "Doctor ID", "Diagnosis", "Treatment", "Notes", "Record Date"
        });

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hospital_db", "root", "");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT record_id, doctor_id, diagnosis, treatment, notes, record_date " +
                             "FROM medical_records WHERE patient_id = ? ORDER BY record_date DESC"
             )
        ) {
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("record_id"),
                        rs.getString("doctor_id"),
                        rs.getString("diagnosis"),
                        rs.getString("treatment"),
                        rs.getString("notes"),
                        rs.getDate("record_date")
                });
            }

            table.setModel(model);

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No medical records found for Patient ID: " + patientId,
                        "No Records", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(33, 150, 243));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 35));

        // Zebra striping for rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            Color evenColor = new Color(245, 245, 245);
            Color oddColor = Color.WHITE;

            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? evenColor : oddColor);
                } else {
                    c.setBackground(new Color(200, 230, 255));
                }
                return c;
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MedicalHistory("PAT88073"));
    }
}



      
