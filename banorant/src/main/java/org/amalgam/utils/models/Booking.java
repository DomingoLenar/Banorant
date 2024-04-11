package org.amalgam.utils.models;

public class Booking {
    private int roomID;
    private int scheduleID;
    private int userID;
    private int paymentID;

    public Booking() {
        // Default constructor
    }

    public Booking(int roomID, int scheduleID, int userID, int paymentID) {
        this.roomID = roomID;
        this.scheduleID = scheduleID;
        this.userID = userID;
        this.paymentID = paymentID;
    }

    // Getters and setters
    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

}
