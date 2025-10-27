/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author priyankavadivel
 */
public class Business {

    private final Map<String, Admin> Admins = new HashMap<>();
    private final Map<String, Faculty> Faculties = new HashMap<>();
    private final Map<String, Student> Students = new HashMap<>();

    private final Map<String, Course> courses = new HashMap<>();
    private final Map<String, CourseOffering> offerings = new HashMap<>();
    private final Map<String, TuitionInvoice> invoices = new HashMap<>();

    private final UserAccountDirectory accounts = new UserAccountDirectory();

    /**
     *
     */
    public UserAccountDirectory getAccounts() {
        return accounts;
    }

    /**
     *
     */
    public Collection<Admin> getAdmins() {
        return Admins.values();
    }

    /**
     *
     */
    public Collection<Faculty> getFaculties() {
        return Faculties.values();
    }

    /**
     *
     */
    public Collection<Student> getStudents() {
        return Students.values();
    }

    /**
     *
     */
    public Collection<Course> getCourses() {
        return courses.values();
    }

    /**
     *
     */
    public Collection<CourseOffering> getOfferings() {
        return offerings.values();
    }

    /**
     *
     */
    public Collection<TuitionInvoice> getInvoices() {
        return invoices.values();
    }

    /**
     *
     */
    public Admin addAdmin(Admin a, String username, String password) {
        Admins.put(a.getId(), a);
        accounts.add(new UserAccount(username, password, Role.ADMIN, a.getId()));
        return a;
    }

    /**
     *
     */
    public Faculty addFaculty(Faculty f, String username, String password) {
        Faculties.put(f.getId(), f);
        accounts.add(new UserAccount(username, password, Role.FACULTY, f.getId()));
        return f;
    }

    /**
     *
     */
    public Student addStudent(Student s, String username, String password) {
        Students.put(s.getId(), s);
        accounts.add(new UserAccount(username, password, Role.STUDENT, s.getId()));
        return s;
    }

    /**
     *
     */
    public void deleteFacultyById(String facultyId) {
        Faculties.remove(facultyId);
        for (UserAccount ua : new ArrayList<>(accounts.list())) {
            if (ua.getPersonId().equals(facultyId) && ua.getRole() == Role.FACULTY) {
                accounts.remove(ua.getUsername());
            }
        }
        for (CourseOffering co : offerings.values()) {
            if (co.getFaculty() != null && facultyId.equals(co.getFaculty().getId())) {
                co.setFaculty(null);
            }
        }
    }

    /**
     *
     */
    public void deleteStudentById(String studentId) {
        Student s = Students.remove(studentId);
        if (s != null) {
            for (Enrollment e : new ArrayList<>(s.getEnrollments())) {
                e.getOffering().getEnrollments().remove(e);
            }
        }
        for (UserAccount ua : new ArrayList<>(accounts.list())) {
            if (ua.getPersonId().equals(studentId) && ua.getRole() == Role.STUDENT) {
                accounts.remove(ua.getUsername());
            }
        }
        invoices.remove(studentId);
    }

    /**
     *
     */
    public Course addCourse(Course c) {
        courses.put(c.getId(), c);
        return c;
    }

    /**
     *
     */
    public CourseOffering addOffering(CourseOffering o) {
        offerings.put(o.getId(), o);
        return o;
    }

    /**
     *
     */
    public Enrollment enroll(Student s, CourseOffering o) {
        if (!o.hasSeat()) {
            return null;
        }
        Enrollment e = new Enrollment(s, o);
        s.getEnrollments().add(e);
        o.getEnrollments().add(e);
        return e;
    }

    /**
     *
     */
    public void setGrade(Student s, CourseOffering o, Grade g) {
        for (Enrollment e : s.getEnrollments()) {
            if (e.getOffering() == o) {
                e.setGrade(g);
                return;
            }
        }
    }

    /**
     *
     */
    public void createOrUpdateInvoice(Student s) {
        int credits = 0;
        for (Enrollment e : s.getEnrollments()) {
            credits += e.getOffering().getCourse().getCredits();
        }
        double amount = credits * 1000.0;
        TuitionInvoice inv = invoices.get(s.getId());
        if (inv == null) {
            inv = new TuitionInvoice(s, amount);
            invoices.put(s.getId(), inv);
        } else {
            if (!inv.isPaid()) {
                invoices.put(s.getId(), new TuitionInvoice(s, amount));
            }
        }
    }

    /**
     *
     */
    public Admin getAdminById(String id) {
        return Admins.get(id);
    }

    /**
     *
     */
    public Faculty getFacultyById(String id) {
        return Faculties.get(id);
    }

    /**
     *
     */
    public Student getStudentById(String id) {
        return Students.get(id);
    }

    public void deleteAdminById(String id) {
        Admins.remove(id);
    }

    public List<TuitionInvoice> getInvoicesByStudent(Student s) {
        return invoices.values().stream()
                .filter(inv -> inv.getStudent().equals(s))
                .toList();
    }

}
