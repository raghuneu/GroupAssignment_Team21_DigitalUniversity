/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.UUID;

/**
 * Represents a university course with unique ID, course code, title, and credit value.
 * @author priyankavadivel
 */
public class Course {

    private final String id;
    private String code;
    private String title;
    private int credits;

    /**
     * Creates a new Course with provided details.
     * @param code course code (e.g., "CS5010")
     * @param title course title
     * @param credits number of credit hours
     */
    public Course(String code, String title, int credits) {
        this.id = UUID.randomUUID().toString();
        this.code = code;
        this.title = title;
        this.credits = credits;
    }

    /**
     * @return unique course identifier
     */
    public String getId() {
        return id;
    }

    /**
     * @return course code
     */
    public String getCode() {
        return code;
    }

    /**
     * Updates the course code.
     * @param code new course code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return course title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates the course title.
     * @param title new course title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return number of course credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Updates the number of credits for the course.
     * @param credits new credit value
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * @return short text combining course code and title
     */
    @Override
    public String toString() {
        return code + " - " + title;
    }
}
