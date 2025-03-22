package com.ct.cacatrackerproject.clases;

public class UserSession {

    private static UserSession instance;
    private String username;
    private int userId;

    private UserSession() {
        this.username = null;
        this.userId = -1;
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUserInfo(String username, int userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public void clearUserInfo() {
        this.username = null;
        this.userId = -1;
    }
}
