package org.amalgam.utils.models;

public class Rate {
    private int rateID;
    private int playerID;
    private double rate;
//    private String effectiveDate;

    public Rate(int rateID, int playerID, double rate) {
        this.rateID = rateID;
        this.playerID = playerID;
        this.rate = rate;
    }

    public int getRateID() {
        return rateID;
    }

    public void setRateID(int rateID) {
        this.rateID = rateID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
