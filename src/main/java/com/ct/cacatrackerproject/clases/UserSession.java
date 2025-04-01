package com.ct.cacatrackerproject.clases;

public class UserSession {

    private static UserSession instance;

    private String username;
    private String token;

    private UserSession() {

    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        if (username == null) {
            return "Guest";
        }
        return username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void clearUserInfo() {
        this.username = null;
        this.token = null;
    }
}
