/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author priyankavadivel
 */
public class CourseOffering {

    private final String id;
    private Course course;
    private Faculty faculty;
    private String term;
    private int capacity;
    private final List<Enrollment> enrollments = new ArrayList<>();
    private boolean enrollmentOpen = true;
    private String syllabusPath = null;

    /**
     *
     */
    public CourseOffering(String id, Course course, Faculty faculty, String term, int capacity) {
        this.id = id;
        this.course = course;
        this.faculty = faculty;
        this.term = term;
        this.capacity = capacity;
    }

    /**
     *
     */
    public String getId() {
        return id;
    }

    /**
     *
     */
    public Course getCourse() {
        return course;
    }

    /**
     *
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     *
     */
    public Faculty getFaculty() {
        return faculty;
    }

    /**
     *
     */
    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
        
    }

    /**
     *
     */
    public String getTerm() {
        return term;
    }

    /**
     *
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     *
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     *
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     *
     */
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    /**
     *
     */
    public boolean hasSeat() {
        return enrollments.size() < capacity;
    }

    public boolean isEnrollmentOpen() {
        return enrollmentOpen;
    }

    public void setEnrollmentOpen(boolean enrollmentOpen) {
        this.enrollmentOpen = enrollmentOpen;
    }

    public String getSyllabusPath() {
        return syllabusPath;
    }

    public void setSyllabusPath(String syllabusPath) {
        this.syllabusPath = syllabusPath;
    }

}
