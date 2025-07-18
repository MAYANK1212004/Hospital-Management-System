/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doctor;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class AddMedicalRecord extends JFrame {

    private final String doctorId;
    private Connection conn;

    private JComboBox<String> comboPatients;
    private JTextField tfDiagnosis, tfTreatment;
    private JTextArea taNotes;
    private JButton btnSubmit, btnBack;
    private JLabel lblStatus;

    private final Map<String, String> displayToPatientId = new HashMap<>();
    private final Map<String, String> patientIdToEmail = new HashMap<>();

    public AddMedicalRecord(String doctorId) {
        this.doctorId = doctorId;
        setTitle("Add Medical Record");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        connectDB();
        initUI();
        loadConfirmedAppointments();
        setVisible(true);
    }

    private void connectDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "");
        } catch (SQLException e) {
            showError("Database connection failed: " + e.getMessage());
        }
    }

    private void initUI() {
        JLabel header = new JLabel("Add Medical Record", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setOpaque(true);
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(getWidth(), 60));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Select Patient:"), gbc);

        comboPatients = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(comboPatients, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Diagnosis:"), gbc);

        tfDiagnosis = new JTextField();
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(tfDiagnosis, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Treatment:"), gbc);

        tfTreatment = new JTextField();
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(tfTreatment, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Notes:"), gbc);

        taNotes = new JTextArea(5, 30);
        taNotes.setLineWrap(true);
        taNotes.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(taNotes);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        formPanel.add(scroll, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        bottomPanel.setBackground(Color.WHITE);

        btnSubmit = new JButton("Submit");
        btnSubmit.setPreferredSize(new Dimension(120, 40));
        btnSubmit.setBackground(new Color(46, 204, 113));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnBack = new JButton("Back");
        btnBack.setPreferredSize(new Dimension(120, 40));
        btnBack.setBackground(new Color(231, 76, 60));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));

        lblStatus = new JLabel(" ");
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setForeground(Color.RED);

        bottomPanel.add(btnSubmit);
        bottomPanel.add(btnBack);

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(Color.WHITE);
        south.add(bottomPanel, BorderLayout.CENTER);
        south.add(lblStatus, BorderLayout.SOUTH);

        add(south, BorderLayout.SOUTH);

        btnSubmit.addActionListener(e -> saveMedicalRecord());
        btnBack.addActionListener(e -> {
            dispose();
            new DoctorDashboard(doctorId); // Ensure this class exists
        });
    }

    private void loadConfirmedAppointments() {
        try {
            String today = LocalDate.now().toString();
            String sql = "SELECT a.patient_id, p.name, p.email FROM appointments a " +
                    "JOIN patients p ON a.patient_id = p.patient_id " +
                    "WHERE a.doctor_id = ? AND a.appointment_date = ? AND a.status = 'Confirmed' " +
                    "AND a.patient_id NOT IN (SELECT patient_id FROM medical_records WHERE doctor_id = ? AND record_date = ?)";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, doctorId);
            pst.setString(2, today);
            pst.setString(3, doctorId);
            pst.setString(4, today);
            ResultSet rs = pst.executeQuery();

            comboPatients.removeAllItems();
            displayToPatientId.clear();
            patientIdToEmail.clear();

            boolean hasRecords = false;

            while (rs.next()) {
                String pid = rs.getString("patient_id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                String display = pid + " - " + name;
                comboPatients.addItem(display);
                displayToPatientId.put(display, pid);
                patientIdToEmail.put(pid, email);
                hasRecords = true;
            }

            if (!hasRecords) {
                comboPatients.addItem("No eligible patients today");
                comboPatients.setEnabled(false);
                btnSubmit.setEnabled(false);
            } else {
                comboPatients.setEnabled(true);
                btnSubmit.setEnabled(true);
            }

        } catch (SQLException e) {
            showError("Error loading appointments: " + e.getMessage());
        }
    }

    private void saveMedicalRecord() {
        lblStatus.setText("");
        lblStatus.setForeground(Color.RED);

        String display = (String) comboPatients.getSelectedItem();
        if (display == null || !displayToPatientId.containsKey(display)) {
            lblStatus.setText("Please select a valid patient.");
            return;
        }

        String patientId = displayToPatientId.get(display);
        String diagnosis = tfDiagnosis.getText().trim();
        String treatment = tfTreatment.getText().trim();
        String notes = taNotes.getText().trim();

        if (diagnosis.isEmpty() || treatment.isEmpty()) {
            lblStatus.setText("Diagnosis and Treatment are required.");
            return;
        }

        try {
            String sql = "INSERT INTO medical_records (patient_id, doctor_id, diagnosis, treatment, notes, record_date) " +
                    "VALUES (?, ?, ?, ?, ?, CURDATE())";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, patientId);
            pst.setString(2, doctorId);
            pst.setString(3, diagnosis);
            pst.setString(4, treatment);
            pst.setString(5, notes.isEmpty() ? null : notes);

            int inserted = pst.executeUpdate();
            if (inserted > 0) {
                boolean emailSent = sendEmail(patientId, display, diagnosis, treatment, notes);
                lblStatus.setForeground(emailSent ? new Color(39, 174, 96) : Color.ORANGE);
                lblStatus.setText("Record saved " + (emailSent ? "and email sent!" : "but email failed."));
                comboPatients.removeItem(display);
                tfDiagnosis.setText("");
                tfTreatment.setText("");
                taNotes.setText("");
            } else {
                lblStatus.setText("Failed to save medical record.");
            }

        } catch (SQLException e) {
            showError("Error saving record: " + e.getMessage());
        }
    }

    private boolean sendEmail(String patientId, String display, String diagnosis, String treatment, String notes) {
        try {
            final String from = "aroramayank488@gmail.com"; // Replace with your email
            final String pass = "zojw kkfv hdyl etih";       // App-specific password
            String to = patientIdToEmail.get(patientId);

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, pass);
                }
            });

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, "Hospital System"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("Medical Record Added");

            msg.setText("Dear " + display + ",\n\n"
                    + "Your medical record has been added today.\n"
                    + "Diagnosis: " + diagnosis + "\n"
                    + "Treatment: " + treatment + "\n"
                    + (!notes.isEmpty() ? "Notes: " + notes + "\n" : "")
                    + "\nStay healthy!\nHospital Team");

            Transport.send(msg);
            return true;

        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
            return false;
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddMedicalRecord("DOC3247"));
    }
}

