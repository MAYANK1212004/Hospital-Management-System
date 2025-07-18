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

public class BookPatientAppointment extends JFrame {

    JTextField tfPatientName, tfEmail;
    JTextArea tfReason;
    JComboBox<String> specialityComboBox, doctorComboBox, timeSlotBox;
    JDateChooser dateChooser;
    JButton btnBook, btnDownload, btnBack;
    JLabel statusLabel, lblImage;
    String generatedPDFPath = "", patientEmail = "", loggedInPatientID;

    Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
    Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

    public BookPatientAppointment(String patientID) {
        this.loggedInPatientID = patientID;
        setTitle("📅 Book Appointment | Hospital System");
        setSize(1000, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);

        // Left Panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("👤 Patient Details"));
        leftPanel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfPatientName = new JTextField(); tfPatientName.setEditable(false);
        tfEmail = new JTextField(); tfEmail.setEditable(false);
        tfReason = new JTextArea();
        specialityComboBox = new JComboBox<>();
        doctorComboBox = new JComboBox<>();
        dateChooser = new JDateChooser(); dateChooser.setMinSelectableDate(new Date());
        timeSlotBox = new JComboBox<>(new String[]{"09:00", "10:00", "11:00", "12:00", "14:00", "15:00"});

        lblImage = new JLabel("No Image", SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(180, 180));
        lblImage.setBorder(BorderFactory.createTitledBorder("Photo"));

        String[] labels = {"Name:", "Email:", "Speciality:", "Doctor:", "Date:", "Time:"};
        JComponent[] fields = {tfPatientName, tfEmail, specialityComboBox, doctorComboBox, dateChooser, timeSlotBox};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            leftPanel.add(createLabel(labels[i]), gbc);
            gbc.gridx = 1;
            leftPanel.add(fields[i], gbc);
        }

        gbc.gridx = 0; gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        leftPanel.add(lblImage, gbc);

        // Right Panel
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createTitledBorder("📋 Appointment Details"));
        rightPanel.setBackground(new Color(250, 250, 250));

        JPanel reasonPanel = new JPanel(new BorderLayout(5, 5));
        reasonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblReason = new JLabel("Reason for Visit:");
        lblReason.setFont(labelFont);
        lblReason.setForeground(new Color(50, 50, 50));

        tfReason.setFont(fieldFont);
        tfReason.setBackground(new Color(245, 245, 245));
        tfReason.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        tfReason.setLineWrap(true);
        tfReason.setWrapStyleWord(true);

        reasonPanel.add(lblReason, BorderLayout.NORTH);
        reasonPanel.add(new JScrollPane(tfReason), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnBook = new JButton("📥 Book");
        btnDownload = new JButton("📄 PDF");
        btnBack = new JButton("🔙 Back");

        btnDownload.setEnabled(false);

        styleButton(btnBook);
        styleButton(btnDownload);
        styleButton(btnBack);

        buttonPanel.add(btnBook);
        buttonPanel.add(btnDownload);
        buttonPanel.add(btnBack);

        rightPanel.add(reasonPanel, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Combine Panels
        splitPane.setLeftComponent(new JScrollPane(leftPanel));
        splitPane.setRightComponent(new JScrollPane(rightPanel));

        // Status Label
        statusLabel = new JLabel(" ", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(Color.BLUE);

        add(splitPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // Event Handlers
        btnBook.addActionListener(e -> bookAppointment());
        btnDownload.addActionListener(e -> openPDF());
        btnBack.addActionListener(e -> {
            dispose();
            new PatientDashboard(patientID).setVisible(true);
        });

        specialityComboBox.addActionListener(e -> loadDoctorsBySpeciality());

        // Load Data
        autoFillPatientDetails();
        populateSpecialities();

        setVisible(true);
    }

    void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }

    JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(labelFont);
        return lbl;
    }

    void autoFillPatientDetails() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hospital_db", "root", "")) {
            PreparedStatement stmt = con.prepareStatement("SELECT name, email, image_path FROM patients WHERE patient_id=?");
            stmt.setString(1, loggedInPatientID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tfPatientName.setText(rs.getString("name"));
                tfEmail.setText(rs.getString("email"));
                patientEmail = rs.getString("email");
                String imagePath = rs.getString("image_path");
                if (imagePath != null && !imagePath.isEmpty()) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH));
                    lblImage.setIcon(icon);
                    lblImage.setText(null);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    void populateSpecialities() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hospital_db", "root", "")) {
            PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT speciality FROM doctor");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                specialityComboBox.addItem(rs.getString("speciality"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void loadDoctorsBySpeciality() {
        doctorComboBox.removeAllItems();
        String selectedSpeciality = (String) specialityComboBox.getSelectedItem();
        if (selectedSpeciality == null) return;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hospital_db", "root", "")) {
            PreparedStatement stmt = con.prepareStatement("SELECT doctor_id, name FROM doctor WHERE speciality = ?");
            stmt.setString(1, selectedSpeciality);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                doctorComboBox.addItem(rs.getString("doctor_id") + " - " + rs.getString("name"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void bookAppointment() {
        String selected = (String) doctorComboBox.getSelectedItem();
        if (selected == null || !selected.contains(" - ")) return;

        String doctorID = selected.split(" - ")[0].trim();
        Date date = dateChooser.getDate();
        String time = (String) timeSlotBox.getSelectedItem();
        String reason = tfReason.getText().trim();

        if (date == null || reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all required fields.");
            return;
        }

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hospital_db", "root", "")) {
            PreparedStatement checkStmt = con.prepareStatement("SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=? AND appointment_time=?");
            checkStmt.setString(1, doctorID);
            checkStmt.setString(2, formattedDate);
            checkStmt.setString(3, time);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) >= 1) {
                JOptionPane.showMessageDialog(this, "This time slot is already booked.");
                return;
            }

            PreparedStatement insert = con.prepareStatement("INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, reason, status) VALUES (?, ?, ?, ?, ?, 'Scheduled')", Statement.RETURN_GENERATED_KEYS);
            insert.setString(1, loggedInPatientID);
            insert.setString(2, doctorID);
            insert.setString(3, formattedDate);
            insert.setString(4, time);
            insert.setString(5, reason);
            insert.executeUpdate();

            ResultSet keys = insert.getGeneratedKeys();
            keys.next();
            int appID = keys.getInt(1);

            generatedPDFPath = generatePDF(appID, loggedInPatientID, tfPatientName.getText(), formattedDate, time, reason, doctorID);
            sendEmail(generatedPDFPath);
            btnDownload.setEnabled(true);
            statusLabel.setText("✔ Appointment booked & emailed!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    String generatePDF(int appID, String pid, String name, String date, String time, String reason, String doctorID) throws Exception {
        String filePath = "Appointment_" + appID + ".pdf";
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(filePath));
        doc.open();

        Paragraph title = new Paragraph("Hospital Appointment Slip", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLUE));
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        String[][] data = {
            {"Appointment ID:", String.valueOf(appID)},
            {"Patient ID:", pid},
            {"Name:", name},
            {"Doctor ID:", doctorID},
            {"Date:", date},
            {"Time:", time},
            {"Reason:", reason}
        };

        for (String[] row : data) {
            PdfPCell c1 = new PdfPCell(new Phrase(row[0]));
            PdfPCell c2 = new PdfPCell(new Phrase(row[1]));
            c1.setPadding(5); c2.setPadding(5);
            table.addCell(c1); table.addCell(c2);
        }

        doc.add(table);
        doc.close();
        return filePath;
    }

    void sendEmail(String filePath) {
        String from = "aroramayank488@gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "zojw kkfv hdyl etih"); // App Password
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(patientEmail));
            msg.setSubject("Appointment Slip");

            BodyPart textPart = new MimeBodyPart();
            textPart.setText("Attached is your appointment slip.");

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setDataHandler(new DataHandler(new FileDataSource(filePath)));
            attachment.setFileName(filePath);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachment);

            msg.setContent(multipart);
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send email.");
        }
    }

    void openPDF() {
        try {
            Desktop.getDesktop().open(new File(generatedPDFPath));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cannot open PDF.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookPatientAppointment("PAT88073"));
    }
}