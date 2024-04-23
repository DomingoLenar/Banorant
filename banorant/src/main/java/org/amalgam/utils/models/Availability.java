package org.amalgam.utils.models;

public class Availability {

    private int availabilityID;
    private int playerID;
    private String availabilityDate;
    private boolean availability;
    private double ratePerHour;

    public Availability(){}

    public Availability(int availabilityID, int playerID, String availabilityDate, boolean availability, double ratePerHour) {
        this.availabilityID = availabilityID;
        this.playerID = playerID;
        this.availabilityDate = availabilityDate;
        this.availability = availability;
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

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }
}
