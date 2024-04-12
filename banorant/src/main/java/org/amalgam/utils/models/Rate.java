package org.amalgam.utils.models;

public class Rate {
    private int rateID;
    private int userID;
    private double rate;
//    private String effectiveDate;


    public Rate(int rateID, int userID, double rate) {
        this.rateID = rateID;
        this.userID = userID;
        this.rate = rate;
    }

    public int getRateID() {
        return rateID;
    }

    public void setRateID(int rateID) {
        this.rateID = rateID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
