package org.amalgam.server.models;

import java.io.Serializable;

public class User implements Serializable {
    private int userID;
    private String username;
    private String password;
//    private String role;

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
//        this.role = null;
    }

//    public User(String username, String password, String role) {
//        this.username = username;
//        this.password = password;
//        this.role = role;
//    }

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

//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
}
