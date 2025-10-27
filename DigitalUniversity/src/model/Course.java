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
public class Course {

    private final String id;
    private String code;
    private String title;
    private int credits;

    /**
     * 
     */
    public Course(String code, String title, int credits) {
        this.id = UUID.randomUUID().toString();
        this.code = code;
        this.title = title;
        this.credits = credits;
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
    public String getCode() {
        return code;
    }

    /**
     * 
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     */
    public int getCredits() {
        return credits;
    }

    /**
     * 
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        return code + " - " + title;
    }
}
