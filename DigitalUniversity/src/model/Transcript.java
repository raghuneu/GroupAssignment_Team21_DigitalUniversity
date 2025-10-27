/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vedanarayananshrirangesh
 */
public class Transcript {

    private final Student student;

    /**
     * 
     * @param student
     */
    public Transcript(Student student) {
        this.student = student;
    }

    /**
     * 
     */
    public double calculateGpa() {
        double points = 0.0;
        int credits = 0;
        for (Enrollment e : student.getEnrollments()) {
            if (e.getGrade() != Grade.IP) {
                int cr = e.getOffering().getCourse().getCredits();
                points += e.getGrade().getPoints() * cr;
                credits += cr;
            }
        }
        if (credits == 0) {
            return 0.0;
        }
        return Math.round((points / credits) * 100.0) / 100.0;
    }

    /**
     * 
     */
    public List<Enrollment> list() {
        return new ArrayList<>(student.getEnrollments());
    }
}
