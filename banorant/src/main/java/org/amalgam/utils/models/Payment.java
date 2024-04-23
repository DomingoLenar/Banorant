package org.amalgam.utils.models;

import org.amalgam.utils.Status;

import java.io.Serializable;

public class Payment implements Serializable {
    private int paymentID;
    private int userID;
    private int amount;
    private Status status;
    private String paymentDate; // format of date is 'YYYY-MM-DD HH:MM:SS'

    public Payment(int paymentID, int userID, int amount, Status status, String paymentDate) {
        this.paymentID = paymentID;
        this.userID = userID;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
