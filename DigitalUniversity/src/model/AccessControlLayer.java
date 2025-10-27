package model;

import digitaluniversity.ui.admin.AdminWorkAreaPanel;
import digitaluniversity.ui.faculty.FacultyWorkAreaPanel;
//import digitaluniversity.ui.faculty.FacultyWorkAreaPanel;
import digitaluniversity.ui.student.StudentWorkAreaPanel;
import java.awt.CardLayout;
import javax.swing.JPanel;

/**
 * Handles post-login role routing
 * @author priyankavadivel
 * @author vedanarayananshrirangesh
 */
public class AccessControlLayer extends JPanel {

    private JPanel root;
    private Business business;
    private UserAccount loggedInAccount;

    public AccessControlLayer(JPanel root, Business business, UserAccount ua) {
        this.root = root;
        this.business = business;
        this.loggedInAccount = ua;
        this.setLayout(new CardLayout());
        route();
    }

    /**
     *
     */
    private void route() {
        if (loggedInAccount.getRole() == Role.ADMIN) {
            AdminWorkAreaPanel adminPanel = new AdminWorkAreaPanel();
            adminPanel.putClientProperty("business", business);
            adminPanel.putClientProperty("root", root);
            adminPanel.putClientProperty("account", loggedInAccount);
            this.add("adminWorkArea", adminPanel);
            ((CardLayout) this.getLayout()).show(this, "adminWorkArea");
        } else if (loggedInAccount.getRole() == Role.FACULTY) {
            FacultyWorkAreaPanel facultyPanel = new FacultyWorkAreaPanel();
            facultyPanel.putClientProperty("business", business);
            facultyPanel.putClientProperty("root", root);
            facultyPanel.putClientProperty("account", loggedInAccount);
            this.add("facultyWorkArea", facultyPanel);
            ((CardLayout) this.getLayout()).show(this, "facultyWorkArea");
        }
    }
}
