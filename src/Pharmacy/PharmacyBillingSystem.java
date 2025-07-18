/*
 Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Pharmacy;

/*
 * PharmacyBillingSystem.java  –  July 2025
 * Advanced pharmacy dispensing & billing screen (no “Print Bill” button)
 *
 * Requires: itextpdf‑5.5.x, Jakarta Mail 2.x
 */


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Font;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class PharmacyBillingSystem extends JFrame {

    // ───── DB CONFIG ────────────────────────────────────────────────────────
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    // ───── UI FIELDS ────────────────────────────────────────────────────────
    private final DefaultTableModel model;
    private final JTable   table;
    private final JComboBox<String> patientDropdown;
    private final JLabel  lblTotal;

    private final JButton btnDispenseAndBill, btnRefresh, btnBack; // ← no btnPrintBill
    private Connection    con;
    private String        currentBillFilePath;

    // ───── CONSTRUCTOR ──────────────────────────────────────────────────────
    public PharmacyBillingSystem() {
        super("Pharmacy – Dispense & Bill Prescriptions");
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        connect();

        // Header
        JLabel header = new JLabel("Pharmacy Dispensing & Billing System", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(new Color(33, 47, 61));
        header.setBorder(new EmptyBorder(20, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        // Top panel
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        top.setBackground(Color.WHITE);
        top.setBorder(new EmptyBorder(5, 10, 5, 10));

        patientDropdown = new JComboBox<>();
        patientDropdown.setPreferredSize(new Dimension(180, 28));
        loadPendingPatients();

        btnRefresh = createButton("Refresh", new Color(23, 162, 184));
        btnRefresh.addActionListener(e -> {
            loadPendingPatients();
            loadPrescriptions();
        });

        btnDispenseAndBill = createButton("Dispense, Generate Bill & Email", new Color(34, 153, 84));
        btnDispenseAndBill.addActionListener(e -> runDispenseJob());

        btnBack = createButton("Back", new Color(220, 53, 69));
        btnBack.addActionListener(e -> {
            dispose();
            new PharmacyDashboardModern().setVisible(true);
        });

        top.add(new JLabel("Patient ID:"));
        top.add(patientDropdown);
        top.add(btnRefresh);
        top.add(btnDispenseAndBill);
        top.add(btnBack);                       // ← btnPrintBill removed
        add(top, BorderLayout.BEFORE_FIRST_LINE);

        // Table
        model = new DefaultTableModel(new String[]{
                "Prescription ID", "Medicine", "Dosage", "Qty",
                "Price (₹)", "Amount (₹)", "Available Stock", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionBackground(new Color(174, 214, 241));

        // highlight if qty > stock
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable tbl, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, row, col);
                int qty   = (int) model.getValueAt(row, 3);
                int stock = (int) model.getValueAt(row, 6);
                c.setBackground(qty > stock ? new Color(255, 204, 204)
                                            : isSel ? tbl.getSelectionBackground() : Color.WHITE);
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(0, 10, 0, 10));
        add(scroll, BorderLayout.CENTER);

        // Bottom – Total
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 8));
        bottom.setBackground(Color.WHITE);
        lblTotal = new JLabel("Total: ₹0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bottom.add(lblTotal);
        add(bottom, BorderLayout.SOUTH);

        patientDropdown.addActionListener(e -> loadPrescriptions());
        loadPrescriptions();

        setVisible(true);
    }

    // ───── HELPERS ──────────────────────────────────────────────────────────
    private JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return b;
    }

    private void connect() {
        try { con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS); }
        catch (SQLException e) { showErr("DB Error: " + e.getMessage()); }
    }

    // ───── DATA LOADERS ─────────────────────────────────────────────────────
    private void loadPendingPatients() {
        patientDropdown.removeAllItems();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT DISTINCT patient_id FROM prescriptions WHERE status='Pending'")) {
            while (rs.next()) patientDropdown.addItem(rs.getString(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadPrescriptions() {
        model.setRowCount(0);
        String pid = (String) patientDropdown.getSelectedItem();
        if (pid == null) return;

        String sql = """
            SELECT p.prescription_id, m.name, p.dosage, p.quantity,
                   m.price, m.quantity AS stock, p.status
            FROM prescriptions p
            JOIN medicines m ON p.medicine_id = m.medicine_id
            WHERE p.patient_id=? AND p.status='Pending'""";

        double total = 0;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int qty   = rs.getInt("quantity");
                    double pr = rs.getDouble("price");
                    double amt = qty * pr;
                    total += amt;

                    model.addRow(new Object[]{
                            rs.getInt("prescription_id"),
                            rs.getString("name"),
                            rs.getString("dosage"),
                            qty, pr, amt,
                            rs.getInt("stock"),
                            rs.getString("status")});
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }

        lblTotal.setText("Total: ₹" + String.format("%.2f", total));
        btnDispenseAndBill.setEnabled(model.getRowCount() > 0);
        currentBillFilePath = null;    // reset
    }

    // ───── DISPENSE WORKFLOW ────────────────────────────────────────────────
    private void runDispenseJob() {
        String pid = (String) patientDropdown.getSelectedItem();
        if (pid == null || model.getRowCount() == 0) return;

        // Stock check
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < model.getRowCount(); i++) {
            int qty   = (int) model.getValueAt(i, 3);
            int stock = (int) model.getValueAt(i, 6);
            if (qty > stock) {
                sb.append("• ").append(model.getValueAt(i, 1))
                  .append(" – need ").append(qty)
                  .append(", have ").append(stock).append("\n");
            }
        }
        if (sb.length() > 0) {
            showErr("Dispense aborted – insufficient stock:\n" + sb);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Proceed to dispense, generate bill, and email?",
                "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

        JDialog busy = createBusyDialog();
        SwingWorker<Void, Void> w = new SwingWorker<>() {
            @Override protected Void doInBackground() {
                try { dispenseAndBill(pid); }
                catch (Exception e) { showErr("Error: " + e.getMessage()); }
                return null;
            }
            @Override protected void done() { busy.dispose(); }
        };
        w.execute();
        busy.setVisible(true);
    }

    private void dispenseAndBill(String pid) throws Exception {

        con.setAutoCommit(false);
        try {
            // Update stock & status
            String qMed = "SELECT medicine_id FROM prescriptions WHERE prescription_id=?";
            String uMed = "UPDATE medicines SET quantity=quantity-? WHERE medicine_id=?";
            String uPre = "UPDATE prescriptions SET status='Dispensed' WHERE prescription_id=?";

            try (PreparedStatement psQMed = con.prepareStatement(qMed);
                 PreparedStatement psUMed = con.prepareStatement(uMed);
                 PreparedStatement psUPre = con.prepareStatement(uPre)) {

                for (int i = 0; i < model.getRowCount(); i++) {
                    int presId = (int) model.getValueAt(i, 0);
                    int qty    = (int) model.getValueAt(i, 3);

                    psQMed.setInt(1, presId);
                    int medId;
                    try (ResultSet r = psQMed.executeQuery()) {
                        if (!r.next()) continue;
                        medId = r.getInt(1);
                    }
                    psUMed.setInt(1, qty); psUMed.setInt(2, medId); psUMed.executeUpdate();
                    psUPre.setInt(1, presId);                     psUPre.executeUpdate();
                }
            }

            // PDF generation
            currentBillFilePath = generateBillPDF(pid);

            // Email
            sendEmail(pid);

            con.commit();
            JOptionPane.showMessageDialog(this, "Dispensed & emailed successfully!");

            if (Desktop.isDesktopSupported())
                Desktop.getDesktop().open(new File(currentBillFilePath));

            loadPrescriptions();
            loadPendingPatients();

        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally { con.setAutoCommit(true); }
    }

    // ───── PDF ──────────────────────────────────────────────────────────────
    private String generateBillPDF(String pid) throws Exception {

        double total = 0;
        String filename = "Bill_" + pid + "_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filename));
            doc.open();

            Paragraph title = new Paragraph("Prescription Bill – Patient: " + pid,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD,
                                     new BaseColor(33, 47, 61)));
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("Date: " +
                new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(new Date())));
            doc.add(Chunk.NEWLINE);

            PdfPTable pdfTable = new PdfPTable(6);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{10, 35, 20, 10, 15, 20});

            for (String col : new String[]{"ID", "Medicine", "Dosage", "Qty",
                                           "Price (₹)", "Amount (₹)"}) {
                PdfPCell cell = new PdfPCell(new Phrase(col));
                cell.setBackgroundColor(new BaseColor(230, 230, 250));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < 6; j++) {
                    Object v = model.getValueAt(i, j);
                    PdfPCell cell = new PdfPCell(new Phrase(v.toString()));
                    cell.setHorizontalAlignment((j==1||j==2) ? Element.ALIGN_LEFT
                                                              : Element.ALIGN_CENTER);
                    pdfTable.addCell(cell);
                }
                total += Double.parseDouble(model.getValueAt(i, 5).toString());
            }

            PdfPCell totalCell = new PdfPCell(new Phrase("Total: ₹" + String.format("%.2f", total)));
            totalCell.setColspan(6);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalCell.setPaddingTop(8);
            pdfTable.addCell(totalCell);

            doc.add(pdfTable);
        } finally { doc.close(); }

        return new File(filename).getAbsolutePath();
    }

    // ───── EMAIL ────────────────────────────────────────────────────────────
    private void sendEmail(String pid) {
        if (currentBillFilePath == null) return;

        String email = null;
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT email FROM patients WHERE patient_id=?")) {
            ps.setString(1, pid);
            try (ResultSet r = ps.executeQuery()) {
                if (r.next()) email = r.getString(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }

        if (email == null) { showErr("Patient email not found."); return; }

        try { sendEmailWithAttachment(email, new File(currentBillFilePath)); }
        catch (Exception e) { showErr("Email error: " + e.getMessage()); }
    }

    private void sendEmailWithAttachment(String to, File file) throws Exception {
        final String from = "aroramayank488@gmail.com";
        final String pass = "zojw kkfv hdyl etih";

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
        msg.setFrom(new InternetAddress(from));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject("Your Prescription Bill");
        msg.setSentDate(new Date());

        MimeBodyPart text = new MimeBodyPart();
        text.setText("Dear patient,\n\nPlease find attached your bill.\n\nRegards,\nHospital Pharmacy");

        MimeBodyPart attach = new MimeBodyPart();
        attach.attachFile(file);

        Multipart mp = new MimeMultipart();
        mp.addBodyPart(text);
        mp.addBodyPart(attach);

        msg.setContent(mp);
        Transport.send(msg);
    }

    // ───── UTILITIES ────────────────────────────────────────────────────────
    private JDialog createBusyDialog() {
        JDialog d = new JDialog(this, "Please wait", true);
        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        d.setSize(250, 90);
        d.setLayout(new BorderLayout());
        d.add(new JLabel("Processing…", SwingConstants.CENTER), BorderLayout.CENTER);
        d.setLocationRelativeTo(this);
        return d;
    }

    private void showErr(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // ───── MAIN ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(PharmacyBillingSystem::new);
    }
}



