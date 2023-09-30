package com.sliit.budgetplanner.model;

import com.google.firebase.Timestamp;

public class Income {
    private String id;
    private float amount;
    private String type;
    private String paymentMethod;
    private String comments;

    private Timestamp date;

    public Income() {
    }

    public Income(float amount, String type, String paymentMethod, String comments, Timestamp date) {
        this.amount = amount;
        this.type = type;
        this.paymentMethod = paymentMethod;
        this.comments = comments;
        this.date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Income{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", comments='" + comments + '\'' +
                ", date=" + date +
                '}';
    }
}
