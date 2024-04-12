package org.amalgam.utils.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;

public class Session implements Serializable {
    private int sessionID;
    private int userID;
    private String date; // format of date is 'YYYY-MM-DD HH:MM:SS'
    private int duration;

    public Session(int sessionID, int userID, String date, int duration) {
        this.sessionID = sessionID;
        this.userID = userID;
        this.date = date;
        this.duration = duration;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
