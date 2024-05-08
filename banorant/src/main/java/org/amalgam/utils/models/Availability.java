package org.amalgam.utils.models;

import java.io.Serializable;

public class Availability implements Serializable {

    private int availabilityID;
    private int playerID;
    private String availabilityDate; // Format: YYYY/MM/DD
    private String startTime; // Format: HH/MM/SS
    private String endTime; //Format: HH/MM/SS
    private int ratePerHour;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Availability(){}

    public Availability(int playerID, String availabilityDate, String startTime, String endTime, int ratePerHour) {
        this.playerID = playerID;
        this.availabilityDate = availabilityDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ratePerHour = ratePerHour;
    }

    public Availability(int availabilityID, int playerID, String availabilityDate, String startTime, String endTime, int ratePerHour) {
        this.availabilityID = availabilityID;
        this.playerID = playerID;
        this.availabilityDate = availabilityDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ratePerHour = ratePerHour;
    }

    public int getAvailabilityID() {
        return availabilityID;
    }

    public void setAvailabilityID(int availabilityID) {
        this.availabilityID = availabilityID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getAvailabilityDate() {
        return availabilityDate;
    }

    public void setAvailabilityDate(String availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(int ratePerHour) {
        this.ratePerHour = ratePerHour;
    }
}
