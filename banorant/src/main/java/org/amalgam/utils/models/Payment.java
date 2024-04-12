package org.amalgam.utils.models;

import org.amalgam.utils.Status;

import java.time.LocalDate;

public class Payment {
    private int paymentID;
    private int userID;
    private int sessionID;
    private int amount;
    private Status status;
    private LocalDate paymentDate;

    public Payment(int paymentID, int userID, int sessionID, int amount, Status status, LocalDate paymentDate) {
        this.paymentID = paymentID;
        this.userID = userID;
        this.sessionID = sessionID;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
    }
}
