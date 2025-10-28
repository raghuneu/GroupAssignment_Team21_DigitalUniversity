/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package digitaluniversity.ui.faculty;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.Business;
import model.Course;
import model.CourseOffering;
import model.Enrollment;
import model.Student;
import model.TuitionInvoice;
import model.UserAccount;

/**
 * Faculty panel displaying tuition and enrollment insights. Shows number of
 * students enrolled per course and total tuition collected.
 *
 * @author rg
 * @author priyankavadivel
 * @author vedanarayananshrirangesh
 */
public class TuitionEnrollmentPanel extends javax.swing.JPanel {

    /**
     * Creates new form TuitionEnrollmentPanel. Configures the table and
     * populates data when the panel is shown.
     */
    public TuitionEnrollmentPanel() {
        initComponents();

        configureTable();

        // Populate when panel becomes visible
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                populateTable();
            }
        });

        // Perform initial population
        SwingUtilities.invokeLater(this::populateTable);
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
     * Configures table columns and disables cell editing.
     */
    private void configureTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Course ID", "Students Enrolled", "Total Tuition"}
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblTutionEnrollmentInsight.setModel(model);
    }

    /**
     * Populates tuition insights for each course and calculates total collected
     * tuition.
     */
    private void populateTable() {
        Business business = getBusiness();
        if (business == null) {
            JOptionPane.showMessageDialog(this, "Business context not found.");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblTutionEnrollmentInsight.getModel();
        model.setRowCount(0);

        double totalTuitionCollected = 0.0;

        // Map each course to its enrolled students
        Map<String, List<Student>> courseEnrollments = new HashMap<>();

        for (Student s : business.getStudents()) {
            for (Enrollment e : s.getEnrollments()) {
                CourseOffering off = e.getOffering();
                if (off == null || off.getCourse() == null) {
                    continue;
                }

                String courseId = off.getCourse().getCode();
                courseEnrollments.putIfAbsent(courseId, new ArrayList<>());
                courseEnrollments.get(courseId).add(s);
            }
        }

        // Calculate total tuition per course
        for (Map.Entry<String, List<Student>> entry : courseEnrollments.entrySet()) {
            String courseId = entry.getKey();
            List<Student> students = entry.getValue();
            int enrolledCount = students.size();
            int credits = offeredCredits(business, courseId);
            double courseTuition = enrolledCount * credits * 1000.0;

            model.addRow(new Object[]{
                courseId,
                enrolledCount,
                String.format("$%.2f", courseTuition)
            });
        }

        // Add total from paid invoices
        for (TuitionInvoice inv : business.getInvoices()) {
            if (inv.isPaid()) {
                totalTuitionCollected += inv.getOriginalAmount();
            }
        }

        fieldTotalTuitionCollected.setText(String.format("$%.2f", totalTuitionCollected));
    }

    /**
     * Finds number of credits for a given course.
     *
     * @param business business model instance
     * @param courseId course code
     * @return credit count or 0 if not found
     */
    private int offeredCredits(Business business, String courseId) {
        for (Course c : business.getCourses()) {
            if (c.getCode().equals(courseId)) {
                return c.getCredits();
            }
        }
        return 0;
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
        tblTutionEnrollmentInsight = new javax.swing.JTable();
        lblTitle = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        fieldTotalTuitionCollected = new javax.swing.JTextField();
        lblTotalTuition = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        tblTutionEnrollmentInsight.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Course ID", "Students Enrolled", "Total Tuition"
            }
        ));
        jScrollPane1.setViewportView(tblTutionEnrollmentInsight);

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblTitle.setText("Tuition Enrollment Insight");

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        fieldTotalTuitionCollected.setEditable(false);
        fieldTotalTuitionCollected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldTotalTuitionCollectedActionPerformed(evt);
            }
        });

        lblTotalTuition.setText("Total Tuition Collected");

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
                        .addGap(98, 98, 98)
                        .addComponent(lblTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefresh))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTotalTuition)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldTotalTuitionCollected, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(btnRefresh)
                    .addComponent(btnBack))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldTotalTuitionCollected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalTuition))
                .addContainerGap(56, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        populateTable();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void fieldTotalTuitionCollectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldTotalTuitionCollectedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldTotalTuitionCollectedActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        ((CardLayout) getRoot().getLayout()).previous(getRoot());
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JTextField fieldTotalTuitionCollected;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalTuition;
    private javax.swing.JTable tblTutionEnrollmentInsight;
    // End of variables declaration//GEN-END:variables
}
