/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;
import java.awt.CardLayout;


/**
 *
 * @author priyankavadivel
 */
public class Student extends Person {

    private String program;
    private final List<Enrollment> enrollments = new ArrayList<>();


    /**
     * 
     */
    public Student(String name, String email, String program) {
        super(name, email);
        this.program = program;
    }

    /**
     * 
     */
    public String getProgram() {
        return program;
    }

    /**
     * 
     */
    public void setProgram(String program) {
        this.program = program;
    }

    /**
     * 
     */
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
}
