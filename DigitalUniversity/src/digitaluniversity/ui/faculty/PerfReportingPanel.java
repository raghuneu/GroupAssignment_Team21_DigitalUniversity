/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package digitaluniversity.ui.faculty;

import java.awt.CardLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.print.PrinterException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import model.Business;
import model.CourseOffering;
import model.Enrollment;
import model.Faculty;
import model.Grade;
import model.UserAccount;

/**
 * Faculty panel for generating course performance reports, grade distribution
 * and exporting results.
 *
 * @author rg
 * @author priyankavadivel
 * @author vedanarayananshrirangesh
 */
public class PerfReportingPanel extends javax.swing.JPanel {

    private Faculty currentFaculty;
    private List<CourseOffering> facultyOfferings = new ArrayList<>();

    /**
     * Creates new form PerfReportingPanel. Sets up listeners and initializes
     * table structure.
     */
    public PerfReportingPanel() {
        initComponents();
        configureTable();

        // Load data and build report when panel becomes visible
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                loadSemestersAndData();
            }
        });
    }

    /**
     * @return parent panel for navigation
     */
    private JPanel getRoot() {
        return (JPanel) this.getClientProperty("root");
    }

    /**
     * @return current Business instance
     */
    private Business getBusiness() {
        return (Business) this.getClientProperty("business");
    }

    /**
     * @return current logged-in UserAccount
     */
    private UserAccount getAccount() {
        return (UserAccount) this.getClientProperty("account");
    }

    /**
     * Configure performance table structure and headers.
     */
    private void configureTable() {
        tblPerfReporting.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Course ID", "Average Grade", "Grade Distribution", "Enrollment Count"}
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });
    }

    /**
     * Load all semesters handled by this faculty and generate initial report.
     */
    private void loadSemestersAndData() {
        ddFilterSemester.removeAllItems();

        Business b = getBusiness();
        UserAccount acc = getAccount();
        if (b == null || acc == null) {
            JOptionPane.showMessageDialog(this, "Business context not found.");
            return;
        }

        currentFaculty = b.getFacultyById(acc.getPersonId());
        if (currentFaculty == null) {
            JOptionPane.showMessageDialog(this, "Faculty record not found.");
            return;
        }

        facultyOfferings.clear();
        Set<String> terms = new HashSet<>();
        for (CourseOffering off : b.getOfferings()) {
            if (off.getFaculty() != null && off.getFaculty().getId().equals(currentFaculty.getId())) {
                facultyOfferings.add(off);
                if (off.getTerm() != null) {
                    terms.add(off.getTerm());
                }
            }
        }

        ddFilterSemester.addItem("All");
        java.util.List<String> sorted = new ArrayList<>(terms);
        Collections.sort(sorted);
        for (String t : sorted) {
            ddFilterSemester.addItem(t);
        }

        generateReport(); // initial fill
    }

    /**
     * Generate performance report filtered by selected semester.
     */
    private void generateReport() {
        String termFilter = (String) ddFilterSemester.getSelectedItem();
        DefaultTableModel m = (DefaultTableModel) tblPerfReporting.getModel();
        m.setRowCount(0);

        if (facultyOfferings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No offerings for this faculty.");
            return;
        }

        for (CourseOffering off : facultyOfferings) {
            if (termFilter != null && !"All".equals(termFilter)
                    && (off.getTerm() == null || !off.getTerm().equals(termFilter))) {
                continue;
            }

            java.util.List<Enrollment> enrolls = off.getEnrollments();
            if (enrolls == null || enrolls.isEmpty()) {
                continue;
            }

            double totalPoints = 0.0;
            int graded = 0;
            Map<String, Integer> dist = new LinkedHashMap<>();

            for (Enrollment e : enrolls) {
                Grade g = e.getGrade();
                if (g != null && g != Grade.IP) {
                    totalPoints += g.getPoints();
                    graded++;
                    dist.merge(g.name(), 1, Integer::sum);
                }
            }

            String avgStr = graded == 0 ? "-" : String.format("%.2f", totalPoints / graded);
            String distStr = dist.isEmpty() ? "N/A" : formatDist(dist);
            int count = enrolls.size();

            m.addRow(new Object[]{
                off.getCourse().getCode(), // uses your Course#getCode()
                avgStr,
                distStr,
                count
            });
        }

        if (m.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No data for selected term.");
        }
    }

    /**
     * Formats grade distribution map into readable string.
     *
     * @param dist grade and count mapping
     * @return formatted grade distribution
     */
    private String formatDist(Map<String, Integer> dist) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> e : dist.entrySet()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(e.getKey()).append(':').append(e.getValue());
        }
        return sb.toString();
    }

    /**
     * Export report table to a CSV file.
     */
    private void exportToCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save CSV Report");
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {
            DefaultTableModel m = (DefaultTableModel) tblPerfReporting.getModel();
            // header
            for (int c = 0; c < m.getColumnCount(); c++) {
                fw.write(m.getColumnName(c));
                if (c < m.getColumnCount() - 1) {
                    fw.write(",");
                }
            }
            fw.write("\n");
            // rows
            for (int r = 0; r < m.getRowCount(); r++) {
                for (int c = 0; c < m.getColumnCount(); c++) {
                    fw.write(String.valueOf(m.getValueAt(r, c)));
                    if (c < m.getColumnCount() - 1) {
                        fw.write(",");
                    }
                }
                fw.write("\n");
            }
            fw.flush();
            JOptionPane.showMessageDialog(this, "CSV exported successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error exporting CSV: " + ex.getMessage());
        }
    }

    /**
     * Print the performance report table.
     */
    private void printReport() {
        try {
            boolean ok = tblPerfReporting.print();
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Printing canceled.");
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Print error: " + ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblPerfReporting = new javax.swing.JTable();
        lblTitle = new javax.swing.JLabel();
        btnExportToCSV = new javax.swing.JButton();
        lblFilterSemester = new javax.swing.JLabel();
        ddFilterSemester = new javax.swing.JComboBox<>();
        btnPrintReport = new javax.swing.JButton();
        btnGenerateReport = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        tblPerfReporting.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Course ID", "Average Grade", "Grade Distribution", "Enrollment Count"
            }
        ));
        jScrollPane1.setViewportView(tblPerfReporting);

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblTitle.setText("Performance Reporting");

        btnExportToCSV.setBackground(new java.awt.Color(153, 204, 255));
        btnExportToCSV.setText("Export to CSV");
        btnExportToCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportToCSVActionPerformed(evt);
            }
        });

        lblFilterSemester.setText("Filter Semester");

        ddFilterSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddFilterSemesterActionPerformed(evt);
            }
        });

        btnPrintReport.setText("Print Report");
        btnPrintReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintReportActionPerformed(evt);
            }
        });

        btnGenerateReport.setText("Generate Report");
        btnGenerateReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateReportActionPerformed(evt);
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
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addGap(92, 92, 92)
                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPrintReport))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFilterSemester)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ddFilterSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnExportToCSV)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGenerateReport))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(btnPrintReport)
                    .addComponent(btnBack))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFilterSemester)
                    .addComponent(ddFilterSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExportToCSV)
                    .addComponent(btnGenerateReport))
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        ((CardLayout) getRoot().getLayout()).previous(getRoot());
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnGenerateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateReportActionPerformed
        generateReport();
    }//GEN-LAST:event_btnGenerateReportActionPerformed

    private void btnExportToCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportToCSVActionPerformed
        exportToCSV();
    }//GEN-LAST:event_btnExportToCSVActionPerformed

    private void btnPrintReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintReportActionPerformed
        printReport();
    }//GEN-LAST:event_btnPrintReportActionPerformed

    private void ddFilterSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddFilterSemesterActionPerformed
        // Refresh report when user selects a different semester
        if (ddFilterSemester.getItemCount() > 0) {
            generateReport();
        }
    }//GEN-LAST:event_ddFilterSemesterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnExportToCSV;
    private javax.swing.JButton btnGenerateReport;
    private javax.swing.JButton btnPrintReport;
    private javax.swing.JComboBox<String> ddFilterSemester;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFilterSemester;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblPerfReporting;
    // End of variables declaration//GEN-END:variables
}
