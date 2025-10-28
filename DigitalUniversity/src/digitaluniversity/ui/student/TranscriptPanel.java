/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
/**
 * @author priyankavadivel
 * @author vedanarayananshrirangesh
 * @author rg
 */

package digitaluniversity.ui.student;

import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.CardLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 *
 * @author vedanarayananshrirangesh
 */
public class TranscriptPanel extends javax.swing.JPanel {

    private JPanel getRoot() {
        // Gets parent card container for navigation
        return (JPanel) this.getClientProperty("root");
    }

    private Business getBusiness() {
        // Provides access to shared university data model
        return (Business) this.getClientProperty("business");
    }

    private UserAccount getAccount() {
        // Returns the authenticated student session
        return (UserAccount) this.getClientProperty("account");
    }

    private final DecimalFormat df2 = new DecimalFormat("0.00");

    /**
     * Creates new form TranscriptPanel
     */// Initialize and auto-populate transcript when panel becomes visible
    public TranscriptPanel() {
        initComponents();
        configureTable();

        // Populate when shown (ensures client properties are ready)
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                populateTranscript();
            }
        });

        // Change listener for semester dropdown
        cmbSemester.addActionListener(e -> populateTranscript());
    }

    private Student currentStudent() {
        // Retrieves Student instance mapped to logged-in account
        if (getBusiness() == null || getAccount() == null) {
            return null;
        }
        return getBusiness().getStudentById(getAccount().getPersonId());
    }

    private void configureTable() {
        // Sets non-editable transcript table with fixed column schema
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Term", "Course ID", "Course Name", "Grade", "Credits"}
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblTranscript.setModel(model);
    }

    private void populateTranscript() {
        // Fills table + computes current term & overall GPA based on dropdown
        DefaultTableModel model = (DefaultTableModel) tblTranscript.getModel();
        model.setRowCount(0);

        Student s = currentStudent();
        if (s == null) {
            return;
        }

        String selectedTerm = (String) cmbSemester.getSelectedItem();
        double termPoints = 0.0, totalPoints = 0.0;
        int termCredits = 0, totalCredits = 0;

        for (Enrollment e : s.getEnrollments()) {
            CourseOffering o = e.getOffering();
            if (o == null || o.getCourse() == null) {
                continue;
            }

            Course c = o.getCourse();
            String term = (o.getTerm() != null) ? o.getTerm() : "N/A";
            Grade g = e.getGrade();
            String grade = (g == null || g == Grade.IP) ? "-" : formatGrade(g);
            int credits = c.getCredits();

            // Add row if matching semester (or all if null)
            if (selectedTerm == null || selectedTerm.equals(term)) {
                model.addRow(new Object[]{term, c.getCode(), c.getTitle(), grade, credits});
            }

            // GPA calculation (overall)
            if (g != null && g != Grade.IP) {
                totalCredits += credits;
                totalPoints += g.getPoints() * credits;
                if (selectedTerm != null && selectedTerm.equals(term)) {
                    termCredits += credits;
                    termPoints += g.getPoints() * credits;
                }
            }
        }

        double termGpa = (termCredits == 0) ? 0.0 : round2(termPoints / termCredits);
        double overallGpa = (totalCredits == 0) ? 0.0 : round2(totalPoints / totalCredits);

        // Use formatted strings with fixed width and padding
        lblTermGpa.setText(String.format("Term GPA: %.2f", termGpa));
        lblOverallGpa.setText(String.format("Overall GPA: %.2f", overallGpa));

    }

    private String formatGrade(Grade g) {
        switch (g) {
            case A:
                return "A";
            case A_MINUS:
                return "A-";
            case B_PLUS:
                return "B+";
            case B:
                return "B";
            case B_MINUS:
                return "B-";
            case C_PLUS:
                return "C+";
            case C:
                return "C";
            case D:
                return "D";
            case F:
                return "F";
            default:
                return "-";
        }
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTranscriptViewer = new javax.swing.JLabel();
        lblSelectSemister = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTranscript = new javax.swing.JTable();
        lblTermGpa = new javax.swing.JLabel();
        lblOverallGpa = new javax.swing.JLabel();
        btnExport = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        lblTranscriptViewer.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblTranscriptViewer.setText("Transcript Viewer  ");

        lblSelectSemister.setText("Select Semester");

        cmbSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fall 2024", "Spring 2025", "Fall 2025" }));

        tblTranscript.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Term", "Course Id", "Course Name", "Grade", "Credits"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblTranscript);

        lblTermGpa.setText("Term GPA:");

        lblOverallGpa.setText("Overall GPA:");

        btnExport.setBackground(new java.awt.Color(153, 204, 255));
        btnExport.setText("Export");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExport)
                    .addComponent(lblOverallGpa, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTermGpa, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSelectSemister)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnBack)
                            .addGap(79, 79, 79)
                            .addComponent(lblTranscriptViewer)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPrint))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(lblTranscriptViewer)
                    .addComponent(btnPrint))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSelectSemister)
                    .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(lblTermGpa)
                .addGap(18, 18, 18)
                .addComponent(lblOverallGpa)
                .addGap(18, 18, 18)
                .addComponent(btnExport)
                .addContainerGap(100, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Exports transcript to a formatted text/PDF-like file via JFileChooser
    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Transcript PDF");
            chooser.setSelectedFile(new File("Transcript.pdf"));
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = chooser.getSelectedFile();
            StringBuilder sb = new StringBuilder();

            Student s = currentStudent();
            sb.append("Official Transcript\n\n");
            sb.append("Student: ").append((s != null) ? s.getName() : "N/A").append("\n");
            sb.append("Email: ").append((s != null) ? s.getEmail() : "N/A").append("\n\n");

            DefaultTableModel model = (DefaultTableModel) tblTranscript.getModel();
            sb.append(String.format("%-10s %-10s %-35s %-10s %-8s\n", "Term", "Course", "Course Name", "Grade", "Credits"));
            sb.append("-------------------------------------------------------------------------------\n");
            for (int i = 0; i < model.getRowCount(); i++) {
                sb.append(String.format("%-10s %-10s %-35s %-10s %-8s\n",
                        model.getValueAt(i, 0),
                        model.getValueAt(i, 1),
                        model.getValueAt(i, 2),
                        model.getValueAt(i, 3),
                        model.getValueAt(i, 4)));
            }
            sb.append("\n").append(lblTermGpa.getText()).append("\n").append(lblOverallGpa.getText());

            java.nio.file.Files.writeString(file.toPath(), sb.toString());
            JOptionPane.showMessageDialog(this, "Exported successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to export transcript: " + e.getMessage());
        }
    }//GEN-LAST:event_btnExportActionPerformed
    // Sends JTable transcript to printer using Swing print API
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        try {
            boolean done = tblTranscript.print(JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat("Student Transcript"),
                    new java.text.MessageFormat("Page {0}"));
            if (!done) {
                JOptionPane.showMessageDialog(this, "Printing cancelled");
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Printing failed: " + e.getMessage());
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        ((CardLayout) getRoot().getLayout()).previous(getRoot());
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnPrint;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblOverallGpa;
    private javax.swing.JLabel lblSelectSemister;
    private javax.swing.JLabel lblTermGpa;
    private javax.swing.JLabel lblTranscriptViewer;
    private javax.swing.JTable tblTranscript;
    // End of variables declaration//GEN-END:variables
}
