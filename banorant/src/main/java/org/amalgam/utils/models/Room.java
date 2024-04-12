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
}
