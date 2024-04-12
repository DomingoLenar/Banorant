package org.amalgam.utils.models;

import java.time.LocalDate;

public class Booking {
    private int bookingID;
    private int userID;
    private int sessionID;
    private int roomID;
    private int paymentID;
    private String bookingDate; // format of date is 'YYYY-MM-DD HH:MM:SS'

    public Booking() {

    }

    public Booking(int bookingID, int userID, int sessionID, int roomID, int paymentID, String bookingDate) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.sessionID = sessionID;
        this.roomID = roomID;
        this.paymentID = paymentID;
        this.bookingDate = bookingDate;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
}
