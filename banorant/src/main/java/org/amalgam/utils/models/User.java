package org.amalgam.utils.models;

import java.io.Serializable;

public class User implements Serializable {
    private int userID;
    private String username;
    private String password;
    private boolean isCelebrity = false;
    public User(int userID, String username, String password, boolean isCelebrity) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.isCelebrity = isCelebrity;
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setCelebrity(boolean celebrity) {
        isCelebrity = celebrity;
    }
    public boolean isCelebrity() {
        return isCelebrity;
    }
}
