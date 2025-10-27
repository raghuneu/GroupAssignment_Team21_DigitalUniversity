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
 */
public class AccessControlLayer extends JPanel {

    private JPanel root;
    private Business business;
    private UserAccount loggedInAccount;

    public AccessControlLayer(JPanel root, UserAccount ua) {
        this.root = root;
        this.loggedInAccount = ua;
        this.setLayout(new CardLayout());
       
    }

}
