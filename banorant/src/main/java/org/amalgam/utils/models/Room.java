package org.amalgam.utils.models;

public class Room {
    private int roomID;
    private String name = "Meeting Room";
    private int paymentID;

    public Room(int roomID, String name, int paymentID) {
        this.roomID = roomID;
        this.name = name;
        this.paymentID = paymentID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }
}
