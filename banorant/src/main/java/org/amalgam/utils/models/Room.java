package org.amalgam.utils.models;

import java.io.Serializable;

public class Room implements Serializable {
    private int roomID;
    private String name = "Meeting Room";
    private String date;
    private int paymentID;

    public Room(int roomID, String name, String date, int paymentID) {
        this.roomID = roomID;
        this.name = name;
        this.date = date;
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
