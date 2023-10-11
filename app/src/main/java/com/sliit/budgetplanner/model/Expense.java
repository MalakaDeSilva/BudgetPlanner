package com.sliit.budgetplanner.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;

public class Expense {
    private String id;
    private String userId;
    private float amount;
    private String type;
    private String paymentMethod;
    private String comments;

    private Timestamp date;
    private String fileRef;

    public Expense() {
    }

    public Expense(float amount, String type, String paymentMethod, String comments, Timestamp date) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getFileRef() {
        return fileRef;
    }

    public void setFileRef(String fileRef) {
        this.fileRef = fileRef;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", comments='" + comments + '\'' +
                ", date=" + date +
                '}';
    }
}
