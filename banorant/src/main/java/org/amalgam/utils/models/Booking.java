package org.amalgam.utils.models;

import java.time.LocalDate;

public class Booking {
    private int bookingID;
    private int userID;
    private int sessionID;
    private int roomID;
    private int paymentID;
    private LocalDate bookingDate;

    public Booking(int bookingID, int userID, int sessionID, int roomID, int paymentID, LocalDate bookingDate) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.sessionID = sessionID;
        this.roomID = roomID;
        this.paymentID = paymentID;
        this.bookingDate = bookingDate;
    }
}
