/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Patient;

import com.toedter.calendar.JDateChooser;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.sql.Date.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.Properties;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PatientAppointmentManager extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private String loggedInPatientId;
    private String loggedInPatientEmail;
    private JButton btnReschedule, btnCancel;
    
    public PatientAppointmentManager(String patientId) {
        this.loggedInPatientId = patientId;
        this.loggedInPatientEmail = fetchPatientEmail(patientId);
        
        setTitle("My Appointments");
        setSize(1100, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        model = new DefaultTableModel(new String[]{
            "Appointment ID", "Doctor ID", "Doctor Name", "Speciality",
            "Date", "Time", "Reason", "Status"
        }, 0);
        
        table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        
        JScrollPane scroll = new JScrollPane(table);
        
        
        btnReschedule = new JButton("Reschedule");
        btnCancel = new JButton("Cancel");
        
        Font btnFont = new Font("Arial", Font.BOLD, 14);
        btnReschedule.setFont(btnFont);
        btnCancel.setFont(btnFont);
        
        btnReschedule.setBackground(new Color(0, 123, 255));
        btnCancel.setBackground(new Color(220, 53, 69));
        btnReschedule.setForeground(Color.WHITE);
        btnCancel.setForeground(Color.WHITE);
        
        btnReschedule.setToolTipText("Reschedule selected appointment");
        btnCancel.setToolTipText("Cancel selected appointment");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.add(btnReschedule);
        buttonPanel.add(btnCancel);
        
        
        add(scroll, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        checkAndCancelPastAppointments();
        loadAppointments();
        
        btnCancel.addActionListener(e -> cancelAppointment());
        btnReschedule.addActionListener(e -> rescheduleAppointment());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        
        JButton btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setFocusPainted(false);
        btnBack.setBackground(Color.LIGHT_GRAY);
        btnBack.setForeground(Color.BLACK);
        btnBack.setPreferredSize(new Dimension(100, 30));
        btnBack.setToolTipText("Go back to the previous screen");
        
        btnBack.addActionListener(e -> {
            dispose(); // Closes current window
            new PatientDashboard(loggedInPatientId).setVisible(true);
        });
        
        topPanel.add(btnBack);
        add(topPanel, BorderLayout.NORTH);
        setVisible(true);
        
    }

    private String fetchPatientEmail(String patientId) {
        String email = "";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", ""); PreparedStatement ps = con.prepareStatement("SELECT email FROM patients WHERE patient_id = ?")) {
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }
    
    private void checkAndCancelPastAppointments() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", ""); PreparedStatement ps = con.prepareStatement(
                "UPDATE appointments SET status = 'Cancelled' WHERE patient_id = ? AND appointment_date < CURDATE() AND status = 'Scheduled'")) {
            ps.setString(1, loggedInPatientId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadAppointments() {
        model.setRowCount(0);
        String query = "SELECT a.appointment_id, a.doctor_id, d.name AS doctor_name, d.speciality, "
                + "a.appointment_date, a.appointment_time, a.reason, a.status "
                + "FROM appointments a JOIN doctor d ON a.doctor_id = d.doctor_id "
                + "WHERE a.patient_id = ?";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", ""); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, loggedInPatientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("appointment_id"),
                    rs.getString("doctor_id"),
                    rs.getString("doctor_name"),
                    rs.getString("speciality"),
                    rs.getDate("appointment_date"),
                    rs.getTime("appointment_time"),
                    rs.getString("reason"),
                    rs.getString("status")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load appointments.");
        }
    }
    
    private void cancelAppointment() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an appointment to cancel.");
            return;
        }
        
        String status = model.getValueAt(row, 7).toString();
        if (status.equalsIgnoreCase("Cancelled")) {
            JOptionPane.showMessageDialog(this, "Appointment is already cancelled.");
            return;
        }
        
        int id = (int) model.getValueAt(row, 0);
        String doctorId = model.getValueAt(row, 1).toString();
        String date = model.getValueAt(row, 4).toString();
        String time = model.getValueAt(row, 5).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this appointment?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", ""); PreparedStatement ps = con.prepareStatement("UPDATE appointments SET status = 'Cancelled' WHERE appointment_id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            generatePDFAndSend(id, doctorId, date, time, "Cancelled");
            loadAppointments();
            JOptionPane.showMessageDialog(this, "Appointment cancelled. Email sent.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void rescheduleAppointment() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an appointment to reschedule.");
            return;
        }
        
        String status = model.getValueAt(row, 7).toString();
        if (status.equalsIgnoreCase("Cancelled")) {
            JOptionPane.showMessageDialog(this, "Cancelled appointment cannot be rescheduled.");
            return;
        }
        
        int id = (int) model.getValueAt(row, 0);
        String doctorId = model.getValueAt(row, 1).toString();
        
        String newDate = JOptionPane.showInputDialog(this, "Enter new date (YYYY-MM-DD):");
        String newTime = JOptionPane.showInputDialog(this, "Enter new time (HH:MM:SS):");
        
        try {
            LocalDate inputDate = LocalDate.parse(newDate);
            LocalTime inputTime = LocalTime.parse(newTime);
            
            if (inputDate.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Cannot reschedule to a past date.");
                return;
            }
            
            if (inputTime.isBefore(LocalTime.of(9, 0)) || inputTime.isAfter(LocalTime.of(18, 0))) {
                JOptionPane.showMessageDialog(this, "Time must be between 09:00 and 18:00.");
                return;
            }
            
            if (!isTimeSlotAvailable(doctorId, newDate, newTime)) {
                JOptionPane.showMessageDialog(this, "Time slot not available.");
                return;
            }
            
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", ""); PreparedStatement ps = con.prepareStatement("UPDATE appointments SET appointment_date = ?, appointment_time = ?, status = 'Rescheduled' WHERE appointment_id = ?")) {
                ps.setString(1, newDate);
                ps.setString(2, newTime);
                ps.setInt(3, id);
                ps.executeUpdate();
                generatePDFAndSend(id, doctorId, newDate, newTime, "Rescheduled");
                loadAppointments();
                JOptionPane.showMessageDialog(this, "Appointment rescheduled. Email sent.");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date or time format.");
            e.printStackTrace();
        }
    }
    
    private boolean isTimeSlotAvailable(String doctorId, String date, String time) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", ""); PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? AND status != 'Cancelled'")) {
            ps.setString(1, doctorId);
            ps.setString(2, date);
            ps.setString(3, time);
            ResultSet rs = ps.executeQuery();
            return !rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void generatePDFAndSend(int appointmentId, String doctorId, String date, String time, String status) {
        try {
            String fileName = "Appointment_" + appointmentId + "_" + status + ".pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            document.add(new Paragraph("Appointment Details"));
            document.add(new Paragraph("Generated On: " + new Date()));
            document.add(new Paragraph("Patient ID: " + loggedInPatientId));
            document.add(new Paragraph("Appointment ID: " + appointmentId));
            document.add(new Paragraph("Doctor ID: " + doctorId));
            document.add(new Paragraph("Date: " + date));
            document.add(new Paragraph("Time: " + time));
            document.add(new Paragraph("Status: " + status));
            document.close();
            
            sendEmailWithPDF(fileName, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sendEmailWithPDF(String pdfFile, String status) {
        String to = loggedInPatientEmail;
        String from = "aroramayank488@gmail.com";
        String host = "smtp.gmail.com";
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "zojw kkfv hdyl etih"); // Use your app password
            }
        });
        
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("Appointment " + status + " Notification");
            
            Multipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Dear Patient,\n\nPlease find your appointment details attached.\n\nRegards,\nHospital Management");
            multipart.addBodyPart(textPart);
            
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(pdfFile));
            multipart.addBodyPart(attachmentPart);
            
            msg.setContent(multipart);
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new PatientAppointmentManager("PAT47341"); // Replace with valid patient_id
    }
}
