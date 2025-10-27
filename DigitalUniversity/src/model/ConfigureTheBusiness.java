/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.UUID;

/**
 *
 * @author priyankavadivel
 */
public class ConfigureTheBusiness {

    public static Business init() {
        Business b = new Business();

        // ========== ADMIN ==========
        Admin admin = new Admin("System Admin", "admin@du.edu");
        b.addAdmin(admin, "admin", "admin");

        // ========== FACULTY ==========
        Faculty f1 = new Faculty("Dr. Rivera", "rivera@du.edu", "Computer Science");
        Faculty f2 = new Faculty("Dr. Chen", "chen@du.edu", "Information Systems");
        Faculty f3 = new Faculty("Dr. Martinez", "martinez@du.edu", "Data Analytics");

        b.addFaculty(f1, "rivera", "pass");
        b.addFaculty(f2, "chen", "pass");
        b.addFaculty(f3, "martinez", "pass");

        // ========== STUDENTS ==========
        Student s1 = new Student("Priya Kumar", "priya@du.edu", "MS in Information Systems");
        Student s2 = new Student("Alex Lopez", "alex@du.edu", "MS in Data Analytics");

        b.addStudent(s1, "priya", "pass");
        b.addStudent(s2, "alex", "pass");

        // ========== COURSES ==========
        Course c1 = new Course("INFO 5100", "Application Engineering and Development", 4);
        Course c2 = new Course("INFO 6150", "Web Design and User Experience", 4);
        Course c3 = new Course("INFO 6205", "Program Structures and Algorithms", 4);
        Course c4 = new Course("DS 5010", "Probability and Statistics for Data Science", 4);
        Course c5 = new Course("DS 5020", "Data Mining and Machine Learning", 4);
        Course c6 = new Course("DS 6030", "Big Data Engineering", 4);

        b.addCourse(c1);
        b.addCourse(c2);
        b.addCourse(c3);
        b.addCourse(c4);
        b.addCourse(c5);
        b.addCourse(c6);

        // ========== OFFERINGS (3 terms) ==========
        CourseOffering o1 = new CourseOffering(UUID.randomUUID().toString(), c1, f1, "Fall 2024", 25);
        CourseOffering o2 = new CourseOffering(UUID.randomUUID().toString(), c2, f2, "Fall 2024", 25);
        CourseOffering o3 = new CourseOffering(UUID.randomUUID().toString(), c3, f1, "Spring 2025", 30);
        CourseOffering o4 = new CourseOffering(UUID.randomUUID().toString(), c4, f3, "Spring 2025", 20);
        CourseOffering o5 = new CourseOffering(UUID.randomUUID().toString(), c5, f3, "Fall 2025", 25);
        CourseOffering o6 = new CourseOffering(UUID.randomUUID().toString(), c6, f2, "Fall 2025", 20);

        b.addOffering(o1);
        b.addOffering(o2);
        b.addOffering(o3);
        b.addOffering(o4);
        b.addOffering(o5);
        b.addOffering(o6);

        // ========== ENROLLMENTS FOR STUDENT 1 (Priya) ==========
        Enrollment e1 = b.enroll(s1, o1);
        e1.setGrade(Grade.A);
        Enrollment e2 = b.enroll(s1, o2);
        e2.setGrade(Grade.B_PLUS);
        Enrollment e3 = b.enroll(s1, o3);
        e3.setGrade(Grade.A_MINUS);
        Enrollment e4 = b.enroll(s1, o4);
        e4.setGrade(Grade.B);
        Enrollment e5 = b.enroll(s1, o5);
        e5.setGrade(Grade.IP);  // In Progress
        Enrollment e6 = b.enroll(s1, o6);
        e6.setGrade(Grade.IP);

        // ========== ENROLLMENTS FOR STUDENT 2 (Alex) ==========
        Enrollment e7 = b.enroll(s2, o1);
        e7.setGrade(Grade.B);
        Enrollment e8 = b.enroll(s2, o3);
        e8.setGrade(Grade.C_PLUS);
        Enrollment e9 = b.enroll(s2, o4);
        e9.setGrade(Grade.B_MINUS);
        Enrollment e10 = b.enroll(s2, o5);
        e10.setGrade(Grade.A);
        Enrollment e11 = b.enroll(s2, o6);
        e11.setGrade(Grade.IP);

        // ========== INVOICES ==========
        b.createOrUpdateInvoice(s1);
        b.createOrUpdateInvoice(s2);

        
        java.util.List<TuitionInvoice> priyaInvs = b.getInvoicesByStudent(s1);
        if (!priyaInvs.isEmpty()) {
            priyaInvs.get(0).pay();
        }

        return b;
    }
}
