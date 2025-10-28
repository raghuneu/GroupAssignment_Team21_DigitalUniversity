/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package digitaluniversity.ui.faculty;

import java.awt.CardLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import model.Business;
import model.Course;
import model.CourseOffering;
import model.Faculty;
import model.UserAccount;

/**
 * Faculty panel to manage course offerings, update details, upload syllabus and
 * control enrollment.
 *
 * @author rg
 * @author priyankavadivel
 * @author vedanarayananshrirangesh
 */
public class CourseMgmtPanel extends javax.swing.JPanel {

    /**
     * Offering mapping so table actions can update the model correctly
     */
    private static class RowRef {

        final CourseOffering offering;

        RowRef(CourseOffering off) {
            this.offering = off;
        }
    }
    private final List<RowRef> rowRefs = new ArrayList<>();

    // In-memory syllabus storage per offering id
    private final Map<String, String> syllabusByOfferingId = new LinkedHashMap<>();

    /**
     * Initializes the Course Management panel and sets up listeners.
     */
    public CourseMgmtPanel() {
        initComponents();

        configureTable();

        // Populate when the panel becomes visible
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                populateTable();
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
     * Configure table model and editable columns.
     */
    private void configureTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Course ID", "Title", "Description", "Term", "Capacity", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                // allow edits only on Title & Capacity
                return c == 1 || c == 4;
            }

            @Override
            public Class<?> getColumnClass(int col) {
                return col == 4 ? Integer.class : String.class;
            }
        };
        tblCourseMgmt.setModel(model);
    }

    /**
     * Populate table with all course offerings of the faculty.
     */
    private void populateTable() {
        DefaultTableModel m = (DefaultTableModel) tblCourseMgmt.getModel();
        m.setRowCount(0);
        rowRefs.clear();

        Business b = getBusiness();
        if (b == null) {
            return;
        }
        UserAccount acc = getAccount();
        if (acc == null) {
            return;
        }
        Faculty f = b.getFacultyById(acc.getPersonId());
        if (f == null) {
            return;
        }

        for (CourseOffering off : b.getOfferings()) {
            if (off.getFaculty() == null || !off.getFaculty().getId().equals(f.getId())) {
                continue;
            }

            Course c = off.getCourse();
            String id = (c != null) ? c.getCode() : off.getId();
            String title = (c != null) ? c.getTitle() : "(Untitled)";
            String desc = title; // placeholder
            String term = off.getTerm();
            int capacity = off.getCapacity();
            String status = off.isEnrollmentOpen() ? "OPEN" : "CLOSED";

            m.addRow(new Object[]{id, title, desc, term, capacity, status});
            rowRefs.add(new RowRef(off));
        }
    }

    /**
     * Save all edited table values back to the business model.
     */
    private void doSaveEdits() {
        DefaultTableModel m = (DefaultTableModel) tblCourseMgmt.getModel();
        if (m.getRowCount() == 0) {
            return;
        }

        for (int r = 0; r < m.getRowCount(); r++) {
            RowRef ref = rowRefs.get(r);
            CourseOffering off = ref.offering;
            Course c = off.getCourse();

            // update title
            Object titleObj = m.getValueAt(r, 1);
            String newTitle = (titleObj == null) ? "" : titleObj.toString().trim();
            if (c != null && !newTitle.isEmpty() && !newTitle.equals(c.getTitle())) {
                c.setTitle(newTitle);
            }

            // update capacity
            Object capObj = m.getValueAt(r, 4);
            try {
                int newCap = Integer.parseInt(capObj.toString());
                if (newCap > 0) {
                    off.setCapacity(newCap);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid capacity in row " + (r + 1) + ". Keeping previous value.");
            }
        }

        populateTable();
        JOptionPane.showMessageDialog(this, "Changes saved successfully!");
    }

    /**
     * Opens enrollment for the selected course offering.
     */
    private void doOpenEnrollment() {
        int row = tblCourseMgmt.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to open enrollment.");
            return;
        }
        RowRef ref = rowRefs.get(row);
        CourseOffering off = ref.offering;
        off.setEnrollmentOpen(true);
        populateTable();
        tblCourseMgmt.setRowSelectionInterval(row, row);
        JOptionPane.showMessageDialog(this,
                "Enrollment opened for " + off.getCourse().getCode() + " (" + off.getTerm() + ")");
    }

    /**
     * Closes enrollment for the selected course offering.
     */
    private void doCloseEnrollment() {
        int row = tblCourseMgmt.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row to close enrollment.");
            return;
        }
        RowRef ref = rowRefs.get(row);
        CourseOffering off = ref.offering;
        off.setEnrollmentOpen(false);
        populateTable();
        tblCourseMgmt.setRowSelectionInterval(row, row);
        JOptionPane.showMessageDialog(this,
                "Enrollment closed for " + off.getCourse().getCode() + " (" + off.getTerm() + ")");
    }

    /**
     * Uploads syllabus file for the selected course offering.
     */
    private void doUploadSyllabus() {
        int row = tblCourseMgmt.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a course row first.");
            return;
        }

        RowRef ref = rowRefs.get(row);
        CourseOffering off = ref.offering;
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select Syllabus File");
        int res = fc.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File f = fc.getSelectedFile();
        if (f == null || !f.exists()) {
            JOptionPane.showMessageDialog(this, "Invalid file.");
            return;
        }

        // Map syllabus path to offering ID
        syllabusByOfferingId.put(off.getId(), f.getAbsolutePath());
        JOptionPane.showMessageDialog(this,
                "Syllabus uploaded for " + off.getCourse().getCode()
                + ":\n" + f.getAbsolutePath());
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
        tblCourseMgmt = new javax.swing.JTable();
        lblTitle = new javax.swing.JLabel();
        btnUploadSyllabus = new javax.swing.JButton();
        btnCloseEnrollment = new javax.swing.JButton();
        btnOpenEnrollment = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        tblCourseMgmt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Course ID", "Title", "Description", "Schedule", "Capcity", "Status"
            }
        ));
        jScrollPane1.setViewportView(tblCourseMgmt);

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblTitle.setText("Course Management");

        btnUploadSyllabus.setText("Upload Syllabus");
        btnUploadSyllabus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadSyllabusActionPerformed(evt);
            }
        });

        btnCloseEnrollment.setText("Close Enrollment");
        btnCloseEnrollment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseEnrollmentActionPerformed(evt);
            }
        });

        btnOpenEnrollment.setText("Open Enrollment");
        btnOpenEnrollment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenEnrollmentActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(153, 204, 255));
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addGap(113, 113, 113)
                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnUploadSyllabus)
                            .addGap(18, 18, 18)
                            .addComponent(btnSave)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnOpenEnrollment)
                                .addComponent(btnCloseEnrollment)))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(btnBack))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUploadSyllabus)
                    .addComponent(btnOpenEnrollment)
                    .addComponent(btnSave))
                .addGap(18, 18, 18)
                .addComponent(btnCloseEnrollment)
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        ((CardLayout) getRoot().getLayout()).previous(getRoot());
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnOpenEnrollmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenEnrollmentActionPerformed
        doOpenEnrollment();
    }//GEN-LAST:event_btnOpenEnrollmentActionPerformed

    private void btnUploadSyllabusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadSyllabusActionPerformed
        doUploadSyllabus();
    }//GEN-LAST:event_btnUploadSyllabusActionPerformed

    private void btnCloseEnrollmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseEnrollmentActionPerformed
        doCloseEnrollment();
    }//GEN-LAST:event_btnCloseEnrollmentActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        doSaveEdits();
    }//GEN-LAST:event_btnSaveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCloseEnrollment;
    private javax.swing.JButton btnOpenEnrollment;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUploadSyllabus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblCourseMgmt;
    // End of variables declaration//GEN-END:variables

}
