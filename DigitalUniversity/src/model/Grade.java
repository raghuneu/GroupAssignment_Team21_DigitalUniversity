/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author vedanarayananshrirangesh
 */
public enum Grade {
    A(4.0), A_MINUS(3.7), B_PLUS(3.3), B(3.0), B_MINUS(2.7), C_PLUS(2.3), C(2.0), D(1.0), F(0.0), IP(0.0);

    private final double points;

    /**
     * 
     */
    Grade(double points) {
        this.points = points;
    }

    /**
     * 
     */
    public double getPoints() {
        return points;
    }
}
