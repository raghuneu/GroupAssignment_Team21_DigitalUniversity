/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author vedanarayananshrirangesh
 */
public class Enrollment {

    private final Student student;
    private final CourseOffering offering;
    private Grade Grade;

    /**
     * 
     */
    public Enrollment(Student student, CourseOffering offering) {
        this.student = student;
        this.offering = offering;
        this.grade = Grade.IP;
    }

    /**
     * 
     */
    public Student getStudent() {
        return student;
    }

    /**
     * 
     */
    public CourseOffering getOffering() {
        return offering;
    }

    /**
     * 
     */
    public Grade getGrade() {
        return grade;
    }

    /**
     * 
     */
    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
