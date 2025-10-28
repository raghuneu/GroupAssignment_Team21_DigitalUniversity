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

import java.awt.CardLayout;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 *
 * @author vedanarayananshrirangesh
 */
public class StudentProgressPanel extends javax.swing.JPanel {

    private JPanel getRoot() {
        // Gets parent container for navigation
        // via CardLayout
        return (JPanel) this.getClientProperty("root");
    }

    private Business getBusiness() {
        // Accesses central Business model
        // holding all academic data
        return (Business) this.getClientProperty("business");
    }

    private UserAccount getAccount() {
        // Returns logged-in user session
        return (UserAccount) this.getClientProperty("account");
    }

    private static final int TOTAL_CREDITS_REQUIRED = 32;

    /**
     * Creates new form StudentProgressPanel
     */// Initialize UI + prepare table
    // and populate when panel is shown
    public StudentProgressPanel() {
        initComponents();
        configureTable();

        // Wait until shown to populate, ensuring client properties exist
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                populateAll();
            }
        });
    }

    private Student currentStudent() {
        // Resolves Student object using
        // current authenticated account
        if (getBusiness() == null || getAccount() == null) {
            return null;
        }
        return getBusiness().getStudentById(getAccount().getPersonId());
    }

    private void configureTable() {
        // Set non-editable table model
        // defining course progress columns
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Course ID", "Course Name", "Credits", "Status", "Grade"}
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblAcademicProgress.setModel(model);
    }

    private void populateAll() {
        // Fills table and GPA/progress metrics
        // only when student data exists
        Student s = currentStudent();
        if (s == null) {
            return;
        }

        populateTable(s);
        computeAndShowMetrics(s);
    }

    private void populateTable(Student s) {
        // Iterates enrollments to display each course
        // with status and grade visibility

        DefaultTableModel model = (DefaultTableModel) tblAcademicProgress.getModel();
        model.setRowCount(0);

        for (Enrollment e : s.getEnrollments()) {
            CourseOffering offering = e.getOffering();
            if (offering == null || offering.getCourse() == null) {
                continue;
            }

            Course c = offering.getCourse();
            String courseId = c.getCode();
            String courseName = c.getTitle();
            int credits = c.getCredits();
            Grade g = e.getGrade();

            String status = (g == Grade.IP) ? "In Progress" : "Completed";
            String grade = (g == Grade.IP) ? "-" : formatGrade(g);

            model.addRow(new Object[]{courseId, courseName, credits, status, grade});
        }
    }

    private void computeAndShowMetrics(Student s) {
        // Calculates GPA, completed credits,
        // and updates JProgressBar + labels
        double points = 0.0;
        int gradedCredits = 0;
        int passedCredits = 0;

        for (Enrollment e : s.getEnrollments()) {
            Grade g = e.getGrade();
            CourseOffering o = e.getOffering();
            if (g == null || o == null || o.getCourse() == null) {
                continue;
            }

            int cr = o.getCourse().getCredits();
            if (g != Grade.IP) {
                gradedCredits += cr;
                points += g.getPoints() * cr;
                if (g.getPoints() > 0.0) {
                    passedCredits += cr;
                }
            }
        }

        double gpa = (gradedCredits == 0) ? 0.0 : round2(points / gradedCredits);
        String standing = (gpa >= 3.0) ? "Good Standing" : "On Probation";
        int progressPercent = (int) Math.round(100.0 * passedCredits / (double) TOTAL_CREDITS_REQUIRED);

        // Display metrics
        fieldCreditsCompleted.setText(passedCredits + " / " + TOTAL_CREDITS_REQUIRED);
        fieldAcademicStanding.setText(standing);

        pbGraduationProgress.setMinimum(0);
        pbGraduationProgress.setMaximum(100);
        pbGraduationProgress.setValue(Math.min(100, progressPercent));
        pbGraduationProgress.setStringPainted(true);
        pbGraduationProgress.setString(progressPercent + "%");
    }

    private String formatGrade(Grade g) {
        // Converts enum to readable letter grade
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
            case IP:
                return "-";
            default:
                return g.name();
        }
    }

    private double round2(double v) {
        // Rounds GPA to 2 decimal precision
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

        lblAcademicProgress = new javax.swing.JLabel();
        lblCreditsCompleted = new javax.swing.JLabel();
        lblAcademicStanding = new javax.swing.JLabel();
        lblGraduationProgress = new javax.swing.JLabel();
        pbGraduationProgress = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAcademicProgress = new javax.swing.JTable();
        btnCheckGraduationEligibility = new javax.swing.JButton();
        fieldCreditsCompleted = new javax.swing.JTextField();
        fieldAcademicStanding = new javax.swing.JTextField();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        lblAcademicProgress.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblAcademicProgress.setText("Academic Progress");

        lblCreditsCompleted.setText("Credits Completed");

        lblAcademicStanding.setText("Academic Standing");

        lblGraduationProgress.setText("Graduation Progress");

        tblAcademicProgress.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Course ID", " Course Name", "Credits", "Status", "Grade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblAcademicProgress);
        if (tblAcademicProgress.getColumnModel().getColumnCount() > 0) {
            tblAcademicProgress.getColumnModel().getColumn(0).setResizable(false);
            tblAcademicProgress.getColumnModel().getColumn(1).setResizable(false);
            tblAcademicProgress.getColumnModel().getColumn(2).setResizable(false);
            tblAcademicProgress.getColumnModel().getColumn(3).setResizable(false);
            tblAcademicProgress.getColumnModel().getColumn(4).setResizable(false);
        }

        btnCheckGraduationEligibility.setBackground(new java.awt.Color(153, 204, 255));
        btnCheckGraduationEligibility.setText("Check Graduation Eligibility");
        btnCheckGraduationEligibility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckGraduationEligibilityActionPerformed(evt);
            }
        });

        fieldCreditsCompleted.setEditable(false);

        fieldAcademicStanding.setEditable(false);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBack)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnCheckGraduationEligibility)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblCreditsCompleted)
                                    .addGap(34, 34, 34)
                                    .addComponent(fieldCreditsCompleted, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblAcademicProgress, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblAcademicStanding, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(fieldAcademicStanding, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(136, 136, 136))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(61, 61, 61)
                            .addComponent(lblGraduationProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(pbGraduationProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAcademicProgress)
                    .addComponent(btnBack))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCreditsCompleted)
                    .addComponent(fieldCreditsCompleted, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAcademicStanding)
                    .addComponent(fieldAcademicStanding, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pbGraduationProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblGraduationProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnCheckGraduationEligibility)
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Re-evaluates degree eligibility
    // and shows dialog for graduation status
    private void btnCheckGraduationEligibilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckGraduationEligibilityActionPerformed
        Student s = currentStudent();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "No student loaded");
            return;
        }

        double points = 0.0;
        int gradedCredits = 0;
        int passedCredits = 0;

        for (Enrollment e : s.getEnrollments()) {
            Grade g = e.getGrade();
            CourseOffering o = e.getOffering();
            if (g == null || o == null || o.getCourse() == null) {
                continue;
            }

            int cr = o.getCourse().getCredits();
            if (g != Grade.IP) {
                gradedCredits += cr;
                points += g.getPoints() * cr;
                if (g.getPoints() > 0.0) {
                    passedCredits += cr;
                }
            }
        }

        double gpa = (gradedCredits == 0) ? 0.0 : round2(points / gradedCredits);
        boolean creditOk = passedCredits >= TOTAL_CREDITS_REQUIRED;
        boolean gpaOk = gpa >= 3.0;

        if (creditOk && gpaOk) {
            JOptionPane.showMessageDialog(this,
                    "🎓 Eligible for Graduation!\nCredits: " + passedCredits
                    + " / " + TOTAL_CREDITS_REQUIRED + "\nGPA: " + String.format("%.2f", gpa),
                    "Graduation Status", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("Not Yet Eligible\n");
            if (!creditOk) {
                sb.append("- Need at least ").append(TOTAL_CREDITS_REQUIRED)
                        .append(" credits (you have ").append(passedCredits).append(")\n");
            }
            if (!gpaOk) {
                sb.append("- Need GPA ≥ 3.00 (current: ").append(String.format("%.2f", gpa)).append(")\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString().trim(), "Graduation Status", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnCheckGraduationEligibilityActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        ((CardLayout) getRoot().getLayout()).previous(getRoot());
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCheckGraduationEligibility;
    private javax.swing.JTextField fieldAcademicStanding;
    private javax.swing.JTextField fieldCreditsCompleted;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAcademicProgress;
    private javax.swing.JLabel lblAcademicStanding;
    private javax.swing.JLabel lblCreditsCompleted;
    private javax.swing.JLabel lblGraduationProgress;
    private javax.swing.JProgressBar pbGraduationProgress;
    private javax.swing.JTable tblAcademicProgress;
    // End of variables declaration//GEN-END:variables
}
