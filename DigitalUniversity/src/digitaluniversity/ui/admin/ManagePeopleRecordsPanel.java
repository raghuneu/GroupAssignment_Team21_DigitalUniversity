/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package digitaluniversity.ui.admin;

import java.awt.CardLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import model.Business;
import model.Faculty;
import model.Student;

/**
 *
 * @author priyankavadivel
 */
public class ManagePeopleRecordsPanel extends javax.swing.JPanel {

    private JPanel getRoot() {
        return (JPanel) this.getClientProperty("root");
    }

    private Business getBusiness() {
        return (Business) this.getClientProperty("business");
    }

    public ManagePeopleRecordsPanel() {
        initComponents();

        // Click selection populates fields
        tblPeople.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPeople.getSelectedRow();
                if (row >= 0) {
                    populateFieldsFromTable(row);
                }
            }
        });

        // Keyboard selection populates fields
        tblPeople.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = tblPeople.getSelectedRow();
                    if (row >= 0) {
                        populateFieldsFromTable(row);
                    }
                }
            }
        });

        // Auto-refresh when the panel becomes visible and Business is available
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (getBusiness() != null) {
                    refreshPeopleTables();
                } else {
                    // try once in the next tick (in case client properties were just set)
                    SwingUtilities.invokeLater(() -> {
                        if (getBusiness() != null) {
                            refreshPeopleTables();
                        }
                    });
                }
            }
        });

        // Role is display-only here; avoid accidental cross-role edits
        cmbRole.setEnabled(false);
    }

    /**
     * Populate form fields when a table row is selected *
     */
    private void populateFieldsFromTable(int row) {
        if (getBusiness() == null || row < 0) {
            return;
        }

        String id = tblPeople.getValueAt(row, 0).toString();
        String role = tblPeople.getValueAt(row, 4).toString();

        if (role.equalsIgnoreCase("Faculty")) {
            Faculty f = getBusiness().getFacultyById(id);
            if (f != null) {
                fieldDept.setText(f.getDepartment());
                fieldEmail.setText(f.getEmail());
                fieldStatus.setText("Active");
                cmbRole.setSelectedItem("Faculty");
                fieldDept.setEnabled(true);   // faculty can edit dept
            }
        } else {
            Student s = getBusiness().getStudentById(id);
            if (s != null) {
                fieldDept.setText("-");       // students have no dept here
                fieldEmail.setText(s.getEmail());
                fieldStatus.setText("Active");
                cmbRole.setSelectedItem("Student");
                fieldDept.setEnabled(false);  // lock dept for students
            }
        }
    }

    void refreshPeopleTables() {
        if (getBusiness() == null) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblPeople.getModel();
        model.setRowCount(0);

        for (Faculty f : getBusiness().getFaculties()) {
            model.addRow(new Object[]{f.getId(), f.getName(), f.getDepartment(), f.getEmail(), "Faculty", "Active"});
        }
        for (Student s : getBusiness().getStudents()) {
            model.addRow(new Object[]{s.getId(), s.getName(), "-", s.getEmail(), "Student", "Active"});
        }

        // Optional UX: select first row if available to show details
        if (model.getRowCount() > 0) {
            tblPeople.setRowSelectionInterval(0, 0);
            populateFieldsFromTable(0);
        } else {
            clearForm();
        }
    }

    /**
     *
     */
    private void clearForm() {
        fieldDept.setText("");
        fieldEmail.setText("");
        fieldStatus.setText("");
        cmbRole.setSelectedIndex(0);
        fieldDept.setEnabled(false);
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
        tblPeople = new javax.swing.JTable();
        lblSearch = new javax.swing.JLabel();
        lblSearchby = new javax.swing.JLabel();
        cmbSearchby = new javax.swing.JComboBox<>();
        fieldSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblDept = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        fieldDept = new javax.swing.JTextField();
        fieldEmail = new javax.swing.JTextField();
        fieldStatus = new javax.swing.JTextField();
        cmbRole = new javax.swing.JComboBox<>();
        btnBack = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        tblPeople.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Dept", "Email", "Role", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPeople);
        if (tblPeople.getColumnModel().getColumnCount() > 0) {
            tblPeople.getColumnModel().getColumn(0).setResizable(false);
            tblPeople.getColumnModel().getColumn(1).setResizable(false);
            tblPeople.getColumnModel().getColumn(2).setResizable(false);
            tblPeople.getColumnModel().getColumn(3).setResizable(false);
            tblPeople.getColumnModel().getColumn(4).setResizable(false);
            tblPeople.getColumnModel().getColumn(5).setResizable(false);
        }

        lblSearch.setText("Search");

        lblSearchby.setText("Search by");

        cmbSearchby.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Name", "ID", "Dept" }));
        cmbSearchby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchbyActionPerformed(evt);
            }
        });

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnView.setText("View");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(153, 204, 255));
        btnEdit.setText("Save");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        lblDept.setText("Department");

        lblEmail.setText("Email");

        lblStatus.setText("Status");

        lblRole.setText("Role");

        cmbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Student", "Faculty" }));

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel1.setText("People Records Management");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addGap(49, 49, 49)
                        .addComponent(jLabel1))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblSearchby)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbSearchby, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblSearch)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnSearch))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(56, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(161, 161, 161)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatus)
                            .addComponent(lblRole))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnView)
                                .addGap(18, 18, 18)
                                .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblDept)
                                .addGap(66, 66, 66)
                                .addComponent(fieldDept, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblEmail)
                                .addGap(102, 102, 102)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbRole, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(fieldStatus)
                                    .addComponent(fieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(jLabel1))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblSearch)
                        .addComponent(fieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSearch))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblSearchby)
                        .addComponent(cmbSearchby, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDept)
                    .addComponent(fieldDept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmail)
                    .addComponent(fieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(fieldStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRole)
                    .addComponent(cmbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnView)
                    .addComponent(btnEdit)
                    .addComponent(btnDelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        int row = tblPeople.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row");
            return;
        }
        populateFieldsFromTable(row);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        if (getBusiness() == null) {
            return;
        }

        String by = String.valueOf(cmbSearchby.getSelectedItem());
        String q = fieldSearch.getText().trim().toLowerCase();

        DefaultTableModel model = (DefaultTableModel) tblPeople.getModel();
        model.setRowCount(0);

        // Faculty
        for (Faculty f : getBusiness().getFaculties()) {
            boolean match = by.equals("Name") ? f.getName().toLowerCase().contains(q)
                    : by.equals("ID") ? f.getId().toLowerCase().contains(q)
                    : by.equals("Dept") ? (f.getDepartment() != null && f.getDepartment().toLowerCase().contains(q))
                    : false;
            if (q.isEmpty() || match) {
                model.addRow(new Object[]{f.getId(), f.getName(), f.getDepartment(), f.getEmail(), "Faculty", "Active"});
            }
        }

        // Students
        for (Student s : getBusiness().getStudents()) {
            boolean match = by.equals("Name") ? s.getName().toLowerCase().contains(q)
                    : by.equals("ID") ? s.getId().toLowerCase().contains(q)
                    : by.equals("Dept") ? false // students have no dept in this grid
                    : false;
            if (q.isEmpty() || match) {
                model.addRow(new Object[]{s.getId(), s.getName(), "-", s.getEmail(), "Student", "Active"});
            }
        }

        if (model.getRowCount() > 0) {
            tblPeople.setRowSelectionInterval(0, 0);
            populateFieldsFromTable(0);
        } else {
            clearForm();
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (getBusiness() == null) {
            return;
        }
        int row = tblPeople.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row");
            return;
        }

        String id = tblPeople.getValueAt(row, 0).toString();
        String role = tblPeople.getValueAt(row, 4).toString();
        String email = fieldEmail.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required");
            return;
        }

        if (role.equalsIgnoreCase("Faculty")) {
            Faculty f = getBusiness().getFacultyById(id);
            if (f != null) {
                f.setEmail(email);
                f.setDepartment(fieldDept.getText().trim());
            }
        } else {
            Student s = getBusiness().getStudentById(id);
            if (s != null) {
                s.setEmail(email);
            }
        }

        refreshPeopleTables();
        JOptionPane.showMessageDialog(this, "Record updated");
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if (getBusiness() == null) {
            return;
        }
        int row = tblPeople.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row");
            return;
        }

        String id = tblPeople.getValueAt(row, 0).toString();
        String role = tblPeople.getValueAt(row, 4).toString();

        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected " + role + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if (role.equalsIgnoreCase("Faculty")) {
            getBusiness().deleteFacultyById(id);
        } else {
            getBusiness().deleteStudentById(id);
        }

        refreshPeopleTables();
        clearForm();
        JOptionPane.showMessageDialog(this, "Record deleted");
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        AdminWorkAreaPanel adminPanel = new AdminWorkAreaPanel();
        adminPanel.putClientProperty("business", getBusiness());
        adminPanel.putClientProperty("root", getRoot());
        getRoot().add("adminWorkArea", adminPanel);
        ((CardLayout) getRoot().getLayout()).show(getRoot(), "adminWorkArea");
    }//GEN-LAST:event_btnBackActionPerformed

    private void cmbSearchbyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchbyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSearchbyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnView;
    private javax.swing.JComboBox<String> cmbRole;
    private javax.swing.JComboBox<String> cmbSearchby;
    private javax.swing.JTextField fieldDept;
    private javax.swing.JTextField fieldEmail;
    private javax.swing.JTextField fieldSearch;
    private javax.swing.JTextField fieldStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDept;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblSearchby;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tblPeople;
    // End of variables declaration//GEN-END:variables
}
