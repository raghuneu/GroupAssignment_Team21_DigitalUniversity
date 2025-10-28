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
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CourseRegistrationPanel extends javax.swing.JPanel {

    private JPanel getRoot() {
        return (JPanel) this.getClientProperty("root");
    }

    private Business getBusiness() {
        return (Business) this.getClientProperty("business");
    }

    private UserAccount getAccount() {
        return (UserAccount) this.getClientProperty("account");
    }

    private List<CourseOffering> allOfferings = new ArrayList<>();
    private List<CourseOffering> currentView = new ArrayList<>();

    public CourseRegistrationPanel() {
        initComponents();

        // make total credits read-only
        txtTotalCredits.setEditable(false);

        configureTable();

        // refresh AFTER props are injected
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshAll();
            }
        });

        // when the user changes selection, recompute visible-term credits
        tblCourses.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateTotalCreditsLabelForVisibleTerm();
                }
            }
        });
    }

    private Student currentStudent() {
        // Safely resolves the logged-in student's object
        // from Business using the current UserAccount
        Business b = getBusiness();
        UserAccount ua = getAccount();
        return (b == null || ua == null) ? null : b.getStudentById(ua.getPersonId());
    }

    /**
     * Ensures null/empty term values are normalized so credit logic doesn't
     * break
     */
    private String safeTerm(String t) {
        return (t == null || t.trim().isEmpty()) ? "Current" : t.trim();
    }

    /**
     * Initializes JTable with a fixed non-editable model and single-selection
     * mode
     */
    private void configureTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Course ID", "Name", "Instructor", "Credits", "Seats", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblCourses.setModel(model);
        tblCourses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Initializes JTable with a fixed non-editable model and single-selection
     * mode
     */
    private void loadOfferings() {
        Business b = getBusiness();
        if (b == null) {
            allOfferings = new ArrayList<>();
            return;
        }
        allOfferings = new ArrayList<>(b.getOfferings());
    }

    /**
     * Checks if student is already enrolled by comparing offering instance
     */
    private boolean alreadyEnrolled(Student s, CourseOffering o) {
        for (Enrollment e : s.getEnrollments()) {
            if (e.getOffering() == o) {
                return true; // same instance (Business holds uniques)
            }
        }
        return false;
    }

    /**
     *
     * Calculates total earned credits limited to a specific academic term
     */
    private int creditsInTerm(Student s, String term) {
        String t = safeTerm(term);
        int credits = 0;
        for (Enrollment e : s.getEnrollments()) {
            CourseOffering eo = e.getOffering();
            if (t.equals(safeTerm(eo.getTerm()))) {
                credits += eo.getCourse().getCredits();
            }
        }
        return credits;
    }

    /**
     * Returns Enrolled / Full / Available based on student and seat
     * availability
     */
    private String statusFor(Student s, CourseOffering o) {
        if (s != null && alreadyEnrolled(s, o)) {
            return "Enrolled";
        }
        if (!o.hasSeat()) {
            return "Full";
        }
        return "Available";
    }

    /**
     * Returns Seats as String type
     */
    private String seatsAsString(CourseOffering o) {
        return o.getEnrollments().size() + "/" + o.getCapacity();
    }

    /**
     * Populates JTable with visible courses while computing seat+status data
     */
    private void applyStatusAndFillTable(List<CourseOffering> list) {
        Student s = currentStudent();
        DefaultTableModel model = (DefaultTableModel) tblCourses.getModel();
        model.setRowCount(0);
        currentView = new ArrayList<>(list);

        for (CourseOffering o : list) {
            Course c = o.getCourse();
            String courseId = c.getCode();
            String name = c.getTitle();
            String instructor = (o.getFaculty() == null) ? "(TBD)" : o.getFaculty().getName();
            int credits = c.getCredits();
            String seats = seatsAsString(o);
            String st = statusFor(s, o);
            model.addRow(new Object[]{courseId, name, instructor, credits, seats, st});
        }

        if (model.getRowCount() > 0) {
            tblCourses.setRowSelectionInterval(0, 0);
        }
    }

    /**
     * Reloads offerings and reapplies active search/filter then updates
     * total-credits label
     */
    private void refreshAll() {
        loadOfferings();
        // keep any active search
        if (txtSearch.getText().trim().isEmpty()) {
            applyStatusAndFillTable(allOfferings);
        } else {
            doSearch();
        }
        updateTotalCreditsLabelForVisibleTerm();
    }

    /**
     * Determines current view's term and shows total enrolled credits in that
     * term
     */
    private void updateTotalCreditsLabelForVisibleTerm() {
        Student s = currentStudent();
        if (s == null) {
            txtTotalCredits.setText("0");
            return;
        }

        String term = null;
        if (!currentView.isEmpty()) {
            int sel = tblCourses.getSelectedRow();
            CourseOffering chosen = (sel >= 0 && sel < currentView.size()) ? currentView.get(sel)
                    : currentView.get(0);
            term = chosen.getTerm();
        }
        int cr = (term == null) ? computeAllCredits(s) : creditsInTerm(s, term);
        txtTotalCredits.setText(String.valueOf(cr));
    }

    /**
     * Computes all the credits
     */
    private int computeAllCredits(Student s) {
        int credits = 0;
        for (Enrollment e : s.getEnrollments()) {
            credits += e.getOffering().getCourse().getCredits();
        }
        return credits;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCourseRegistration = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCourses = new javax.swing.JTable();
        btnEnroll = new javax.swing.JButton();
        btnDrop = new javax.swing.JButton();
        lblSearch = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        cmbSearchBy = new javax.swing.JComboBox<>();
        btnSearch = new javax.swing.JButton();
        lblSearchBy = new javax.swing.JLabel();
        lblTotalCredits = new javax.swing.JLabel();
        txtTotalCredits = new javax.swing.JTextField();
        btnBack = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        lblCourseRegistration.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblCourseRegistration.setText("Course Registration");

        tblCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Course ID", "Name", "Instructor", "Credits", "Seats", "Status"
            }
        ));
        jScrollPane1.setViewportView(tblCourses);

        btnEnroll.setBackground(new java.awt.Color(153, 204, 255));
        btnEnroll.setText("Enroll");
        btnEnroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrollActionPerformed(evt);
            }
        });

        btnDrop.setText("Drop");
        btnDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDropActionPerformed(evt);
            }
        });

        lblSearch.setText("Search");

        cmbSearchBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Course ID", "Instructor", "Status" }));

        btnSearch.setText("search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        lblSearchBy.setText("Search by");

        lblTotalCredits.setText("Total credits");

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setText("Max credits per sem: 8");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTotalCredits)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotalCredits, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSearchBy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbSearchBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(lblSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addGap(73, 73, 73)
                        .addComponent(lblCourseRegistration, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEnroll)
                            .addGap(18, 18, 18)
                            .addComponent(btnDrop))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCourseRegistration)
                    .addComponent(btnBack))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSearch)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSearchBy)
                    .addComponent(cmbSearchBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalCredits)
                    .addComponent(txtTotalCredits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnroll)
                    .addComponent(btnDrop)
                    .addComponent(jLabel1))
                .addGap(30, 30, 30))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        doSearch();
    }//GEN-LAST:event_btnSearchActionPerformed
    
    // Handles seat checks, 8-credit cap, and enrollment
    // then regenerates UI+invoice after success
    private void btnEnrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrollActionPerformed
        int row = tblCourses.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a course");
            return;
        }
        if (row >= currentView.size()) {
            JOptionPane.showMessageDialog(this, "Invalid selection");
            return;
        }

        CourseOffering o = currentView.get(row);
        Student s = currentStudent();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "No student loaded");
            return;
        }

        if (alreadyEnrolled(s, o)) {
            JOptionPane.showMessageDialog(this, "Already enrolled in this course");
            return;
        }
        if (!o.hasSeat()) {
            JOptionPane.showMessageDialog(this, "No seats available");
            return;
        }

        String term = safeTerm(o.getTerm());
        int termCredits = creditsInTerm(s, term);
        int newCr = o.getCourse().getCredits();
        if (termCredits + newCr > 8) {
            JOptionPane.showMessageDialog(this, "Cannot exceed 8 credits in the term: " + term);
            return;
        }

        Enrollment e = getBusiness().enroll(s, o);
        if (e == null) {
            JOptionPane.showMessageDialog(this, "Enrollment failed");
            return;
        }

        getBusiness().createOrUpdateInvoice(s);
        JOptionPane.showMessageDialog(this, "Enrolled successfully");
        refreshAll();
    }//GEN-LAST:event_btnEnrollActionPerformed
    // Safely removes enrollment instance
    // from both student and course offering
    private void btnDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDropActionPerformed
        int row = tblCourses.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a course");
            return;
        }
        if (row >= currentView.size()) {
            JOptionPane.showMessageDialog(this, "Invalid selection");
            return;
        }

        CourseOffering o = currentView.get(row);
        Student s = currentStudent();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "No student loaded");
            return;
        }

        Enrollment found = null;
        for (Enrollment e : new ArrayList<>(s.getEnrollments())) {
            if (e.getOffering() == o) {
                found = e;
                break;
            }
        }
        if (found == null) {
            JOptionPane.showMessageDialog(this, "You are not enrolled in this course");
            return;
        }

        s.getEnrollments().remove(found);
        o.getEnrollments().remove(found);
        getBusiness().createOrUpdateInvoice(s);
        JOptionPane.showMessageDialog(this, "Dropped successfully");
        refreshAll();
    }//GEN-LAST:event_btnDropActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        ((CardLayout) getRoot().getLayout()).previous(getRoot());
    }//GEN-LAST:event_btnBackActionPerformed
    private void doSearch() {
        // Filters offerings list based on search input
        // and rebuilds table accordingly
        String key = txtSearch.getText().trim().toLowerCase();
        String by = (String) cmbSearchBy.getSelectedItem();

        if (key.isEmpty()) {
            applyStatusAndFillTable(allOfferings);
            updateTotalCreditsLabelForVisibleTerm();
            return;
        }

        Student s = currentStudent();
        List<CourseOffering> filtered = new ArrayList<>();
        for (CourseOffering o : allOfferings) {
            String courseId = o.getCourse().getCode().toLowerCase();
            String courseName = o.getCourse().getTitle().toLowerCase();
            String instructor = (o.getFaculty() == null) ? "" : o.getFaculty().getName().toLowerCase();
            String stat = statusFor(s, o).toLowerCase();

            if ("Course ID".equals(by) && courseId.contains(key)) {
                filtered.add(o);
            }
            if ("Course Name".equals(by) && courseName.contains(key)) {
                filtered.add(o);
            }
            if ("Instructor".equals(by) && instructor.contains(key)) {
                filtered.add(o);
            }
            if ("Status".equals(by) && stat.contains(key)) {
                filtered.add(o);
            }
        }
        applyStatusAndFillTable(filtered);
        updateTotalCreditsLabelForVisibleTerm();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDrop;
    private javax.swing.JButton btnEnroll;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cmbSearchBy;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCourseRegistration;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblSearchBy;
    private javax.swing.JLabel lblTotalCredits;
    private javax.swing.JTable tblCourses;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTotalCredits;
    // End of variables declaration//GEN-END:variables
}
