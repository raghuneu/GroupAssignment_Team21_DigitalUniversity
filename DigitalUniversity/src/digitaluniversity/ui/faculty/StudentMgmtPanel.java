/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package digitaluniversity.ui.faculty;

import java.awt.CardLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import model.Business;
import model.CourseOffering;
import model.Enrollment;
import model.Faculty;
import model.Grade;
import static model.Grade.A;
import static model.Grade.A_MINUS;
import static model.Grade.B;
import static model.Grade.B_MINUS;
import static model.Grade.B_PLUS;
import static model.Grade.C;
import static model.Grade.C_PLUS;
import static model.Grade.D;
import static model.Grade.F;
import model.Student;
import model.UserAccount;

/**
 * Faculty panel for managing student grading, GPA calculation, and ranking
 * students based on performance.
 *
 * @author rg
 * @author priyankavadivel
 * @author vedanarayananshrirangesh
 */
public class StudentMgmtPanel extends javax.swing.JPanel {

    /**
     * Helper class for mapping table rows to Enrollment objects.
     */
    private static class RowRef {

        final Enrollment enrollment;

        RowRef(Enrollment e) {
            this.enrollment = e;
        }
    }

    private final List<RowRef> rowRefs = new ArrayList<>();

    /**
     * Creates new form StudentMgmtPanel. Initializes the table and sets up
     * auto-population on show.
     */
    public StudentMgmtPanel() {
        initComponents();
        configureTable();

        // Populate table when panel becomes visible
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
     * Configures student table columns and edit behavior.
     */
    private void configureTable() {
        DefaultTableModel m = new DefaultTableModel(
                new Object[][]{},
                // keep your headers; first “Grade” shows current model grade, last “Grade” = computed
                new String[]{"Student ID", "Name", "Grade", "Assignment Score", "Midterm", "Final", "Grade"}
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                // Only allow editing for score columns
                return c == 3 || c == 4 || c == 5;
            }

            @Override
            public Class<?> getColumnClass(int c) {
                if (c >= 3 && c <= 5) {
                    return Double.class; // score columns
                }
                return String.class;
            }
        };
        tblStudents.setModel(m);
        fieldClassGPA.setEditable(false);
    }

    /**
     * @return the current faculty user
     */
    private Faculty currentFaculty() {
        if (getBusiness() == null || getAccount() == null) {
            return null;
        }
        return getBusiness().getFacultyById(getAccount().getPersonId());
    }

    /**
     * Populates the table with students enrolled under this faculty.
     */
    private void populateTable() {
        rowRefs.clear();
        DefaultTableModel m = (DefaultTableModel) tblStudents.getModel();
        m.setRowCount(0);

        Business b = getBusiness();
        Faculty f = currentFaculty();
        if (b == null || f == null) {
            return;
        }

        // for each student enrollment taught by this faculty
        for (Student s : b.getStudents()) {
            for (Enrollment e : s.getEnrollments()) {
                CourseOffering off = e.getOffering();
                if (off == null || off.getFaculty() == null) {
                    continue;
                }
                if (!off.getFaculty().getId().equals(f.getId())) {
                    continue;
                }

                String currentLetter = letter(e.getGrade());

                Object[] row = new Object[]{
                    s.getId(),
                    s.getName(),
                    currentLetter,
                    null, // assignment
                    null, // midterm
                    null, // final
                    "" // computed grade
                };
                m.addRow(row);
                rowRefs.add(new RowRef(e));
            }
        }

        // reset GPA display
        fieldClassGPA.setText("");
    }

    /**
     * Converts grade enum to readable letter.
     *
     * @param g grade enum
     * @return grade as letter string
     */
    private static String letter(Grade g) {
        if (g == null) {
            return "";
        }
        if (g == Grade.IP) {
            return "IP";
        }
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
                return g.name();
        }
    }

    /**
     * Converts a numeric score to a grade based on cutoffs.
     *
     * @param score numeric total (0–100)
     * @return Grade enum
     */
    private static Grade toLetterFromScore(double score) {
        // classic cutoffs (adjust if you prefer)
        if (score >= 93) {
            return Grade.A;
        }
        if (score >= 90) {
            return Grade.A_MINUS;
        }
        if (score >= 87) {
            return Grade.B_PLUS;
        }
        if (score >= 83) {
            return Grade.B;
        }
        if (score >= 80) {
            return Grade.B_MINUS;
        }
        if (score >= 77) {
            return Grade.C_PLUS;
        }
        if (score >= 73) {
            return Grade.C;
        }
        if (score >= 70) {
            return Grade.D;
        }
        return Grade.F;
    }

    /**
     * @param g Grade enum
     * @return GPA point value of the grade
     */
    private static double points(Grade g) {
        if (g == null || g == Grade.IP) {
            return 0.0;
        }
        return g.getPoints(); // use your enum’s scale
    }

    /**
     * Safely gets a numeric score from the table cell.
     */
    private Double getScore(DefaultTableModel m, int row, int col) {
        Object o = m.getValueAt(row, col);
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        try {
            return Double.parseDouble(o.toString());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Computes weighted total score using assignment, midterm, and final.
     */
    private double computeNumeric(Double a, Double mid, Double fin) {
        double as = (a == null ? 0 : a);
        double ms = (mid == null ? 0 : mid);
        double fs = (fin == null ? 0 : fin);
        // 20% assignment, 30% midterm, 50% final
        return as * 0.20 + ms * 0.30 + fs * 0.50;
    }

    /**
     * Prompts user to enter a score between 0 and 100.
     *
     * @param prompt label to show in dialog
     * @param preset previous score if available
     * @return valid Double score or null
     */
    private Double askForScore(String prompt, Double preset) {
        String val = JOptionPane.showInputDialog(this, prompt, preset == null ? "" : String.valueOf(preset));
        if (val == null) {
            return null;
        }
        try {
            double d = Double.parseDouble(val.trim());
            if (d < 0 || d > 100) {
                throw new IllegalArgumentException();
            }
            return d;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter a number between 0 and 100.");
            return null;
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
        tblStudents = new javax.swing.JTable();
        btnComputeFinalGrade = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        btnRankStudents = new javax.swing.JButton();
        btnGradeAssignment = new javax.swing.JButton();
        fieldClassGPA = new javax.swing.JTextField();
        lblClassGPA = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        tblStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Student ID", "Name", "Current Grade", "Assignment Score", "Midterm", "Final", "Grade"
            }
        ));
        jScrollPane1.setViewportView(tblStudents);

        btnComputeFinalGrade.setBackground(new java.awt.Color(153, 204, 255));
        btnComputeFinalGrade.setText("Compute Final Grade");
        btnComputeFinalGrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComputeFinalGradeActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblTitle.setText("Student Management");

        btnRankStudents.setText("Rank Students");
        btnRankStudents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRankStudentsActionPerformed(evt);
            }
        });

        btnGradeAssignment.setText("Grade Assignment");
        btnGradeAssignment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGradeAssignmentActionPerformed(evt);
            }
        });

        fieldClassGPA.setEditable(false);
        fieldClassGPA.setBackground(new java.awt.Color(229, 229, 229));

        lblClassGPA.setText("Class GPA");

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblClassGPA)
                        .addGap(18, 18, 18)
                        .addComponent(fieldClassGPA, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnBack)
                            .addGap(99, 99, 99)
                            .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnGradeAssignment)
                                .addGap(18, 18, 18)
                                .addComponent(btnComputeFinalGrade)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRankStudents))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldClassGPA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblClassGPA))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGradeAssignment)
                    .addComponent(btnComputeFinalGrade)
                    .addComponent(btnRankStudents))
                .addContainerGap(17, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnGradeAssignmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGradeAssignmentActionPerformed
        int row = tblStudents.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student row to grade.");
            return;
        }
        DefaultTableModel m = (DefaultTableModel) tblStudents.getModel();

        Double a = askForScore("Assignment score (0-100):", getScore(m, row, 3));
        if (a == null) {
            return;
        }
        Double mid = askForScore("Midterm score (0-100):", getScore(m, row, 4));
        if (mid == null) {
            return;
        }
        Double fin = askForScore("Final score (0-100):", getScore(m, row, 5));
        if (fin == null) {
            return;
        }

        m.setValueAt(a, row, 3);
        m.setValueAt(mid, row, 4);
        m.setValueAt(fin, row, 5);
    }//GEN-LAST:event_btnGradeAssignmentActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        ((CardLayout) getRoot().getLayout()).previous(getRoot());
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnComputeFinalGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComputeFinalGradeActionPerformed
        DefaultTableModel m = (DefaultTableModel) tblStudents.getModel();
        if (m.getRowCount() == 0) {
            return;
        }

        double totalPts = 0.0;
        int countForGpa = 0;

        for (int r = 0; r < m.getRowCount(); r++) {
            Double a = getScore(m, r, 3);
            Double mid = getScore(m, r, 4);
            Double fin = getScore(m, r, 5);

            double numeric = computeNumeric(a, mid, fin);
            Grade g = toLetterFromScore(numeric);

            // write to UI
            m.setValueAt(letter(g), r, 6);

            // write back to model (Enrollment)
            if (r < rowRefs.size()) {
                rowRefs.get(r).enrollment.setGrade(g);
            }

            totalPts += points(g);
            countForGpa++;
        }

        double classGpa = (countForGpa == 0) ? 0.0 : Math.round((totalPts / countForGpa) * 100.0) / 100.0;
        fieldClassGPA.setText(String.format("%.2f", classGpa));
    }//GEN-LAST:event_btnComputeFinalGradeActionPerformed

    private void btnRankStudentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRankStudentsActionPerformed
        DefaultTableModel m = (DefaultTableModel) tblStudents.getModel();
        // build list of indices with their numeric score
        List<int[]> rows = new ArrayList<>();
        for (int r = 0; r < m.getRowCount(); r++) {
            Double a = getScore(m, r, 3);
            Double mid = getScore(m, r, 4);
            Double fin = getScore(m, r, 5);
            double numeric = computeNumeric(a, mid, fin);
            rows.add(new int[]{r, (int) Math.round(numeric * 100)}); // store as int to avoid FP issues
        }
        rows.sort(Comparator.<int[]>comparingInt(x -> -x[1]));

        // recreate table in that order
        Object[][] data = new Object[m.getRowCount()][m.getColumnCount()];
        for (int i = 0; i < rows.size(); i++) {
            int r = rows.get(i)[0];
            for (int c = 0; c < m.getColumnCount(); c++) {
                data[i][c] = m.getValueAt(r, c);
            }
        }
        m.setRowCount(0);
        for (Object[] row : data) {
            m.addRow(row);
        }

        // keep rowRefs in the same order
        List<RowRef> newRefs = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            newRefs.add(rowRefs.get(rows.get(i)[0]));
        }
        rowRefs.clear();
        rowRefs.addAll(newRefs);

        tblStudents.revalidate();
        tblStudents.repaint();
    }//GEN-LAST:event_btnRankStudentsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnComputeFinalGrade;
    private javax.swing.JButton btnGradeAssignment;
    private javax.swing.JButton btnRankStudents;
    private javax.swing.JTextField fieldClassGPA;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblClassGPA;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblStudents;
    // End of variables declaration//GEN-END:variables
}
