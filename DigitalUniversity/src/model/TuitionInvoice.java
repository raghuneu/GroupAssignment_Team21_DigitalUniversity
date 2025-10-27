/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.util.UUID;

/**
 *
 * @author priyankavadivel
 */
public class TuitionInvoice {

    private final String id;
    private final Student student;
    private final String term;

    private final double originalAmount;
    private double amountDue;

    private boolean paid;
    private LocalDate paidDate;

    public TuitionInvoice(Student student, String term, double amount) {
        this.id = "INV-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.student = student;
        this.term = (term == null || term.isBlank()) ? "Current" : term;
        this.originalAmount = amount;
        this.amountDue = amount;
        this.paid = false;
        this.paidDate = null;
    }

    // Back-compat constructor used by Business#createOrUpdateInvoice
    public TuitionInvoice(Student student, double amount) {
        this(student, "Current", amount);
    }

    public String getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public String getTerm() {
        return term;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public boolean isPaid() {
        return paid;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void applyPayment(double amt) {
        if (paid || amt <= 0) {
            return;
        }
        amountDue = Math.max(0.0, amountDue - amt);
        if (amountDue == 0.0) {
            paid = true;
            paidDate = LocalDate.now();
        }
    }

    public void pay() {
        if (!paid) {
            amountDue = 0;
            paid = true;
            paidDate = LocalDate.now();
        }
    }
}
